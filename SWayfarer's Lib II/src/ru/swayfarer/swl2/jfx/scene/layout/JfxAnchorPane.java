package ru.swayfarer.swl2.jfx.scene.layout;

import javafx.scene.layout.AnchorPane;
import ru.swayfarer.swl2.jfx.helpers.KeyboardEventsHelper;
import ru.swayfarer.swl2.jfx.helpers.MouseEventsHelper;
import ru.swayfarer.swl2.jfx.helpers.ScaleEventsHelper;
import ru.swayfarer.swl2.jfx.scene.controls.IJavafxWidget;

public class JfxAnchorPane extends AnchorPane implements IJavafxWidget{

	public KeyboardEventsHelper eventsKeyboard = new KeyboardEventsHelper().init(this);
	public MouseEventsHelper eventsMouse = new MouseEventsHelper().init(this);
	public ScaleEventsHelper eventsScale = new ScaleEventsHelper().init(this);
	
	
}
