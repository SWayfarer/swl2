package ru.swayfarer.swl2.swconf.primitives;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.DynamicString;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Массив Swconf
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class SwconfArray extends SwconfPrimitive {

	/** Элементы */
	@InternalElement
	public IExtendedList<SwconfPrimitive> elements = CollectionsSWL.createExtendedList();
	
	/** Конструктор */
	public SwconfArray()
	{
		this.rawValue = elements;
	}
	
	/** Добавить элемент */
	public <T extends SwconfArray> T addChild(SwconfPrimitive primitive) 
	{
		this.elements.add(primitive);
		return (T) this;
	}
	
	@Override
	public boolean isArray()
	{
		return true;
	}
	
	@Override
	public <T extends SwconfPrimitive> T copy()
	{
		SwconfArray array = new SwconfArray();
		array.name = name;
		array.comment = comment;
		
		for (SwconfPrimitive primitive : elements)
		{
			array.elements.add(primitive.copy());
		}
		
		return (T) array;
	}
	
	public String toString(int indent)
	{
		String indentStr = StringUtils.createSpacesSeq(4 * indent);
		DynamicString ret = new DynamicString();
		ret.append(indentStr + "array: " + name + " = {\n");
		
		boolean isFirst = true;
		
		for (SwconfPrimitive child : elements)
		{
			if (!isFirst)
			{
				ret.append(",\n");
			}
			
			ret.append(child.toString(indent + 1));
			
			isFirst = false;
		}
		ret.append("\n");
		ret.append(indentStr);
		ret.append("}");
		
		return ret.toString();
	}
}
