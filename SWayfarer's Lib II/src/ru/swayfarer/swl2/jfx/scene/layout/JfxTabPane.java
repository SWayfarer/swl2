package ru.swayfarer.swl2.jfx.scene.layout;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import ru.swayfarer.swl2.jfx.helpers.KeyboardEventsHelper;
import ru.swayfarer.swl2.jfx.helpers.MouseEventsHelper;
import ru.swayfarer.swl2.jfx.helpers.ScaleEventsHelper;
import ru.swayfarer.swl2.jfx.scene.controls.IJavafxWidget;
import ru.swayfarer.swl2.jfx.utils.JfxUtils;

public class JfxTabPane extends TabPane implements IJavafxWidget {

	public KeyboardEventsHelper eventsKeyboard = new KeyboardEventsHelper().init(this);
	public MouseEventsHelper eventsMouse = new MouseEventsHelper().init(this);
	public ScaleEventsHelper eventsScale = new ScaleEventsHelper().init(this);
	
	public Tab addTab(String name, Node content)
	{
		return addTab(name, content, false);
	}
	
	public Tab addTab(String name, Node content, boolean isClosable)
	{
		Tab tab = new Tab(name);
		tab.setContent(content);
		tab.setClosable(isClosable);
		getTabs().add(tab);
		
		if (content instanceof Region)
			JfxUtils.fitTo(this, (Region) content);
		else if (content instanceof ImageView)
			JfxUtils.fitTo(this, (ImageView) content);
		
		return tab;
	}
	
}
