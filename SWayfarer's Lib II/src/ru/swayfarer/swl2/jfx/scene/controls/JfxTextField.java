package ru.swayfarer.swl2.jfx.scene.controls;

import javafx.scene.control.TextField;
import ru.swayfarer.swl2.jfx.helpers.KeyboardEventsHelper;
import ru.swayfarer.swl2.jfx.helpers.MouseEventsHelper;
import ru.swayfarer.swl2.jfx.helpers.ScaleEventsHelper;
import ru.swayfarer.swl2.jfx.utils.JfxUtils;
import ru.swayfarer.swl2.observable.Observables;
import ru.swayfarer.swl2.observable.property.ObservableProperty;

public class JfxTextField extends TextField implements IJavafxWidget {

	public KeyboardEventsHelper eventsKeyboard = new KeyboardEventsHelper().init(this);
	public MouseEventsHelper eventsMouse = new MouseEventsHelper().init(this);
	public ScaleEventsHelper eventsScale = new ScaleEventsHelper().init(this);
	
	public ObservableProperty<String> value = Observables.createProperty();
	
	public JfxTextField()
	{
		super();
		init();
	}

	public JfxTextField(String text)
	{
		super(text);
		init();
	}
	
	public void init()
	{
		JfxUtils.attachProperties(value, textProperty());
	}

}
