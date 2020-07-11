package ru.swayfarer.swl2.collections.wrapper;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import ru.swayfarer.swl2.collections.extended.IExtendedMap;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.markers.InternalElement;

/**
 * Обертка на карту
 * @author swayfarer
 *
 * @param <K> Тип Ключа
 * @param <V> Тип Значения
 */
@SuppressWarnings("unchecked")
public class MapWrapper<K, V> implements IExtendedMap<K, V> {

	/** Обернутая карта */
	@InternalElement
	public Map<K, V> wrappedMap;
	
	/**
	 * Конструктор
	 * @param wrappedMap обернутая карта
	 */
	public MapWrapper(Map<K, V> wrappedMap)
	{
		super();
		ExceptionsUtils.IfNullArg(wrappedMap, "Wrapped map can't be null!");
		this.wrappedMap = wrappedMap;
	}

	/**
	 * Задать обернутую карту
	 * @param map Карта, которая будет обернута
	 * @return Этот лист (this)
	 */
	public <T extends MapWrapper<K, V>> T setWrappedMap(Map<K, V> map) 
	{
		ExceptionsUtils.IfNullArg(map, "Wrapped map can't be null!");
		this.wrappedMap = map;
		return (T) this;
	}
	
	@Override
	public int size()
	{
		return wrappedMap.size();
	}

	@Override
	public boolean isEmpty()
	{
		return wrappedMap.isEmpty();
	}

	@Override
	public boolean containsKey(Object key)
	{
		return wrappedMap.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value)
	{
		return wrappedMap.containsValue(value);
	}

	@Override
	public V get(Object key)
	{
		return wrappedMap.get(key);
	}

	@Override
	public V put(K key, V value)
	{
		return wrappedMap.put(key, value);
	}

	@Override
	public V remove(Object key)
	{
		return wrappedMap.remove(key);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m)
	{
		wrappedMap.putAll(m);
	}

	@Override
	public void clear()
	{
		wrappedMap.clear();
	}

	@Override
	public Set<K> keySet()
	{
		return wrappedMap.keySet();
	}

	@Override
	public Collection<V> values()
	{
		return wrappedMap.values();
	}

	@Override
	public Set<Entry<K, V>> entrySet()
	{
		return wrappedMap.entrySet();
	}

	@Override
	public String toString()
	{
		return "MapWrapper(" + wrappedMap.toString() + ")";
	}

	@Override
	public Iterator<Entry<K, V>> iterator() {
		return entrySet().iterator();
	}
}
