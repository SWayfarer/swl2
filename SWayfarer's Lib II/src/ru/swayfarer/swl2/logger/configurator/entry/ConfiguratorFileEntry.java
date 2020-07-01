package ru.swayfarer.swl2.logger.configurator.entry;

import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.event.LogEvent;
import ru.swayfarer.swl2.logger.handlers.LogFileHandler;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.swconf2.mapper.annotations.CommentedSwconf;

/**
 * Конфигуратор сохранения логов в файл
 * @author swayfarer
 *
 */
public class ConfiguratorFileEntry {
	
	/** Имя лог. файла */
	@CommentedSwconf("Logfile name")
	public String fileName = "logs/log.log";
	
	/** Фильтрация логов, пройдя которую они будут записаны в файл */
	@CommentedSwconf("Filtering of logs for this logfile")
	public LogFiltering filtering = new LogFiltering();
	
	/** Конфигуратор архивации лог. файла */
	@CommentedSwconf("Archiving of this log file")
	public ConfiguratorArchivingEntry archiving;
	
	/**
	 * Получить файл логов
	 * @return Файл логов
	 */
	public FileSWL getLogFile()
	{
		return new FileSWL(fileName);
	}
	
	public void applyToLogger(ILogger logger)
	{
		IFunction1<LogEvent, Boolean> condition = getLogCondition();
		LogFileHandler fileHandler = new LogFileHandler(getLogFile(), condition);

		if (archiving != null)
			archiving.applyToLogger(logger, this);
		
		logger.evtPostLogging().subscribe(fileHandler);
	}
	
	/**
	 * Получить условие, при соответствии которого лога он будет записан
	 * @return Условие
	 */
	public IFunction1<LogEvent, Boolean> getLogCondition()
	{
		if (filtering != null)
		{
			return (evt) -> {
				return filtering.isAccepts(evt);
			};
		}
		
		return (f) -> true;
	}
}
