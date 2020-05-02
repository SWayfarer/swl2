package ru.swayfarer.swl2.jfx.scene.controls;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import ru.swayfarer.swl2.jfx.tags.window.JfxWindow;
import ru.swayfarer.swl2.jfx.utils.JfxUtils;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.resource.rlink.RLUtils;
import ru.swayfarer.swl2.resource.rlink.ResourceLink;
import ru.swayfarer.swl2.string.StringUtils;

@SuppressWarnings("unchecked")
public interface IJavafxWidget {

	public default void setTextColor(Color color)
	{
		JfxUtils.setTextColor(self(), color);
	}
	
	public default void setTextColor(int r, int g, int b)
	{
		JfxUtils.setTextColor(self(), Color.rgb(r, g, b));
	}
	
	public default void setTextColor(float r, float g, float b)
	{
		JfxUtils.setTextColor(self(), new Color(r, g, b, 1));
	}
	
	public default void setBackgroundColor(Color color)
	{
		Object obj = self();
		
		if (obj instanceof Region)
		{
			Region rg = self();
			
			rg.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
		}
		else
			JfxUtils.setBackgroundColor(self(), color);
	}
	
	public default void setBackgroundColor(int r, int g, int b)
	{
		JfxUtils.setBackgroundColor(self(), Color.rgb(r, g, b));
	}
	
	public default void setBackgroundColor(float r, float g, float b)
	{
		JfxUtils.setBackgroundColor(self(), new Color(r, g, b, 1));
	}
	
	public default void fitToParent()
	{
		Node self = self();
		Node parent = self.getParent();
		
		if (parent != null)
		{
			if (this instanceof Region)
			{
				if (parent instanceof Region)
					JfxUtils.fitTo((Region) parent, (Region) this);
			}
			else if (this instanceof ImageView)
			{
				if (parent instanceof Region)
					JfxUtils.fitTo((Region) parent, (ImageView) this);
			}
		}
	}
	
	public default void setVerticalSizePolicy(Priority priority)
	{
		VBox.setVgrow(self(), priority);
	}
	
	public default void setSizePolicy(Priority priority)
	{
		setHorizontalSizePolicy(priority);
		setVerticalSizePolicy(priority);
	}
	
	public default void setHorizontalSizePolicy(Priority priority)
	{
		HBox.setHgrow(self(), priority);
	}
	
	public default <T extends Node> T self()
	{
		return (T) this;
	}
	
	public default void addItems(Node... nodes)
	{
		for (Node node : nodes)
		{
			addItem(node);
		}
	}
	
	public default void addItem(Node node)
	{
		Pane pane = self();
		pane.getChildren().add(node);
	}
	
	public default void setMarigins(int left, int top, int right, int down)
	{
		Node node = self();
		Node parent = node.getParent();
		
		Insets insets = new Insets(top, right, down, left);
		
		if (parent != null)
		{
			if (parent instanceof HBox)
			{
				HBox.setMargin(node, insets);
			}
			else
			{
				VBox.setMargin(node, insets);
			}
		}
		
		VBox.setMargin(node, insets);
	}
	
	public default <T extends IJavafxWidget> T setBackgroundImage(Image img, boolean isAutoscale) 
	{
		if (this instanceof Region)
		{
			Region self = self();

			if (isAutoscale)
			{
				if (self.getWidth() > 0 && self.getHeight() > 0)
					setBackgroundImageTo(self, JfxUtils.resizeImage(img, (int) self.getWidth(), (int) self.getHeight()));
				
				self.widthProperty().addListener((item, oldValue, newValue) -> {
					if (self.getHeight() > 0 && newValue.intValue() > 0)
						setBackgroundImageTo(self, JfxUtils.resizeImage(img, newValue.intValue(), (int) self.getHeight()));
				});
				
				self.heightProperty().addListener((item, oldValue, newValue) -> {
					if (self.getWidth() > 0 && newValue.intValue() > 0)
						setBackgroundImageTo(self, JfxUtils.resizeImage(img, (int) self.getWidth(), newValue.intValue()));
				});
			}
			
			setBackgroundImageTo(self, img);
		}
		
		return (T) this;
	}
	
	public static void setBackgroundImageTo(Region rg, Image image)
	{	
		BackgroundImage myBI= new BackgroundImage(image,
		        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
		          BackgroundSize.DEFAULT);
		
		rg.setBackground(new Background(myBI));
	}
	
	/** Задать изображение */
	public default <T extends IJavafxWidget> T setBackgroundImage(@ConcattedString Object... rlink) 
	{
		return setBackgroundImage(RLUtils.createLink(StringUtils.concat(rlink)));
	}
	
	/** Задать изображение */
	public default <T extends IJavafxWidget> T setBackgroundImage(ResourceLink rlink) 
	{
		ILogger logger = LoggingManager.getLogger(getClass().getSimpleName());
		logger.safe(() -> {
			setBackgroundImage(new Image(rlink.toStream()));
		}, "Error while setting image from rlink", rlink, "to", this);
		
		return (T) this;
	}
	
	public default JfxWindow toWindow()
	{
		return new JfxWindow(self());
	}
}
