package ru.swayfarer.swl2.observable.subscription;

import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1NoR;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction3NoR;
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
	
	/** Задать обработчик ошибок */
	public <T extends ISubscription<Event_Type>> T error(IFunction1NoR<Throwable> errorHandler);
	
	/** Получить приоритет подписки */
	public int getPriority();
	
	public <T extends ISubscription<Event_Type>> T executeOn(IFunction3NoR<Event_Type, ISubscription<Event_Type>, IFunction2NoR<ISubscription<Event_Type>, Event_Type>> executor);
}
