package ru.swayfarer.swl2.swconf.primitives;

import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Строка в Swconf
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class SwconfString extends SwconfPrimitive {

	/** Получить значение */
	public String getValue()
	{
		return String.valueOf(rawValue);
	}
	
	/** Задать значение */
	public <T extends SwconfString> T setValue(@ConcattedString Object... text) 
	{
		this.rawValue = StringUtils.concat(text);
		return (T) this;
	}
	
	@Override
	public boolean isString()
	{
		return true;
	}
	
	@Override
	public <T extends SwconfPrimitive> T copy()
	{
		return new SwconfString().setName(name).setComment(comment).setRawValue(getValue());
	}
}
