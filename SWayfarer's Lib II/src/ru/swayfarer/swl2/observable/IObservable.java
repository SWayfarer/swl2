package ru.swayfarer.swl2.observable;

import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction0NoR;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1NoR;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.markers.Alias;
import ru.swayfarer.swl2.observable.subscription.ISubscription;
import ru.swayfarer.swl2.observable.subscription.WeakSubscription;
import ru.swayfarer.swl2.observable.subscription.listener.ListenerSubsription;
import ru.swayfarer.swl2.reference.IReference;

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
	public default <T extends ISubscription<Event_Type>> T subscribe(IFunction0NoR fun)
	{
		return subscribe(0, (sub, e) -> fun.apply());
	}
	
	/** Подписаться на события объекта */
	public default <T extends ISubscription<Event_Type>> T subscribe(IFunction1NoR<Event_Type> fun)
	{
		return subscribe((sub, event) -> fun.apply(event));
	}
	
	/** Подписаться на события объекта */
	public <T extends ISubscription<Event_Type>> T subscribe(int priority, IFunction2NoR<ISubscription<Event_Type>, Event_Type> fun);
	
	/** 
	 * Подписаться на события объекта по ссылке на подписчика. Позволяет динамически менять подписчика 
	 * <h1> Внимание: Ссылка должна указывать на IFunction2NoR </h1> 
	 */
	public default <T extends ISubscription<Event_Type>> T subscribe(int priority, IReference<IFunction2NoR<ISubscription<Event_Type>, Event_Type>> fun2Reference)
	{
		return subscribe(priority, (sub, evt) -> {
			
			Object handler = fun2Reference.getValue();
			
			if (handler == null)
				return;
			
			ExceptionsUtils.IfNot(handler instanceof IFunction2NoR, IllegalStateException.class, "Handler must be a IFunction2NoR with sub and event args!");
			
			((IFunction2NoR<ISubscription<Event_Type>, Event_Type>)fun2Reference.getValue()).apply(sub, evt);
		});
	}
	
	
	/** Подписаться "слабой" подпиской, см {@link WeakSubscription} */
	public <T extends ISubscription<Event_Type>> T subscribeWeak(IFunction2NoR<ISubscription<Event_Type>, Event_Type> fun);
	
	/** Подписаться "слабой" подпиской, см {@link WeakSubscription} */
	public <T extends ListenerSubsription<Event_Type>> T subscribeOn(IFunction2NoR<ISubscription<Event_Type>, Event_Type> fun);
	
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
	
	/** Отписать всех подписчиков */
	public <T extends IObservable<Event_Type>> T clear();
	
	public <T extends IObservable<Event_Type>> T onSourceError(IFunction1NoR<Throwable> handler);

	public void handleDataError(Throwable e);
}
