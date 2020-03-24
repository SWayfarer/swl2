package ru.swayfarer.swl2.jvm;

import javafx.application.Application;
import javafx.scene.control.TableView;

public class JavaUtils {

	/** Есть ли в Jvm Javafx? */
	public static boolean isJfxPresent()
	{
		try
		{
			Class<?> checkedClass = Application.class;
			checkedClass = TableView.class;
			
			return true;
		}
		catch (Throwable e)
		{
			
		}
		
		return false;
	}
	
	/** Получить путь до Java */
	public static String getJavaPath()
	{
		return System.getProperty("java.home");
	}
	
}
