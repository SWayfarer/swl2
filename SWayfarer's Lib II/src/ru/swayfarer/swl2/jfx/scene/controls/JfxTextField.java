package ru.swayfarer.swl2.jfx.scene.controls;

import javafx.scene.control.TextField;
import ru.swayfarer.swl2.jfx.helpers.KeyboardEventsHelper;
import ru.swayfarer.swl2.jfx.helpers.MouseEventsHelper;
import ru.swayfarer.swl2.jfx.helpers.ScaleEventsHelper;

public class JfxTextField extends TextField implements IJavafxWidget {

	public KeyboardEventsHelper eventsKeyboard = new KeyboardEventsHelper().init(this);
	public MouseEventsHelper eventsMouse = new MouseEventsHelper().init(this);
	public ScaleEventsHelper eventsScale = new ScaleEventsHelper().init(this);
	
	public JfxTextField()
	{
		super();
	}

	public JfxTextField(String text)
	{
		super(text);
	}

}
