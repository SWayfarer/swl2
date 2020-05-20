package ru.swayfarer.swl2.logger.configurator.entry;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LogInfo;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.swconf.serialization.comments.CommentSwconf;

/**
 * Конфигурация для одного набора пакетов
 * @author swayfarer
 *
 */
public class ConfiguartorEntry {

	/** Источники, к которым будет применена эта конфигурация */
	@CommentSwconf("Packages filtering for this configuration")
	public IExtendedList<String> applySources = CollectionsSWL.createExtendedList();
	
	/** Настройки вывода в консоль */
	@CommentSwconf("Console printing settings")
	public ConfiguratorPrintingEntry printing = new ConfiguratorPrintingEntry();
	
	/** Настройки сохранения логов в файл */
	@CommentSwconf("Files saving settings")
	public IExtendedList<ConfiguratorFileEntry> files = CollectionsSWL.createExtendedList();
	
	/** Применить на логгер */
	public void applyToLogger(ILogger logger)
	{
		if (printing != null)
		{
			printing.applyToLogger(logger);
		}
		
		if (!CollectionsSWL.isNullOrEmpty(files))
		{
			files.each((c) -> c.applyToLogger(logger));
		}
	}
	
	/** Должен ли лог быть обработан при помощи этой конфигурации? */
	public boolean isAcceptingLog(LogInfo logInfo)
	{
		String logSource = logInfo.getCallTrace().getFirstElement().getClassName();
		
		if (CollectionsSWL.isNullOrEmpty(applySources))
			return true;
		
		for (String source : applySources)
		{
			if (isMatchesBy(source, logSource))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/** 
	 * Подходит ли источник лога под паттерн?
	 * @param source Паттерн, источник лога из {@link #applySources}
	 * @param logSource Источник лога
	 * @return Подходит ли?
	 */
	public boolean isMatchesBy(String source, String logSource)
	{
		return StringUtils.isMatchesByExpression(source, logSource);
	}
}
