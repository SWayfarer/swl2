package ru.swayfarer.swl2.ioc.componentscan;

import java.lang.reflect.Method;
import java.util.Map;

import ru.swayfarer.swl2.asm.classfinder.ClassFinder;
import ru.swayfarer.swl2.asm.informated.AnnotationInfo;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction0;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.ioc.DIManager;
import ru.swayfarer.swl2.ioc.DIRegistry;
import ru.swayfarer.swl2.ioc.context.DIContext;
import ru.swayfarer.swl2.ioc.context.elements.ContextElementType;
import ru.swayfarer.swl2.ioc.context.elements.DIContextElementFromFun;
import ru.swayfarer.swl2.ioc.context.elements.IDIContextElement;
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
	
	public static String componentAnnotationDesc = Type.getDescriptor(DISwlComponent.class);
	
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
		AnnotationInfo componentAnnotationInfo = classInfo.getFirstAnnotation(componentAnnotationDesc);
		
		if (componentAnnotationInfo != null)
		{
			if (isLoggingScan)
				logger.info("Found class with component annotation:", name);
			
			Class<?> cl = ReflectionUtils.findClass(name);
			
			DISwlComponent annotation = cl.getAnnotation(DISwlComponent.class);
			
			String contextName = annotation.context();
			String elementName = annotation.name();
			ContextElementType elementType = annotation.type();
			
			scanClass(cl, annotation.associated(),  contextName, elementName, elementType);
		}
		else
		{
			if (isLoggingScan)
			{
				logger.warning("Class", classInfo.name, "has not annotation", componentAnnotationDesc);
			}
		}
	}
	
	public void scanClass(Class<?> cl, Class<?> assocClass, String contextName, String elementName, ContextElementType elementType) throws Throwable
	{
		if (assocClass == Object.class)
			assocClass = cl;
		
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
		
		IDIContextElement elementToAdd = DIContextElementFromFun.of(assocClass, elementName, objectCreationFun);
		
		Map<Class<?>, IDIContextElement> map = context.getElementsForName(elementName, true);
		map.put(assocClass, elementToAdd);
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
}
