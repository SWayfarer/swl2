package ru.swayfarer.swl2.ioc;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2;
import ru.swayfarer.swl2.ioc.DIManager.DIContext;
import ru.swayfarer.swl2.ioc.DIManager.DISwL;
import ru.swayfarer.swl2.ioc.DIManager.IDIContextElement;
import ru.swayfarer.swl2.ioc.DIManager.ReflectionDIInjector;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Меджер регистрации {@link DIManager}'ов
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class DIRegistry {

	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Зарегистрированные {@link DIManager}'ы */
	@InternalElement
	public static Map<String, DIManager> registeredManagers = new HashMap<>();
	
	/** Иньекция контекста в объект с указанием дефолтного контекста */
	public static <T> T injectToObject(String defaultContext, Object object)
	{	
		logger.info("Injecting context", defaultContext, "to object", object);
		ReflectionDIInjector.inject(AnnotationContextFunction.of(defaultContext), object);
		return (T) object;
	}
	
	/** Зарегистрировать контекст как стандартный, если его еще нет*/
	public static <T extends DIManager> T registerDefultIfNotFound(DIManager manager)
	{
		if (registeredManagers.get("default") == null)
			registeredManagers.put("default", manager);
		
		return (T) manager;
	}
	
	/** Получить зарегистрированный менеджер */
	public static <T extends DIManager> T getRegisteredManager(@ConcattedString Object... name)
	{
		String s = StringUtils.concat(name);
		return (T) registeredManagers.get(s);
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
		
		IDIContextElement contextElement = context.getContextElement(elementName, elementType);
		
		return contextElement == null ? null : (T) contextElement.getValue();
	}
	
	/** Зарегистрировать менеджер */
	public static <T extends DIManager> T registerManager(DIManager manager, @ConcattedString Object... name)
	{
		String s = StringUtils.concat(name);
		
		if (registeredManagers.containsKey(s))
			logger.warning("Overwriting di manager:", s);
		
		registeredManagers.put(s, manager);
		
		return (T) manager;
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
