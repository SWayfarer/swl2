package ru.swayfarer.swl2.ioc;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.ioc.context.DIContext;
import ru.swayfarer.swl2.ioc.context.elements.ContextElementType;
import ru.swayfarer.swl2.ioc.context.elements.DIContextElementFromMethod;
import ru.swayfarer.swl2.ioc.context.elements.DIContextElementSingleton;
import ru.swayfarer.swl2.ioc.context.elements.IDIContextElement;
import ru.swayfarer.swl2.ioc.context.elements.ThreadLocalMethodInvokationFun;
import ru.swayfarer.swl2.ioc.context.injector.ReflectionDIInjector;
import ru.swayfarer.swl2.logger.ILogLevel;
import ru.swayfarer.swl2.logger.ILogLevel.StandartLoggingLevels;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LogInfo;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.string.DynamicString;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Контейнер для Dependency Injection
 * 
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
	}

	/**
	 * Создать контекст, если не создан
	 * @param name Имя создаваемого контекста
	 * @return {@link DIManager}
	 */
	public static DIManager createIfNotFound(String name)
	{
		DIManager ret = DIRegistry.getRegisteredManager(name);

		if (ret == null)
		{
			ret = DIRegistry.registerManager(new DIManager(), name);
		}

		return ret;
	}
	
	/**
	 * Заблокировать иньекции из контекста <br>
	 * Может быть использовано для его настройки
	 * @param contextName Имя блокируемого контекста 
	 */
	public static void lockInjections(String contextName)
	{
		DIContext context = DIRegistry.getRegisteredContext(contextName);

		if (context != null)
		{
			context.lockOn(Thread.currentThread());
		}
		else
		{
			logger.warning("Can't lock context", contextName, "because it not found!\n| Registered managers:", DIRegistry.registeredManagers);
		}
	}
	
	/**
	 * Зарегистрировать постпроцессор элементов для контекста <br> <br>
	 * Постпроцессоры обрабатывают элементы контекста при их создании <br>
	 * Постпроцессоры распространяются только на элементы этого контекста. 
	 * Элементы наследуемых контекстов не обрабатываются им! <br>
	 * 
	 * <h1> Внимание </h1>
	 * Постпроцессор возвращает значение, которое будет использовано в контексте. Т.е. через него можно даже заменить обхъект.
	 * @param contextName Имя целевого контекста
	 * @param fun Функция-постпроцессор
	 */
	public static void registerPostProcessor(String contextName, IFunction1<Object, Object> fun)
	{
		DIContext context = DIRegistry.getRegisteredContext(contextName);
		
		if (context == null)
		{
			logger.warning("Can't register post processor to non-existing context");
			return;
		}
		
		context.elementPostprocessors.addExclusive(fun);
	}

	/**
	 * Разблокировать иньекции из контекста <br>
	 * @param contextName Имя разблокируемого контекста 
	 */
	public static void unlockInjections(String contextName)
	{
		DIContext context = DIRegistry.getRegisteredContext(contextName);

		if (context != null)
		{
			context.unlock();
		}
		else
		{
			logger.warning("Can't unlock context", contextName, "because it not found!");
		}
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

		return DIRegistry.injectToObject(obj);
	}

	/** Получить элемент указанного типа с указанным именем из контекста */
	public static <T> T getContextElement(String contextName, String elementName, Class<?> elementType)
	{
		return getContextElement(contextName, elementName, elementType.getName());
	}
	
	/** Получить элемент указанного типа с указанным именем из контекста */
	public static <T> T getContextElement(String contextName, String elementName, String elementType)
	{
		return DIRegistry.getContextElement(contextName, elementName, elementType);
	}
	
	/**
	 * Иньекция в поле контекста
	 * @param instance Объект, в поле которого происходит иньекция
	 * @param fieldName Поле, в которое происходит иньекция
	 */
	public static void injectToFieldSafe(Object instance, String fieldName)
	{
		logger.safe(() -> {
			ReflectionDIInjector.injectToField(instance, fieldName);
		}, "Error while injectiong context element for field", fieldName, "of", instance);
	}
	
	/**
	 * Распечатать контекст
	 * @param name Имя контекста, который необходимо распечатать
	 */
	public static void printContext(String name)
	{
		DynamicString str = new DynamicString();
		
		ILogLevel level = null;
		
		DIManager manager = DIRegistry.getRegisteredManager(name);
		
		if (manager == null)
		{
			str.append("Context with name");
			str.append(name);
			str.append("was not found!");
			level = StandartLoggingLevels.LEVEL_WARNING;
//			return "Context with name '" + name + "' was not found!";
		}
		else
		{
			level = StandartLoggingLevels.LEVEL_INFO;
			
			str.append("Context '");
			str.append(name);
			str.append("' content: {\n");
			
			String indentString = StringUtils.createSpacesSeq(4);
			boolean isFirst = true;
			
			DIContext context = manager.context;
			
			for (Map.Entry<String, Map<Class<?>, IDIContextElement>> entry : context.contextElements.entrySet())
			{
				if (!isFirst)
				{
					str.append(",\n");
				}
				
				str.append(indentString);
				str.append(entry.getKey());
				str.append(": {\n");
				
				String itemIndent = StringUtils.createSpacesSeq(8);
				
				boolean isFisrtElement = true;
				
				for (Map.Entry<Class<?>, IDIContextElement> itemEntry : entry.getValue().entrySet())
				{
					if (!isFisrtElement)
					{
						str.append(",\n");
					}
					
					str.append(itemIndent);
					str.append(itemEntry.getKey().getName());
					str.append(" = ");
					str.append(itemEntry.getValue());
					
					isFisrtElement = false;
				}
				
				str.append(indentString);
				str.append("\n");
				str.append(indentString);
				str.append("}");
				
				isFirst = false;
			}
			
			str.append("\n}");
		}
		
		LogInfo logInfo = LogInfo.of(logger, level, 1, str);
		
		logger.log(logInfo);
	}

	/**
	 * Добавить источник элементов
	 * 
	 * @param obj
	 *            - источник, в котором есть методы-источники элементов
	 */
	public void addContextSource(Object obj)
	{
		synchronized (context.contextElements)
		{
			if (obj == null)
				return;

			logger.safe(() -> {

				IExtendedList<Method> methods = CollectionsSWL.createExtendedList(obj.getClass().getDeclaredMethods());
				methods.sortBy((m1, m2) -> m1.getName().compareTo(m2.getName()));
				
				for (Method method : methods)
				{
					Annotation annotation = DIAnnotation.findDIAnnotation(method); 

					if (annotation != null)
					{
						String name = DIAnnotation.getElementName(annotation);

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
								name = (name.charAt(0) + "").toLowerCase() + name.substring(1);
							}
						}

						ContextElementType annotationElementType = DIAnnotation.getElementType(annotation);
						
						switch (annotationElementType)
						{
							case Singleton:
							{
								
								contextElement = new DIContextElementSingleton()
									.setName(name)
									.setAssociatedClass(method.getReturnType())
									.setContext(context)
									.setObjectCreationFun(() -> {
										return logger.safeReturn(() -> method.invoke(obj, getArgsFromContext(obj.getClass(), method.getParameters()).toArray()), null, "Error while creating singleton from", method);
									});
								;
								
								break;
							}
	
							default:
							{
								DIContextElementFromMethod element;
	
								contextElement = element = new DIContextElementFromMethod(context)
										.setMethodClass(obj.getClass())
										.setAssociatedClass(method.getReturnType())
										.setMethod(method)
										.setSourceInstance(obj)
										.setName(name)
								;
	
								if (annotationElementType == ContextElementType.ThreadLocalPrototype)
									element.methodInvokationFun = new ThreadLocalMethodInvokationFun();
	
								break;
							}
						}

						context.setContextElement(name, method.getReturnType(), contextElement);
					}
				}
			}, "Error while loading sources from", obj);
		}
	}

	/**
	 * Иньекция элементов контекста
	 * 
	 * @param obj
	 *            - Объект, в который производится иньекция
	 * @return Оригинальный obj
	 */
	public <T> T injectToObject(Object obj)
	{
		injectToObjects(obj);
		return (T) obj;
	}

	/**
	 * Иньекция элементов контекста
	 * 
	 * @param objs
	 *            Объекты, в которые нужно иньектировать в контекст
	 */
	public void injectToObjects(Object... objs)
	{
		ReflectionDIInjector.inject(context, objs);
	}

	/**
	 * Отметить для внедрения зависимостей
	 * 
	 * <h1>Если отмечено поле</h1> В поле при наличии будет занесено подходящее
	 * значение из контекста.
	 * <h1>Если отмечен метод</h1> Метод будет использован как источник
	 * элементов контекста. Метод не может быть void или содержать параметры
	 * <h1>Если отмечен класс</h1> Позволяет указать через
	 * {@link DISwL#context()} контекст, из которого по-умолчанию будут тянуться
	 * зависимости
	 * 
	 * @author swayfarer
	 *
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface DISwL
	{

		/** Имя элемента */
		public String name() default "";

		/** Имя контекста */
		public String context() default "";
		
		/** Использовать ли имя, или определять элемент только по типу? */
		public boolean usingName() default true;

		/** Тип элемента контекста (см {@link ContextElementType} ) */
		public ContextElementType type() default ContextElementType.Singleton;

	}

	/**
	 * Получить аргументы, которые будут переданы из контекста для вызова метода
	 * @param classOfMethod Возвращаемый тип метода
	 * @param params Информация о параметрах метода
	 * @return Лист аргументов
	 */
	public static IExtendedList<Object> getArgsFromContext(Class<?> classOfMethod, Parameter[] params)
	{
		String contextName = DIRegistry.getClassContextName(classOfMethod);

		IExtendedList<Object> invokeArgs = CollectionsSWL.createExtendedList();
		
		for (Parameter param : params)
		{
			Class<?> classOfParam = param.getType();
			
			if (!param.isNamePresent())
			{
				logger.error("Can't find name of parameter! Please, re-compile class", classOfMethod, "with -parameters flag! Using null...");
				invokeArgs.add(null);
			}
			else
			{
				String name = param.getName();
				
				Annotation annotation = DIAnnotation.findDIAnnotation(param); //ReflectionUtils.findAnnotationRec(param, DISwL.class); //param.getAnnotation(DISwL.class);
				
				if (annotation != null)
				{
					// Кэш для хранения полученных через DIAnnotation значений
					String s = DIAnnotation.getElementName(annotation);
					
					if (!StringUtils.isEmpty(s))
					{
						name = s;
					}
					
					s = DIAnnotation.getElementContextName(annotation);
					
					if (!StringUtils.isEmpty(s))
					{
						contextName = s;
					}
				}
				
				Object contextElement = DIManager.getContextElement(contextName, name, classOfParam);
				
				invokeArgs.add(contextElement);
			}
			
		}
		
		return invokeArgs;
	}

}
