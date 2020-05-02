package ru.swayfarer.swl2.jvm;

import javafx.application.Application;
import javafx.scene.control.TableView;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.threads.ThreadsUtils;

public class JavaUtils {

	public static ILogger logger = LoggingManager.getLogger();
	
	public static int javaVersion = -1;
	
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
