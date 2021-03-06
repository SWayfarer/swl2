package ru.swayfarer.swl2.logger.printers;

import java.io.PrintStream;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.swayfarer.swl2.observable.events.AbstractCancelableEvent;

/**
 * Событие принта в {@link PrintStreamWrapper} 
 * @author swayfarer
 *
 */
@AllArgsConstructor(staticName = "of") @Data
public class PrintEvent extends AbstractCancelableEvent {

	/** Поток, в который происходит принт */
	public PrintStream stream;
	
	/** Содержание, которое принтится */
	public String content;
	
}
