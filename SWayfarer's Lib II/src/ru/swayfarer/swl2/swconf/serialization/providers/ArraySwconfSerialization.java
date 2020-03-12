package ru.swayfarer.swl2.swconf.serialization.providers;

import java.lang.reflect.Array;

import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.swconf.primitives.SwconfArray;
import ru.swayfarer.swl2.swconf.serialization.ISwconfSerializationProvider;
import ru.swayfarer.swl2.swconf.serialization.SwconfSerialization;

/**
 * Провайдер для массивов 
 * @author swayfarer
 *
 */
public class ArraySwconfSerialization implements ISwconfSerializationProvider<SwconfArray, Object> {

	/** Сериализация, которая будет использована для элементов массива */
	@InternalElement
	public SwconfSerialization serialization;
	
	/** Консктруктор */
	public ArraySwconfSerialization(SwconfSerialization serialization)
	{
		ExceptionsUtils.IfNullArg(serialization, "Array's elements serialization can't be null!");
		this.serialization = serialization;
	}
	
	@Override
	public boolean isAccetps(Class<?> type)
	{
		return type.isArray();
	}

	@Override
	public Object deserialize(Class<?> cl, Object obj, SwconfArray swconfObject)
	{
		Object array = Array.newInstance(cl, swconfObject.elements.size());
		
		for (int i1 = 0; i1 < swconfObject.elements.size(); i1 ++)
		{
			Array.set(array, i1, serialization.deserialize(cl, null, swconfObject.elements.get(i1), null));
		}
		
		return array;
	}

	@Override
	public SwconfArray serialize(Object obj)
	{
		SwconfArray array = new SwconfArray();
		
		int lenght = Array.getLength(obj);
		
		for (int i1 = 0; i1 < lenght; i1 ++)
		{
			array.addChild(serialization.serialize(obj.getClass().getComponentType(), Array.get(obj, i1), null));
		}
		
		return array;
	}

	@Override
	public Object createNewInstance(Class<?> classOfObject, SwconfArray swconfObject)
	{
		return null;
	}

}
