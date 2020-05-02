package ru.swayfarer.swl2.swconf.primitives;

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
}
