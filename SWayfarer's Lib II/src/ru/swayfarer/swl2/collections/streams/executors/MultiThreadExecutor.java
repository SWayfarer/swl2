package ru.swayfarer.swl2.collections.streams.executors;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.system.SystemUtils;
import ru.swayfarer.swl2.tasks.ITask;
import ru.swayfarer.swl2.tasks.factories.ThreadPoolTaskFactory;
import ru.swayfarer.swl2.threads.lock.CounterLock;

/**
 * Экзекьютор, выполняющий таски в многопоточном режиме
 * @author swayfarer
 *
 * @param <Element_Type> Тип элементов
 */
public class MultiThreadExecutor<Element_Type> implements IFunction2NoR<IFunction2NoR<Integer, ? super Element_Type>, IExtendedList<Element_Type>> {

	
	/** Обертка над фабрикой тасков, чтобы не сделать дедлока */
	@InternalElement
	public static ThreadLocal<ThreadPoolTaskFactory> taskFactoryThreadLocal = new ThreadLocal<ThreadPoolTaskFactory>() {
		
		protected ThreadPoolTaskFactory initialValue() 
		{
			return new ThreadPoolTaskFactory(30, SystemUtils.getCpuCoresCount()).setName("DataStreamsPool");
		}
	};
	
	@Override
	public void applyNoR(IFunction2NoR<Integer, ? super Element_Type> fun, IExtendedList<Element_Type> elements)
	{
		int next = 0;
		
		IExtendedList<ITask> tasks = CollectionsSWL.createExtendedList();
		
		CounterLock lock = new CounterLock();
		
		for (Element_Type element : elements)
		{
			final int id = next ++;

			lock.counter.incrementAndGet();
			ITask task = getThreadPoolTaskFactory().execute(() -> {
				fun.apply(id, element);
				lock.reduce();
			});
			
			tasks.add(task);
		}
		 
		lock.waitFor();
	}

	/** Получить локальный для этого потока экзекьютор */
	public static ThreadPoolTaskFactory getThreadPoolTaskFactory()
	{
		return taskFactoryThreadLocal.get();
	}
}
