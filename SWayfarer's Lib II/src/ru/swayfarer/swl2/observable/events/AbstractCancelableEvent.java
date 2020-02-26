package ru.swayfarer.swl2.observable.events;

import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2;
import ru.swayfarer.swl2.markers.InternalElement;

/**
 * Простой шаблон для отменяемого события 
 * @author swayfarer
 */

@SuppressWarnings("unchecked")
public abstract class AbstractCancelableEvent {
	
	/** Отменено ли событие */
	@InternalElement
	public boolean isCanceled;
	
	/** Задать специальную функцию, которая может запрерить, или разрешить применение {@link #setCanceled(boolean)} */
	@InternalElement
	public IFunction2<StackTraceElement, AbstractCancelableEvent, Boolean> cancelAcceptor;
	
	/** Элемент стактрейса, указывающий на последний источник отмены*/
	@InternalElement
	public StackTraceElement whoCanceled;
	
	/** Отменено ли событие */
	public boolean isCanceled()
	{
		return isCanceled;
	}
	
	/** 
	 * Задать статус события 
	 * @param isCanceled Отменено ли?
	 * @return Оригинальное событие с примененными изменениями 
	 */
	public <T extends AbstractCancelableEvent> T setCanceled(boolean isCanceled)
	{
		whoCanceled = isCanceled ? ExceptionsUtils.getThreadStacktrace(1)[0] : null;
		
		this.isCanceled = isCanceled;
		return (T) this;
	}
	
	/**
	 * Получить последнее место, откуда отмелялось событие (затирается при возобновлении) 
	 * @return {@link StackTraceElement}, если событие остановлено или Null, если нет.
	 */
	public StackTraceElement whoCanceled()
	{
		return whoCanceled;
	}
}
