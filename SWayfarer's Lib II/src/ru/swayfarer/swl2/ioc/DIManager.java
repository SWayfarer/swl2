package ru.swayfarer.swl2.ioc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import ru.swayfarer.swl2.asm.transformer.ditransformer.regetter.visitor.DynamicDI;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Контейнер для Dependency Injection
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class DIManager {

	/** Контекст */
	public DIContext context;
	
	/** Логгер */
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Конструктор */
	public DIManager()
	{
		context = new DIContext();
		DIRegistry.registerDefultIfNotFound(this);
	}
	
	/** Зарегистировать источник контекста */
	public static <T extends DIManager> T registerContextSource(String contextName, Object... sources)
	{
		DIManager manager = DIRegistry.getRegisteredManager(contextName);
		
		if (manager == null)
		{
			manager = new DIManager();
			DIRegistry.registerManager(manager, contextName);
		}
		
		for (Object source : sources)
			manager.addContextSource(source);
		
		return (T) manager;
	}
	
	/** Иньекция контекста в объект с указанием дефолтного контекста */
	public static <T> T injectContextElements(T obj)
	{
		if (obj == null)
			return null;
		
		String contextName = "default";
		
		Class<?> cl = obj.getClass();
		
		DISwL annotation = cl.getAnnotation(DISwL.class);
		
		if (annotation != null)
		{
			if (!StringUtils.isEmpty(annotation.context()))
			{
				contextName = annotation.context();
			}
		}
		
		return DIRegistry.injectToObject(contextName, obj);
	}
	
	/** Иньекция контекста в объект с указанием дефолтного контекста */
	public static <T> T injectContextElements(String contextName, T obj)
	{
		return DIRegistry.injectToObject(contextName, obj);
	}
	
	/** Получить элемент указанного типа с указанным именем из контекста */
	public static <T> T getContextElement(String contextName, String elementName, String elementType)
	{
		return DIRegistry.getContextElement(contextName, elementName, elementType);
	}
	
	/**
	 * Добавить источник элементов
	 * @param obj - источник, в котором есть методы-источники элементов 
	 */
	public void addContextSource(Object obj)
	{
		if (obj == null)
			return;
		
		logger.safe(() -> {
			
			for (Method method : obj.getClass().getDeclaredMethods())
			{
				DISwL annotation = method.getDeclaredAnnotation(DISwL.class);
				
				if (annotation != null)
				{
					String name = annotation.name();
					
					if (StringUtils.isEmpty(name))
						name = method.getName();
					
					IDIContextElement contextElement = null;
					
					if (name.startsWith("get"))
					{
						name = name.substring(3);
						
						if (StringUtils.isEmpty(name))
							continue;
						
						if (name.length() == 1)
						{
							name = name.toLowerCase();
						}
						else
						{
							name = (name.charAt(0)+"").toLowerCase() + name.substring(1);
						}
					}
					
					switch(annotation.type())
					{
						case Singleton:
						{
							contextElement = DIContextElementSingleton.builder()
								.associatedClass(method.getReturnType())
								.value(method.invoke(obj))
								.name(name)
								.build();
							
							break;
						}
						
						default:
						{
							DIContextElementFromMethod element;
							
							contextElement = element = DIContextElementFromMethod.builder()
									.associatedClass(method.getReturnType())
									.method(method)
									.sourceInstance(obj)
									.name(name)
									.build();
							
							if (annotation.type() == ContextElementType.ThreadLocalPrototype)
								element.methodInvokationFun = new ThreadLocalMethodInvokationFun();
							
							break;
						}
					}
					
					context.setContextElement(name, method.getReturnType(), contextElement);
				}
			}
		}, "Error while loading sources from", obj);
	}
	
	/**
	 * Иньекция элементов контекста
	 * @param obj - Объект, в который производится иньекция 
	 * @return Оригинальный obj
	 */
	public <T> T injectToObject(Object obj)
	{
		injectToObjects(obj);
		return (T) obj;
	}
	
	/**
	 * Иньекция элементов контекста 
	 * @param objs Объекты, в которые нужно иньектировать в контекст 
	 */
	public void injectToObjects(Object... objs)
	{
		ReflectionDIInjector.inject(context, objs);
	}
	
	/**
	 * Рефлективный иньектор 
	 * @author swayfarer
	 *
	 */
	public static class ReflectionDIInjector {
		
		/** 
		 * Кэшированные иньекторы
		 * Самое долгое в рефлексии - создание аксессоров полей. Для этого они кэшируются для повторного использования 
		 * */
		public static Map<Class<?>, ReflectionDIInjector> cachedInjectors = new IdentityHashMap<>();
		
		/** Список полей, к которым был получен доступ */
		public IExtendedList<FieldDIInfo> fields = CollectionsSWL.createExtendedList();
		
		/** Логгер */
		public ILogger logger = LoggingManager.getLogger();
		
		/** Использовать ли кэширование */
		public static boolean isUsingCache = true;
		
		/**
		 * Конструктор
		 * @param cl Класс, в который будут сгененированы аксессоры
		 */
		public ReflectionDIInjector(Class<?> cl)
		{
			logger.safe(() -> {
				
				for (Field field : cl.getDeclaredFields())
				{
					DISwL annotation = field.getAnnotation(DISwL.class);
					
					if (annotation != null)
					{
						if (ReflectionUtils.setAccessible(field))
						{
							fields.add(FieldDIInfo.of(field, annotation));
						}
						else
						{
							logger.error("Can't access the field", field, ". The field will be skipped during injection...");
						}
					}
 				}
				
			}, "Error while reading fields from", cl);
		}
		
		/**
		 * Иньекция контекста 
		 * @param context Контекст, объекты которого будут иньектиться
		 * @param obj Объект, в который будут проводиться иньекции
		 */
		public void injectContext(DIContext context, Object obj)
		{
			injectContext((field, annotation) -> context, obj);
		}
		
		/**
		 * Иньекция контекста 
		 * @param contextFun Функция, возвращающаа контекст, объекты которого будут иньектиться
		 * @param obj Объект, в который будут проводиться иньекции
		 */
		public void injectContext(IFunction2<Field, DISwL, DIContext> contextFun, Object obj)
		{
			logger.safe( () -> {
				
				DIContext context = null;
				
				for (FieldDIInfo fieldInfo : fields)
				{
					Field field = fieldInfo.field;
					
					String name = fieldInfo.annotation.name();
					
					if (StringUtils.isEmpty(name))
						name = field.getName();
					
					context = contextFun.apply(field, fieldInfo.annotation);
					
					if (context == null)
						continue;
					
					IDIContextElement element = context.getContextElement(name, field.getType());
					
					if (element != null)
					{
						field.set(obj, element.getValue());
					}
				}
			}, "Error while injecting context to", obj);
			
		}
		
		/**
		 * Иньекция контекста 
		 * @param context Контекст, объекты которого будут иньектиться
		 * @param objects Объекты, в которые будут проводиться иньекции
		 */
		public static void inject(DIContext context, Object... objects)
		{
			inject((field, annotation) -> context, objects);
		}
		
		/**
		 * Иньекция контекста 
		 * @param contextFun Генератор контекста, объекты которого будут иньектиться
		 * @param objects Объекты, в которые будут проводиться иньекции
		 */
		public static void inject(IFunction2<Field, DISwL, DIContext> contextFun, Object... objects)
		{
			if (objects != null)
			{
				for (Object obj : objects)
				{
					if (obj != null)
					{
						Class<?> cl = obj.getClass();
						
						if (isUsingCache)
						{
							ReflectionDIInjector diInjector = cachedInjectors.get(cl);
							
							if (diInjector == null)
							{
								diInjector = new ReflectionDIInjector(cl);
								cachedInjectors.put(cl, diInjector);
							}
								
							diInjector.injectContext(contextFun, obj);
						}
						else
						{
							new ReflectionDIInjector(cl).injectContext(contextFun, obj);
						}
					}
				}
			}
		}
		
		@AllArgsConstructor(staticName = "of")
		public static class FieldDIInfo {
			
			public Field field;
			public DISwL annotation;
			
		}
	}
	
	/**
	 * Отметить для внедрения зависимостей
	 * 
	 * <h1>Если отмечено поле</h1>
	 * В поле при наличии будет занесено подходящее значение из контекста.
	 * <h1>Если отмечен метод</h1>
	 * Метод будет использован как источник элементов контекста. Метод не может быть void или содержать параметры
	 * <h1> Если отмечен класс</h1>
	 * Позволяет указать через {@link DISwL#context()} контекст, из которого по-умолчанию будут тянуться зависимости 
	 * @author swayfarer
	 *
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface DISwL {
		
		/** Имя элемента */
		public String name() default "";
		
		/** Имя контекста */
		public String context() default "";
		
		/** Тип элемента контекста (см {@link ContextElementType} )*/
		public ContextElementType type() default ContextElementType.Singleton;
		
	}
	
	/** Типы элементов контектов */
	public static enum ContextElementType {
		
		/** 
		 * Синглтон
		 * <br> Единственный объект 
		 */
		Singleton,
		
		/** 
		 * Прототип 
		 * <br> Каждый раз новый элемент
		 * <br> Если отметить иньектируемое поле {@link DynamicDI}, то каждое обращение к этому полю внутри класа будет возвращать новое значение 
		 */
		Prototype,
		
		/** 
		 * {@link ThreadLocal}-синглтон 
		 * <br> Возвращает новый INSTANCE для каждого потока 
		 */
		ThreadLocalPrototype
	}
	
	/**
	 * 
	 * @author swayfarer
	 *
	 */
	public static class DIContext {
		
		/** Элементы контекста. Имя -> Класс -> Элемент */
		public Map<String, Map<Class<?>, IDIContextElement> > contextElements = new HashMap<>();
		
		/**
		 * Задать элемент контекста 
		 * @param name Имя элемента
		 * @param value Значение элемента
		 * @return Оригинальный {@link DIContext} (this)
		 */
		public <T extends DIContext> T setContextElement(String name, Object value)
		{
			return setContextElement(name, value.getClass(), value);
		}
		
		/**
		 * Задать элемент контекста 
		 * @param name Имя элемента
		 * @param associatedClass Класс, с которым будет ассоциироваться элемент
		 * @param value Значение элемента
		 * @return Оригинальный {@link DIContext} (this)
		 */
		public <T extends DIContext> T setContextElement(String name, Class<?> associatedClass, IDIContextElement value)
		{
			Map<Class<?>, IDIContextElement> contextElements = getElementsForName(name, true);
			contextElements.put(associatedClass, value);
			return (T) this;
		}
		
		/**
		 * Задать элемент контекста 
		 * @param name Имя элемента
		 * @param associatedClass Класс, с которым будет ассоциироваться элемент
		 * @param value Значение элемента
		 * @return Оригинальный {@link DIContext} (this)
		 */
		public <T extends DIContext> T setContextElement(String name, Class<?> associatedClass, Object value)
		{
			return setContextElement(name, associatedClass, DIContextElementSingleton.builder()
					.associatedClass(associatedClass)
					.name(name)
					.value(value)
					.build()
			);
		}
		
		/**
		 * Есть ли элемент контекста
		 * @param name Имя элемента
		 * @param associatedClass Класс, с которым ассоциируется элемент 
		 * @return Есть ли элемент?
		 */
		public boolean hasContextElement(String name, Class<?> associatedClass)
		{
			return getContextElement(name, associatedClass) != null;
		}
		
		/**
		 * Получить элемент контекста
		 * @param name Имя элемента
		 * @param className Имя класа. Не каноничное, но и не Internal. То, что {@link Class#getName()}
		 * @return Элемент. Nyll, если не найден.
		 */
		public IDIContextElement getContextElement(String name, String className)
		{
			try
			{
				return getContextElement(name, Class.forName(className));
			}
			catch (ClassNotFoundException e)
			{
				logger.error(e, "Error while getting context element", name, "of class", className, "from", this);
			}
			
			return null;
		}
		
		/**
		 * Получить элемент контекста
		 * @param name Имя элемента
		 * @param associatedClass Класс, с которым ассоциируется элемент
		 * @return Элемент. Nyll, если не найден.
		 */
		public IDIContextElement getContextElement(String name, Class<?> associatedClass)
		{
			Map<Class<?>, IDIContextElement> elementsForName = getElementsForName(name, false);
			
			Class<?> cl;
			IDIContextElement assignableElement = null;
			
			if (elementsForName == null)
				return null;
			
			if (elementsForName.containsKey(associatedClass))
				return elementsForName.get(associatedClass);
			
			for (IDIContextElement contextElement : elementsForName.values())
			{
				cl = contextElement.getAssociatedClass();
				
				if (cl == associatedClass)
					return contextElement;
				else if (associatedClass.isAssignableFrom(cl))
					assignableElement = contextElement;
			}
			
			return assignableElement;
		}
		
		/**
		 * Получить карту Class -> {@link IDIContextElement} элементов
		 * @param name Имя элементов
		 * @param forceCreate Создать ли, если не найдено?
		 */
		public Map<Class<?>, IDIContextElement> getElementsForName(String name, boolean forceCreate)
		{
			Map<Class<?>, IDIContextElement> ret = contextElements.get(name);
			
			if (ret == null && forceCreate)
			{
				ret = CollectionsSWL.createHashMap();
				contextElements.put(name, ret);
			}
			
			return ret;
		}
		
	}
	
	/**
	 * Функция, вызывающая метод и кэширующая его результат для текущего потока 
	 * @author swayfarer
	 *
	 */
	public static class ThreadLocalMethodInvokationFun implements IFunction2<Method, Object, Object> {

		/** Логгер*/
		@InternalElement
		public static ILogger logger = LoggingManager.getLogger();
		
		/** {@link ThreadLocal}, в котором хранятся результаты работы метода для разных потоков */
		@InternalElement
		public ThreadLocal<Object> threadLocal;
		
		@Override
		public Object apply(Method method, Object instance)
		{
			if (threadLocal == null) {
				threadLocal = new ThreadLocal<Object>() {
					
					protected Object initialValue() {
						
						try
						{
							return method.invoke(instance);
						}
						catch (Throwable e)
						{
							logger.error(e, "Error while creating thread local instance for method", method, "of", instance);
						}
						
						return null;
					}
					
				};
			}
			
			return threadLocal.get();
		}
	}
	
	/**
	 * Элемент контекста, возвращающий значение из метода
	 * @author swayfarer
	 *
	 */
	@Builder
	public static class DIContextElementFromMethod implements IDIContextElement {

		/** Класс, с которым ассоциируется элемент */
		@InternalElement
		public Class<?> associatedClass;
		
		/** Класс, с которым ассоциируется имя */
		@InternalElement
		public String name;
		
		/** Источник, из которого берется элемент */
		@InternalElement
		public Object sourceInstance;
		
		/** Метод, из которого берертся элемент */
		@InternalElement
		public Method method;
		
		/** Логгер */
		@InternalElement
		public static ILogger logger = LoggingManager.getLogger();
		
		/** Функция, которая вызывает метод */
		@Builder.Default
		public IFunction2<Method, Object, Object> methodInvokationFun = (method, obj) -> {
			
			try
			{
				return method.invoke(obj);
			}
			catch (Throwable e)
			{
				logger.error(e, "Error while invoking method", method, "of object", obj);
			}
			
			return null;
		};
		
		@Override
		public Object getValue()
		{
			return methodInvokationFun.apply(method, sourceInstance);
		}

		@Override
		public Class<?> getAssociatedClass()
		{
			return associatedClass;
		}

		@Override
		public String getName()
		{
			return name;
		}
		
	}
	
	/**
	 * Синглтон-элемент контекста. Всегда возвращает одно и то же значение
	 * @author swayfarer
	 *
	 */
	@Builder @ToString
	public static class DIContextElementSingleton implements IDIContextElement{
		
		/** Класс, с которым ассоциируется элемент */
		@InternalElement
		public Class<?> associatedClass;
		
		/** Класс, с которым ассоциируется имя */
		@InternalElement
		public String name;
		
		/** Возвращаемое значение */
		@InternalElement
		public Object value;
		
		public Object getValue()
		{
			return value;
		}
		
		public Class<?> getAssociatedClass()
		{
			return associatedClass;
		}
		
		public String getName()
		{
			return name;
		}
	}
	
	/**
	 * Элемент контекста. Отвечает за возврат значений контекста 
	 * @author swayfarer
	 *
	 */
	public static interface IDIContextElement {
		
		/** Получить значение */
		public Object getValue();
		
		/** Получить класс, с которым ассоциируется элемент */
		public Class<?> getAssociatedClass();
		
		/** Получить имя элемента */
		public String getName();
		
	}
	
}
