package ru.swayfarer.swl2.observable.subscription;

import ru.swayfarer.swl2.observable.IObservable;

/**
 * Подписка на {@link IObservable}
 * <br> Отвечает за передачу данных из {@link IObservable#next(Object)} функции, а так же за обработку  жизненного цикла подписки
 * <br> Т.е. функция может только исполняться, потому для обработки ее как подписки используется интерфейс {@link ISubscription}
 * @author swayfarer
 */

public interface ISubscription<Event_Type> {

	/** Пора ли отписываться? */
	public boolean isDisposed();
	
	/** Отписаться */
	public <T extends ISubscription<Event_Type>> T dispose();
	
	/** Передать событие обработчику */
	public <T extends ISubscription<Event_Type>> T process(Event_Type event);
	
	/** Получить приоритет подписки */
	public int getPriority();
	
}
