package ru.swayfarer.swl2.collections;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import ru.swayfarer.swl2.collections.extended.ExtendedListWrapper;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.SimpleLoggerSWL;
import ru.swayfarer.swl2.markers.Alias;
import ru.swayfarer.swl2.markers.InternalElement;

/**
 * Утилиты для работы с коллекциями 
 * @author swayfarer
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class CollectionsSWL {

	/** Логгер */
	@InternalElement
	public static ILogger logger = new SimpleLoggerSWL().lateinit();
	
	/*
	 * Создание карт
	 */
	
	public static <K, V> Map<K, V> createHashMap()
	{
		return new HashMap<>();
	}
	
	public static <K, V> Map<K, V> createConcurrentHashMap()
	{
		return new ConcurrentHashMap<>();
	}
	
	/*
	 * Создание массивов
	 */
	
	/** Создает массив указанного типа и размера */
	public static <T> T[] createArray(Class<?> cl, int lenght)
	{
		if (cl == null || lenght < 0)
			return null;
		
		return (T[]) Array.newInstance(cl, lenght);
	}
	
	/*
	 * Создание листов
	 */
	
	/** Создает {@link ArrayList} */
	public static <T> List<T> arrayList(Enumeration<T> enumeration)
	{
		List<T> ret = new ArrayList<>();
		
		T next = null;
		
		while (enumeration.hasMoreElements())
		{
			next = enumeration.nextElement();
			ret.add(next);
		}
		
		return ret;
	}
	
	/** Создать раширенный лист с начальными элементами */
	public static <T> IExtendedList<T> createExtendedList(Collection<T> elements)
	{
		if (elements == null || elements.isEmpty())
			return createExtendedList();
		
		return new ExtendedListWrapper<>(new ArrayList<>(elements));
	}
	
	/** Создать раширенный лист с начальными элементами */
	public static <T> IExtendedList<T> createExtendedList(T... elements)
	{
		List<T> wrappedList = new ArrayList<>();
		
		if (!isNullOrEmpty(elements))
			for (T element : elements)
				wrappedList.add(element);
		
		return new ExtendedListWrapper<>(wrappedList);
	}
	
	/** Создать расширенный лист */
	public static <T> IExtendedList<T> createExtendedList()
	{
		return new ExtendedListWrapper<>();
	}
	
	/** Остортировать лист */
	public static <T> List<T> reverse(List<T> list)
	{
		if (isNullOrEmpty(list))
			return list;
		
		Collections.reverse(list);
		
		return list;
	}
	
	/** Остортировать лист */
	public static <T> List<T> sort(List list)
	{
		if (isNullOrEmpty(list))
			return list;
		
		try
		{
			Collections.sort(list);
		}
		catch (Throwable e)
		{
			logger.error(e, "Erorr while sorting list", list);
		}
		
		return (List<T>) list;
	}
	
	/** Коллекция пуста или равна null*/
	public static boolean isNullOrEmpty(Collection<?> collection)
	{
		return collection == null || collection.isEmpty();
	}
	
	/** Перемешать все элементы */
	@Alias("shuffle")
	public static <T> List<T> randomize(List list, long seed)
	{
		return shuffle(list, seed);
	}
	
	/** Перемешать все элементы */
	@Alias("shuffle")
	public static <T> List<T> randomize(List list)
	{
		return shuffle(list);
	}
	
	/** Перемешать все элементы */
	public static <T> List<T> shuffle(List list, long seed)
	{
		list = new ArrayList<>(list);
		Collections.shuffle(list, new Random(seed));
		return list;
	}
	
	/** Перемешать все элементы */
	public static <T> List<T> shuffle(List list)
	{
		list = new ArrayList<>(list);
		Collections.shuffle(list);
		return list;
	}
	
	/** Массив пуст или равен null */
	public static boolean isNullOrEmpty(Object[] arr)
	{
		return arr == null || arr.length == 0;
	}
	
	/** Массив пуст или равен null */
	public static boolean isNullOrEmpty(byte[] arr)
	{
		return arr == null || arr.length == 0;
	}
}
