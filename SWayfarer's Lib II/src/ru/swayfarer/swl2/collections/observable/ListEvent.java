package ru.swayfarer.swl2.collections.observable;

import lombok.AllArgsConstructor;
import ru.swayfarer.swl2.observable.events.AbstractCancelableEvent;

/**
 * Событие, связанное с элементом листа
 * @author swayfarer
 */
@AllArgsConstructor(staticName = "of")
public class ListEvent<Element_Type> extends AbstractCancelableEvent {

	/** 
	 * Индекс элемента, с которым проиходит событие 
	 * <br> В случае очистки листа будет равен -1
	 */
	public int index;
	
	/** 
	 * Элемент, с которым происходит событие 
	 * <br> В случае очистки листа будет равен null
	 */
	public Element_Type element;
	
	/** Лист, с элементами которого проиходят события */
	public IObservableList<Element_Type> list;
	
	/** Тип события*/
	public EventType type;
	
	/**
	 * Типы событий с элементами листов
	 * @author swayfarer
	 */
	public static enum EventType {
		
		/** Добавление нового элемента */
		Add,
		
		/** Задание значения элемента */
		Set,
		
		/** Удаление элемента */
		Remove,
		
		/** Очистка всего листа'
		 * <br> (В этом случае индекс и значение элемента будут -1 и null соответственно) 
		 * */
		Clear
	}
	
}
