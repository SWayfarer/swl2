package ru.swayfarer.swl2.swconf.primitives;

import ru.swayfarer.swl2.string.StringUtils;

/**
 * Число
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class SwconfNum extends SwconfPrimitive {

	/** Получить байтовое значение */
	public byte getByte()
	{
		return getNumber().byteValue();
	}
	
	/** Получить short-значение */
	public short getShort()
	{
		return getNumber().shortValue();
	}
	
	/** Получить int-значение */
	public int getInt()
	{
		return getNumber().intValue();
	}
	
	/** Получить long-значение */
	public long getLong()
	{
		return getNumber().longValue();
	}
	
	/** Получить float-значение */
	public float getFloat()
	{
		return getNumber().floatValue();
	}
	
	/** Получить double-значение */
	public double getDouble()
	{
		return getNumber().doubleValue();
	}
	
	/** Получить {@link Number}-значение */
	public Number getNumber()
	{
		return (Number) rawValue;
	}
	
	/** Задать значение */
	public <T extends SwconfNum> T setValue(double d) 
	{
		this.rawValue = d;
		return (T) this;
	}
	
	@Override
	public boolean isNum()
	{
		return true;
	}
	
	@Override
	public <T extends SwconfPrimitive> T copy()
	{
		return new SwconfNum().setComment(comment).setRawValue(getDouble()).setName(name);
	}
	
	public String toString(int indent)
	{
		return StringUtils.createSpacesSeq(4 * indent) + "num: " + name + " = " + rawValue;
	}
}
