package ru.swayfarer.swl2.jfx.scene.controls;

import javafx.scene.control.Slider;
import ru.swayfarer.swl2.jfx.helpers.KeyboardEventsHelper;
import ru.swayfarer.swl2.jfx.helpers.MouseEventsHelper;
import ru.swayfarer.swl2.jfx.helpers.ScaleEventsHelper;
import ru.swayfarer.swl2.jfx.utils.JfxUtils;
import ru.swayfarer.swl2.observable.Observables;
import ru.swayfarer.swl2.observable.property.ObservableProperty;

public class JfxSlider extends Slider implements IJavafxWidget {

	public KeyboardEventsHelper eventsKeyboard = new KeyboardEventsHelper().init(this);
	public MouseEventsHelper eventsMouse = new MouseEventsHelper().init(this);
	public ScaleEventsHelper eventsScale = new ScaleEventsHelper().init(this);
	
	public ObservableProperty<Double> value = Observables.createProperty();
	
	public JfxSlider()
	{
		super();
		init();
	}

	public JfxSlider(double min, double max, double value)
	{
		super(min, max, value);
		init();
	}
	
	public void init()
	{
		JfxUtils.attachProperties(value, valueProperty());
	}
}
