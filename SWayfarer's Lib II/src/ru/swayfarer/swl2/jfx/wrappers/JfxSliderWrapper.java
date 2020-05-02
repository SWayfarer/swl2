package ru.swayfarer.swl2.jfx.wrappers;

import javafx.scene.control.Slider;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1NoR;
import ru.swayfarer.swl2.markers.Alias;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.SimpleObservable;
import ru.swayfarer.swl2.observable.property.ObservableProperty;
import ru.swayfarer.swl2.observable.property.ObservableProperty.PropertyChangeEvent;
import ru.swayfarer.swl2.observable.ref.ObservableListernerRef;
import ru.swayfarer.swl2.observable.subscription.ISubscription;
import ru.swayfarer.swl2.recursive.RecursiveBreaker;

/**
 * Обертка над {@link Slider} 
 * @author swayfarer
 *
 */
public class JfxSliderWrapper implements IFxmlWrapper {

	/** Контейнер для безопасного от рекурсии запуска задач */
	@InternalElement
	public RecursiveBreaker recursiveBreaker = new RecursiveBreaker();
	
	/** Обернутый слайдер */
	@InternalElement
	public Slider wrappedSlider;
	
	/** Функция, отслеживающая привязку на стороне {@link Slider}'а */
	@InternalElement
	public ObservableListernerRef<Double> attachSliderFunRef = new ObservableListernerRef<>();
	
	/** Активная подписка на проперти, обеспечивающая привязку к ней */
	@InternalElement
	public ISubscription<PropertyChangeEvent> activeAttachPropertySubscription;
	
	/** Событие перемещения слайдера */
	@InternalElement
	public IObservable<Double> eventSlide = new SimpleObservable<>();

	/** Конструктор */
	public JfxSliderWrapper(Slider wrappedSlider)
	{
		super();
		this.wrappedSlider = wrappedSlider;
		
		wrappedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			eventSlide.next(newValue.doubleValue());
		});
		
		eventSlide.subscribe(0, attachSliderFunRef);
	}
	
	/** Добавить обработчик изменений позиции {@link Slider}'а */
	@Alias("addSlideHandler")
	public void subscibe(IFunction1NoR<Double> fun)
	{
		addSlideHandler(fun);
	}
	
	/** Добавить обработчик изменений позиции {@link Slider}'а */
	public void addSlideHandler(IFunction1NoR<Double> fun)
	{
		eventSlide.subscribe(fun);
	}
	
	/** Привязать к числовой проперти */
	public void attachTo(ObservableProperty<? extends Number> property)
	{
		attachSliderFunRef.setValue((sub, d) -> {
			recursiveBreaker.run(() -> setPosition(property.getIntValue()));
		});
		
		// Если есть активная подписка на проперти, то ее надо отменить 
		
		if (activeAttachPropertySubscription != null)
			activeAttachPropertySubscription.dispose();
		
		// Подписываемся на проперти и сохраняем подписку для дальнейшего удаления 
		
		activeAttachPropertySubscription = property.eventChange.subscribe((d) -> {
			recursiveBreaker.run(() -> {
				property.value = d;
			});
		});
	}
	
	/** Задать позицию */
	public void setPosition(int value)
	{
		setPosition(value / (float) wrappedSlider.getMax());
	}
	
	/** Задать позицию */
	public void setPosition(float value)
	{
		wrappedSlider.valueProperty().set(value);
	}
}
