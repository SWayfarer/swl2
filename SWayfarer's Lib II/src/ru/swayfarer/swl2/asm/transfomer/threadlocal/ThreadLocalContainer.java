package ru.swayfarer.swl2.asm.transfomer.threadlocal;

import java.util.Map;

import java.lang.ThreadLocal;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.markers.InternalElement;

/**
 * Контейнер, хранящий ThreadLocal'ы <br> 
 * Позволяет статическим вызовом получить {@link ThreadLocal} объект <br>
 * Необходим для работы аннотации {@link ThreadLocal}
 * @author swayfarer
 *
 */
@SuppressWarnings({"unchecked", "rawtypes"} )
public class ThreadLocalContainer {
	
	/** Зарегистрированные {@link ThreadLocal}'ы */
	@InternalElement
	public static ThreadLocal<Map<String, ThreadLocal>> registeredThreadLocals = new ThreadLocal<>();
		
	/** Есть ли зарегистрированный {@link ThreadLocal} с таким именем? */
	public static boolean hasRegisteredThreadLocal(String key)
	{
		return getThreadLocalMap().get(key) != null;
	}
	
	/** 
	 * Задать объект 
	 * @param key Ключ, по которому будет доступен объект
	 * @param obj Задаваемый объект
	 */
	public static void setThreadLocalObject(String key, Object obj)
	{
		getThreadLocal(key).set(obj);
	}
	
	/**
	 * Получить объект по ключу
	 * @param key ключ, под которым хранится {@link ThreadLocal}
	 * @return Полученный объект или null, если не найдется
	 */
	public static Object getThreadLocalObject(String key)
	{
		return getThreadLocal(key).get();
	}
	
	/**
	 * Получить карту с {@link ThreadLocal} объектами для текущего потока
	 * @return Полученная карта или null
	 */
	public static Map<String, ThreadLocal> getThreadLocalMap()
	{
		Map<String, ThreadLocal> map = registeredThreadLocals.get();
		
		if (map == null)
		{
			map = CollectionsSWL.createHashMap();
			registeredThreadLocals.set(map);
		}
		
		return map;
	}
	
	/**
	 * Получить {@link ThreadLocal} по ключу
	 * @param key Ключ, под которым хранится {@link ThreadLocal}
	 * @return Полученный {@link ThreadLocal} или null, если не найдется
	 */
	public static ThreadLocal getThreadLocal(String key)
	{
		Map<String, ThreadLocal> threadLocalMap = getThreadLocalMap();
		
		ThreadLocal<Object> threadLocal = threadLocalMap.get(key);
		
		if (threadLocal == null)
		{
			threadLocal = new ThreadLocal<>();
			threadLocalMap.put(key, threadLocal);
		}
		
		return threadLocal;
	}
	
}
