package ru.swayfarer.swl2.tasks;

import java.util.Queue;

import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction0NoR;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;

/**
 * Утилиты для работы с "задачами" ({@link ITask})
 * @author User
 *
 */
@SuppressWarnings("unchecked")
public class TaskManager {

	/**
	 * Получить задачу из {@link Runnable}
	 * @param factory Функция, превращающая {@link Runnable} в задачу
	 * @param run {@link Runnable}, который будет запущен
	 * @return Созданная и запущенная задача
	 */
	public static <T extends ITask> T process(IFunction1<Runnable, ITask> factory, Runnable run)
	{
		ExceptionsUtils.IfNull(factory, IllegalArgumentException.class, "Task factory can't be null!");
		ExceptionsUtils.IfNull(run, IllegalArgumentException.class, "Runnable can't be null!");
			
		ITask task = factory.apply(run);
		
		if (task != null)
			task.start();
		
		return (T) task;
	}
	
	public static void waitFor(Queue<ITask> tasks)
	{
		ITask task = null;
		
		while ( (task = tasks.poll() ) != null)
			while (!task.isCompleted()) {}
	}
	
	public static ITask once(IFunction0NoR fun)
	{
		return new OnceRunTask(fun);
	}
	
}
