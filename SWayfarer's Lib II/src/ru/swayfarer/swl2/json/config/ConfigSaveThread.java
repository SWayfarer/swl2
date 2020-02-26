package ru.swayfarer.swl2.json.config;

import ru.swayfarer.swl2.collections.weak.WeakList;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.markers.InternalElement;

/**
 * Поток, фоново сохраняющий все существующие конфиги
 * @author swayfarer
 */
@InternalElement
public class ConfigSaveThread extends Thread {
	
	/** Текущий экземпляр {@link ConfigSaveThread} */
	@InternalElement
	public static ConfigSaveThread instance;
	
	/** Лист всех созданных конфигов */
	@InternalElement
	public static WeakList<AbstractJsonConfig> configs = new WeakList<>();
	
	/**
	 * Добавить конфиг для автосохранения 
	 */
	public static void addConfig(AbstractJsonConfig cfg)
	{
		synchronized (configs)
		{
			configs.addExclusive(cfg);
		}
	}
	
	/**
	 * Инициализация {@link ConfigSaveThread} 
	 */
	public static void init()
	{
		try
		{
			if (instance == null || !instance.isAlive())
			{
				instance = new ConfigSaveThread();
				instance.setDaemon(true);
				instance.start();
			}
		}
		catch (Throwable e)
		{
			AbstractJsonConfig.logger.error(e, "Error while initing ConfigSaveThread");
		}
	}
	
	/** Старт потока */
	@Override
	public void run()
	{
		for (;;)
		{
			synchronized (configs)
			{
				for (AbstractJsonConfig config : configs)
				{
					try
					{
						if (config.isDirty)
							config.save();
					}
					catch (Throwable e)
					{
						AbstractJsonConfig.logger.error(e, "(ConfigSaveThread) Error while saving json config at", config.location);
					}
				}
			}
			
			ExceptionsUtils.safe(() -> Thread.sleep(750));
		}
	}
	
	/**
	 * Выполнится при загрузке класса 
	 */
	static
	{
		init();
	}
	
}