package ru.swayfarer.swl2.jfx.events;

import javafx.event.Event;
import javafx.event.EventHandler;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1NoR;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.SimpleObservable;

/**
 * Дополнительный обработчик событий 
 * @author swayfarer
 */
@SuppressWarnings({ "unchecked" })
public class AdditionalHandler<Event_Type extends Event> implements EventHandler<Event_Type> {

	/** Логгер*/
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Точка для подписки на событие */
	public IObservable<Event_Type> events = new SimpleObservable<>();
	
	/** Обернутый оригинальный обработчик */
	public EventHandler<Event_Type> wrappedHandler;
	
	/** Конструктор */
	public AdditionalHandler(EventHandler<? super Event_Type> wrappedHandler)
	{
		this.wrappedHandler = ReflectionUtils.forceCast(wrappedHandler);
	}
	
	@Override
	public void handle(Event_Type event)
	{
		logger.safe(() -> {
			if (wrappedHandler != null)
				wrappedHandler.handle(event);
		}, "Error while handling event", event, "by handler", wrappedHandler, "thats wrapped by", this);
		
		events.next(event);
	}
	
	/** Добавить событие */
	public <T extends AdditionalHandler<Event_Type>> T addHandler(IFunction1NoR<? super Event_Type> fun)
	{
		events.subscribe((sub, e) -> fun.apply(e));
		return (T) this;
	}
}
