package ru.swayfarer.swl2.reference;

import ru.swayfarer.swl2.markers.InternalElement;

/**
 * Простая ссылка на объект 
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public abstract class AbstractReference<Element_Type> implements IReference<Element_Type>{

	/** Объект, на который указывает ссылка */
	@InternalElement
	public Element_Type obj;
	
	/** Была ли задана ссылка? */
	@InternalElement
	public boolean isSetted;
	
	/** Конструктор с пустым начальным значением */
	public AbstractReference() {}
	
	/** Консруктор с заданным начальным значением */
	public AbstractReference(Element_Type initialValue)
	{
		this.obj = initialValue;
	}
	
	/**
	 * Получить объект
	 */
	public <T extends Element_Type> T getValue()
	{
		return (T) obj;
	}
	
	/**
	 * Была ли задана ссылка? 
	 */
	public boolean isSetted()
	{
		return isSetted;
	}
	
	/** Задать значение ссылки */
	public void setValue(Element_Type obj)
	{
		isSetted = true;
		this.obj = obj;
	}
	
}
