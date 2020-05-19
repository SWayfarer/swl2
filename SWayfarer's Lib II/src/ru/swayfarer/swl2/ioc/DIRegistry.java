package ru.swayfarer.swl2.ioc;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedMap;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2;
import ru.swayfarer.swl2.ioc.DIManager.DISwL;
import ru.swayfarer.swl2.ioc.context.DIContext;
import ru.swayfarer.swl2.ioc.context.elements.IDIContextElement;
import ru.swayfarer.swl2.ioc.context.injector.ReflectionDIInjector;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.DynamicString;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Меджер регистрации {@link DIManager}'ов
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class DIRegistry {

	public static boolean isLoggingBytecodeInjections = false;
	
	public static boolean isLoggingFields = false;
	
	public static boolean isLoggingInjections = false;
	
	public static boolean isLoggingContextSearch = false;
	
	/** Загеристрированные паттерны контекстов (по пакетам можно настроить, какой контекст куда пойдет) */
	@InternalElement
	public static IExtendedMap<String, String> contextPatterns = CollectionsSWL.createExtendedMap();
	
	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Зарегистрированные {@link DIManager}'ы */
	@InternalElement
	public static Map<String, DIManager> registeredManagers = new HashMap<>();
	
	/** Иньекция контекста в объект с указанием дефолтного контекста */
	public static <T> T injectToObject(Object object)
	{	
		AnnotationContextFunction defaultContext = AnnotationContextFunction.of(getClassContextName(object.getClass()));
		
		if (isLoggingInjections)
			logger.info("Injecting context '" + defaultContext + "' to", object);
		
		ReflectionDIInjector.inject(defaultContext, object);
		return (T) object;
	}
	
	public static String getFieldContext(Field field, Object instance)
	{
		return getFieldContext(field, instance.getClass());
	}
	
	public static String getFieldContext(Field field, Class<?> cl)
	{
		DISwL annotation = field.getAnnotation(DISwL.class);
		
		if (annotation != null)
		{
			String context = annotation.context();
			
			if (!StringUtils.isBlank(context))
				return context;
		}
		
		return getClassContextName(cl);
	}
	
	public static DIContext getRegisteredContext(String name)
	{
		DIManager manager = registeredManagers.get(name);
		
		if (manager == null)
			return null;
		
		return manager.context;
	}
	
	/** Зарегистрировать контекст как стандартный, если его еще нет*/
	public static <T extends DIManager> T registerDefultIfNotFound(DIManager manager)
	{
		if (registeredManagers.get("default") == null)
			registeredManagers.put("default", manager);
		
		return (T) manager;
	}
	
	public static <T> T getContextElementValue(Object instance, String fieldName)
	{
		IDIContextElement elem = getContextElement(instance, fieldName);
		
		return elem == null ? null : (T) elem.getValue();
	}
	
	public static IDIContextElement getContextElement(Object instance, String fieldName)
	{
		return getContextElement(instance, ReflectionUtils.findField(instance, fieldName));
	}
	
	public static IDIContextElement getContextElement(Object instance, Field field)
	{
		DIContext context = DIRegistry.getRegisteredContext(DIRegistry.getFieldContext(field, instance));
		return context == null ? null : context.getFieldElement(instance.getClass(), field);
	}
	
	public static void printContext(String contextName)
	{
		DIManager manager = getRegisteredManager(contextName);
		
		if (manager == null)
			return;
		
		DIContext context = manager.context;
		
		if (context == null)
			return;
		
		DynamicString str = new DynamicString();
		
		str.append("Context '", contextName, "': ");
		
		for (Map.Entry<String, Map<Class<?>, IDIContextElement>> elementsByName : context.contextElements.entrySet())
		{
			str.append(elementsByName.getKey(), " = {\n");
			
			for (Map.Entry<Class<?>, IDIContextElement> elementsForType : elementsByName.getValue().entrySet())
			{
				str.append(elementsForType.getKey() + " = " + elementsByName.getValue(), "\n");
			}
			
			str.append("}, \n");
		}
		
		logger.info(str);
	}
	
	/** Получить зарегистрированный менеджер */
	public static <T extends DIManager> T getRegisteredManager(@ConcattedString Object... name)
	{
		String s = StringUtils.concat(name);
		return (T) registeredManagers.get(s);
	}
	
	public static void registerContextPattern(String contextName, @ConcattedString Object... pattern)
	{
		contextPatterns.put(StringUtils.concat(pattern), contextName);
	}
	
	/** Получить элемент указанного типа с указанным именем из контекста */
	public static <T> T getContextElement(String contextName, String elementName, String elementType)
	{
		DIManager manager = getRegisteredManager(contextName);
		
		if (manager == null)
			return null;
		
		DIContext context = manager.context;
		
		if (context == null)
			return null;
		
		IDIContextElement contextElement = context.getContextElement(elementName, false, elementType);
		
		return contextElement == null ? null : (T) contextElement.getValue();
	}
	
	/** Зарегистрировать менеджер */
	public static <T extends DIManager> T registerManager(DIManager manager, @ConcattedString Object... name)
	{
		String s = StringUtils.concat(name);
		
		if (registeredManagers.containsKey(s))
			logger.warning(new Throwable(), "Overwriting di manager:", s);
		
		DIRegistry.registerDefultIfNotFound(manager);
		
		registeredManagers.put(s, manager);
		
		return (T) manager;
	}
	
	public static String getClassContextName(Class<?> cl)
	{
		String classNormalName = cl.getName();
		String current = null;
		int maxLen = -1;
		
		if (isLoggingContextSearch)
			logger.info("Getting context name for class", classNormalName, contextPatterns);
		
		DISwL annotation = cl.getAnnotation(DISwL.class);
		
		if (annotation != null)
		{
			current = annotation.context();
		}
		
		if (StringUtils.isEmpty(current))
		{
			for (String str : contextPatterns.keySet())
			{
				if (isLoggingContextSearch)
					logger.info("Checking pattern", str);
				
				if (classNormalName.startsWith(str))
				{
					int len = str.length();
					
					if (len > maxLen)
					{
						current = contextPatterns.get(str);
						maxLen = len;
					}
					else
					{
						if (isLoggingContextSearch)
							logger.info("Pattern to small!");
					}
				}
				else
				{
					if (isLoggingContextSearch)
						logger.info("Pattern", str, "is invalid for class", classNormalName);
				}
			}
		}
		
		String ret = StringUtils.isEmpty(current) ? "default" : current;
		
		if (isLoggingContextSearch)
			logger.info("Finally, default context is", ret);
		
		return ret;
	}
	
	/** Функция, которая определяет контекст, который будет использован для поля */
	@AllArgsConstructor(staticName = "of")
	public static class AnnotationContextFunction implements IFunction2<Field, DISwL, DIContext>
	{
		public String defaultContextName;

		@Override
		public DIContext apply(Field field, DISwL annotation)
		{
			if (field == null || annotation == null)
				return null;
			
			String name = annotation.context();
			
			if (StringUtils.isEmpty(name))
				name = defaultContextName;
			
			DIManager manager = getRegisteredManager(name);
			
			if (manager == null)
				manager = getRegisteredManager(defaultContextName);
			
			if (manager == null)
				return null;
			
			return manager.context;
		}
	}
}
