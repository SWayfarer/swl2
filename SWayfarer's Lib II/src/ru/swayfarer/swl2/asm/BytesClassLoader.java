package ru.swayfarer.swl2.asm;

import java.util.HashMap;
import java.util.Map;

import ru.swayfarer.swl2.markers.InternalElement;

/**
 * Класслоадер, загружающий класс из переданных байтов
 * @author swayfarer
 */
public class BytesClassLoader extends ClassLoader{

	/** Класслоадер, который и делает всю магию */
	@InternalElement
	public static BytesClassLoader instance = new BytesClassLoader();
	
	/** Карта кэшированных классов */
	@InternalElement
	public static Map<String, Class<?>> cachedClasses = new HashMap<>();
	
	/** Загрузить класс из указанных байтов под определенным именем */
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
