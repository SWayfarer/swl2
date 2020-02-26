package ru.swayfarer.swl2.logger.handlers;

import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.event.LogEvent;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.observable.subscription.ISubscription;

/**
 * Обработчик, перенаправляющий логи другому логгеру 
 * @author swayfarer
 */
@SuppressWarnings("unchecked")
public class LogRedirectHandler implements IFunction2NoR <ISubscription<LogEvent>, LogEvent> {

	/** Логгер, получающий логи */
	@InternalElement
	public ILogger redirectTargetLogger;
	
	/** Будет ли оригинальный логгер обрабатывать событие? */
	@InternalElement
	public boolean isOriginalSilent = true;
	
	/** Конструктор */
	public LogRedirectHandler(ILogger parentLogger)
	{
		ExceptionsUtils.IfNull(parentLogger, IllegalArgumentException.class, "Parent logger can't be null!");
		
		this.redirectTargetLogger = parentLogger;
	}
	
	/** Обработать событие */
	@Override
	public void applyNoR(ISubscription<LogEvent> sub, LogEvent event)
	{
		if (redirectTargetLogger == event.logger)
			return;
		
		if (event.isCanceled())
			return;
		
		redirectTargetLogger.log(event.logInfo);
		event.setCanceled(isOriginalSilent);
	}

	/** Будет ли оригинальный логгер обрабатывать событие? */
	public <T extends LogRedirectHandler> T setOriginaLoggerOutput(boolean isOut)
	{
		this.isOriginalSilent = isOut;
		return (T) this;
	}
}
