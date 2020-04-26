package ru.swayfarer.swl2.jfx.scene.controls;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import ru.swayfarer.swl2.jfx.helpers.KeyboardEventsHelper;
import ru.swayfarer.swl2.jfx.helpers.MouseEventsHelper;
import ru.swayfarer.swl2.jfx.helpers.ScaleEventsHelper;
import ru.swayfarer.swl2.observable.Observables;
import ru.swayfarer.swl2.observable.property.ObservableProperty;
import ru.swayfarer.swl2.tasks.RecursiveSafeTask;

public class JfxComboBox<Item_Type> extends ComboBox<Item_Type> implements IJavafxWidget {

	public KeyboardEventsHelper eventsKeyboard = new KeyboardEventsHelper().init(this);
	public MouseEventsHelper eventsMouse = new MouseEventsHelper().init(this);
	public ScaleEventsHelper eventsScale = new ScaleEventsHelper().init(this);
	
	public RecursiveSafeTask bindingExecutor = new RecursiveSafeTask();
	public ObservableProperty<Item_Type> selectedItem = Observables.createProperty();
	
	public JfxComboBox()
	{
		super();
		init();
	}

	public JfxComboBox(ObservableList<Item_Type> items)
	{
		super(items);
		init();
	}
	
	public void init()
	{
		valueProperty().addListener((item, oldValue, newValue) -> {
			bindingExecutor.start(() -> selectedItem.setValue(newValue));
		});
		
		selectedItem.subscribe((event) -> {
			bindingExecutor.start(() -> valueProperty().set(event.getNewValue()));
		});
	}
	
}
