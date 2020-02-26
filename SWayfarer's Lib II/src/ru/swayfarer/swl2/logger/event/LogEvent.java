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
	public LogInfo logInfo;
	public ILogger logger;
}
