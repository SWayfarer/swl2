package ru.swayfarer.swl2.jfx.scene.controls;

import java.io.InputStream;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ru.swayfarer.swl2.jfx.helpers.KeyboardEventsHelper;
import ru.swayfarer.swl2.jfx.helpers.MouseEventsHelper;
import ru.swayfarer.swl2.jfx.helpers.ScaleEventsHelper;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.resource.rlink.RLUtils;
import ru.swayfarer.swl2.resource.rlink.ResourceLink;
import ru.swayfarer.swl2.string.StringUtils;

@SuppressWarnings("unchecked")
public class JfxButton extends Button implements IJavafxWidget {

	public static ILogger logger = LoggingManager.getLogger();
	
	public KeyboardEventsHelper eventsKeyboard = new KeyboardEventsHelper().init(this);
	public MouseEventsHelper eventsMouse = new MouseEventsHelper().init(this);
	public ScaleEventsHelper eventsScale = new ScaleEventsHelper().init(this);
	
	public JfxButton()
	{
		super();
	}

	public JfxButton(String text, Node graphic)
	{
		super(text, graphic);
	}

	public JfxButton(String text)
	{
		super(text);
	}
	
	public <T extends JfxButton> T setIcon(int width, int height, @ConcattedString Object... rlink) 
	{
		return (T) setIcon(width, height, RLUtils.createLink(StringUtils.concat(rlink)));
	}
	
	public <T extends JfxButton> T setIcon(int width, int height, ResourceLink rlink) 
	{
		return setIcon(width, height, rlink.toStream());
	}
	
	public <T extends JfxButton> T setIcon(int width, int height, InputStream is) 
	{
		logger.safe(() -> {
			Image image = new Image(is, width, height, false, true);
			setIcon(image);
		}, "Error while setting image to", this);
		return (T) this;
	}
	
	public <T extends JfxButton> T setIcon(Image image) 
	{
		setGraphic(new ImageView(image));
		return (T) this;
	}
}
