package ru.swayfarer.swl2.xml;

import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.string.StringUtils;

@SuppressWarnings("unchecked")
public class XmlAttribute {

	public String name;
	public String value;
	
	public XmlAttribute(String name, @ConcattedString Object... value)
	{
		super();
		this.name = name;
		this.value = StringUtils.concat(value);
	}
	
	public <T extends XmlAttribute> T setName(@ConcattedString Object... text) 
	{
		this.name = StringUtils.concat(text);
		return (T) this;
	}
	
	public <T extends XmlAttribute> T setValue(@ConcattedString Object... value) 
	{
		this.value = StringUtils.concat(value);
		return (T) this;
	}
	
	public String getValue()
	{
		return value;
	}
	
	public boolean isNum()
	{
		return StringUtils.isDouble(value);
	}
	
	public boolean isBoolean()
	{
		return StringUtils.isBoolean(value);
	}
	
	public Number getNumberValue()
	{
		ExceptionsUtils.IfNot(isNum(), IllegalStateException.class, "Attribute value is not a number!");
		return Double.valueOf(value);
	}
}
