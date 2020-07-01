package ru.swayfarer.swl2.swconf2.types;

import java.util.Map;

import lombok.Data;
import lombok.var;

@Data
@SuppressWarnings("unchecked")
public abstract class SwconfObject {

	public String comment;
	public String name;
	
	public boolean hasValue()
	{
		return false;
	}
	
	public Object getValue()
	{
		return null;
	}
	
	public void setRawValue(Object obj)
	{
		
	}
	
	public SwconfBoolean asBoolean()
	{
		return (SwconfBoolean) this;
	}
	
	public SwconfNum asNum()
	{
		return (SwconfNum) this;
	}
	
	public SwconfString asString()
	{
		return (SwconfString) this;
	}
	
	public SwconfTable asTable()
	{
		return (SwconfTable) this;
	}
	
	public boolean isNum()
	{
		return false;
	}
	
	public boolean isBoolean()
	{
		return false;
	}

	public boolean isTable()
	{
		return false;
	}

	public boolean isString()
	{
		return false;
	}
	
	public static <T extends SwconfObject> T of(Object obj)
	{
		if (obj == null)
			return null;
		
		if (obj instanceof Map)
		{
			SwconfTable table = new SwconfTable();
			
			var elements = (Map<String, Object>) obj;
			
			for (var entry : elements.entrySet())
			{
				SwconfObject element = of(entry.getValue());
				
				if (element != null)
				{
					element.name = entry.getKey();
					table.put(entry.getKey(), element);
				}
			}
			
			return (T) table;
		}
		else if (obj instanceof Iterable)
		{
			SwconfTable table = new SwconfTable();
			
			var iterator = ((Iterable<Object>)obj).iterator();
			
			while (iterator.hasNext())
			{
				var o = iterator.next();
				
				SwconfObject element = of(o);
				
				if (element != null)
				{
					table.put(element);
				}
			}
			
			return (T) table;
		}
		else if (obj instanceof Number)
		{
			SwconfNum num = new SwconfNum();
			num.setValue(((Number) obj).doubleValue());
			return (T) num;
		}
		else if (obj instanceof Boolean)
		{
			SwconfBoolean bool = new SwconfBoolean((Boolean) obj);
			return (T) bool;
		}
		else if (obj instanceof String)
		{
			SwconfString str = new SwconfString((String) obj);
			return (T) str;
		}
		
		return null;
	}
	
	public String toString(int indent) 
	{
		return "<swconf object>";
	}
}
