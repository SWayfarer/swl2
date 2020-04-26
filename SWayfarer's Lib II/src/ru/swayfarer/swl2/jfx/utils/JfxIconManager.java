package ru.swayfarer.swl2.jfx.utils;

import java.util.Map;

import javafx.scene.image.Image;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.resource.rlink.RLUtils;
import ru.swayfarer.swl2.string.StringUtils;

public class JfxIconManager {

	public static JfxIconManager instance = new JfxIconManager();
	
	public static ILogger logger = LoggingManager.getLogger();
	
	public Map<String, Image> cachedIcons = CollectionsSWL.createWeakMap();
	
	public synchronized Image getIcon(int width, int height, @ConcattedString Object... text)
	{
		String str = StringUtils.concat(text) + "_" + width + "_" + height;
		
		Image cached = cachedIcons.get(str);
		
		if (cached != null)
			return cached;
		
		return logger.safeReturn(() -> {
			Image image = new Image(RLUtils.toStream(text), width, height, false, true);
			cachedIcons.put(str, image);
			return image;
		}, null, "");
	}	
}
