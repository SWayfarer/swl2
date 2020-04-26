package ru.swayfarer.swl2.jfx.scene.controls;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import ru.swayfarer.swl2.jfx.helpers.KeyboardEventsHelper;
import ru.swayfarer.swl2.jfx.helpers.MouseEventsHelper;
import ru.swayfarer.swl2.jfx.helpers.ScaleEventsHelper;

@SuppressWarnings("unchecked")
public class JfxLabel extends Label implements IJavafxWidget {

	public KeyboardEventsHelper eventsKeyboard = new KeyboardEventsHelper().init(this);
	public MouseEventsHelper eventsMouse = new MouseEventsHelper().init(this);
	public ScaleEventsHelper eventsScale = new ScaleEventsHelper().init(this);
	
	public JfxLabel()
	{
		super();
	}

	public JfxLabel(String text, Node graphic)
	{
		super(text, graphic);
	}

	public JfxLabel(String text)
	{
		super(text);
	}
	
	public <T extends JfxLabel> T setFontScale(float scale) 
	{
		Font font = getFont();
		Font newFont = Font.font(font.getFamily(), font.getSize());
		setFont(newFont);
		return (T) this;
	}

}
