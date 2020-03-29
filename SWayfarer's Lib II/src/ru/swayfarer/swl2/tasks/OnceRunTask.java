package ru.swayfarer.swl2.tasks;

import java.util.concurrent.atomic.AtomicBoolean;

import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction0NoR;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.SimpleObservable;

/**
 * Задача, выполняемая один раз
 * @author swayfarer
 *
 */
public class OnceRunTask implements ITask {

	/** Запущена ли задача? */
	@InternalElement
	public AtomicBoolean isRunning = new AtomicBoolean(false);
	
	/** Завершена ли задача? */
	@InternalElement
	public AtomicBoolean isCompleted = new AtomicBoolean(false);
	
	/** События задачи */
	@InternalElement
	public IObservable<TaskEvent> events = new SimpleObservable<>();
	
	/** Функция, выполняемая задачей */
	@InternalElement
	public IFunction0NoR fun;
	
	/** Конструктор */
	public OnceRunTask(IFunction0NoR fun)
	{
		this.fun = fun;
	}
	
	@Override
	public void start()
	{
		isRunning.set(true);
		
		if (fun != null)
			fun.apply();
		
		isRunning.set(false);
		isCompleted.set(true);
	}

	@Override
	public boolean isCompleted()
	{
		return isCompleted.get();
	}

	@Override
	public boolean isPaused()
	{
		return false;
	}

	@Override
	public boolean isRunnig()
	{
		return isRunning.get();
	}

	@Override
	public boolean isEventsSupported()
	{
		return true;
	}

	@Override
	public IObservable<TaskEvent> events()
	{
		return events;
	}

}
