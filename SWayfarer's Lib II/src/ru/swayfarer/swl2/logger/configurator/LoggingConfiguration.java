package ru.swayfarer.swl2.logger.configurator;

import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.string.property.SystemProperty;

public class LoggingConfiguration {

	public static ILogger logger = LoggingManager.getLogger();
	
	public static SystemProperty loggingLocation = new SystemProperty(
			"swl2.logging.config",
			"/assets/config/logging.swconf"
	);
	
	public static SystemProperty isAutoEnable = new SystemProperty(
			"swl2.logging.autoConfig",
			true
	);
	
	public static boolean loadDefaultConfigToRoot()
	{
		return loadDefaultConfigToRoot(true);
	}
	
	public static boolean loadDefaultConfigToRoot(boolean isRedirecting)
	{
		if (loadDefaultConfig(LoggingManager.getRootLogger()))
		{
			if (isRedirecting)
			{
				LoggingManager.setAllToRootRedirecting();
				LoggingManager.onInited();
			}
			
			return true;
		}	
		
		return false;
	}
	
	public static boolean loadDefaultConfig(ILogger ilogger)
	{
		if (isAutoEnable.getBooleanValue())
		{
			String customConfigPath = loggingLocation.getValue();
			
			SwconfLoggerConfiguration configuration = SwconfLoggerConfiguration.of(customConfigPath);
			
			if (configuration.configInfo.isReadedSuccessfully())
			{
				configuration.applyToLogger(ilogger);
				return true;
			}
			else
			{
				logger.warning("Can't read logging configuration at", customConfigPath);
				return false;
			}
		}
		
		return false;
	}
}
