package ru.swayfarer.swl2.logger.configurator;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.SimpleLoggerSWL;
import ru.swayfarer.swl2.logger.configurator.entry.ConfiguartorEntry;
import ru.swayfarer.swl2.logger.handlers.LogMultiloggerHandler;
import ru.swayfarer.swl2.resource.rlink.ResourceLink;
import ru.swayfarer.swl2.swconf.config.AutoSerializableConfig;
import ru.swayfarer.swl2.swconf.format.StandartSwconfFormats;
import ru.swayfarer.swl2.swconf.serialization.comments.CommentSwconf;

/**
 * Конфигуратор логгеров v2
 * @author swayfarer
 *
 */
public class SwconfLoggerConfiguration extends AutoSerializableConfig {

	/** Если задано в false, конфигуратор не будет работать */
	@CommentSwconf("If set to false confirurations will be skipped")
	public boolean isEnabled = true;

	/** Список конфигураций для логгера */
	@CommentSwconf("Configurations for some packages")
	public IExtendedList<ConfiguartorEntry> configuration = CollectionsSWL.createExtendedList();
	
	/** Применить на логгер */
	public void applyToLogger(ILogger logger)
	{
		if (!isEnabled)
			return;
		
		if (CollectionsSWL.isNullOrEmpty(configuration))
			return;
		
		LogMultiloggerHandler multiloggerHandler = new LogMultiloggerHandler();
		
		for (ConfiguartorEntry entry : configuration)
		{
			ILogger subLogger = new SimpleLoggerSWL(logger.getName());
			entry.applyToLogger(subLogger);
			multiloggerHandler.addLogger(entry::isAcceptingLog, subLogger);
		}
		
		logger.evtLogging().subscribe(Integer.MAX_VALUE, multiloggerHandler);
	}
	
	/**
	 * Получить конфигуратор из ресурса по ссылке ({@link ResourceLink}) 
	 * @param rlink Ссылка в формате {@link ResourceLink}, по которой лежит файл конфигурации
	 * @return Прочитанная конфигурация (даже если чтение с ошибкой, вернется не null)
	 */
	public static SwconfLoggerConfiguration of(String rlink)
	{
		SwconfLoggerConfiguration ret = new SwconfLoggerConfiguration();
		ret.configInfo.configurationFormat = StandartSwconfFormats.LUA_FORMAT;
		ret.setResourceLink(rlink);
		ret.init();
		return ret;
	}
}
