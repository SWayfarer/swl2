package ru.swayfarer.swl2.ioc.componentscan;

import java.lang.reflect.Method;
import java.util.Map;

import ru.swayfarer.swl2.asm.classfinder.ClassFinder;
import ru.swayfarer.swl2.asm.informated.AnnotationInfo;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
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
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.Observables;
import ru.swayfarer.swl2.string.ExpressionsList;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.threads.ThreadsUtils;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;

/**
 * Сканнер компонентов 
 * @author swayfarer
 *
 */
public class ComponentScan {

	/** Список выражений, классы соответствующие которым будут пропущены*/
	@InternalElement
	public ExpressionsList blackList = new ExpressionsList();
	
	/** Искалка классов */
	@InternalElement
	public ClassFinder classFinder;
	
	/** Логировать ли сканирование? */
	@InternalElement
	public boolean isLoggingScan = false;
	
	/** Дескриптор аннотации {@link DISwlComponent} */
	@InternalElement
	public static String componentAnnotationDesc = Type.getDescriptor(DISwlComponent.class);
	
	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Событие создания компонента */
	public IObservable<Object> eventComponentCreation = Observables.createObservable();
	
	/**
	 * Конструктор
	 * @param classFinder Искалка классов, которые будут сканироваться
	 */
	public ComponentScan(ClassFinder classFinder)
	{
		ExceptionsUtils.IfNullArg(classFinder, "Class finder can't be null!");
		this.classFinder = classFinder;
		classFinder.eventScan.subscribe(this::scan);
		classFinder.setStreamFun(classFinder.classSources.ofClasspath());
	}
	
	/**
	 * Просканировать класс
	 * @param classInfo Информация о сканируемом классе
	 */
	public void scan(ClassInfo classInfo)
	{
		logger.safe(() -> scanSafe(classInfo));
	}
	
	/**
	 * Безопасно просканировать класс
	 * @param classInfo Информация о сканируемом классе
	 * @throws Throwable
	 */
	public void scanSafe(ClassInfo classInfo) throws Throwable
	{
		String name = classInfo.getCanonicalName();
		
		if (blackList.isMatches(name))
			return;
		
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
			
			createContextElement(cl, annotation.associated(),  contextName, elementName, elementType);
		}
		else
		{
			if (isLoggingScan)
			{
				logger.warning("Class", classInfo.name, "has not annotation", componentAnnotationDesc);
			}
		}
	}
	
	/**
	 * Создать элемент контекста 
	 * @param cl Класс, на основе которого создается элемент
	 * @param assocClass Класс, с которым будет ассоциироваться новый элемент
	 * @param contextName Имя контекста, куда будет добавлен новый элемент
	 * @param elementName Имя элемента, под которым он будет добавлен в контекст 
	 * @param elementType Тип создаваемого элемента
	 * @throws Throwable 
	 */
	public void createContextElement(Class<?> cl, Class<?> assocClass, String contextName, String elementName, ContextElementType elementType) throws Throwable
	{
		if (assocClass == Object.class)
			assocClass = cl;
		
		if (isLoggingScan)
			logger.info("Scanning class", cl);
		
		IFunction0<Object> objectCreationFun = null;
		
		if (StringUtils.isEmpty(elementName))
		{
			elementName = StringUtils.firstCharToLowerCase(cl.getSimpleName());
		}
		
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
	
	/**
	 * Создать новый объект указанного класса
	 * @param cl Класс, элемент которого создается
	 * @param isInject Иньектить ли поля из контекста в созданный объект? 
	 * @return Новый объект
	 */
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
	
	/**
	 * НЕ СОБЫТИЕ! <br> 
	 * Фильтр на методы событий (отмеченные {@link DISwlComponentEvent}) компонента
	 * @author swayfarer
	 *
	 */
	public static class EventAnnotatedMethodFilter implements IFunction1<Method, Boolean> {

		/** Принимаемые типы событий */
		public IExtendedList<ComponentEventType> acceptableTypes = CollectionsSWL.createExtendedList();
		
		/** Конструктор */
		public EventAnnotatedMethodFilter() {}
		
		/**
		 * Конструктор
		 * @param types Типы принимаемых событий 
		 */
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
