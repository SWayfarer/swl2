package ru.swayfarer.swl2.logger.event;

import lombok.AllArgsConstructor;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LogInfo;
import ru.swayfarer.swl2.observable.events.AbstractCancelableEvent;

/** 
 * Событие логирования 
 */
@AllArgsConstructor(staticName = "of")
public class LogEvent extends AbstractCancelableEvent {
	
	/** Информация о логе, с которым произошло событие */
	public LogInfo logInfo;
	
	/** Логгер, с которым произошло событие */
	public ILogger logger;
}
