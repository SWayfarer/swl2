package ru.swayfarer.swl2.logger.handlers;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LogInfo;
import ru.swayfarer.swl2.logger.event.LogEvent;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.observable.subscription.ISubscription;

@SuppressWarnings("unchecked")
public class LogMultiloggerHandler implements IFunction2NoR<ISubscription<LogEvent>, LogEvent> {

	public IExtendedList<LoggerEntry> registeredLoggers = CollectionsSWL.createExtendedList();
	
	@Override
	public void applyNoR(ISubscription<LogEvent> sub, LogEvent logEvent)
	{
		if (logEvent.isCanceled())
			return;
		
		ILogger logger = getCustomLogger(logEvent.logInfo);
		
		if (logger != null)
		{
			logEvent.setCanceled(true);
			logger.log(logEvent.logInfo);
		}
	}
	
	public <T extends LogMultiloggerHandler> T addLogger(IFunction1<LogInfo, Boolean> acceptingFun, ILogger logger) 
	{
		registeredLoggers.add(new LoggerEntry(logger, acceptingFun));
		return (T) this;
	}
	
	public ILogger getCustomLogger(LogInfo logInfo)
	{
		for (LoggerEntry entry : registeredLoggers)
		{
			ILogger logger = entry.logger;
			
			if (entry.isAccepts(logInfo))
			{
				return logger;
			}
		}
		
		return null;
	}

	@InternalElement
	public static class LoggerEntry {
		
		public ILogger logger;
		public IFunction1<LogInfo, Boolean> isAcceptingFun;
		
		public LoggerEntry(ILogger logger, IFunction1<LogInfo, Boolean> isAcceptingFun)
		{
			super();
			this.logger = logger;
			this.isAcceptingFun = isAcceptingFun;
		}
		
		public boolean isAccepts(LogInfo logInfo)
		{
			return isAcceptingFun == null || isAcceptingFun.apply(logInfo);
		}
		
	}
}
