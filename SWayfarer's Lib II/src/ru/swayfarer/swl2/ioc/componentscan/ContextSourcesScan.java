package ru.swayfarer.swl2.ioc.componentscan;

import ru.swayfarer.swl2.asm.classfinder.ClassFinder;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.ioc.DIManager;
import ru.swayfarer.swl2.ioc.DIRegistry;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.ExpressionsList;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;

/**
 * Сканнер источников контекста, отмеченных аннотацией {@link DISwlSource}
 * @author swayfarer
 *
 */
public class ContextSourcesScan {

	/** Список выражений, классы соответствующие которым будут пропущены*/
	@InternalElement
	public ExpressionsList blackList = new ExpressionsList();
	
	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Логировать ли сканирование?*/
	@InternalElement
	public boolean isLoggingScan = false;
	
	/** Дескриптор аннотации {@link DISwlSource} */
	@InternalElement
	public static String contextSourceAnnotationDesc = Type.getDescriptor(DISwlSource.class);
	
	/** Искалка классов */
	@InternalElement
	public ClassFinder classFinder;
	
	/**
	 * Конструктор
	 * @param classFinder искалка классов, которые будут сканироваться 
	 */
	public ContextSourcesScan(ClassFinder classFinder)
	{
		ExceptionsUtils.IfNullArg(classFinder, "Class finder can't be null!");
		this.classFinder = classFinder;
		classFinder.eventScan.subscribe((info) -> logger.safe(() -> scan(info)));
	}
	
	/**
	 * Просканировать класс
	 * @param classInfo Информация о сканируемом классе 
	 */
	public void scan(ClassInfo classInfo)
	{
		if (blackList.isMatches(classInfo.getCanonicalName()))
			return;
		
		if (classInfo.hasAnnotation(contextSourceAnnotationDesc))
		{
			Class<?> cl = ReflectionUtils.findClass(classInfo.getCanonicalName());
			
			DISwlSource annotation = cl.getAnnotation(DISwlSource.class);
			String context = annotation.context();
			
			if (StringUtils.isBlank(context))
			{
				context = DIRegistry.getClassContextName(cl);
			}
			
			Object source = ReflectionUtils.newInstanceOf(cl);
			
			if (isLoggingScan)
			{
				logger.info("Found context's", context, "source", source);
			}
			
			DIManager.registerContextSource(context, source);
		}
	}
	
}
