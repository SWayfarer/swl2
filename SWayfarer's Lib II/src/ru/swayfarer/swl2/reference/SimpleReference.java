package ru.swayfarer.swl2.reference;

import ru.swayfarer.swl2.markers.InternalElement;

/**
 * Простая ссылка на объект 
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class SimpleReference implements IReference{

	/** Объект, на который указывает ссылка */
	@InternalElement
	public Object obj;
	
	/** Была ли задана ссылка? */
	@InternalElement
	public boolean isSetted;
	
	/** Конструктор с пустым начальным значением */
	public SimpleReference() {}
	
	/** Консруктор с заданным начальным значением */
	public SimpleReference(Object initialValue)
	{
		this.obj = initialValue;
	}
	
	/**
	 * Получить объект
	 */
	public <T> T getValue()
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
	public void setValue(Object obj)
	{
		isSetted = true;
		this.obj = obj;
	}
	
}