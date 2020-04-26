package ru.swayfarer.swl2.jfx.scene.layout;

import java.util.concurrent.atomic.AtomicInteger;

import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import ru.swayfarer.swl2.jfx.helpers.KeyboardEventsHelper;
import ru.swayfarer.swl2.jfx.helpers.MouseEventsHelper;
import ru.swayfarer.swl2.jfx.helpers.ScaleEventsHelper;
import ru.swayfarer.swl2.jfx.scene.controls.IJavafxWidget;
import ru.swayfarer.swl2.jfx.utils.JfxUtils;

@SuppressWarnings("unchecked")
public class JfxSplitPane extends SplitPane implements IJavafxWidget {
	
	public KeyboardEventsHelper eventsKeyboard = new KeyboardEventsHelper().init(this);
	public MouseEventsHelper eventsMouse = new MouseEventsHelper().init(this);
	public ScaleEventsHelper eventsScale = new ScaleEventsHelper().init(this);
	
	public JfxSplitPane()
	{
		super();
	}
	
	public JfxSplitPane(Node... items)
	{
		super(items);
	}
	
	public <T extends JfxSplitPane> T setDividerPosition(double pos, boolean isFixed) 
	{
		JfxUtils.inJfxThread(() -> {
			setDividerPositions(pos);
			
			if (isFixed)
				fixDivider();
			
		});
		
		return (T) this;
	}
	
	public <T extends JfxSplitPane> T fixDivider() 
	{
		return fixDivider(0);
	}
	
	public <T extends JfxSplitPane> T fixDivider(int id) 
	{
		if (getDividers().size() <= id)
			return (T) this;
		
		ObservableList<Divider> dividers = getDividers();
		Divider divider = dividers.get(id);
		
		AtomicInteger size = new AtomicInteger();
		
		Orientation orientation = getOrientation();
		
		double width = divider.getPosition() * getWidth();
		double height = divider.getPosition() * getHeight();
		
		DoubleProperty dividesPosProp = divider.positionProperty();
		
		dividesPosProp.addListener((property, oldValue, newValue) -> {
			if (orientation == Orientation.HORIZONTAL)
				size.set((int) (newValue.doubleValue() * getWidth()));
			else
				size.set((int) (newValue.doubleValue() * getHeight()));
		});
		
		if (orientation == Orientation.HORIZONTAL)
			size.set((int) width);
		else
			size.set((int) height);
		
		eventsScale.resize.subscribe((event) -> {
			double sizeValue = (double) size.get();
			boolean isHorizontal = orientation == Orientation.HORIZONTAL;
			dividesPosProp.set(sizeValue / (isHorizontal ? event.width : event.height));
		});
		
		return (T) this;
	}
}
