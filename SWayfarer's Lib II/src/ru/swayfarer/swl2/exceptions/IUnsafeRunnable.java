package ru.swayfarer.swl2.exceptions;

/**
 * {@link Runnable}, который может выдать исключение 
 * @author swayfarer
 *
 */
public interface IUnsafeRunnable {

	public void run() throws Throwable;
	
}
