package ru.swayfarer.swl2.reference;

/**
 * Интерфейс ссылки на объект
 * @author swayfarer
 *
 */
public interface IReference<Element_Type> {

	/**
	 * Получить объект
	 */
	public <T extends Element_Type> T getValue();
	
	/**
	 * Была ли задана ссылка? 
	 */
	public boolean isSetted();
	
	/** Задать значение ссылки */
	public void setValue(Element_Type obj);
	
}
