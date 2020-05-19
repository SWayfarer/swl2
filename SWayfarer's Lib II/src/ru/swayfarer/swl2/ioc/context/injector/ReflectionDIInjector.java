package ru.swayfarer.swl2.ioc.context.injector;

import java.lang.reflect.Field;
import java.util.IdentityHashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2;
import ru.swayfarer.swl2.ioc.DIManager.DISwL;
import ru.swayfarer.swl2.ioc.DIRegistry;
import ru.swayfarer.swl2.ioc.context.DIContext;
import ru.swayfarer.swl2.ioc.context.elements.IDIContextElement;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
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
	public IExtendedList<ReflectionDIInjector.FieldDIInfo> fields = CollectionsSWL.createExtendedList();

	/** Логгер */
	public ILogger logger = LoggingManager.getLogger();

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

			DIContext context = null;

			for (ReflectionDIInjector.FieldDIInfo fieldInfo : fields)
			{
				Field field = fieldInfo.field;

				String name = fieldInfo.annotation.name();

				if (StringUtils.isEmpty(name))
					name = field.getName();

				context = contextFun.apply(field, fieldInfo.annotation);

				if (context == null)
					continue;

				IDIContextElement element = context.getContextElement(name, !fieldInfo.annotation.usingName(), field.getType());

				if (element != null)
				{
					Object value = element.getValue();

					if (DIRegistry.isLoggingFields)
						logger.info("Injecting... ", field, "=", value);

					field.set(obj, value);
				}
			}
		}, "Error while injecting context to", obj);

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

	@AllArgsConstructor(staticName = "of")
	public static class FieldDIInfo {

		public Field field;
		public DISwL annotation;

	}
}