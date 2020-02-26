package ru.swayfarer.swl2.logger.printers;

import java.io.PrintStream;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.swayfarer.swl2.observable.events.AbstractCancelableEvent;

@AllArgsConstructor(staticName = "of") @Data
public class PrintEvent extends AbstractCancelableEvent {

	public PrintStream stream;
	public String content;
	
}
