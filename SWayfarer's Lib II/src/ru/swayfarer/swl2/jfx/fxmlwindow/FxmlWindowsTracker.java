package ru.swayfarer.swl2.jfx.fxmlwindow;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.markers.InternalElement;

/**
 * Трекер показываний {@link FxmlWindow} 
 * @author swayfarer
 *
 */
public class FxmlWindowsTracker {

	/** Классы показываемых окон */
	@InternalElement
	public static IExtendedList<Class<? extends FxmlWindow>> showingWindowsClasses = CollectionsSWL.createExtendedList();
	
	/** Задать окно как показываемое */
	public static synchronized void setWindowShowing(FxmlWindow window, boolean isShowing)
	{
		if (isShowing)
			showingWindowsClasses.addExclusive(window.getClass());
		else
			showingWindowsClasses.remove(window.getClass());
	}
	
	/** Показывается ли хотя бы одно окно с таким же класом? */
	public static synchronized boolean isWindowTypeAlreadyShowing(FxmlWindow window)
	{
		return showingWindowsClasses.contains(window.getClass());
	}
}
