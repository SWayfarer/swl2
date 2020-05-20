package ru.swayfarer.swl2.logger.configurator.entry;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.logger.ILogLevel;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LogInfo;
import ru.swayfarer.swl2.logger.event.LogEvent;
import ru.swayfarer.swl2.logger.handlers.LogFileHandler;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.swconf.serialization.comments.CommentSwconf;

/**
 * Конфигуратор сохранения логов в файл
 * @author swayfarer
 *
 */
public class ConfiguratorFileEntry {

	/** Имя лог. файла */
	@CommentSwconf("Logfile name")
	public String fileName = "logs/log.log";
	
	/** Фильтрация логов, пройдя которую они будут записаны в файл */
	@CommentSwconf("Filtering of logs for this logfile")
	public Filtering filtering = new Filtering();
	
	/** Конфигуратор архивации лог. файла */
	@CommentSwconf("Archiving of this log file")
	public ConfiguratorArchivingEntry archiving;
	
	/**
	 * Фильтрация логов
	 * @author swayfarer
	 *
	 */
	@InternalElement
	public static class Filtering {
		
		/** Минимальный уровень лога */
		@CommentSwconf("Min log level for save")
		public int minLevel = Integer.MIN_VALUE;
		
		/** Максимальный уровень лога */
		@CommentSwconf("Max log level for save")
		public int maxLevel = Integer.MAX_VALUE;
		
		/** Имена логгеров, логи из которых будут приниматься. Если не пустой, то все не попавшие под них будут пропущены */
		@CommentSwconf("Loggers logs from which will be saved to logfile")
		public IExtendedList<String> loggerNames = CollectionsSWL.createExtendedList();
		
		/** Уровни логов, которые будут пропущены при логировании */
		@CommentSwconf("Levels thats will be ignored by logger")
		public IExtendedList<String> ignoreLevels = CollectionsSWL.createExtendedList();
	}
	
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
				LogInfo logInfo = evt.logInfo;
				ILogLevel level = logInfo.getLevel();
				
				int logWeight = level.getWeight();
				
				if (logWeight < filtering.minLevel)
					return false;
				
				if (logWeight > filtering.maxLevel)
					return false;
				
				if (!CollectionsSWL.isNullOrEmpty(filtering.loggerNames))
				{
					if (!filtering.loggerNames.contains(evt.logger.getName()))
						return false;
					
				}
				
				if (!CollectionsSWL.isNullOrEmpty(filtering.ignoreLevels))
				{
					if (filtering.ignoreLevels.contains(evt.logInfo.getLevel().getPrefix()))
						return false;
				}
				
				return true;
			};
		}
		
		return (f) -> true;
	}
}
