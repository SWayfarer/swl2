package ru.swayfarer.swl2.observable.property;

import lombok.AllArgsConstructor;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction0NoR;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1NoR;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2;
import ru.swayfarer.swl2.markers.Alias;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.SimpleObservable;
import ru.swayfarer.swl2.observable.events.AbstractCancelableEvent;
import ru.swayfarer.swl2.observable.subscription.ISubscription;
import ru.swayfarer.swl2.reference.IReference;

/**
 * Наблюдаемая проперти
 * @author swayfarer
 */
@SuppressWarnings("unchecked")
public class ObservableProperty<Element_Type> extends PropertyContainer {

	/** Событие изменения проперти */
	@InternalElement
	public IObservable<ChangeEvent> eventChange = new SimpleObservable<>();
	
	public ISubscription<ChangeEvent> bindingEventSubscribe;
	
	public ObservableProperty() {}
	
	public ObservableProperty(Element_Type initialValue)
	{
		this.setValue(initialValue);
	}
	
	/**
	 * Подписаться на изменения проперти. Когда цель подписки будет меняться, эначение этой будет задано на аналогичное 
	 * @param target Цель подписки
	 * @return Оригинальную(эту) проперти
	 */
	public <T extends ObservableProperty<Element_Type>> T bind(ObservableProperty<? extends Element_Type> target)
	{
		return bind((property, event) -> event.getNewValue(), target);
	}
	
	/**
	 * Подписаться на изменения проперти. Когда цель подписки будет меняться, эначение этой будет задано на аналогичное 
	 * @param target Цель подписки
	 * @param valueFun Функция, конвертирующая значение цели подсписки в значение, которое будет задано этой проперти 
	 * @return Оригинальную(эту) проперти
	 */
	public <T extends ObservableProperty<Element_Type>> T bind(IFunction2<ObservableProperty<? extends Element_Type>, ChangeEvent, ? extends Element_Type> valueFun, ObservableProperty<? extends Element_Type> target)
	{
		if (target == this)
			return (T) this;
		
		unbind();
		
		bindingEventSubscribe = target.eventChange.subscribe(999, (sub, e) -> this.setValue(valueFun.apply(target, e)));
		
		return (T) this;
	}
	
	/** Выполнить действие, если значение null */
	public <T extends ObservableProperty<Element_Type>> T IfNull(IFunction0NoR fun) 
	{
		if (get() == null)
			fun.apply();
		
		return (T) this;
	}
	
	/** Выполнить действие, если значение не null */
	public <T extends ObservableProperty<Element_Type>> T IfNotNull(IFunction0NoR fun) 
	{
		Element_Type value = get();
		
		if (value != null)
			fun.apply();
		
		return (T) this;	
	}
	
	/** Выполнить действие, если значение не null */
	public <T extends ObservableProperty<Element_Type>> T IfNotNull(IFunction1NoR<Element_Type> fun) 
	{
		Element_Type value = get();
		
		if (value != null)
			fun.apply(value);
		
		return (T) this;
	}
	
	/** Отключить биндинги */
	public <T extends ObservableProperty<Element_Type>> T unbind()
	{
		if (bindingEventSubscribe != null)
			bindingEventSubscribe.dispose();
		
		return (T) this;
	}
	
	/** Получить {@link #eventChange} */
	public IObservable<ChangeEvent> getEventChange()
	{
		return eventChange;
	}
	
	/** Подписаться на изменения проперти */
	@Alias("addChangeHandler")
	public <T extends ObservableProperty<Element_Type>> T subscribe(IFunction1NoR<ChangeEvent> handlerFun)
	{
		return addChangeHandler(handlerFun);
	}
	
	/** Подписаться на изменения проперти */
	public <T extends ObservableProperty<Element_Type>> T addChangeHandler(IFunction1NoR<ChangeEvent> handlerFun)
	{
		eventChange.subscribe(handlerFun);
		return (T) this;
	}
	
	/** Подписаться на изменения проперти */
	public <T extends ObservableProperty<Element_Type>> T addChangeHandler(IReference<IFunction1NoR<ChangeEvent>> ref)
	{
		eventChange.subscribe((evt) -> ref.getValue().apply(evt));
		return (T) this;
	}
	
	/** Получить значение проперти */
	public Element_Type get()
	{
		return (Element_Type) super.get();
	}
	
	/** Вернуть к начальному значению */
	public <T extends ObservableProperty<Element_Type>> T reset() 
	{
		setValue(null);
		return (T) this;
	}
	
	/** Задать значение */
	public <T extends ObservableProperty<Element_Type>> T setValue(Element_Type value)
	{
		ChangeEvent event = ChangeEvent.of(this.value, value);
		eventChange.next(event);
		
		if (!event.isCanceled())
			this.value = event.newValue;
		
		return (T) this;
	}
	
	/**
	 * Событие изменения значения проперти
	 * @author swayfarer
	 *
	 */
	@AllArgsConstructor(staticName = "of")
	public static class ChangeEvent extends AbstractCancelableEvent { 
		
		/** Старое значение */
		public Object oldValue;
		
		/** 
		 * Новое значение
		 * <br> Именно это значение будет задано в проперти, если событие не прервется. 
		 * <br> Можно менять, чтобы повлиять на конечный результат изменения проперти.
		 */
		public Object newValue;
		
		/** Получить {@link #oldValue} */
		public <T> T getOldValue()
		{
			return (T) oldValue;
		}
		
		/** Получить {@link #newValue} */
		public <T> T getNewValue()
		{
			return (T) newValue;
		}
		
		/** Задать {@link #newValue} */
		public <T extends ChangeEvent> T setNewValue(Object newValue)
		{
			this.newValue = newValue;
			return (T) this;
		}
	}
}
