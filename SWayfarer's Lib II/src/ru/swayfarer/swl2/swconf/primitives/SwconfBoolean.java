package ru.swayfarer.swl2.swconf.primitives;

import ru.swayfarer.swl2.string.StringUtils;

/**
 * Булиевое значение в Swconf
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class SwconfBoolean extends SwconfPrimitive {

	/** Получить значение */
	public boolean getValue()
	{
		return (Boolean) rawValue;
	}
	
	/** Задать значение */
	public <T extends SwconfBoolean> T setValue(boolean value) 
	{
		this.rawValue = value;
		return (T) this;
	}
	
	@Override
	public boolean isBoolean()
	{
		return true;
	}
	
	@Override
	public <T extends SwconfPrimitive> T copy()
	{
		return new SwconfBoolean().setName(name).setComment(comment).setRawValue(getValue());
	}
	
	public String toString(int indent)
	{
		return StringUtils.createSpacesSeq(4 * indent) + "boolean: " + name + " = " + rawValue;
	}
}
