package ru.swayfarer.swl2.tasks;

import ru.swayfarer.swl2.observable.IObservable;

/**
 * Задача
 * <br> Интерфейс для работы со статусом задачи
 * @author swayfarer
 */
public interface ITask {

	/** Запустить задачу */
	public void start();
	
	/** Завершена ли задача? */
	public boolean isCompleted();
	
	/** Приостановлена ли задача? */
	public boolean isPaused();
	
	/** Выполняется ли задача? */
	public boolean isRunnig();
	
	/** Поддерживаются ли события */
	public boolean isEventsSupported();
	
	/** Получить точку для подписки на события */
	public IObservable<TaskEvent> events();
	
}