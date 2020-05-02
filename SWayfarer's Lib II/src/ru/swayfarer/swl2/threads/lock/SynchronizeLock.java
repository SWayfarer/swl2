package ru.swayfarer.swl2.threads.lock;

import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;

public class SynchronizeLock {

	public static ILogger logger = LoggingManager.getLogger();
	
	public volatile Object lock = new Object();
	
	public void waitFor()
	{
		synchronized (lock)
		{
			logger.safe(lock::wait, "Error while trying to wait thread", Thread.currentThread(), "on object", lock);
		}
	}
	
	public void waitFor(long timeout)
	{
		synchronized (lock)
		{
			logger.safe(() -> lock.wait(timeout), "Error while trying to wait thread", Thread.currentThread(), "on object", lock);
		}
	}
	
	public void notifyLock()
	{
		synchronized (lock)
		{
			logger.safe(lock::notify, "Error while notifying lock object", lock);
		}
	}

	public void notifyLockAll()
	{
		synchronized (lock)
		{
			logger.safe(lock::notifyAll, "Error while notifying lock object", lock);
		}
	}
	
}
