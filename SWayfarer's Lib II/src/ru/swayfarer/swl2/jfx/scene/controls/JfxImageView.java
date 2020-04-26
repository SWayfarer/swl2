package ru.swayfarer.swl2.jfx.scene.controls;

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
public class JfxImageView extends ImageView implements IJavafxWidget {

	public static ILogger logger = LoggingManager.getLogger();
	
	public KeyboardEventsHelper eventsKeyboard = new KeyboardEventsHelper().init(this);
	public MouseEventsHelper eventsMouse = new MouseEventsHelper().init(this);
	public ScaleEventsHelper eventsScale = new ScaleEventsHelper().init(this);
	
	public JfxImageView()
	{
		super();
	}

	public JfxImageView(Image image)
	{
		super(image);
	}

	public JfxImageView(String url)
	{
		super(url);
	}
	
	/** Задать изображение */
	public <T extends JfxImageView> T setImage(@ConcattedString Object... rlink) 
	{
		return setImage(RLUtils.createLink(StringUtils.concat(rlink)));
	}
	
	/** Задать изображение */
	public <T extends JfxImageView> T setImage(ResourceLink rlink) 
	{
		logger.safe(() -> {
			setImage(new Image(rlink.toStream()));
		}, "Error while setting image from rlink", rlink, "to", this);
		
		return (T) this;
	}

}
