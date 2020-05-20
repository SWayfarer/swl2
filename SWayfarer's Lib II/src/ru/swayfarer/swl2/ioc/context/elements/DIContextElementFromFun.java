package ru.swayfarer.swl2.ioc.context.elements;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction0;
import ru.swayfarer.swl2.markers.InternalElement;

/**
 * Элемент контекста, получающий свое значение из функции 
 * @author swayfarer
 *
 */
@Accessors(chain = true)
@Getter
@Setter
@AllArgsConstructor(staticName = "of")
public class DIContextElementFromFun implements IDIContextElement {

	/** Класс, с которым ассоциирован элемент */
	@InternalElement
	public Class<?> associatedClass;
	
	/** Имя элемента */
	@InternalElement
	public String name;
	
	/** Функция, возвращающая значение элемента */
	@InternalElement
	public IFunction0<Object> objectCreationFun;

	/** Конструктор */
	public DIContextElementFromFun() {}
	
	@Override
	public Object getValue()
	{
		return objectCreationFun.apply();
	}
	
	@Override
	public String toString()
	{
		return "prototype from fun: " + objectCreationFun;
	}
}