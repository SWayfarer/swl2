package ru.swayfarer.swl2.collections;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import ru.swayfarer.swl2.binary.buffers.DynamicByteBuffer;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.extended.ExtendedListWrapper;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.collections.extended.IExtendedMap;
import ru.swayfarer.swl2.collections.observable.IObservableList;
import ru.swayfarer.swl2.collections.observable.ObservableListWrapper;
import ru.swayfarer.swl2.collections.weak.WeakList;
import ru.swayfarer.swl2.collections.wrapper.MapWrapper;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.SimpleLoggerSWL;
import ru.swayfarer.swl2.markers.Alias;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Утилиты для работы с коллекциями 
 * @author swayfarer
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class CollectionsSWL {

	/** Логгер */
	@InternalElement
	public static ILogger logger = new SimpleLoggerSWL().lateinit();
	
	public static String getArrayString(Object obj)
	{
		ExceptionsUtils.IfNull(obj, IllegalArgumentException.class, "Object for string generation can't be null!");
		ExceptionsUtils.If(!ReflectionUtils.isArray(obj), IllegalArgumentException.class, "Object for string generation must be a array!");
		
		ExtendedListWrapper<?> list = ExtendedListWrapper.valueOf(obj); 
		
		String s = StringUtils.concat(", ", list.toArray());
		
		return obj.getClass().getComponentType().getSimpleName() + ": {" + s + "}";
	}
	
	/*
	 * Создание карт
	 */
	
	/** Создать расширенную карту */
	public static <K, V> IExtendedMap<K, V> createExtendedMap()
	{
		return new MapWrapper<>(createHashMap());
	}
	
	/** Создать расширенную карту с указанием контента */
	public static <K, V> IExtendedMap<K, V> createExtendedMap(Object... content)
	{
		return new MapWrapper<>(createHashMap(content));
	}
	
	/** Создать {@link HashMap} */
	public static <K, V> Map<K, V> createHashMap()
	{
		return new LinkedHashMap<>();
	}
	
	/** Создать конкуррентную очередь */
	public static <T> Queue<T> createConcurrentQueue()
	{
		return new ConcurrentLinkedDeque<>();
	}
	
	public static <K, V> Map<K, V> createLinkedMap()
	{
		return new LinkedHashMap<>();
	}
	
	/** Создать {@link IdentityHashMap} */
	public static <K, V> Map<K, V> createIdentityMap()
	{
		return new HashMap<>();
	}
	
	/** Создать конкруррентную {@link HashMap} */
	public static <K, V> Map<K, V> createConcurrentHashMap()
	{
		return new ConcurrentHashMap<>();
	}
	
	/** Создать {@link HashMap} */
	public static <K, V> Map<K, V> createHashMap(Object... content)
	{
		if (!CollectionsSWL.isNullOrEmpty(content))
		{
			ExceptionsUtils.If(content.length % 2 != 0, IllegalArgumentException.class, "Content lenght % 2 must be a zero!");
			
			Map<K, V> map = createHashMap();
			
			Object key = null;
			boolean isKey = true;
			
			for (Object obj : content)
			{
				if (isKey)
					key = obj;
				else
					map.put((K) key, (V) obj);
				
				isKey = !isKey;
			}
			
			return map;
		}
		
		return new HashMap<>();
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
	public static <T> List<T> createArrayList(Enumeration<T> enumeration)
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
	
	/** Создать новый слабый лист */
	public static <T> WeakList<T> createWeakList()
	{
		return new WeakList<>();
	}
	
	/** Создать слабую карту */
	public static <K, V> Map<K, V> createWeakMap()
	{
		return new WeakHashMap<>();
	}
	
	public static Class<?> getListType(final List<?> aListWithSomeType) throws ClassNotFoundException
	{
		return Class.forName(((ParameterizedType) aListWithSomeType.getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName());
	}
	
	/** Создать раширенный лист с начальными элементами */
	public static <T> IExtendedList<T> createExtendedList(Collection<T> elements)
	{
		if (elements == null || elements.isEmpty())
			return createExtendedList();
		
		return new ExtendedListWrapper<>(new ArrayList<>(elements));
	}
	
	/** Создать раширенный лист с начальными элементами */
	public static <T> IExtendedList<T> createExtendedList(Enumeration<? extends T> enumeration)
	{
		if (enumeration == null || !enumeration.hasMoreElements())
			return createExtendedList();
		
		IExtendedList<T> ret = CollectionsSWL.createExtendedList();
		
		while (enumeration.hasMoreElements())
		{
			T elem = enumeration.nextElement();
			ret.add(elem);
		}
		
		return ret;
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
	
	/** Создать наблюдаемый лист */
	public static <T> IObservableList<T> createObservableList()
	{
		return new ObservableListWrapper<>();
	}
	
	/** Создать наблюдаемый лист */
	public static <T> IObservableList<T> createObservableList(T... elements)
	{
		return new ObservableListWrapper<>(createExtendedList(elements));
	}
	
	/** Создать раширенный лист с начальными элементами */
	public static IExtendedList<Character> createExtendedList(char element)
	{
		return createExtendedList(new char[] {element});
	}
	
	/** Создать раширенный лист с начальными элементами */
	public static IExtendedList<Character> createExtendedList(char... elements)
	{
		List<Character> wrappedList = new ArrayList<>();
		
		if (elements != null && elements.length != 0)
			for (char element : elements)
				wrappedList.add(element);
		
		return new ExtendedListWrapper<>(wrappedList);
	}
	
	/** Создать раширенный лист с заданной капасити */
	public static <T> IExtendedList<T> createExtendedList(int capacity)
	{
		List<T> wrappedList = new ArrayList<>(capacity);
		
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
	
	/** Карта пуста или равна null*/
	public static boolean isNullOrEmpty(Map<?, ?> map)
	{
		return map == null || map.isEmpty();
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
	
	/** Получить последний элемент листа */
	public static <T> T getLastElement(List<T> list)
	{
		return isNullOrEmpty(list) ? null : list.get(list.size() - 1);
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
	
	/** Массив пуст или равен null */
	public static boolean isNullOrEmpty(int[] arr)
	{
		return arr == null || arr.length == 0;
	}
	
	/** Буффер пуст или равен null */
	public static boolean isNullOrEmpty(DynamicByteBuffer buffer)
	{
		return buffer == null || buffer.isEmpty();
	}
	
	
}
