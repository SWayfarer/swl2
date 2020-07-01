package ru.swayfarer.swl2.ioc.context.elements;

import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.ioc.context.DIContext;

/**
 * Элемент контекста. Отвечает за возврат значений контекста
 * 
 * @author swayfarer
 *
 */
public interface IDIContextElement {

	default public Object getValue()
	{
		Object obj = getElementValue();
		DIContext context = getContext();
		ExceptionsUtils.IfNull(context, IllegalStateException.class, "Element context can't be null!");
		return !mustPostProcess() ? obj : context.postProcessElement(obj);
	}
	
	public boolean mustPostProcess();
	
	/** Получить значение */
	public Object getElementValue();

	/** Получить класс, с которым ассоциируется элемент */
	public Class<?> getAssociatedClass();

	/** Получить имя элемента */
	public String getName();
	
	/** Получить контекст элемента */
	public DIContext getContext();

}