package ru.swayfarer.swl2.jfx.utils;
import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.observable.Observables;
import ru.swayfarer.swl2.observable.property.ObservableProperty;
import ru.swayfarer.swl2.resource.rlink.RLUtils;

public class JfxIconFactory {

	public static ObservableProperty<Color> titleIconsColor = Observables.createProperty(Color.WHITE);
	public static ObservableProperty<Color> titleIconsHighlightedColor = Observables.createProperty(Color.WHEAT);
	
	public static ILogger logger = LoggingManager.getLogger();
	
	public static JfxIconFactory instance = new JfxIconFactory();
	
	public Color currentColor;
	
	public ObservableProperty<Image> closeIconImage = Observables.createProperty();
	public ObservableProperty<Image> closeIconImageHighlighted = Observables.createProperty();
	
	public ObservableProperty<Image> hideIconImage = Observables.createProperty();
	public ObservableProperty<Image> hideIconImageHighlighted = Observables.createProperty();
	
	public ObservableProperty<Image> maximizeIconImage = Observables.createProperty();
	public ObservableProperty<Image> maximizeIconImageHighlighted = Observables.createProperty();
	
	public ObservableProperty<Image> unMaximizeIconImage = Observables.createProperty();
	public ObservableProperty<Image> unMaximizeIconImageHighlighted = Observables.createProperty();
	
	public JfxIconFactory()
	{
		updateImages();
	}
	
	public static int getIntFromColor(int red, int green, int blue){
	    red = (red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
	    green = (green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
	    blue = blue & 0x000000FF; //Mask out anything not blue.

	    return 0xFF000000 | red | green | blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
	}
	
	public Image createIcon(String templateRlink)
	{
		templateRlink = "/assets/swl2/icons/templates/" + templateRlink + ".png";
		
		BufferedImage image = RLUtils.createImage(templateRlink);
		
		int newColor = getIntFromColor
		(
			(int) (currentColor.getRed() * 255),
			(int) (currentColor.getGreen() * 255),
			(int) (currentColor.getBlue() * 255)
		);
		
		for (int x = 0; x < image.getWidth(); x ++)
		{
			for (int y = 0; y < image.getHeight(); y ++)
			{
				int color = image.getRGB(x, y);
				
				boolean isNonTransient = color != 16777215;
				
				image.setRGB(x, y, isNonTransient ? newColor : 100663295);
			}
		}
		
		return SwingFXUtils.toFXImage(image, null);
	}
	
	public void updateImages()
	{
		logger.operation(() -> {
			
			currentColor = titleIconsColor.get();
			
			closeIconImage.setValue(createIcon("close"));
			hideIconImage.setValue(createIcon("hide"));
			maximizeIconImage.setValue(createIcon("maximize"));
			unMaximizeIconImage.setValue(createIcon("maximize"));
			
			currentColor = titleIconsHighlightedColor.get();
			
			closeIconImageHighlighted.setValue(createIcon("close"));
			hideIconImageHighlighted.setValue(createIcon("hide"));
			maximizeIconImageHighlighted.setValue(createIcon("maximize"));
			unMaximizeIconImageHighlighted.setValue(createIcon("maximize"));
			
		}, "Updating icon images");
	}
	
}
