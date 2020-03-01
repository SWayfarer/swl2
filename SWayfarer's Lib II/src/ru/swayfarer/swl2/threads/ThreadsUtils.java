package ru.swayfarer.swl2.threads;

import java.util.Map;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction0;

/**
 * Утилиты для работы с потоками 
 * @author swayfarer
 *
 */
@SuppressWarnings({"rawtypes", "unchecked"} )
public class ThreadsUtils {

	/** Зарегистрированные {@link ThreadLocal}'ы относительно их функций-факторей */
	public static Map<Class<?>, ThreadLocal> cachedThreadLocals = CollectionsSWL.createIdentityMap();
	
	/**
	 *  Получить локальное для потока значение через функцию-фактрю 
	 *  <h1> Предупреждение: </h1>
	 *  В качестве ключа используется класс функции-фактори
	 */
	public static <T> T getThreadLocal(IFunction0<T> factoryFun)
	{
		Class<?> key = factoryFun.getClass();
		ThreadLocal<T> cachedThreadLocal = cachedThreadLocals.get(key);
		
		if (cachedThreadLocal != null)
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
