package ru.swayfarer.swl2.exceptions;

/**
 * {@link Runnable}, который может выдать исключение 
 * @author swayfarer
 *
 */
public interface IUnsafeRunnable {

	public void run() throws Throwable;

	/**
	 * Версия {@link IUnsafeRunnable}, которая умеет возвращать значения
	 * @author swayfarer
	 */
	public static interface IUnsafeRunnableWithReturn <T> {
		T run() throws Throwable;
	}
}
