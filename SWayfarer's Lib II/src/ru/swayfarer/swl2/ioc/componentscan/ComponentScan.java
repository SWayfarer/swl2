package ru.swayfarer.swl2.ioc.componentscan;

import java.lang.reflect.Method;
import java.util.Map;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.swayfarer.swl2.asm.classfinder.ClassFinder;
import ru.swayfarer.swl2.asm.informated.AnnotationInfo;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction0;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.ioc.DIManager;
import ru.swayfarer.swl2.ioc.DIManager.ContextElementType;
import ru.swayfarer.swl2.ioc.DIManager.DIContext;
import ru.swayfarer.swl2.ioc.DIManager.DIContextElementFromFun;
import ru.swayfarer.swl2.ioc.DIManager.IDIContextElement;
import ru.swayfarer.swl2.ioc.DIRegistry;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.Observables;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.threads.ThreadsUtils;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;

@SuppressWarnings("unchecked")
public class ComponentScan {

	public ClassFinder classFinder = new ClassFinder();
	
	public boolean isLoggingScan = false;
	
	public ScanInfo scanInfo = new ScanInfo();
	
	public static ILogger logger = LoggingManager.getLogger();
	
	public IExtendedList<String> packages = CollectionsSWL.createExtendedList();
	
	public IObservable<Object> eventComponentCreation = Observables.createObservable();
	
	public ComponentScan()
	{
		classFinder.eventScan.subscribe(this::scan);
		classFinder.setStreamFun(classFinder.classSources.ofClasspath());
	}
	
	public void scan(ClassInfo classInfo)
	{
		logger.safe(() -> scanSafe(classInfo));
	}
	
	public void scanSafe(ClassInfo classInfo) throws Throwable
	{
		String name = classInfo.getCanonicalName();
		String componentAnnotationDesc = getComponentAnnotationDesc();
		AnnotationInfo componentAnnotationInfo = classInfo.getFirstAnnotation(componentAnnotationDesc);
		
		if (componentAnnotationInfo != null)
		{
			if (isLoggingScan)
				logger.info("Found class with component annotation:", name);
			
			String contextName = componentAnnotationInfo.getParam(getContextNameParameter());
			String elementName = componentAnnotationInfo.getParam(getElementNameParameter());
			String elementTypeStr = componentAnnotationInfo.getParam(getElementTypeParameter());
			ContextElementType elementType = null;
			
			if (StringUtils.isEmpty(elementTypeStr))
			{
				elementType = ContextElementType.Singleton;
			}
			else
			{
				elementType = ContextElementType.valueOf(elementTypeStr);
			}
			
			Class<?> cl = ReflectionUtils.findClass(name);
			
			scanClass(cl, contextName, elementName, elementType);
		}
		else
		{
			if (isLoggingScan)
			{
				logger.warning("Class", classInfo.name, "has not annotation", scanInfo.componentAnnotationDesc);
			}
		}
	}
	
	public void scanClass(Class<?> cl, String contextName, String elementName, ContextElementType elementType) throws Throwable
	{
		if (isLoggingScan)
			logger.info("Scanning class", cl);
		
		IFunction0<Object> objectCreationFun = null;
		
		if (StringUtils.isEmpty(contextName))
		{
			contextName = DIRegistry.getClassContextName(cl);
		}
		
		if (elementType == null)
			elementType = ContextElementType.Singleton;
		
		switch(elementType)
		{
			case Prototype:
			{
				objectCreationFun = () -> createNewObject(cl, true);
				break;
			}
			case Singleton:
			{
				objectCreationFun = new IFunction0<Object>()
				{
					public Object instance;
					
					@Override
					public Object apply()
					{
						if (instance == null)
						{
							instance = createNewObject(cl, false);
							DIManager.injectContextElements(instance);
							ReflectionUtils.invokeMethods(new EventAnnotatedMethodFilter(ComponentEventType.Init), cl, instance, new Object[0]);
						}
						
						return instance;
					}
				};
				break;
			}
			case ThreadLocalPrototype:
			{
				objectCreationFun = () -> ThreadsUtils.getThreadLocal(() -> createNewObject(cl, true));
				break;
			}
			default:
				break;
		}
		
		if (objectCreationFun == null)
			return;
		
		DIManager.createIfNotFound(contextName);
		DIContext context = DIRegistry.getRegisteredContext(contextName);
		
		IDIContextElement elementToAdd = DIContextElementFromFun.of(cl, elementName, objectCreationFun);
		
		Map<Class<?>, IDIContextElement> map = context.getElementsForName(elementName, true);
		map.put(cl, elementToAdd);
	}
	
	public Object createNewObject(Class<?> cl, boolean isInject)
	{
		Object instance = ReflectionUtils.newInstanceOf(cl);
		
		if (instance == null)
		{
			logger.error("Can't create new instance of class", cl, "Maybe constructor without args is not exists?");
			return null;
		}
		
		eventComponentCreation.next(instance);
		
		ReflectionUtils.invokeMethods(new EventAnnotatedMethodFilter(ComponentEventType.PreInit), cl, instance, new Object[0]);
		
		if (isInject)
			DIManager.injectContextElements(instance);
		
		if (isInject)
			ReflectionUtils.invokeMethods(new EventAnnotatedMethodFilter(ComponentEventType.Init), cl, instance, new Object[0]);
		
		return instance;
	}
	
	public String getElementNameParameter()
	{
		return scanInfo == null ? null : scanInfo.elementNameParameter;
	}
	
	public String getContextNameParameter()
	{
		return scanInfo == null ? null : scanInfo.contextNameParameter;
	}
	
	public String getElementTypeParameter()
	{
		return scanInfo == null ? null : scanInfo.elementTypeParameter;
	}
	
	public String getComponentAnnotationDesc()
	{
		return scanInfo == null ? null : scanInfo.componentAnnotationDesc;
	}
	
	public <T extends ComponentScan> T scan(String pkg) 
	{
		classFinder.scan(pkg);
		return (T) this;
	}
	
	public static class EventAnnotatedMethodFilter implements IFunction1<Method, Boolean> {

		public IExtendedList<ComponentEventType> acceptableTypes = CollectionsSWL.createExtendedList();
		
		public EventAnnotatedMethodFilter()
		{
			
		}
		
		public EventAnnotatedMethodFilter(ComponentEventType... types)
		{
			acceptableTypes.addAll(types);
		}
		
		@Override
		public Boolean apply(Method method)
		{
			if (method == null)
				return false;
			
			DISwlComponentEvent annotation = method.getAnnotation(DISwlComponentEvent.class);
			
			if (annotation != null)
			{
				ComponentEventType value = annotation.value();
				
				if (acceptableTypes.contains(value))
				{
					return true;
				}
			}
			
			return false;
		}
	}
	
	@Data @Accessors(chain = true)
	public static class ScanInfo {
		
		public String componentAnnotationDesc = Type.getDescriptor(DISwlComponent.class);
		public String contextNameParameter = "context";
		public String elementNameParameter = "name";
		public String elementTypeParameter = "type";
		
	}
}
