package ru.swayfarer.swl2.jfx.utils;

import java.util.concurrent.atomic.AtomicBoolean;

import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction0NoR;
import ru.swayfarer.swl2.markers.InternalElement;

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
		AtomicBoolean waitCondition = new AtomicBoolean(wait);
		
		Runnable runWait = () -> {
			
			run.apply();
			waitCondition.set(false);
		};
		
		Platform.runLater(runWait);
		
		// Ждем окончания работы 
		while (waitCondition.get()) {}
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
		
		@Override
		public void start(Stage arg0) throws Exception
		{
			run.apply();
		}
		
	}
}
