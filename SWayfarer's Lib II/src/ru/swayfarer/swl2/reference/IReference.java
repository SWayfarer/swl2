package ru.swayfarer.swl2.reference;

/**
 * Ссылка на объект
 * @author swayfarer
 *
 */
public interface IReference {

	/**
	 * Получить объект
	 */
	public <T> T getValue();
	
	/**
	 * Была ли задана ссылка? 
	 */
	public boolean isSetted();
	
	/** Задать значение ссылки */
	public void setValue(Object obj);
	
}
