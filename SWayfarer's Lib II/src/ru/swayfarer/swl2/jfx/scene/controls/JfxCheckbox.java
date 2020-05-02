package ru.swayfarer.swl2.jfx.scene.controls;

import javafx.scene.control.CheckBox;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.jfx.helpers.KeyboardEventsHelper;
import ru.swayfarer.swl2.jfx.helpers.MouseEventsHelper;
import ru.swayfarer.swl2.jfx.helpers.ScaleEventsHelper;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.observable.Observables;
import ru.swayfarer.swl2.observable.property.ObservableProperty;
import ru.swayfarer.swl2.observable.ref.ObservableListernerRef;
import ru.swayfarer.swl2.observable.subscription.ISubscription;
import ru.swayfarer.swl2.tasks.RecursiveSafeTask;

public class JfxCheckbox extends CheckBox implements IJavafxWidget {

	/** Ссылка на обработчик изменения значения чекбокса для обеспечения привязки */
	@InternalElement
	public ObservableListernerRef<Boolean> activeAttachCheckboxFunRef = new ObservableListernerRef<>();
	
	/** Подписка на событие изменения проперти, к которой прикреплен {@link CheckBox}. Хранится для отписки при перепривязке. */
	@InternalElement
	public ISubscription<?> activeAttachPropertySubscription;

	
	public KeyboardEventsHelper eventsKeyboard = new KeyboardEventsHelper().init(this);
	public MouseEventsHelper eventsMouse = new MouseEventsHelper().init(this);
	public ScaleEventsHelper eventsScale = new ScaleEventsHelper().init(this);
	
	public ObservableProperty<Boolean> isSelected = Observables.createProperty(false);
	
	public RecursiveSafeTask recursiveSafeTask = new RecursiveSafeTask();
	
	public JfxCheckbox()
	{
		super();
		init();
	}

	public JfxCheckbox(String text)
	{
		super(text);
		init();
	}
	
	public void init()
	{
		RecursiveSafeTask recursiveSafeTask = new RecursiveSafeTask();
		
		isSelected.subscribe((event) -> {
			recursiveSafeTask.start(() -> {
				selectedProperty().set(event.getNewValue());
			});
		});
		
		selectedProperty().addListener((item, oldValue, newValue) -> {
			recursiveSafeTask.start(() -> {
				isSelected.setValue(newValue);
			});
		});
	}
	
	/** Привязать к проперти */
	public <T extends JfxCheckbox> T attachTo(ObservableProperty<Boolean> propery)
	{
		return attachTo(propery, (b) -> b, (b) -> (Boolean) b);
	}
	
	/** Привязать к пропети, указав функции преобразования значений чекбокса и проперти */
	public <Property_Type, T extends JfxCheckbox> T attachTo(ObservableProperty<Property_Type> propery, IFunction1<Boolean, ? extends Property_Type> valueMapper, IFunction1<Property_Type, Boolean> checkMapper)
	{
		// Если подписка на проперти уже есть, то ее надо удалить 
		
		if (activeAttachPropertySubscription != null && !activeAttachPropertySubscription.isDisposed())
			activeAttachPropertySubscription.dispose();
		
		setSelected(checkMapper.apply(propery.getValue()));
		
		activeAttachPropertySubscription = propery.eventChange.subscribe((listener) -> { 
			recursiveSafeTask.start(() -> setSelected(checkMapper.apply(listener.getNewValue())));
		});
		
		activeAttachCheckboxFunRef.setValue((sub, isChecked) -> {
			recursiveSafeTask.start(() -> propery.setValue(valueMapper.apply(isSelected())));
		});
		
		return (T) this;
	}

}
