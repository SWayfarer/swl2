package ru.swayfarer.swl2.observable.subscription;

import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.markers.InternalElement;

/**
 * Базовая реализация {@link AbstractSubscription}
 * @author swayfarer
 */
public class Subscription<Event_Type> extends AbstractSubscription<Event_Type> {

	/** Функция, выполняющая обработку события ("подписываемая функция")  */
	@InternalElement
	public IFunction2NoR<ISubscription<Event_Type>, Event_Type> subscribeFunction;
	
	public Subscription()
	{
		
	}
	
	public Subscription(IFunction2NoR<ISubscription<Event_Type>, Event_Type> subscribeFunction)
	{
		ExceptionsUtils.IfNull(subscribeFunction, IllegalArgumentException.class, "Function of subsription can't be null!");
		this.subscribeFunction = subscribeFunction;
	}
	
	/** Необходимо ли отвязать подписку от слушаемого объекта? */
	@Override
	public IFunction2NoR<ISubscription<Event_Type>, Event_Type> getSubscribedFun()
	{
		return subscribeFunction;
	}
}
