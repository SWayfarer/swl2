package ru.swayfarer.swl2.ioc.context.elements;

import java.lang.reflect.Method;

import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;

/**
 * Функция, вызывающая метод и кэширующая его результат для текущего потока
 * 
 * @author swayfarer
 *
 */
public class ThreadLocalMethodInvokationFun implements IFunction2<Method, Object, Object> {

	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();

	/**
	 * {@link ThreadLocal}, в котором хранятся результаты работы метода для
	 * разных потоков
	 */
	@InternalElement
	public ThreadLocal<Object> threadLocal;

	@Override
	public Object apply(Method method, Object instance)
	{
		if (threadLocal == null)
		{
			threadLocal = new ThreadLocal<Object>()
			{

				protected Object initialValue()
				{

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