package ru.swayfarer.swl2.process;

import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.Observables;
import ru.swayfarer.swl2.process.handlers.InpusStreamListenerFun;
import ru.swayfarer.swl2.process.handlers.ProcessAliveHandlerFun;
import ru.swayfarer.swl2.process.utils.ProcessUtils;
import ru.swayfarer.swl2.resource.streams.DataInputStreamSWL;
import ru.swayfarer.swl2.resource.streams.DataOutputStreamSWL;
import ru.swayfarer.swl2.threads.ThreadsUtils;
import ru.swayfarer.swl2.threads.lock.SynchronizeLock;

public class SimpleProcess implements IProcess {

	public IObservable<IProcess> eventStart = Observables.createObservable();
	public IObservable<Integer> eventKill = Observables.createObservable();
	
	public IObservable<String> eventOut = Observables.createObservable();
	public IObservable<String> eventErr = Observables.createObservable();
	
	public Process wrappedProcess;
	
	public InpusStreamListenerFun outHandler, errHandler;
	
	public volatile long processId;
	public volatile int exitCode;
	public volatile boolean isAlive = true;
	
	public volatile SynchronizeLock lock = new SynchronizeLock();
	
	public SimpleProcess(Process wrappedProcess)
	{
		this.wrappedProcess = wrappedProcess;
		this.processId = ProcessUtils.getProcessId(wrappedProcess);
		ProcessAliveHandlerFun.handleProcess(this);
	}
	
	@Override
	public IObservable<Integer> eventKill()
	{
		return eventKill;
	}

	@Override
	public int getExitCode()
	{
		ExceptionsUtils.If(isAlive(), IllegalStateException.class, "Can't get process exit code because it's not closed! Maybe use waitFor() method? ");
		
		return exitCode;
	}

	@Override
	public int waitFor()
	{
		if (isAlive)
			lock.waitFor();
		
		return exitCode;
	}

	@Override
	public boolean isAlive()
	{
		return isAlive;
	}

	@Override
	public void kill(boolean isForce, boolean isKillChildren)
	{
		ProcessUtils.killProcess(processId, isForce, isKillChildren);
	}

	@Override
	public long getProcessId()
	{
		return processId;
	}

	@Override
	public boolean isEventsSupported()
	{
		return true;
	}

	@Override
	public Process asJavaProcess()
	{
		return wrappedProcess;
	}

	@Override
	public DataInputStreamSWL getInputStream()
	{
		return DataInputStreamSWL.of(wrappedProcess.getInputStream());
	}

	@Override
	public DataOutputStreamSWL getOutputStream()
	{
		return DataOutputStreamSWL.of(wrappedProcess.getOutputStream());
	}

	@Override
	public void onProcessExited(int processId)
	{
		this.isAlive = false;
		this.exitCode = processId;
		this.lock.notifyLockAll();
		eventKill.next(processId);
	}

	@Override
	public IObservable<String> eventOut()
	{
		return eventOut;
	}

	@Override
	public IObservable<String> eventErr()
	{
		return eventErr;
	}

	public DataInputStreamSWL getErrorStream()
	{
		return DataInputStreamSWL.of(wrappedProcess.getErrorStream());
	}
	
	@Override
	public void enableOutHandling(boolean isDaemon)
	{
		if (outHandler != null)
			return;
		
		outHandler = new InpusStreamListenerFun(eventOut, this::getInputStream);
		ThreadsUtils.newThread("ProcessOutListener:" + processId, outHandler, isDaemon);
	}

	@Override
	public void enableErrHandling(boolean isDaemon)
	{
		if (errHandler != null)
			return;
		
		errHandler = new InpusStreamListenerFun(eventErr, this::getErrorStream);
		ThreadsUtils.newThread("ProcessErrListener:" + processId, errHandler, isDaemon);
	}

}
