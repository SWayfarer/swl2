package ru.swayfarer.swl2.swconf.primitives;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.markers.InternalElement;

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
}
