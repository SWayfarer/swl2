package ru.swayfarer.swl2.ioc.context.elements;

/**
 * Элемент контекста. Отвечает за возврат значений контекста
 * 
 * @author swayfarer
 *
 */
public interface IDIContextElement {

	/** Получить значение */
	public Object getValue();

	/** Получить класс, с которым ассоциируется элемент */
	public Class<?> getAssociatedClass();

	/** Получить имя элемента */
	public String getName();

}