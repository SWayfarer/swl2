package ru.swayfarer.swl2.jvm;

import javafx.application.Application;
import javafx.scene.control.TableView;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.threads.ThreadsUtils;

/**
 * Утилиты для работы с Java (и jvm)
 * @author swayfarer
 *
 */
public class JavaUtils {

	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Прочитанная версия Java. Если -1, то будет прочитана при первом вызове {@link #getJavaVersion()} */
	@InternalElement
	public static int javaVersion = -1;
	
	/** Есть ли в Jvm Javafx? */
	public static boolean isJfxPresent()
	{
		try
		{
			Class.forName("javafx.application.Application");
			return true;
		}
		catch (Throwable e)
		{
			
		}
		
		return false;
	}
	
	/** 
	 * Получить версию Java
	 * @return Целое число, обозначающее версию. Для 7 - 7, для 14 - 14 и т.п.
	 */
	public static int getJavaVersion()
	{
		if (javaVersion == -1)
		{
			String javaVer = System.getProperty("java.runtime.version");
			
			if (!StringUtils.isEmpty(javaVer))
			{
				if (javaVer.startsWith("1.") && javaVer.length() > 2)
				{
					String versionNum = String.valueOf(javaVer.charAt(2));
					
					if (StringUtils.isInteger(versionNum))
					{
						javaVersion = Integer.valueOf(versionNum);
					}
				}
				else if (javaVer.contains("+"))
				{
					String versionNum = StringUtils.subStringByLast("+", javaVer);
					
					if (StringUtils.isInteger(versionNum))
					{
						javaVersion = Integer.valueOf(versionNum);
					}
				}
			}
		}
		return javaVersion;
	}
	
	/** Получить путь до Java */
	public static String getJavaPath()
	{
		return System.getProperty("java.home");
	}
	
	/** Запустить GarbadgeCollector в отдельном потоке */
	public static Thread runMemoryCleaner(long cleanDelay)
	{
		return runMemoryCleaner(cleanDelay, false);
	}
	
	/** Запустить GarbadgeCollector в отдельном потоке */
	public static Thread runMemoryCleaner(long cleanDelay, boolean isSilent)
	{
		return ThreadsUtils.newThread(() -> {
			
			for (;;)
			{
				long oldFree = Runtime.getRuntime().freeMemory();
				Runtime.getRuntime().gc();
				long free = Runtime.getRuntime().freeMemory();
				
				if (!isSilent)
					logger.info("[MemoryCleaner] Cleaned", (free - oldFree) / 1024L, "kbs. Now free", free, "kbs");
				
				ThreadsUtils.sleepSafe(cleanDelay);
			}
			
		}, true);
	}
}
