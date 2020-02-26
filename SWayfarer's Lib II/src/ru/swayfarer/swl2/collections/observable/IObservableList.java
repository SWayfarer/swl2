package ru.swayfarer.swl2.collections.observable;

import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.observable.IObservable;

/**
 * Наблюдаемый лист.
 * <br> Позволяет обрабатывать события, связанные с элементами листа, посредством {@link #evtUpdate()} и {@link #evtPostUpdate()}
 * @author swayfarer
 */
public interface IObservableList<Element_Type> extends IExtendedList<Element_Type> {

	/** 
	 * Точка для подписки на pre-версию событий. 
	 * <br> На этом этапе {@link ListEvent#setCanceled(boolean)} может повлиять на отмену операции
	 */
	public IObservable< ListEvent<Element_Type> > evtUpdate();
	
	/** 
	 * Точка для подписки на post-версию событий. 
	 * <br> На этом этапе {@link ListEvent#setCanceled(boolean)} НЕ может повлиять на отмену операции
	 */
	public IObservable< ListEvent<Element_Type> > evtPostUpdate();
	
}
