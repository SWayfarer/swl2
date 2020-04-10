package ru.swayfarer.swl2.jvm;

import javafx.application.Application;
import javafx.scene.control.TableView;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.threads.ThreadsUtils;

public class JavaUtils {

	public static ILogger logger = LoggingManager.getLogger();
	
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
				long oldFree = Runtime.getRuntime().freeMemory();
				Runtime.getRuntime().gc();
				long free = Runtime.getRuntime().freeMemory();
				
				logger.info("[MemoryCleaner] Cleaned", (free - oldFree) / 1024L, "kbs. Now free", free, "kbs");
				
				ThreadsUtils.sleepSafe(cleanDelay);
			}
			
		}, true);
	}
}
