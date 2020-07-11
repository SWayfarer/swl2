package ru.swayfarer.swl2.observable.subscription;

import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1NoR;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction3NoR;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;

@SuppressWarnings("unchecked")
public abstract class AbstractSubscription<Event_Type> implements ISubscription<Event_Type>, Comparable<AbstractSubscription<Event_Type>> {

	/** Обработчик ошибок */
	@InternalElement
	public IFunction1NoR<Throwable> errorHandler;
	
	/** Функция, выполняющая {@link #getSubscibedFun()} */
	@InternalElement
	public IFunction3NoR<Event_Type, ISubscription<Event_Type>, IFunction2NoR<ISubscription<Event_Type>, Event_Type>> executeFun = (e, sub, f) -> f.apply(sub, e);
	
	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Необходимо ли отвязать подписку от слушаемого объекта? */
	@InternalElement
	public boolean isDisposed = false;
	
	/** Приоритет */
	@InternalElement
	public int priority;
	
	/** Функция, выполняющая обработку события ("подписываемая функция")  */
	public abstract IFunction2NoR<ISubscription<Event_Type>, Event_Type> getSubscribedFun();
	
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
			IFunction2NoR<ISubscription<Event_Type>, Event_Type> fun = getSubscribedFun();
			
			if (fun == null)
			{
				dispose();
			}
			else
			{
				try
				{
					executeFun.apply(event, this, fun);
				}
				catch (Throwable e)
				{
					processError(e);
					
					e.printStackTrace();
					logger.error(e, "Error while processing subscription", this);
				}
			}
		}
		
		return (T) this;
	}
	
	public <T extends AbstractSubscription<Event_Type>> T setPriority(int priority)
	{
		this.priority = priority;
		
		return (T) this;
	}
	
	public void processError(Throwable t)
	{
		if (errorHandler != null)
		{
			logger.safe(() -> errorHandler.apply(t));
		}
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
	
	@Override
	public <T extends ISubscription<Event_Type>> T error(IFunction1NoR<Throwable> errorHandler)
	{
		this.errorHandler = errorHandler;
		return (T) this;
	}
	
	@Override
	public <T extends ISubscription<Event_Type>> T executeOn(IFunction3NoR<Event_Type, ISubscription<Event_Type>, IFunction2NoR<ISubscription<Event_Type>, Event_Type>> executor)
	{
		this.executeFun = executor;
		return (T) this;
	}
}
