package ru.swayfarer.swl2.logger.handlers;

import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.logger.ILogLevel;
import ru.swayfarer.swl2.logger.event.LogEvent;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.observable.subscription.ISubscription;

/**
 * Обработчик, фильтрующий логи по их уровню
 * @author swayfarer
 */
public class LogLevelFilteringHandler implements IFunction2NoR<ISubscription<LogEvent>, LogEvent>{

	/** Минимальный вес лога, чтобы он был отлогирован */
	@InternalElement
	public int minLevel;
	
	/** Конструктор */
	public LogLevelFilteringHandler(ILogLevel minLevel)
	{
		ExceptionsUtils.IfNull(minLevel, IllegalArgumentException.class, "Minimum log level can't be null!");
		this.minLevel = minLevel.getWeight();
	}
	
	/** Конструктор */
	public LogLevelFilteringHandler(int minLevel)
	{
		this.minLevel = minLevel;
	}
	
	/** Обработать событие */
	@Override
	public void applyNoR(ISubscription<LogEvent> sub, LogEvent event)
	{
		if (event.logInfo.getLevel().getWeight() < minLevel)
			event.setCanceled(true);
	}

}
