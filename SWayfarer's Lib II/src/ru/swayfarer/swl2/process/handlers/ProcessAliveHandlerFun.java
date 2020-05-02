package ru.swayfarer.swl2.process.handlers;

import java.lang.ref.WeakReference;

import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction0NoR;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.process.IProcess;
import ru.swayfarer.swl2.threads.ThreadsUtils;

@SuppressWarnings("unchecked")
public class ProcessAliveHandlerFun implements IFunction0NoR {

	public static ILogger logger = LoggingManager.getLogger();
	
	public WeakReference<IProcess> handlingProcess = new WeakReference<IProcess>(null);
	
	@Override
	public void applyNoR()
	{
		IProcess handlingProcess = getHandlingProcess();
		
		if (handlingProcess != null)
		{
			try
			{
				int exitCode = handlingProcess.asJavaProcess().waitFor();
				handlingProcess.onProcessExited(exitCode);
			}
			catch (InterruptedException e)
			{
				// Nope
			}
		}
	}
	
	public <T extends ProcessAliveHandlerFun> T setProcess(IProcess process) 
	{
		this.handlingProcess = new WeakReference<IProcess>(process);
		return (T) this;
	}
	
	public IProcess getHandlingProcess()
	{
		return handlingProcess.get();
	}

	public static ProcessAliveHandlerFun handleProcess(IProcess process)
	{
		ProcessAliveHandlerFun handlerFun = new ProcessAliveHandlerFun();
		handlerFun.setProcess(process);
		
		ThreadsUtils.newThread("ProcessAliveHandler:"+process.getProcessId(), handlerFun::apply, true);
		
		return handlerFun;
	}
}
