package ru.swayfarer.swl2.threads;

import java.util.Map;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction0;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction0NoR;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Утилиты для работы с потоками 
 * @author swayfarer
 *
 */
@SuppressWarnings({"rawtypes", "unchecked"} )
public class ThreadsUtils {

	/** Зарегистрированные {@link ThreadLocal}'ы относительно их функций-факторей */
	public static Map<Class<?>, ThreadLocal> cachedThreadLocals = CollectionsSWL.createIdentityMap();
	
	/** Безопасный {@link Thread#sleep(long)} */
	public static void sleepSafe(long milisis)
	{
		ExceptionsUtils.safe(() -> Thread.sleep(milisis));
	}
	
	public static boolean isThis(Thread thread)
	{
		return Thread.currentThread() == thread;
	}
	
	/** Запустить в новом потоке */
	public static Thread newThread(IFunction0NoR run, boolean isDaemon)
	{
		return newThread(null, run, isDaemon);
	}
	
	/** Запустить в новом потоке */
	public static Thread newThread(String name, IFunction0NoR run, boolean isDaemon)
	{
		Thread thread = StringUtils.isEmpty(name) ? new Thread(run.asJavaRunnable()) : new Thread(run.asJavaRunnable(), name);
		thread.setDaemon(isDaemon);
		thread.start();
		return thread;
	}
	
	/**
	 *  Получить локальное для потока значение через функцию-фактрю 
	 *  <h1> Предупреждение: </h1>
	 *  В качестве ключа используется класс функции-фактори
	 */
	public static <T> T getThreadLocal(IFunction0<T> factoryFun)
	{
		Class<?> key = factoryFun.getClass();
		ThreadLocal<T> cachedThreadLocal = cachedThreadLocals.get(key);
		
		if (cachedThreadLocal == null)
		{
			cachedThreadLocal = new ThreadLocal<>();
			
			synchronized (cachedThreadLocal)
			{
				cachedThreadLocals.put(key, cachedThreadLocal);
			}
		}
		
		T ret = cachedThreadLocal.get();
		
		if (ret == null)
		{
			ret = factoryFun.apply();
			cachedThreadLocal.set(ret);
		}
		
		return ret;
	}
}
