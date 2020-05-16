package ru.swayfarer.swl2.string.property;

import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.string.StringUtils;

@SuppressWarnings("unchecked")
public class SystemProperty extends StringProperty {

	public String name;
	
	public SystemProperty(String name)
	{
		this(name, (Object[]) null);
	}
	
	public SystemProperty(String name, @ConcattedString Object... defaultValue)
	{
		this.name = name;
		rawValue = System.getProperty(name);
		
		if (rawValue == null)
			rawValue = StringUtils.concat(defaultValue);
	}
	
	public <T extends SystemProperty> T setValue(@ConcattedString Object... value) 
	{
		this.rawValue = StringUtils.concat(value);
		System.setProperty(name, rawValue);
		return (T) this;
	}
	
	public String getName()
	{
		return name;
	}
	
	public <T extends SystemProperty> T setName(@ConcattedString Object... name) 
	{
		this.name = StringUtils.concat(name);
		return (T) this;
	}
	
}
