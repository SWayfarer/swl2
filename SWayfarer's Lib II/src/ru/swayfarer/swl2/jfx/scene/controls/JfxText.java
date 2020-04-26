package ru.swayfarer.swl2.jfx.scene.controls;

import javafx.scene.text.Text;
import ru.swayfarer.swl2.jfx.helpers.KeyboardEventsHelper;
import ru.swayfarer.swl2.jfx.helpers.MouseEventsHelper;

public class JfxText extends Text implements IJavafxWidget {

	public KeyboardEventsHelper eventsKeyboard = new KeyboardEventsHelper().init(this);
	public MouseEventsHelper eventsMouse = new MouseEventsHelper().init(this);
	
	public JfxText()
	{
		super();
	}
	public JfxText(double x, double y, String text)
	{
		super(x, y, text);
	}
	public JfxText(String text)
	{
		super(text);
	}
}
