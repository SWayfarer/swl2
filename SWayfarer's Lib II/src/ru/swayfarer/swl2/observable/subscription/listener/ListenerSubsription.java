package ru.swayfarer.swl2.observable.subscription.listener;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.observable.subscription.ISubscription;
import ru.swayfarer.swl2.observable.subscription.Subscription;
import ru.swayfarer.swl2.threads.ThreadsUtils;

@SuppressWarnings("unchecked")
public class ListenerSubsription<Event_Type> extends Subscription<Event_Type> {

	public AtomicLong delay = new AtomicLong(-1);
	
	public static ILogger logger = LoggingManager.getLogger();
	
	public ObservableListenerThread<Event_Type> listenerThread = new ObservableListenerThread<>();
	
	public ListenerSubsription(IFunction2NoR<ISubscription<Event_Type>, Event_Type> subscribeFunction)
	{
		this.subscribeFunction = (sub, e) -> listenerThread.nextEvent(e);
		
		listenerThread.listeningSub = this;
		listenerThread.subscribeFunction = subscribeFunction;
		listenerThread.start();
	}
	
	public <T extends ListenerSubsription<Event_Type>> T observePerSec(int secCount)
	{
		this.delay.set(secCount * 1000L);
		return (T) this;
	}
	
	public <T extends ListenerSubsription<Event_Type>> T observePer(int milisisCount)
	{
		this.delay.set(milisisCount);
		return (T) this;
	}

	public static class ObservableListenerThread<Event_Type> extends Thread {
		
		public IFunction2NoR<ISubscription<Event_Type>, Event_Type> subscribeFunction;
		public ListenerSubsription<Event_Type> listeningSub;
		
		public Queue<Event_Type> events = CollectionsSWL.createConcurrentQueue();
		
		public ObservableListenerThread()
		{
			setDaemon(true);
		}
		
		@Override
		public void run()
		{
			logger.safe(this::runSafe, "Error while listening observalbe");
		}
		
		public void runSafe() throws Throwable 
		{
			while (!listeningSub.isDisposed())
			{
				Event_Type event = events.poll();
				
				while (event != null)
				{
					try
					{
						subscribeFunction.apply(listeningSub, event);
					}
					catch (Throwable e)
					{
						listeningSub.processError(e);
						logger.error(e, "Error while listening observable event");
					}
					
					long delay = listeningSub.delay.get();
					
					if (delay > 0)
					{
						events.clear();
						ThreadsUtils.sleepSafe(delay);
					}
					
					event = events.poll();
				}
			}
		}
		
		public <T extends ObservableListenerThread<Event_Type>> T setSubscription(ListenerSubsription<Event_Type> sub)
		{
			this.listeningSub = sub;
			return (T) this;
		}
		
		public <T extends ObservableListenerThread<Event_Type>> T nextEvent(Event_Type event)
		{
			events.add(event);
			return (T) this;
		}
		
		public <T extends ObservableListenerThread<Event_Type>> T setSubscriptionFun(IFunction2NoR<ISubscription<Event_Type>, Event_Type> fun)
		{
			this.subscribeFunction = fun;
			return (T) this;
		}
	}
}
