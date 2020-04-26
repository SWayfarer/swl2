package ru.swayfarer.swl2.jfx.helpers;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import ru.swayfarer.swl2.jfx.utils.JfxUtils;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.Observables;

@SuppressWarnings("unchecked")
public class ScaleEventsHelper {

	public IObservable<ResizeEvent> resize = Observables.createObservable();
	
	public <T extends ScaleEventsHelper> T init(ImageView rg) 
	{
		return init(JfxUtils.getRegionOf(rg));
	}
	
	public <T extends ScaleEventsHelper> T init(Stage rg) 
	{
		return init(JfxUtils.getRegionOf(rg));
	}
	
	public <T extends ScaleEventsHelper> T init(Region rg) 
	{
		rg.widthProperty().addListener((item, oldValue, newValue) -> {
			if (oldValue.doubleValue() != newValue.doubleValue())
			{
				resize.next(new ResizeEvent(newValue.doubleValue(), rg.getHeight()));
			}
		});
		
		rg.heightProperty().addListener((item, oldValue, newValue) -> {
			if (oldValue.doubleValue() != newValue.doubleValue())
				resize.next(new ResizeEvent(rg.getWidth(), newValue.doubleValue()));
		});
		
		return (T) this;
	}
	
	public <T extends ScaleEventsHelper> T clear() 
	{
		resize.clear();
		return (T) this;
	}
	
	public static class ResizeEvent {
		
		public double width, height;

		public ResizeEvent(double width, double height)
		{
			super();
			this.width = width;
			this.height = height;
		}
	}
	
}
