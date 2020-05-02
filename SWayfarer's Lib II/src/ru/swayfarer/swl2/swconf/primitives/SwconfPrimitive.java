package ru.swayfarer.swl2.swconf.primitives;

import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Базовый примитив в Swconf 
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class SwconfPrimitive {

	/** Имя */
	public String name;
	
	/** Коментарий */
	public String comment;
	
	/** Сырое значение */
	public Object rawValue;
	
	/** Задать сырое значение */
	public <T extends SwconfPrimitive> T setRawValue(Object value) 
	{
		this.rawValue = value;
		return (T) this;
	}
	
	/** Задать имя */
	public <T extends SwconfPrimitive> T setName(@ConcattedString Object... text) 
	{
		this.name = StringUtils.concat(text);
		return (T) this;
	}
	
	/** Представить как массив */
	public SwconfArray asArray()
	{
		return (SwconfArray) this;
	}
	
	/** Представить как Boolean */
	public SwconfBoolean asBoolean()
	{
		return (SwconfBoolean) this;
	}
	
	/** Представить как число */
	public SwconfNum asNum()
	{
		return (SwconfNum) this;
	}
	
	/** Представить как объект */
	public SwconfObject asObject()
	{
		return (SwconfObject) this;
	}
	
	/** Представить как строку */
	public SwconfString asString()
	{
		return (SwconfString) this;
	}
	
	/** Задать коментарий */
	public <T extends SwconfPrimitive> T setComment(@ConcattedString Object... text) 
	{
		this.comment = StringUtils.concatWithSpaces(text);
		return (T) this;
	}
	
	/** Булиевый ли этот объект? */
	public boolean isBoolean()
	{
		return false;
	}
	
	/** Числовой ли этот объект? */
	public boolean isNum()
	{
		return false;
	}
	
	/** Строковый ли этот объект? */
	public boolean isString()
	{
		return false;
	}
	
	/** Массив ли это? */
	public boolean isArray()
	{
		return false;
	}
	
	/** Объект ли это? */
	public boolean isObject()
	{
		return false;
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + " [name=" + name + ", rawValue=" + rawValue + ", comment=" + comment + "]";
	}
	
	public <T extends SwconfPrimitive> T copy() 
	{
		SwconfPrimitive ret = new SwconfPrimitive();
		ret.comment = comment;
		ret.rawValue = rawValue;
		ret.name = name;
		
		return (T) ret;
	}
}
