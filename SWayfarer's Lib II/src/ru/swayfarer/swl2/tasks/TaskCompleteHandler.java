package ru.swayfarer.swl2.tasks;

import ru.swayfarer.swl2.markers.InternalElement;

/**
 * Отслеживатель завершения тасок
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class TaskCompleteHandler {

	/** Следующий id отслеживателя */
	@InternalElement
	public static volatile int nextId = 0;
	
	/** Количество активных задач */
	@InternalElement
	public volatile int activeTasksCount;
	
	/** Id отслеживателя */
	@InternalElement
	public int id;
	
	/** Конструктор */
	public TaskCompleteHandler()
	{
		id = getNextId();
	}
	
	/** Получить следующий id */
	public static synchronized int getNextId()
	{
		return nextId ++;
	}
	
	/** Добавить таску */
	public synchronized <T extends TaskCompleteHandler> T addTask(ITask task)
	{
		activeTasksCount ++;
		
		task.events().subscribe((evt) -> {
			if (evt.type == TaskEvent.EventType.Completed)
			{
				synchronized (TaskCompleteHandler.class)
				{
					activeTasksCount --;
				}
			}
		});
		
		return (T) this;
	}
	
	/** Подождать, пока все таски не завершатся */
	public void waitFor()
	{
		while (!isTasksCompleted()) {}
	}
	
	/** Завершена ли таска? */
	public synchronized boolean isTasksCompleted()
	{
		return activeTasksCount <= 0;
	}
	
}
