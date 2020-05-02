package ru.swayfarer.swl2.process;

import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.resource.streams.DataInputStreamSWL;
import ru.swayfarer.swl2.resource.streams.DataOutputStreamSWL;

/**
 * Процесс
 * @author swayfarer
 *
 */
public interface IProcess {

	/** 
	 * Получить событие завершения приложения
	 * @return Событие или null, если не {@link #isEventsSupported()}
	 */
	public IObservable<Integer> eventKill();
	
	/**
	 * Получить событие вывода строки out-потока процесса 
	 * @return Событие или null, если не {@link #isEventsSupported()}
	 */
	public IObservable<String> eventOut();

	/**
	 * Получить событие вывода строки err-потока процесса 
	 * @return Событие или null, если не {@link #isEventsSupported()}
	 */
	public IObservable<String> eventErr();
	
	/** Получить код, с которым завершилось приложение */
	public int getExitCode();
	
	/** Дождаться завершения приложения и получить его {@link #getExitCode()}*/
	public int waitFor();
	
	/** Работает ли процесс? */
	public boolean isAlive();
	
	/** Получить Java-{@link Process} */
	public Process asJavaProcess();
	
	/** 
	 * Звершить процесс
	 * @param isForce Принудительно ли?
	 * @param isKillChildren Завершать ли дочерние процессы? 
	 */
	public void kill(boolean isForce, boolean isKillChildren);
	
	/** Получить id процесса */
	public long getProcessId();
	
	/** Поддерживаются ли события? */
	public boolean isEventsSupported();
	
	/** Получить {@link DataInputStreamSWL}, в который пишется вывод процесса */
	public DataInputStreamSWL getInputStream();
	
	/** Получить {@link DataOutputStreamSWL}, через который можно передавать данные процессу */
	public DataOutputStreamSWL getOutputStream();
	
	/** Включить событие {@link #eventOut()}  */
	public void enableOutHandling(boolean isDaemon);
	
	/** Включить событие {@link #eventErr()}  */
	public void enableErrHandling(boolean isDaemon);
	
	/**
	 * <h1> Системный метод для потоков-обработчиков, обеспечивающих работу {@link IProcess} </h1>
	 * @param processId Код, с которым завершился процесс
	 */
	@InternalElement
	public void onProcessExited(int processId);
	
}
