package ru.swayfarer.swl2.asm;

import java.util.HashMap;
import java.util.Map;

/**
 * Класслоадер, загружающий класс из байтов
 * @author swayfarer
 *
 */
public class BytesClassLoader extends ClassLoader{

	public static BytesClassLoader instance = new BytesClassLoader();
	
	public static Map<String, Class<?>> cachedClasses = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	public static <T> Class<T> loadClass(String name, byte[] bytes)
	{
		name = name.replace("/", ".");
		
		if (cachedClasses.containsKey(name))
			return (Class<T>) cachedClasses.get(name);
		
		Class<?> ret = (Class<T>)instance.defineClass(name, bytes, 0, bytes.length);
		
		if (ret != null)
		{
			cachedClasses.put(name, ret);
		}
		
		return (Class<T>) ret;
	}
	
}
