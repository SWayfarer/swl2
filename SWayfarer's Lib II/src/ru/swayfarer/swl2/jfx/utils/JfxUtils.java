package ru.swayfarer.swl2.jfx.utils;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

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
}
