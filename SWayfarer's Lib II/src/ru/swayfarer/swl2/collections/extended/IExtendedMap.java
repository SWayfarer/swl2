package ru.swayfarer.swl2.collections.extended;

import java.util.HashMap;
import java.util.Map;

import ru.swayfarer.swl2.collections.streams.DataStream;
import ru.swayfarer.swl2.collections.streams.IDataStream;
import ru.swayfarer.swl2.collections.wrapper.MapWrapper;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1NoR;

/**
 * Расширенная карта
 * @author swayfarer
 *
 * @param <K> Тип Ключа
 * @param <V> Тип Значения
 */
@SuppressWarnings("unchecked")
public interface IExtendedMap<K, V> extends Map<K, V> {

	/**
	 * Выполнить функцию для каждой {@link Entry} 
	 * @param fun Функция, в которую передаются {@link Entry}
	 */
	public default void each(IFunction1NoR<Map.Entry<K, V>> fun)
	{
		entrySet().forEach(fun.asJavaConsumer());
	}
	
	/**
	 * Получить автоматически прикастованный элемент
	 * @param key Ключ, по которому лежит элемент
	 * @return Элемент карты или null, если не найдется
	 */
	public default <T> T getValue(K key)
	{
		return (T) get(key);
	}
	
	/** Получить поток {@link Entry} карты*/
	public default IDataStream<Map.Entry<K, V>> dataStream()
	{
		return DataStream.of(entrySet());
	}

	/** Получить парраллельный поток {@link Entry} карты*/
	public default IDataStream<Map.Entry<K, V>> parrallelDataStream()
	{
		return DataStream.of(entrySet()).setParrallel(true);
	}
	
	/** 
	 * Копировать карту <br> <br>
	 * Скопирована будет карта, а не сами элементы.
	 * @return Копия этой карты
	 */
	public default IExtendedMap<K, V> copy()
	{
		IExtendedMap<K, V> map = new MapWrapper<>(new HashMap<>());
		map.putAll(this);
		return map;
	}
}
