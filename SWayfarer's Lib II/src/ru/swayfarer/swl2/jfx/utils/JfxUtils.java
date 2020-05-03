package ru.swayfarer.swl2.jfx.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction0NoR;
import ru.swayfarer.swl2.jfx.scene.layout.JfxRegion;
import ru.swayfarer.swl2.jfx.tags.JfxTags;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.observable.property.ObservableProperty;
import ru.swayfarer.swl2.string.DynamicString;
import ru.swayfarer.swl2.tasks.RecursiveSafeTask;
import ru.swayfarer.swl2.threads.lock.SynchronizeLock;

/**
 * Утилиты для работы с Jfx 
 * @author swayfarer
 *
 */
public class JfxUtils {

	/** Получить разрешение окна */
	public static Rectangle2D getScreenResolution()
	{
		return Screen.getPrimary().getVisualBounds();
	}
	
	/** Инициализация Jfx */
	public static void initJfx(IFunction0NoR run)
	{
		DummyJfxApp.run = run;
		DummyJfxApp.launch(DummyJfxApp.class);
	}
	
	/** Запустить в Jfx-потоке */
	public static void inJfxThread(IFunction0NoR run)
	{
		inJfxThread(run, false);
	}
	
	/** Запустить в Jfx-потоке */
	public static void inJfxThread(IFunction0NoR run, boolean wait)
	{
		if (!Platform.isFxApplicationThread())
		{
			SynchronizeLock lock = new SynchronizeLock();
			
			Runnable runWait = () -> {
				
				run.apply();
				lock.notifyLockAll();
			};
			
			Platform.runLater(runWait);
			
			lock.waitFor();
		}
		else
		{
			run.apply();
		}
	}
	
	public static void setTextColor(Parent node, Color color)
	{
		setColorTag(JfxTags.TEXT_COLOR, node, color);
	}
	
	public static void showNodeApp(Parent node)
	{
		JfxUtils.initJfx(() -> {
			showNode(node);
		});
	}
	
	public static void showNode(Parent node)
	{
		Stage stage = new Stage();
		Scene scene = new Scene(node);
		stage.setScene(scene);
		stage.showAndWait();
	}
	
	public static void setBackgroundColor(Node node, Color color)
	{
		setColorTag(JfxTags.BACKGROUND_COLOR, node, color);
	}
	
	public static <T> void attachProperties(ObservableProperty<T> prop1, ObjectProperty<T> prop2)
	{
		RecursiveSafeTask recursiveSafeTask = new RecursiveSafeTask();
		
		prop1.eventChange.subscribe((event) -> {
			recursiveSafeTask.start(() -> {
				prop2.set(event.getNewValue());
			});
		});
		
		prop2.addListener((item, oldValue, newValue) -> {
			recursiveSafeTask.start(() -> {
				prop1.setValue(newValue);
			});
		});
	}
	
	public static void attachProperties(ObservableProperty<String> prop1, StringProperty prop2)
	{
		RecursiveSafeTask recursiveSafeTask = new RecursiveSafeTask();
		
		prop1.eventChange.subscribe((event) -> {
			recursiveSafeTask.start(() -> {
				prop2.set(event.getNewValue());
			});
		});
		
		prop2.addListener((item, oldValue, newValue) -> {
			recursiveSafeTask.start(() -> {
				prop1.setValue(newValue);
			});
		});
	}
	
	public static void attachProperties(ObservableProperty<Boolean> prop1, BooleanProperty prop2)
	{
		RecursiveSafeTask recursiveSafeTask = new RecursiveSafeTask();
		
		prop1.eventChange.subscribe((event) -> {
			recursiveSafeTask.start(() -> {
				prop2.set(event.getNewValue());
			});
		});
		
		prop2.addListener((item, oldValue, newValue) -> {
			recursiveSafeTask.start(() -> {
				prop1.setValue(newValue);
			});
		});
	}
	
	public static void attachProperties(ObservableProperty<Long> prop1, LongProperty prop2)
	{
		RecursiveSafeTask recursiveSafeTask = new RecursiveSafeTask();
		
		prop1.eventChange.subscribe((event) -> {
			recursiveSafeTask.start(() -> {
				prop2.set(event.getNewValue());
			});
		});
		
		prop2.addListener((item, oldValue, newValue) -> {
			recursiveSafeTask.start(() -> {
				prop1.setValue(newValue.longValue());
			});
		});
	}
	
	public static void attachProperties(ObservableProperty<Integer> prop1, IntegerProperty prop2)
	{
		RecursiveSafeTask recursiveSafeTask = new RecursiveSafeTask();
		
		prop1.eventChange.subscribe((event) -> {
			recursiveSafeTask.start(() -> {
				prop2.set(event.getNewValue());
			});
		});
		
		prop2.addListener((item, oldValue, newValue) -> {
			recursiveSafeTask.start(() -> {
				prop1.setValue(newValue.intValue());
			});
		});
	}
	
	public static void attachProperties(ObservableProperty<Float> prop1, FloatProperty prop2)
	{
		RecursiveSafeTask recursiveSafeTask = new RecursiveSafeTask();
		
		prop1.eventChange.subscribe((event) -> {
			recursiveSafeTask.start(() -> {
				prop2.set(event.getNewValue());
			});
		});
		
		prop2.addListener((item, oldValue, newValue) -> {
			recursiveSafeTask.start(() -> {
				prop1.setValue(newValue.floatValue());
			});
		});
	}
	
	public static void attachProperties(ObservableProperty<Double> prop1, DoubleProperty prop2)
	{
		RecursiveSafeTask recursiveSafeTask = new RecursiveSafeTask();
		
		prop1.eventChange.subscribe((event) -> {
			recursiveSafeTask.start(() -> {
				prop2.set(event.getNewValue());
			});
		});
		
		prop2.addListener((item, oldValue, newValue) -> {
			recursiveSafeTask.start(() -> {
				prop1.setValue(newValue.doubleValue());
			});
		});
	}
	
	public static void setColorTag(String tagName, Node node, Color color)
	{
		setColorTag(tagName, node, (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
	}
	
	public static Image resizeImage(Image image, int width, int height)
	{
		BufferedImage img = SwingFXUtils.fromFXImage(image, null);
		
		java.awt.Image tmp = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return SwingFXUtils.toFXImage(dimg, null);
	}
	
	public static void setTag(String tagName, Node node, String value)
	{
		node.setStyle(replaceTagValue(tagName, value, node.getStyle()));
	}
	
	public static void setColorTag(String tagName, Node node, int red, int green, int blue)
	{
		String style = replaceTagValue(tagName, "rgb(" + red + ", " + green + ", " + blue + ")", node.getStyle());
		node.setStyle(style);
	}
	
	public static Region getRegionOf(Stage stage)
	{	
		JfxRegion ret = new JfxRegion();
		
		RecursiveSafeTask task = new RecursiveSafeTask();
		
		ret.eventsScale.resize.subscribe((event) -> {
			task.start(() -> {
				stage.setWidth(event.width);
				stage.setHeight(event.height);
			});
		});
		
		stage.widthProperty().addListener((item, oldValue, newValue) -> {
			task.start(() -> {
				ret.setWidth(newValue.doubleValue());
			});
		});
		
		stage.heightProperty().addListener((item, oldValue, newValue) -> {
			task.start(() -> {
				ret.setHeight(newValue.doubleValue());
			});
		});
		
		return ret;
	}
	
	public static Region getRegionOf(ImageView img)
	{	
		JfxRegion ret = new JfxRegion();
		
		RecursiveSafeTask task = new RecursiveSafeTask();
		
		ret.eventsScale.resize.subscribe((event) -> {
			task.start(() -> {
				img.resize(event.width, event.height);
			});
		});
		
		img.fitWidthProperty().addListener((item, oldValue, newValue) -> {
			task.start(() -> {
				ret.setWidth(newValue.doubleValue());
			});
		});
		
		img.fitHeightProperty().addListener((item, oldValue, newValue) -> {
			task.start(() -> {
				ret.setHeight(newValue.doubleValue());
			});
		});
		
		return ret;
	}
	
	public static String replaceTagValue(String name, Object value, String cssString)
	{
		String start = name + ":";
		DynamicString newString = new DynamicString();
		
		for (String tag : cssString.split(";"))
		{
			if (!tag.startsWith(start))
			{
				newString.append(tag);
				newString.append(";");
			}
		}
		
		newString.append(start + value + ";");
		
		return newString.toString();
	}
	
	/**
	 * Пустое приложения Javafx для метода {@link JfxUtils#initJfx()}
	 * @author swayfarer
	 *
	 */
	@InternalElement
	public static class DummyJfxApp extends javafx.application.Application {

		/** То, что будет выполнено */
		public static volatile IFunction0NoR run;
		
		/** Запущено ли сейчас приложение? */
		public static AtomicBoolean isInited = new AtomicBoolean(false);
		
		@Override
		public void start(Stage arg0) throws Exception
		{
			run.apply();
		}
		
	}
	
	/**
	 * Забиндить размеры окон
	 * @param imgToFit Окно, которое будет изменять размер всед за другим
	 * @param paneFromFit Окно, которое будет образцом для второго
	 */
	public static void fitTo(Region paneFromFit, ImageView imgToFit)
	{
		imgToFit.setFitWidth(paneFromFit.widthProperty().doubleValue());
		imgToFit.setFitHeight(paneFromFit.heightProperty().doubleValue());
		
		paneFromFit.widthProperty().addListener(new ChangeListener<Number>()
		{
			@Override public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2)
			{
				imgToFit.setFitWidth((Double) arg0.getValue());
			}
		});
		
		paneFromFit.heightProperty().addListener(new ChangeListener<Number>()
		{
			@Override public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2)
			{
				imgToFit.setFitHeight((Double) arg0.getValue());
			}
		});
	}
	
	/**
	 * Забиндить размеры окон
	 * @param paneToFit Окно, которое будет изменять размер всед за другим
	 * @param paneFromFit Окно, которое будет образцом для второго
	 */
	public static void fitTo(TabPane paneFromFit, Region paneToFit)
	{
		paneToFit.minWidthProperty().bind(paneFromFit.widthProperty());
		paneToFit.maxWidthProperty().bind(paneFromFit.widthProperty());
		paneToFit.prefWidthProperty().bind(paneFromFit.widthProperty());
		
		DoubleProperty prop2 = paneToFit.prefHeightProperty();
		paneFromFit.heightProperty().addListener((ChangeListener<Number>) (var1, var2, var3) -> prop2.set(var1.getValue().doubleValue() - paneFromFit.tabMaxHeightProperty().doubleValue() - 8));
		
		DoubleProperty prop = paneToFit.minHeightProperty();
		paneFromFit.heightProperty().addListener((ChangeListener<Number>) (var1, var2, var3) -> prop.set(var1.getValue().doubleValue() - paneFromFit.tabMaxHeightProperty().doubleValue() - 8));
		
		DoubleProperty prop1 = paneToFit.maxHeightProperty();
		paneFromFit.heightProperty().addListener((ChangeListener<Number>) (var1, var2, var3) -> prop1.set(var1.getValue().doubleValue() - paneFromFit.tabMaxHeightProperty().doubleValue() - 8));
	}
	
	/**
	 * Забиндить размеры окон
	 * @param paneToFit Окно, которое будет изменять размер всед за другим
	 * @param paneFromFit Окно, которое будет образцом для второго
	 */
	public static void fitTo(TabPane paneFromFit, ImageView paneToFit)
	{
		paneToFit.fitWidthProperty().bind(paneFromFit.widthProperty());
		
		paneFromFit.heightProperty().addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> var1, Number var2, Number var3)
			{
				paneToFit.setFitHeight(var1.getValue().doubleValue() - paneFromFit.tabMaxHeightProperty().doubleValue() - 8);
			}
		});
	}
	
	/**
	 * Забиндить размеры окон
	 * @param paneToFit Окно, которое будет изменять размер всед за другим
	 * @param paneFromFit Окно, которое будет образцом для второго
	 */
	public static void fitTo(Region paneFromFit, Rectangle paneToFit)
	{
		paneToFit.widthProperty().bind(paneFromFit.widthProperty());
		paneToFit.heightProperty().bind(paneFromFit.heightProperty());
	}
	
	/**
	 * Забиндить размеры окон
	 * @param paneToFit Окно, которое будет изменять размер всед за другим
	 * @param paneFromFit Окно, которое будет образцом для второго
	 */
	public static void fitTo(Region paneFromFit, Region paneToFit)
	{
		paneToFit.prefWidthProperty().bind(paneFromFit.widthProperty());
		paneToFit.prefHeightProperty().bind(paneFromFit.heightProperty());
	}
}
