package ru.swayfarer.swl2.swconf.primitives;

import java.util.Map;

import ru.swayfarer.swl2.collections.CollectionsSWL;
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
	public Map<String, SwconfPrimitive> children = CollectionsSWL.createIdentityMap();
	
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
	
	@Override
	public boolean isObject()
	{
		return true;
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
	
}
