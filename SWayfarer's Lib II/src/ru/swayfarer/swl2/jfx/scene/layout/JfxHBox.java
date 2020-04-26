package ru.swayfarer.swl2.jfx.scene.layout;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import ru.swayfarer.swl2.jfx.helpers.KeyboardEventsHelper;
import ru.swayfarer.swl2.jfx.helpers.MouseEventsHelper;
import ru.swayfarer.swl2.jfx.helpers.ScaleEventsHelper;
import ru.swayfarer.swl2.jfx.scene.controls.IJavafxWidget;

public class JfxHBox extends HBox implements IJavafxWidget {

	public KeyboardEventsHelper eventsKeyboard = new KeyboardEventsHelper().init(this);
	public MouseEventsHelper eventsMouse = new MouseEventsHelper().init(this);
	public ScaleEventsHelper eventsScale = new ScaleEventsHelper().init(this);
	
	public JfxHBox()
	{
		super();
	}

	public JfxHBox(double spacing, Node... children)
	{
		super(spacing, children);
	}

	public JfxHBox(double spacing)
	{
		super(spacing);
	}

	public JfxHBox(Node... children)
	{
		super(children);
	}

}
