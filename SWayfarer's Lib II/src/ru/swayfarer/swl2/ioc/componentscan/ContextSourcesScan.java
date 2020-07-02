package ru.swayfarer.swl2.ioc.componentscan;

import java.lang.annotation.Annotation;

import lombok.val;
import lombok.var;
import ru.swayfarer.swl2.asm.classfinder.ClassFinder;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.ioc.DIAnnotation;
import ru.swayfarer.swl2.ioc.DIManager;
import ru.swayfarer.swl2.ioc.DIRegistry;
import ru.swayfarer.swl2.ioc.componentscan.DISwlSource.ElementPostprocessor;
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
		
		if (classInfo.findAnnotationRec(contextSourceAnnotationDesc) != null)
		{
			Class<?> cl = ReflectionUtils.findClass(classInfo.getCanonicalName());
			
			if (cl.isAnonymousClass() || cl.isInterface())
			{
				if (isLoggingScan)
				{
					logger.warning("Class", cl, "has", contextSourceAnnotationDesc, "annotation, but it's anonymous class. Skiping...");
				}
				
				return;
			}
			
			Annotation annotation = DIAnnotation.findDISourceAnnotation(cl);
			
			String context = DIAnnotation.getSourceContextName(annotation);
			
			if (StringUtils.isBlank(context))
			{
				context = DIRegistry.getClassContextName(cl);
			}
			
			Object source = ReflectionUtils.newInstanceOf(cl);
			
			if (isLoggingScan)
			{
				logger.info("Found context's", context, "source", source);
			}
			
			// Регистрация постпроцессоров 
			val finalContextName = context;
			
			DIManager.createIfNotFound(finalContextName);
			
			var methodsStream = ReflectionUtils.methods(source);
			
			methodsStream.annotated(ElementPostprocessor.class)
				.argsCount(1)
				.args(Object.class)
				.returnType(Object.class, void.class)
				.each((method) -> {
					DIManager.registerPostProcessor(finalContextName, (element) -> {
						
						if (isLoggingScan)
						{
							logger.info("Found context element postprocessor method", method, "! Registering...");
						}
						
						return logger.safeReturn(() -> {
							Object ret = method.invoke(source, element);
							return method.getReturnType() == void.class ? element : ret;
						}, element, "Error while invoking postprocessor", method, "on object", element);
					});
				})
			;
			
			methodsStream
				.argsCount(0)
				.annotated(DISwlSource.PreInitEvent.class)
			.invoke(context);
			
			DIManager.registerContextSource(context, source);
			
			methodsStream
				.argsCount(0)
				.annotated(DISwlSource.InitEvent.class)
			.invoke(context);
		}
	}
	
}
