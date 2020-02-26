package ru.swayfarer.swl2.observable.subscription;

import java.lang.ref.WeakReference;

import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.observable.IObservable;

/**
 * Самоотписывающаяся, ("Слабая") подписка.
 * <br> Не требует вызова {@link #dispose()}, но необходимо хранить ее в отдельном поле, чтобы GC не собрал её
 * @author swayfarer
 */
public class WeakSubscription<Event_Type> extends AbstractSubscription<Event_Type> {

	/** Слабая ссылка на функцию, выполняемую при вызове {@link IObservable#next(Object)} */
	public WeakReference<IFunction2NoR<ISubscription<Event_Type>, Event_Type>> functionRef;
	
	/** Конструктор */
	public WeakSubscription(IFunction2NoR<ISubscription<Event_Type>, Event_Type> fun)
	{
		ExceptionsUtils.IfNull(fun, IllegalArgumentException.class, "Function of subscription can not be null!");
		functionRef = new WeakReference<IFunction2NoR<ISubscription<Event_Type>,Event_Type>>(fun);
	}
	
	/** Необходимо ли отвязать подписку от слушаемого объекта? */
	@Override
	public boolean isDisposed()
	{
		return functionRef == null || functionRef.get() == null || super.isDisposed();
	}

	@Override
	public IFunction2NoR<ISubscription<Event_Type>, Event_Type> getFun()
	{
		return functionRef.get();
	}

}
