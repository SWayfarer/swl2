package ru.swayfarer.swl2.tasks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * Событие задачи 
 * @author swayfarer
 */
@Data @AllArgsConstructor(staticName = "of")
public class TaskEvent {

	/** Задача */
	@NonNull
	public ITask task;
	
	/** Тип события */
	@NonNull
	public EventType type;
	
	/**
	 * Тип задачи 
	 * @author swayfarer
	 */
	public static enum EventType {
		
		/** Задача запущена */
		Started,
		
		/** Задача завершена */
		Completed,
		
		/** Задача приостановлена */
		Paused,
		
		/** Задача продолжена */
		Resumed
	}
}
