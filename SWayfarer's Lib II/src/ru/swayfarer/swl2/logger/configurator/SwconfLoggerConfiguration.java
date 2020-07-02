package ru.swayfarer.swl2.logger.configurator;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.config.AutoSerializableConfig;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.SimpleLoggerSWL;
import ru.swayfarer.swl2.logger.configurator.entry.ConfiguartorEntry;
import ru.swayfarer.swl2.logger.handlers.LogMultiloggerHandler;
import ru.swayfarer.swl2.resource.rlink.ResourceLink;
import ru.swayfarer.swl2.swconf2.mapper.annotations.CommentedSwconf;

/**
 * Конфигуратор логгеров v2
 * @author swayfarer
 *
 */
public class SwconfLoggerConfiguration extends AutoSerializableConfig {

	/** Если задано в false, конфигуратор не будет работать */
	@CommentedSwconf("If set to false confirurations will be skipped")
	public boolean isEnabled = true;

	/** Список конфигураций для логгера */
	@CommentedSwconf("Base logs configuration that will apply to all logs")
	public ConfiguartorEntry base;
	
	/** Список конфигураций для логгера */
	@CommentedSwconf("Configurations for some packages")
	public IExtendedList<ConfiguartorEntry> custom = CollectionsSWL.createExtendedList();
	
	/** Применить на логгер */
	public void applyToLogger(ILogger logger)
	{
		if (!isEnabled)
			return;
		
		if (CollectionsSWL.isNullOrEmpty(custom))
		{
			if (base != null)
			{
				base.applyToLogger(logger);
			}
		}
		else
		{
			LogMultiloggerHandler multiloggerHandler = new LogMultiloggerHandler();
			
			for (ConfiguartorEntry entry : custom)
			{
				ILogger subLogger = new SimpleLoggerSWL(logger.getName());
				
				// Если базовые настройки заданы и кастомные настройки их используют, то применяем их до кастомных
				if (base != null && entry.useBaseConfiguration)
					base.applyToLogger(subLogger);
				
				// А затем применяем кастомные
				entry.applyToLogger(subLogger);
				multiloggerHandler.addLogger(entry::isAcceptingLog, subLogger);
			}
			
			// Дефолтный логгер, который будет выдавать логи в том случае, когда не подошли кастомные 
			if (base != null)
			{
				ILogger baseLogger = new SimpleLoggerSWL(logger.getName());
				base.applyToLogger(baseLogger);
				multiloggerHandler.addLogger(base::isAcceptingLog, baseLogger);
			}
			
			logger.evtLogging().subscribe(Integer.MAX_VALUE, multiloggerHandler);
		}
	}
	
	/**
	 * Получить конфигуратор из ресурса по ссылке ({@link ResourceLink}) 
	 * @param rlink Ссылка в формате {@link ResourceLink}, по которой лежит файл конфигурации
	 * @return Прочитанная конфигурация (даже если чтение с ошибкой, вернется не null)
	 */
	public static SwconfLoggerConfiguration of(String rlink)
	{
		SwconfLoggerConfiguration ret = new SwconfLoggerConfiguration();
		ret.setResourceLink(rlink);
		ret.init();
		return ret;
	}
}
