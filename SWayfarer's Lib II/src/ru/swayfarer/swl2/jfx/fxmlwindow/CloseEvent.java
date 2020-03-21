package ru.swayfarer.swl2.jfx.fxmlwindow;

import ru.swayfarer.swl2.observable.events.AbstractCancelableEvent;

public class CloseEvent extends AbstractCancelableEvent {

	public FxmlWindow closingWindow;

	public CloseEvent(FxmlWindow closingWindow)
	{
		super();
		this.closingWindow = closingWindow;
	}
}
