package ru.swayfarer.swl2.swconf.config;

import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.threads.ThreadsUtils;

/**
 * Поток, слушающий изменение конфига-цели и сохраняет его раз в какое-то время 
 * @author swayfarer
 *
 */
public class ConfigurationSaveThread extends Thread {

	/** Логгер */
	@InternalElement
	public ILogger logger = LoggingManager.getLogger(getClass().getSimpleName());
	
	/** Слушаемый конфиг */
	@InternalElement
	public AutoSerializableConfig cfg;
	
	/** Конструктор */
	public ConfigurationSaveThread(AutoSerializableConfig cfg)
	{
		ExceptionsUtils.IfNullArg(cfg, "Handled config can't be null!");
		this.cfg = cfg;
	}
	
	@Override
	public void run()
	{
		for (;;)
		{
			saveConfig();
			ThreadsUtils.sleepSafe(AutoSerializableConfig.ConfigInfo.DEFAULT_SAVE_DELAY);
		}
	}
	
	/** Сохранить конфиг */
	@InternalElement
	public void saveConfig()
	{
		if (cfg != null && cfg.configInfo != null)
		{
			logger.safe(() -> {
				if (cfg.configInfo.isNeedsToSave)
					cfg.save();
				
				Thread.sleep(cfg.configInfo.saveDelayInMilisis);
				
			}, "Error while handling config's", cfg, "update");
			
		}
	}
	
}
