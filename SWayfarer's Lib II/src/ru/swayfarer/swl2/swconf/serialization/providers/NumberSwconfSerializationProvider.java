package ru.swayfarer.swl2.swconf.serialization.providers;

import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.equals.EqualsUtils;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.swconf.primitives.SwconfNum;
import ru.swayfarer.swl2.swconf.serialization.ISwconfSerializationProvider;

/**
 * Провайдер для чисел 
 * @author swayfarer
 *
 */
public class NumberSwconfSerializationProvider implements ISwconfSerializationProvider<SwconfNum, Object> {

	/** Дефолтное значение, которое возаращается, если десериализация не удалась */
	@InternalElement
	public int defaultValue = Short.MIN_VALUE;
	
	@Override
	public boolean isAccetps(Class<?> type)
	{
		return ReflectionUtils.isNumber(type);
	}

	@Override
	public Object deserialize(Class<?> cl, Object obj, SwconfNum swconfObject)
	{
		if (EqualsUtils.objectEqualsSome(cl, byte.class, Byte.class))
		{
			return swconfObject.getByte();
		}
		else if (EqualsUtils.objectEqualsSome(cl, short.class, Short.class))
		{
			return swconfObject.getShort();
		}
		else if (EqualsUtils.objectEqualsSome(cl, int.class, Integer.class))
		{
			return swconfObject.getInt();
		}
		else if (EqualsUtils.objectEqualsSome(cl, long.class, Long.class))
		{
			return swconfObject.getLong();
		}
		else if (EqualsUtils.objectEqualsAll(cl, float.class, Float.class))
		{
			return swconfObject.getFloat();
		}
		else if (EqualsUtils.objectEqualsSome(cl, double.class, Double.class))
		{
			return swconfObject.getDouble();
		}
		
		return defaultValue;
	}

	@Override
	public Object createNewInstance(Class<?> classOfObject, SwconfNum swconfObject)
	{
		return null;
	}

	@Override
	public SwconfNum serialize(Object obj)
	{
		Number number = (Number) obj;
		
		return new SwconfNum().setValue(number.doubleValue());
	}

}
