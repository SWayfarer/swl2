package ru.swayfarer.swl2.observable;

import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.markers.Alias;
import ru.swayfarer.swl2.observable.subscription.ISubscription;
import ru.swayfarer.swl2.observable.subscription.WeakSubscription;

/**
 * Слушаемый объект.
 * <br> Имеет слушателей(подписчиков), которые обрабатывают данные, исходящие из него.
 * @author swayfarer
 */
public interface IObservable<Event_Type> {

	/** Передать следующую партию данных подписчикам */
	public <T extends IObservable<Event_Type>> T next(Event_Type event);
	
	/** Подписаться на события объекта */
	public default <T extends ISubscription<Event_Type>> T subscribe(IFunction2NoR<ISubscription<Event_Type>, Event_Type> fun)
	{
		return subscribe(0, fun);
	}
	
	/** Подписаться на события объекта */
	public <T extends ISubscription<Event_Type>> T subscribe(int priority, IFunction2NoR<ISubscription<Event_Type>, Event_Type> fun);
	
	/** Подписаться "слабой" подпиской, см {@link WeakSubscription} */
	public <T extends ISubscription<Event_Type>> T subscribeWeak(IFunction2NoR<ISubscription<Event_Type>, Event_Type> fun);
	
	/** Пуст ли список подписок? */
	public boolean isEmpty();
	
	/** 
	 * Подписаться на события объекта 
	 * <h1>Alias:</h1> {@link #subscribe(IFunction2NoR)}
	 * */
	@Alias("subscribe")
	public default <T extends ISubscription<Event_Type>> T observe(IFunction2NoR<ISubscription<Event_Type>, Event_Type> fun)
	{
		return subscribe(fun);
	}
}
