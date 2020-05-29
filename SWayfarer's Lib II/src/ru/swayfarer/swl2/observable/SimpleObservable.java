package ru.swayfarer.swl2.observable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

import lombok.var;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1NoR;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.observable.subscription.ISubscription;
import ru.swayfarer.swl2.observable.subscription.Subscription;
import ru.swayfarer.swl2.observable.subscription.WeakSubscription;
import ru.swayfarer.swl2.observable.subscription.listener.ListenerSubsription;

/**
 * Простая реализация "Наблюдаемого" объекта.
 * <br> Не блокируемый, умеет бросать {@link ConcurrentModificationException}, если пользоваться не грамотно
 * @author swayfarer
 */
@SuppressWarnings("unchecked")
public class SimpleObservable<Event_Type> implements IObservable<Event_Type> {

	public IExtendedList<IFunction1NoR<Throwable>> dataErrorHandlers = CollectionsSWL.createExtendedList();
	
	/** Список подписок */
	public List<ISubscription<Event_Type>> subscriptions = Collections.synchronizedList(new ArrayList<>());
	
	/** Передать слушателям следующую партию данных */
	@Override
	public <T extends IObservable<Event_Type>> T next(Event_Type event)
	{
		Iterator<ISubscription<Event_Type>> $i = new ArrayList<>(subscriptions).iterator();
		
		while ($i.hasNext())
		{
			ISubscription<Event_Type> next = $i.next();
			
			// Удаляем отписанные подписчики
			if (next == null || next.isDisposed())
				$i.remove();
			
			// Или передаем данные 
			next.process(event);
		}	
		
		return (T) this;
	}

	/** Подписаться на события объекта */
	@Override
	public <T extends ISubscription<Event_Type>> T subscribe(int priority, IFunction2NoR<ISubscription<Event_Type>, Event_Type> fun)
	{
		ISubscription<Event_Type> sub = new Subscription<>(fun).setPriority(priority);
		subscriptions.add(sub);
		CollectionsSWL.sort(subscriptions);
		return (T) sub;
	}
 
	/** Подписаться "слабой" подпиской, см {@link WeakSubscription} */
	@Override
	public <T extends ISubscription<Event_Type>> T subscribeWeak(IFunction2NoR<ISubscription<Event_Type>, Event_Type> fun)
	{
		ISubscription<Event_Type> sub = new WeakSubscription<>(fun);
		subscriptions.add(sub);
		return (T) sub;
	}

	@Override
	public synchronized boolean isEmpty()
	{
		return subscriptions.isEmpty();
	}

	@Override
	public synchronized <T extends IObservable<Event_Type>> T clear()
	{
		for (ISubscription<Event_Type> sub : subscriptions)
			sub.dispose();
		
		subscriptions.clear();
		
		return (T) this;
	}

	@Override
	public <T extends ListenerSubsription<Event_Type>> T subscribeOn(IFunction2NoR<ISubscription<Event_Type>, Event_Type> fun)
	{
		ListenerSubsription<Event_Type> sub = new ListenerSubsription<>(fun);
		subscriptions.add(sub);
		return (T) sub;
	}

	@Override
	public void handleDataError(Throwable e)
	{
		for (var handler: dataErrorHandlers)
		{
			if (handler != null)
			{
				try
				{
					handler.apply(e);
				}
				catch (Throwable e1)
				{
					e1.printStackTrace();
				}
			}
		}
	}
	
	public <T extends IObservable<Event_Type>> T onSourceError(IFunction1NoR<Throwable> handler)
	{
		dataErrorHandlers.addExclusive(handler);
		return (T) this;
	}
}
