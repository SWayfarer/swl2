package ru.swayfarer.swl2.ioc.context.injector;

import java.lang.reflect.Field;
import java.util.IdentityHashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.var;
import ru.swayfarer.swl2.asm.transformer.ditransformer.regetter.visitor.DynamicDI;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2;
import ru.swayfarer.swl2.generics.GenericObject;
import ru.swayfarer.swl2.ioc.DIManager.DISwL;
import ru.swayfarer.swl2.ioc.DIAnnotation;
import ru.swayfarer.swl2.ioc.DIRegistry;
import ru.swayfarer.swl2.ioc.FindInContext;
import ru.swayfarer.swl2.ioc.context.DIContext;
import ru.swayfarer.swl2.ioc.context.elements.IDIContextElement;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Рефлективный иньектор
 * 
 * @author swayfarer
 *
 */
public class ReflectionDIInjector {

	/**
	 * Кэшированные иньекторы Самое долгое в рефлексии - создание аксессоров
	 * полей. Для этого они кэшируются для повторного использования
	 */
	public static Map<Class<?>, ReflectionDIInjector> cachedInjectors = new IdentityHashMap<>();

	/** Список полей, к которым был получен доступ */
	public IExtendedList<InjectFieldDIInfo> injectFields = CollectionsSWL.createExtendedList();
	
	/** Список полей, к которым был получен доступ */
	public IExtendedList<FindFieldDIInfo> findFields = CollectionsSWL.createExtendedList();

	/** Логгер */
	public static ILogger logger = LoggingManager.getLogger();

	/** Использовать ли кэширование */
	public static boolean isUsingCache = true;
	
	/**
	 * Конструктор
	 * 
	 * @param cl
	 *            Класс, в который будут сгененированы аксессоры
	 */
	public ReflectionDIInjector(Class<?> cl)
	{
		logger.safe(() -> {

			for (Field field : cl.getDeclaredFields())
			{ 
				FindInContext findInContext = field.getAnnotation(FindInContext.class);
				DISwL annotation = ReflectionUtils.findAnnotationRec(field, DISwL.class); // field.getAnnotation(DISwL.class);
				DynamicDI dynamicDI = field.getAnnotation(DynamicDI.class);
				
				if (dynamicDI == null)
				{
					if (annotation != null)
					{
						if (ReflectionUtils.setAccessible(field))
						{
							injectFields.add(InjectFieldDIInfo.of(field, annotation));
						}
						else
						{
							logger.error("Can't access the field", field, ". The field will be skipped during injection...");
						}
					}
					else 
					{
						if (ReflectionUtils.setAccessible(field))
						{
							findFields.add(FindFieldDIInfo.of(field, findInContext));
						}
						else
						{
							logger.error("Can't access the field", field, ". The field will be skipped during injection...");
						}
					}
				}
			}

		}, "Error while reading fields from", cl);
	}

	/**
	 * Иньекция контекста
	 * 
	 * @param context
	 *            Контекст, объекты которого будут иньектиться
	 * @param obj
	 *            Объект, в который будут проводиться иньекции
	 */
	public void injectContext(DIContext context, Object obj)
	{
		injectContext((field, annotation) -> context, obj);
	}

	/**
	 * Иньекция контекста
	 * 
	 * @param contextFun
	 *            Функция, возвращающаа контекст, объекты которого будут
	 *            иньектиться
	 * @param obj
	 *            Объект, в который будут проводиться иньекции
	 */
	public void injectContext(IFunction2<Field, DISwL, DIContext> contextFun, Object obj)
	{
		logger.safe(() -> {

			for (ReflectionDIInjector.InjectFieldDIInfo fieldInfo : injectFields)
			{
				injectToField(obj, fieldInfo, contextFun.apply(fieldInfo.field, fieldInfo.annotation));
			}
			
		}, "Error while injecting context to", obj);
	}
	
	public static void injectToField(Object instance, String fieldName) throws IllegalArgumentException, IllegalAccessException
	{
		injectToField(instance, ReflectionUtils.findField(instance, fieldName));
	}
	
	public static void injectToField(Object instance, Field field) throws IllegalArgumentException, IllegalAccessException 
	{
		FindInContext findInContext = ReflectionUtils.findAnnotationRec(field, FindInContext.class);
		
		DISwL annotation = ReflectionUtils.findAnnotationRec(field, DISwL.class); //field.getAnnotation(DISwL.class);
		Class<?> cl = instance.getClass();
		
		if (findInContext != null)
		{
			String contextName = DIAnnotation.getFindAnnotationContextName(findInContext);
			
			if (StringUtils.isBlank(contextName))
			{
				contextName = DIRegistry.getClassContextName(cl);
			}
			
			var context = DIRegistry.getRegisteredContext(contextName);
			
			if (context != null)
			{
				var generics = GenericObject.ofField(field);
				
				if (generics == null || generics.size() != 1)
				{
					return;
				}
				
				
			}
		}
		else if (annotation != null)
		{
			String context = DIAnnotation.getElementName(annotation);
			
			if (StringUtils.isBlank(context))
			{
				context = DIRegistry.getClassContextName(cl);
			}
			
			injectToField(instance, InjectFieldDIInfo.of(field, annotation), DIRegistry.getRegisteredContext(context));
		}
	}
	
	public static void injectToField(Object instance, ReflectionDIInjector.InjectFieldDIInfo fieldInfo, DIContext context) throws IllegalArgumentException, IllegalAccessException 
	{
		Field field = fieldInfo.field;

		if (context == null)
			return;
		
		IDIContextElement element = context.getFieldElement(instance.getClass(), field); //context.getContextElement(name, !fieldInfo.annotation.usingName(), field.getType());

		if (element != null)
		{
			Object value = element.getValue();

			if (DIRegistry.isLoggingFields)
				logger.info("Injecting... ", field, "=", value);

			field.set(instance, value);
		}
	}
	
	/**
	 * Иньекция контекста
	 * 
	 * @param context
	 *            Контекст, объекты которого будут иньектиться
	 * @param objects
	 *            Объекты, в которые будут проводиться иньекции
	 */
	public static void inject(DIContext context, Object... objects)
	{
		inject((field, annotation) -> context, objects);
	}

	/**
	 * Иньекция контекста
	 * 
	 * @param contextFun
	 *            Генератор контекста, объекты которого будут иньектиться
	 * @param objects
	 *            Объекты, в которые будут проводиться иньекции
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

	/**
	 * Информация о поле
	 * @author swayfarer
	 *
	 */
	@InternalElement
	@AllArgsConstructor(staticName = "of")
	public static class FindFieldDIInfo {
		
		/** Поле */
		@InternalElement
		public Field field;
		
		/** Аннотация {@link DISwL}, которой отмечено поле */
		@InternalElement
		public FindInContext annotation;

	}
	
	/**
	 * Информация о поле
	 * @author swayfarer
	 *
	 */
	@InternalElement
	@AllArgsConstructor(staticName = "of")
	public static class InjectFieldDIInfo {
		
		/** Поле */
		@InternalElement
		public Field field;
		
		/** Аннотация {@link DISwL}, которой отмечено поле */
		@InternalElement
		public DISwL annotation;

	}
}