package ru.swayfarer.swl2.collections.extended;

import java.util.Map;

import ru.swayfarer.swl2.collections.streams.DataStream;
import ru.swayfarer.swl2.collections.streams.IDataStream;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1NoR;

@SuppressWarnings("unchecked")
public interface IExtendedMap<K, V> extends Map<K, V> {

	public default void each(IFunction1NoR<Map.Entry<K, V>> fun)
	{
		entrySet().forEach(fun.asJavaConsumer());
	}
	
	public default <T> T getValue(K key)
	{
		return (T) get(key);
	}
	
	public default IDataStream<Map.Entry<K, V>> dataStream()
	{
		return DataStream.of(entrySet());
	}
	
	public default IDataStream<Map.Entry<K, V>> parrallelDataStream()
	{
		return DataStream.of(entrySet()).setParrallel(true);
	}
}
