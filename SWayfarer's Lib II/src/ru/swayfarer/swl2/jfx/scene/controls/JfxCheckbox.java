package ru.swayfarer.swl2.jfx.scene.controls;

import javafx.scene.control.CheckBox;
import ru.swayfarer.swl2.jfx.helpers.KeyboardEventsHelper;
import ru.swayfarer.swl2.jfx.helpers.MouseEventsHelper;
import ru.swayfarer.swl2.jfx.helpers.ScaleEventsHelper;

public class JfxCheckbox extends CheckBox implements IJavafxWidget {

	public KeyboardEventsHelper eventsKeyboard = new KeyboardEventsHelper().init(this);
	public MouseEventsHelper eventsMouse = new MouseEventsHelper().init(this);
	public ScaleEventsHelper eventsScale = new ScaleEventsHelper().init(this);
	
	public JfxCheckbox()
	{
		super();
	}

	public JfxCheckbox(String text)
	{
		super(text);
	}

}
