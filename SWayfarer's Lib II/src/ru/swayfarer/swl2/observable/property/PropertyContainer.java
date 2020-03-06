package ru.swayfarer.swl2.observable.property;

import ru.swayfarer.swl2.exceptions.ExceptionsUtils;

/**
 * Контейнер, позвояющий удобно преобразовывать хранимое в нем значение 
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class PropertyContainer {

	/** Значние */
	public Object value;

	/** Получить byte-значение */
	public byte getByteValue()
	{
		return getNumberValue().byteValue();
	}

	/** Получить short-значение */
	public short getShortValue()
	{
		return getNumberValue().shortValue();
	}

	/** Получить int-значение */
	public int getIntValue()
	{
		return getNumberValue().intValue();
	}

	/** Получить long-значение */
	public long getLongValue()
	{
		return getNumberValue().longValue();
	}
	
	/** Получить float-значение */
	public float getFloatValue()
	{
		return getNumberValue().floatValue();
	}
	
	/** Получить double-значение */
	public double getDoubleValue()
	{
		return getNumberValue().doubleValue();
	}
	
	/** Получить значение */
	public <T> T getValue()
	{
		return (T) value;
	}
	
	/** Получить голое значение */
	public Object get()
	{
		return value;
	}
	
	/** null ли значение?*/
	public boolean isNull()
	{
		return value == null;
	}
	
	/** 
	 * Получить значение как число
	 * @throws IllegalStateException если хранимое число не null
	 */
	public Number getNumberValue()
	{
		if (value == null)
			return 0;
		
		ExceptionsUtils.IfNot(value instanceof Number, IllegalStateException.class, "Value of property is not a number!");
		
		return (Number) value;
	}
	
}
