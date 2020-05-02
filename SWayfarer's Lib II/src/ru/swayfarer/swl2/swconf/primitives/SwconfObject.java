package ru.swayfarer.swl2.swconf.primitives;

import java.util.Map;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Объект, содержащий дочерние объекты Swconf
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class SwconfObject extends SwconfPrimitive {

	/** Дочерние элементы */
	@InternalElement
	public Map<String, SwconfPrimitive> children = CollectionsSWL.createLinkedMap();
	
	/** Конструктор */
	public SwconfObject()
	{
		this.rawValue = children;
	}
	
	/** Добавить дочерний элемент */
	public <T extends SwconfObject> T addChild(SwconfPrimitive primitive) 
	{
		children.put(primitive.name, primitive);
		return (T) this;
	}
	
	public <T extends SwconfObject> T setNum(String name, double value) 
	{
		addChild(new SwconfNum().setValue(value).setName(name));
		return (T) this;
	}
	
	public <T extends SwconfObject> T setBoolean(String name, boolean value) 
	{
		addChild(new SwconfBoolean().setValue(value).setName(name));
		return (T) this;
	}
	
	public <T extends SwconfObject> T setString(String name, @ConcattedString Object... text) 
	{
		addChild(new SwconfString().setValue(text).setName(name));
		return (T) this;
	}
	
	@Override
	public boolean isObject()
	{
		return true;
	}
	
	/** Получить дочерний элемент */
	public <T extends SwconfPrimitive> T getChild(String name)
	{
		return (T) children.get(name);
	}
	
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		
		if (!StringUtils.isEmpty(name))
			builder.append(name + " = ");
		
		builder.append("{\n");
		
		children.values().forEach((e) -> builder.append(e.toString() + ",\n"));
		
		builder.append("}");
		
		return builder.toString();
	}
	
	@Override
	public <T extends SwconfPrimitive> T copy()
	{
		SwconfObject ret = new SwconfObject();
		ret.name = name;
		ret.comment = comment;
		
		for (Map.Entry<String, SwconfPrimitive> entry : children.entrySet())
		{
			ret.children.put(entry.getKey(), entry.getValue().copy());
		}
		
		return (T) ret;
	}
	
}
