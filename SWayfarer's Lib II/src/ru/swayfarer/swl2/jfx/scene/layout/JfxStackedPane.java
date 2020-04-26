package ru.swayfarer.swl2.jfx.scene.layout;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ru.swayfarer.swl2.jfx.helpers.KeyboardEventsHelper;
import ru.swayfarer.swl2.jfx.helpers.MouseEventsHelper;
import ru.swayfarer.swl2.jfx.helpers.ScaleEventsHelper;
import ru.swayfarer.swl2.jfx.scene.controls.IJavafxWidget;
import ru.swayfarer.swl2.jfx.utils.JfxUtils;

@SuppressWarnings("unchecked")
public class JfxStackedPane extends ScrollPane implements IJavafxWidget {

	public KeyboardEventsHelper eventsKeyboard = new KeyboardEventsHelper().init(this);
	public MouseEventsHelper eventsMouse = new MouseEventsHelper().init(this);
	public ScaleEventsHelper eventsScale = new ScaleEventsHelper().init(this);
	
	public Pane box = new VBox();
	public float spacing;
	
	public JfxStackedPane()
	{
		setPrefSize(400, 400);
		setFitToWidth(true);
		setFitToHeight(true);
		setContent(box);
	}
	
	public <T extends JfxStackedPane> T clear() 
	{
		box.getChildren().clear();
		return (T) this;
	}
	
	public <T extends JfxStackedPane> T add(Node item) 
	{
		box.getChildren().add(item);
		return (T) this;
	}
	
	public <T extends JfxStackedPane> T setSpacing(float spacing) 
	{
		this.spacing = spacing;
		return (T) this;
	}
	
	public <T extends JfxStackedPane> T setAlligment(AlligmentType type) 
	{
		switch (type)
		{
			case HORIZONTAL:
			{
				HBox box = new HBox();
				box.setSpacing(spacing);
				box.getChildren().setAll(this.box.getChildren());
				this.box = box;
				
				break;
			}
			case VERTICAL:
			{
				VBox box = new VBox();
				box.setSpacing(spacing);
				box.getChildren().setAll(this.box.getChildren());
				this.box = box;
				break;
			}
			default:
				break;
		}
		
		setContent(box);
		
		return (T) this;
	}
	
	public void setContent(Pane pane)
	{
		JfxUtils.fitTo(this, pane);
		setContent((Node) pane);
	}
	
	public static enum AlligmentType {
		VERTICAL,
		HORIZONTAL
	}
	
	@Override
	public <T extends Node> T self()
	{
		return (T) box;
	}
	
}
