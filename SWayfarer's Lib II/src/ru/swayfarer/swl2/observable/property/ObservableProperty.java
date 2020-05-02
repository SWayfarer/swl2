package ru.swayfarer.swl2.observable.property;

import lombok.AllArgsConstructor;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction0NoR;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1NoR;
import ru.swayfarer.swl2.markers.Alias;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.SimpleObservable;
import ru.swayfarer.swl2.observable.events.AbstractCancelableEvent;
import ru.swayfarer.swl2.observable.subscription.ISubscription;
import ru.swayfarer.swl2.reference.IReference;
import ru.swayfarer.swl2.tasks.RecursiveSafeTask;

/**
 * Наблюдаемая проперти
 * @author swayfarer
 */
@SuppressWarnings("unchecked")
public class ObservableProperty<Element_Type> extends PropertyContainer {

	/** Подписываемся ли на уже совершенное событие по-умолчанию? */
	@InternalElement
	public boolean isPostByDefault = false;
	
	/** Событие изменения проперти */
	@InternalElement
	public IObservable<PropertyChangeEvent> eventChange = new SimpleObservable<>();
	
	/** Событие изменения проперти */
	@InternalElement
	public IObservable<PropertyChangeEvent> eventPostChange = new SimpleObservable<>();
	
	public ISubscription<PropertyChangeEvent> bindingEventSubscribe;
	
	public ObservableProperty() {}
	
	public ObservableProperty(Element_Type initialValue)
	{
		this.setValue(initialValue);
	}
	
	/** Задать {@link #isPostByDefault} */
	public <T extends ObservableProperty<Element_Type>> T setPost(boolean isPost) 
	{
		this.isPostByDefault = isPost;
		return (T) this;
	}
	
	public <T extends ObservableProperty<Element_Type>> T update() 
	{
		setValue(get());
		return (T) this;
	}
	
	/**
	 * Подписаться на изменения проперти. Когда цель подписки будет меняться, эначение этой будет задано на аналогичное 
	 * @param target Цель подписки
	 * @return Оригинальную(эту) проперти
	 */
	public <T extends ObservableProperty<Element_Type>> T bind(ObservableProperty<? extends Element_Type> target)
	{
		return bind((event) -> event.getNewValue(), target);
	}
	
	/**
	 * Подписать каждую проперти на вторую см ({@link #bind(ObservableProperty)}))
	 */
	public <T extends ObservableProperty<Element_Type>> T bindTwin(ObservableProperty<Element_Type> target) 
	{
		RecursiveSafeTask taskExec = new RecursiveSafeTask();
		
		target.subscribe((event) -> {
			taskExec.start(() -> {
				this.setValue(event.getNewValue());
			});
		});
		
		this.subscribe((event) -> {
			taskExec.start(() -> {
				target.setValue(event.getNewValue());
			});
		});
		
		return (T) this;
	}
	
	/**
	 * Подписать каждую проперти на вторую см ({@link #bind(ObservableProperty)}))
	 * <h1> Эта подписка не проверяет совместимость значение пропертей. Делайте это сами! </h1> 
	 */
	public <T extends ObservableProperty<Element_Type>> T bindTwinAutocast(ObservableProperty<? extends Object> target) 
	{
		RecursiveSafeTask taskExec = new RecursiveSafeTask();
		
		target.subscribe((event) -> {
			taskExec.start(() -> {
				this.setValue(event.getNewValue());
			});
		});
		
		this.subscribe((event) -> {
			taskExec.start(() -> {
				target.setValue(event.getNewValue());
			});
		});
		
		return (T) this;
	}
	
	/**
	 * Подписаться на изменения проперти. Когда цель подписки будет меняться, эначение этой будет задано на аналогичное 
	 * @param target Цель подписки
	 * @param valueFun Функция, конвертирующая значение цели подсписки в значение, которое будет задано этой проперти 
	 * @return Оригинальную(эту) проперти
	 */
	public <E, T extends ObservableProperty<Element_Type>> T bind(IFunction1<PropertyChangeEvent, ? extends Element_Type> valueFun, ObservableProperty<? extends E> target)
	{
		if (target == this)
			return (T) this;
		
		unbind();
		
		bindingEventSubscribe = target.eventChange.subscribe(999, (sub, e) -> this.setValue(valueFun.apply(e)));
		
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
	public IObservable<PropertyChangeEvent> getEventChange()
	{
		return eventChange;
	}
	
	/** Подписаться на изменения проперти */
	@Alias("addChangeHandler")
	public <T extends ObservableProperty<Element_Type>> T subscribe(IFunction1NoR<PropertyChangeEvent> handlerFun)
	{
		return addChangeHandler(handlerFun);
	}
	
	/** Подписаться на изменения проперти */
	public <T extends ObservableProperty<Element_Type>> T addChangeHandler(IFunction1NoR<PropertyChangeEvent> handlerFun)
	{
		IObservable<PropertyChangeEvent> observable = isPostByDefault ? eventPostChange : eventChange;
		
		observable.subscribe(handlerFun);
		
		return (T) this;
	}
	
	/** Подписаться на изменения проперти */
	public <T extends ObservableProperty<Element_Type>> T addChangeHandler(IReference<IFunction1NoR<PropertyChangeEvent>> ref)
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
		PropertyChangeEvent event = PropertyChangeEvent.of(this.value, value);
		eventChange.next(event);
		
		if (!event.isCanceled())
			this.value = event.newValue;
		
		eventPostChange.next(event);
		
		return (T) this;
	}
	
	/**
	 * Событие изменения значения проперти
	 * @author swayfarer
	 *
	 */
	@AllArgsConstructor(staticName = "of")
	public static class PropertyChangeEvent extends AbstractCancelableEvent { 
		
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
		public <T extends PropertyChangeEvent> T setNewValue(Object newValue)
		{
			this.newValue = newValue;
			return (T) this;
		}
	}
}
