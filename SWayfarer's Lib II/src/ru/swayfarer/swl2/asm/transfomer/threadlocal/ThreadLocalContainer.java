package ru.swayfarer.swl2.asm.transfomer.threadlocal;

import java.util.Map;

import java.lang.ThreadLocal;
import ru.swayfarer.swl2.collections.CollectionsSWL;

@SuppressWarnings({"unchecked", "rawtypes"} )
public class ThreadLocalContainer {

	public static ThreadLocal<Map<String, ThreadLocal>> registeredThreadLocals = new ThreadLocal<>();
		
	public static boolean hasRegisteredThreadLocal(String key)
	{
		return getThreadLocalMap().get(key) != null;
	}
	
	public static void setThreadLocalObject(String key, Object obj)
	{
		getThreadLocal(key).set(obj);
	}
	
	public static Object getThreadLocalObject(String key)
	{
		return getThreadLocal(key).get();
	}
	
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
