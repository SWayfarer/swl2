package ru.swayfarer.swl2.jvm;

import javafx.application.Application;
import javafx.scene.control.TableView;
import ru.swayfarer.swl2.threads.ThreadsUtils;

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
	
	/** Запустить GarbadgeCollector в отдельном потоке */
	public static Thread runMemoryCleaner(long cleanDelay)
	{
		return ThreadsUtils.newThread(() -> {
			
			for (;;)
			{
				Runtime.getRuntime().gc();
				ThreadsUtils.sleepSafe(cleanDelay);
				System.out.println("Clened memory!");
			}
			
		}, true);
	}
}
