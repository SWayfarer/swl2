package ru.swayfarer.swl2.jfx.scene.layout;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import ru.swayfarer.swl2.jfx.helpers.KeyboardEventsHelper;
import ru.swayfarer.swl2.jfx.helpers.MouseEventsHelper;
import ru.swayfarer.swl2.jfx.helpers.ScaleEventsHelper;
import ru.swayfarer.swl2.jfx.scene.controls.IJavafxWidget;

public class JfxVBox extends VBox implements IJavafxWidget {

	public KeyboardEventsHelper eventsKeyboard = new KeyboardEventsHelper().init(this);
	public MouseEventsHelper eventsMouse = new MouseEventsHelper().init(this);
	public ScaleEventsHelper eventsScale = new ScaleEventsHelper().init(this);
	
	public JfxVBox()
	{
		super();
	}

	public JfxVBox(double spacing, Node... children)
	{
		super(spacing, children);
	}

	public JfxVBox(double spacing)
	{
		super(spacing);
	}

	public JfxVBox(Node... children)
	{
		super(children);
	}

}
