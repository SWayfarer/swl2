package ru.swayfarer.swl2.jfx.scene.layout;

import javafx.scene.layout.Region;
import ru.swayfarer.swl2.jfx.helpers.KeyboardEventsHelper;
import ru.swayfarer.swl2.jfx.helpers.MouseEventsHelper;
import ru.swayfarer.swl2.jfx.helpers.ScaleEventsHelper;
import ru.swayfarer.swl2.jfx.scene.controls.IJavafxWidget;

public class JfxRegion extends Region implements IJavafxWidget {

	public KeyboardEventsHelper eventsKeyboard = new KeyboardEventsHelper().init(this);
	public MouseEventsHelper eventsMouse = new MouseEventsHelper().init(this);
	public ScaleEventsHelper eventsScale = new ScaleEventsHelper().init(this);
	
	@Override
	public void setWidth(double value)
	{
		super.setWidth(value);
	}
	
	@Override
	public void setHeight(double value)
	{
		super.setHeight(value);
	}
	
}
