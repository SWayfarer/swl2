package ru.swayfarer.swl2.tasks;

import java.util.concurrent.atomic.AtomicBoolean;

import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction0NoR;
import ru.swayfarer.swl2.observable.IObservable;

public class RecursiveSafeTask implements ITask {

	public IFunction0NoR run;
	
	public AtomicBoolean isWorking = new AtomicBoolean();
	
	public RecursiveSafeTask()
	{
		this(() -> {
			
		});
	}
	
	public RecursiveSafeTask(IFunction0NoR run)
	{
		super();
		this.run = run;
	}

	public void start(IFunction0NoR run)
	{
		if (isRunnig())
			return;
		
		this.run = run;
		start();
	}
	
	@Override
	public void start()
	{
		if (isRunnig())
			return;
		
		isWorking.set(true);
		
		run.__();
		
		isWorking.set(false);
	}

	@Override
	public boolean isCompleted()
	{
		return !isWorking.get();
	}

	@Override
	public boolean isPaused()
	{
		return false;
	}

	@Override
	public boolean isRunnig()
	{
		return isWorking.get();
	}

	@Override
	public boolean isEventsSupported()
	{
		return false;
	}

	@Override
	public IObservable<TaskEvent> events()
	{
		return null;
	}

}
