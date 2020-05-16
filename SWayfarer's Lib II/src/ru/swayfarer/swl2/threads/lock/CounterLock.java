package ru.swayfarer.swl2.threads.lock;

import java.util.concurrent.atomic.AtomicInteger;

import ru.swayfarer.swl2.markers.Alias;

@SuppressWarnings("unchecked")
public class CounterLock {

	public SynchronizeLock lock = new SynchronizeLock();
	public AtomicInteger counter = new AtomicInteger(0);
	
	public CounterLock()
	{
		
	}
	
	public CounterLock(int initialValue)
	{
		setCounter(initialValue);
	}
	
	public void waitFor()
	{
		if (counter.get() <= 0)
			return;
		
		lock.waitFor();
	}
	
	public void waitFor(long timeout)
	{
		if (counter.get() <= 0)
		return;
	
		lock.waitFor(timeout);
	}
	
	public void reduce()
	{
		if (counter.decrementAndGet() == 0)
			lock.notifyLockAll();
	}
	
	@Alias("setCounter")
	public <T extends CounterLock> T setValue(int value) 
	{
		return (T) setCounter(value);
	}
	
	public <T extends CounterLock> T setCounter(int value) 
	{
		this.counter.set(value);
		return (T) this;
	}
}
