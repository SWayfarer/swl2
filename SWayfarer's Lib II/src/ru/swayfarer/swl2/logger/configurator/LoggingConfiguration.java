package ru.swayfarer.swl2.logger.configurator;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.resource.rlink.RLUtils;
import ru.swayfarer.swl2.resource.rlink.ResourceLink;
import ru.swayfarer.swl2.string.property.SystemProperty;

/**
 * Конфигуратор логгеров v2
 * @author swayfarer
 *
 */
public class LoggingConfiguration {

	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	public static IExtendedList<String> loggingConfigurationsExt = CollectionsSWL.createExtendedList(
			"lua",
			"swconf",
			"json",
			"properties"
	);
	
	/** Системная проперти, отвечающая за расположение(в формате {@link ResourceLink}) файла конфигурации логгера */
	public static SystemProperty loggingLocation = new SystemProperty(
			"swl2.logging.config",
			"/assets/config/loggingswl.*"
	);
	
	/** Системная проперти, отвечающая за включение конфигурации логгера */
	public static SystemProperty isAutoEnable = new SystemProperty(
			"swl2.logging.autoConfig",
			true
	);
	
	/** Загрузить конфигурацию по стандартному расположению (или по расположению из {@link #loggingLocation}) */
	public static boolean loadDefaultConfigToRoot()
	{
		return loadDefaultConfigToRoot(true);
	}
	
	/**
	 * Загрузить конфигурацию по стандартному расположению (или по расположению из {@link #loggingLocation})
	 * @param isRedirecting Редиректить ли все логгеры на рутовый? 
	 * @return Успешна ли загрузка?
	 */
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
	
	/**
	 * Загрузить конфигурацию по стандартному расположению (или по расположению из {@link #loggingLocation})
	 * @param ilogger Логгер, на который применяется конфигурация 
	 * @return Успешна ли загрузка?
	 */
	public static boolean loadDefaultConfig(ILogger ilogger)
	{
		if (isAutoEnable.getBooleanValue())
		{
			String customConfigPath = loggingLocation.getValue();
			boolean isConfigurationFound = false;
			
			for (String ext : loggingConfigurationsExt)
			{
				String rlink = customConfigPath.replace("*", ext);
				
				if (RLUtils.exists(rlink))
				{
					customConfigPath = rlink;
					isConfigurationFound = true;
					break;
				}
			}
			
			if (!isConfigurationFound)
			{
				logger.warning("Can't read logging configuration at", customConfigPath);
				return false;
			}
			
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
