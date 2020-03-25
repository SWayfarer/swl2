package ru.swayfarer.swl2.string.property;

import lombok.ToString;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Обертка над {@link String}, которая позволяет удобно представлять ее как другие типы
 * @author swayfarer
 *
 */
@ToString
public class StringProperty {

	/** Чистое значение */
	@InternalElement
	public String rawValue;
	
	/** Конструктор */
	public StringProperty(@ConcattedString Object... text)
	{
		rawValue = StringUtils.concat(text);
	}
	
	/** Получить {@link Boolean}-значение */
	public boolean getBooleanValue()
	{
		if (rawValue == null)
			return false;
		
		ExceptionsUtils.IfNot(isBoolean(), IllegalStateException.class, "Current value is not a boolean: " + rawValue);
		return Boolean.valueOf(rawValue);
	}
	
	/** Получить байтовое значение */
	public byte getByteValue()
	{
		Number num = getNumValue();
		return num == null ? -1 : num.byteValue();
	}
	
	/** Получить {@link Double}-значение */
	public double getDoubleValue()
	{
		Number num = getNumValue();
		return num == null ? -1 : num.doubleValue();
	}
	
	/** Получить {@link Float}-значение */
	public float getFloatValue()
	{
		Number num = getNumValue();
		return num == null ? -1 : num.floatValue();
	}
	
	/** Получить {@link Integer}-значение */
	public int getIntValue()
	{
		Number num = getNumValue();
		return num == null ? -1 : num.intValue();
	}
	
	/** Получить {@link Long}-значение */
	public long getLongValue()
	{
		Number num = getNumValue();
		return num == null ? -1 : num.longValue();
	}
	
	/** Получить {@link Number}-значение */
	public Number getNumValue()
	{
		if (rawValue == null)
			return null;
		
		ExceptionsUtils.IfNot(isNumber(), IllegalStateException.class, "Current value is not a double: " + rawValue);
		return Double.valueOf(rawValue);
	}
	
	/** Получить {@link Short}-значение */
	public short getShortValue()
	{
		Number num = getNumValue();
		return num == null ? null : num.shortValue();
	}
	
	/** Получить {@link String}-значение (оригинальное значение проперти) */
	public String getValue()
	{
		return rawValue;
	}
	
	/** Является ли Boolean? */
	public boolean isBoolean()
	{
		return StringUtils.isBoolean(rawValue);
	}
	
	/** Пустая ли? (см {@link StringUtils#isEmpty(Object)}) */
	public boolean isEmpty()
	{
		return StringUtils.isEmpty(rawValue);
	}
	
	/** Является ли null? */
	public boolean isNull()
	{
		return rawValue == null;
	}
	
	/** Является ли числом? */
	public boolean isNumber()
	{
		return StringUtils.isDouble(rawValue);
	}
	
}
