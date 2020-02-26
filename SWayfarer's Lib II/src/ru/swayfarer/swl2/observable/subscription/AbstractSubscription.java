package ru.swayfarer.swl2.observable.subscription;

import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.markers.InternalElement;

@SuppressWarnings("unchecked")
public abstract class AbstractSubscription<Event_Type> implements ISubscription<Event_Type>, Comparable<AbstractSubscription<Event_Type>> {

	/** Необходимо ли отвязать подписку от слушаемого объекта? */
	@InternalElement
	public boolean isDisposed = false;
	
	/** Приоритет */
	@InternalElement
	public int priority;
	
	public abstract IFunction2NoR<ISubscription<Event_Type>, Event_Type> getFun();
	
	/** Необходимо ли отвязать подписку от слушаемого объекта? */
	@Override
	public boolean isDisposed()
	{
		return isDisposed;
	}

	/** Отписаться от слушания объекта */
	@Override
	public <T extends ISubscription<Event_Type>> T dispose()
	{
		isDisposed = true;
		return (T) this;
	}

	/** Обработать пакет данных */
	@Override
	public <T extends ISubscription<Event_Type>> T process(Event_Type event)
	{
		if (!isDisposed())
		{
			IFunction2NoR<ISubscription<Event_Type>, Event_Type> fun = getFun();
			
			if (fun == null)
			{
				dispose();
			}
			else
			{
				fun.process(this, event);
			}
		}
		
		return (T) this;
	}
	
	public <T extends AbstractSubscription<Event_Type>> T setPriority(int priority)
	{
		this.priority = priority;
		
		return (T) this;
	}
	
	@Override
	public int getPriority()
	{
		return priority;
	}
	
	@Override
	public int compareTo(AbstractSubscription<Event_Type> o)
	{
		return o == null ? 1 : o.priority - priority;
	}
}
