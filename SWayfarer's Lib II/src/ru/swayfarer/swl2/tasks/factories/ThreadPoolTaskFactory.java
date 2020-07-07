package ru.swayfarer.swl2.tasks.factories;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.Alias;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.SimpleObservable;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.tasks.ITask;
import ru.swayfarer.swl2.tasks.TaskEvent;

/**
 * Задачи, созданные этой фабрикой, будут запущены в одном из нескольких
 * потоков, выделенных для этого. <br>
 * Помогает распарраллельвать задачи, не используя поток для каждой
 * 
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class ThreadPoolTaskFactory implements IFunction1<Runnable, ITask>, Executor {

	/** Логгер */
	public static final ILogger logger = LoggingManager.getLogger();

	/**
	 * Максимальное время (в миллиек.), которое поток может находиться без
	 * задачи. <br>
	 * По истечению этого времени поток будет удален
	 */
	@InternalElement
	public long maxThreadLifetime;

	/** Максимальное кол-во потоков */
	@InternalElement
	public int maxThreadsCount;

	/**
	 * Создавать ли потоки, как потоки-демоны (см
	 * {@link Thread#setDaemon(boolean)})
	 */
	@InternalElement
	public boolean isThreadsDaemons = false;

	/** Следующий свободный id для потока. Для наименований. */
	@InternalElement
	public AtomicInteger nextThreadId = new AtomicInteger(0);

	/** Очередь из задач, которые нужно выполнить */
	@InternalElement
	public Queue<ThreadExecutorTask> workQueue = new ConcurrentLinkedQueue<>();

	/** Лист потоков, которые выполняют задачи */
	@InternalElement
	public List<TaskProcessorThread> threads = new ArrayList<>();

	/** Следующий свободный id для набора потоков. Для наименований. */
	@InternalElement
	public static AtomicInteger nextPoolId = new AtomicInteger(0);

	/** Префикс, вставляемый в имя потока */
	@InternalElement
	public String name = getClass().getSimpleName() + "-" + (nextPoolId.getAndAdd(1)) + "";

	/**
	 * Конструктор
	 * 
	 * @param maxThreadLifetime
	 *            {@link #maxThreadLifetime}
	 * @param maxThreadsCount
	 *            {@link #maxThreadsCount}
	 */
	public ThreadPoolTaskFactory(long maxThreadLifetime, int maxThreadsCount)
	{
		super();
		if (maxThreadsCount < 0)
			maxThreadsCount = 1;
		this.maxThreadLifetime = maxThreadLifetime;
		this.maxThreadsCount = maxThreadsCount;
	}

	/** Задать {@link #name} */
	public <T extends ThreadPoolTaskFactory> T setName(@ConcattedString Object... text)
	{
		this.name = StringUtils.concat(text);
		return (T) this;
	}

	/** Добавить задачу на выполнение */
	@Override @Alias("execute(Runnable)")
	public ITask apply(Runnable task)
	{
		return executeTask(task);
	}
	
	/** Добавить задачу на выполнение */
	public synchronized ITask executeTask(Runnable task)
	{
		// Если ао время обновления потоков не было ни одного свободного, то пытаемся создать новый
		if (!updateThreads())
		{
			int size = threads.size();

			if (size < maxThreadsCount)
				createNewThread();
		}

		ThreadExecutorTask ret = new ThreadExecutorTask(task);
		workQueue.add(ret);
		return ret;
	}

	/**
	 * Обновить рабочие потоки.
	 * @return Есть ли свободные потоки на момент обновления?
	 */
	@InternalElement
	public boolean updateThreads()
	{
		Iterator<TaskProcessorThread> $i = threads.iterator();

		boolean ret = false;

		while ($i.hasNext())
		{
			TaskProcessorThread thread = $i.next();

			if (!thread.isAlive.get())
				$i.remove();
			else if (thread.emptyLifeTime.get() > 0)
			{
				thread.emptyLifeTime.set(0);
				ret = true;
			}
		}

		return ret;
	}

	/**
	 * Настроить поток для работы
	 * 
	 * @param thread
	 *            Настраиваемый поток
	 */
	@InternalElement
	public void configureNewThread(TaskProcessorThread thread)
	{
		thread.setDaemon(isThreadsDaemons);
	}

	/**
	 * Создать новый рабочий поток
	 */
	@InternalElement
	public void createNewThread()
	{
		TaskProcessorThread thread = new TaskProcessorThread(this);
		configureNewThread(thread);
		threads.add(thread);
		thread.start();
	}

	/** 
	 * Получить следующую задачу из очереди
	 */
	@InternalElement
	public ThreadExecutorTask getNextTask()
	{
		return workQueue.poll();
	}

	/** Получить {@link #maxThreadLifetime} */
	public long getMaxThreadLifetime()
	{
		return maxThreadLifetime;
	}

	/** Задача, выполняемая в пуле потоков */
	@InternalElement
	public static class ThreadExecutorTask implements ITask {

		/** События задачи */
		@InternalElement
		public IObservable<TaskEvent> events = new SimpleObservable<>();
		
		/** Завершена ли задача */
		@InternalElement
		public volatile boolean isCompleted = false;

		/** {@link Runnable}, который выполняется в задаче */
		@InternalElement
		public Runnable content;

		/**
		 * Конструктор
		 * @param content См. {@link #content}
		 */
		public ThreadExecutorTask(Runnable content)
		{
			super();
			ExceptionsUtils.IfNull(content, IllegalArgumentException.class, "Runnable of task can't be null!");
			this.content = content;
		}

		/** Запустить задачу */
		@Override
		public void start()
		{
			TaskEvent startEvent = TaskEvent.of(this, TaskEvent.EventType.Started);
			events.next(startEvent);
			
			content.run();
			isCompleted = true;
			
			TaskEvent completeEvent = TaskEvent.of(this, TaskEvent.EventType.Completed);
			events.next(completeEvent);
		}

		/** Завершена ли задача? */
		@Override
		public boolean isCompleted()
		{
			return isCompleted;
		}

		/** Приостановлена ли задача? */
		@Override
		public boolean isPaused()
		{
			return false;
		}

		/** Запущена ли задача? */
		@Override
		public boolean isRunnig()
		{
			return !isCompleted;
		}

		/** Поддерживает ли задача обработку событий? */
		@Override
		public boolean isEventsSupported()
		{
			return events != null;
		}

		/**
		 * Точка для подписки на события, если поддерживаются. Null, если нет.
		 */
		@Override
		public IObservable<TaskEvent> events()
		{
			return events;
		}
	}

	/**
	 * Поток, в котором выполняются задачи из {@link ThreadPoolTaskFactory}
	 * 
	 * @author swayfarer
	 */
	@InternalElement
	public static class TaskProcessorThread extends Thread {

		/** Менеджер задач, в котором выполняется поток */
		@InternalElement
		public ThreadPoolTaskFactory poolManager;

		/** Время работы потока без задачи */
		@InternalElement
		public AtomicLong emptyLifeTime = new AtomicLong(0);

		/** Выполняется ли в данный момент задача? */
		@InternalElement
		public AtomicBoolean isProcessingTask = new AtomicBoolean(false);

		/**
		 * Жив ли поток? ("Мертвые" потоки удаляются из
		 * {@link ThreadPoolTaskFactory})
		 */
		@InternalElement
		public AtomicBoolean isAlive = new AtomicBoolean(true);

		/**
		 * Конструктор
		 * 
		 * @param poolManager
		 *            См. {@link #poolManager}
		 */
		public TaskProcessorThread(ThreadPoolTaskFactory poolManager)
		{
			super(poolManager.name + ":Thread-" + poolManager.nextThreadId.getAndAdd(1));
			this.poolManager = poolManager;
		}

		/**
		 * Поток завершает свою работу
		 */
		@InternalElement
		public void onStop()
		{
			isProcessingTask.set(false);
			isAlive.set(false);
		}

		/**
		 * Старт потока
		 */
		@InternalElement
		@Override
		public void run()
		{
			try
			{
				for (;;)
				{
					ThreadExecutorTask task = poolManager.getNextTask();

					if (task == null)
					{
						isProcessingTask.set(false);

						if (emptyLifeTime.get() > poolManager.getMaxThreadLifetime())
						{
							onStop();
							interrupt();
							return;
						}
						else
						{
							sleep(1);
							emptyLifeTime.incrementAndGet();
						}
					}
					else
					{
						isProcessingTask.set(true);
						task.start();
						emptyLifeTime.set(0);
					}
				}
			}
			catch (Throwable e)
			{
				onStop();
				logger.error(e, "Error while processing tasks");
				e.printStackTrace();
			}
		}

		/** Выполняется ли сейчас задача? */
		public boolean isProcessingTask()
		{
			return isProcessingTask.get();
		}
	}

	@Override
	public void execute(Runnable command) {
		executeTask(command);
	}
}
