package ru.swayfarer.swl2.ioc.componentscan;

import ru.swayfarer.swl2.asm.classfinder.ClassFinder;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.ioc.DIManager;
import ru.swayfarer.swl2.ioc.DIRegistry;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.string.ExpressionsList;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;

public class ContextSourcesScan {

	public ExpressionsList blackList = new ExpressionsList();
	
	public static ILogger logger = LoggingManager.getLogger();
	
	public boolean isLoggingScan = false;
	
	public static String contextSourceAnnotationDesc = Type.getDescriptor(DISwlSource.class);
	public ClassFinder classFinder = new ClassFinder();
	
	public ContextSourcesScan()
	{
		classFinder.eventScan.subscribe((info) -> logger.safe(() -> scan(info)));
	}
	
	@SuppressWarnings("unchecked")
	public <T extends ContextSourcesScan> T scan(String pkg)
	{
		classFinder.scan(pkg);
		return (T) this;
	}
	
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
