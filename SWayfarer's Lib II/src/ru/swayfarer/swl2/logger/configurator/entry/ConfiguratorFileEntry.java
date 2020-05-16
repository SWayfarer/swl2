package ru.swayfarer.swl2.logger.configurator.entry;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.logger.ILogLevel;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LogInfo;
import ru.swayfarer.swl2.logger.event.LogEvent;
import ru.swayfarer.swl2.logger.handlers.LogFileHandler;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.swconf.serialization.comments.CommentSwconf;

public class ConfiguratorFileEntry {

	@CommentSwconf("Logfile name")
	public String fileName = "logs/log.log";
	
	@CommentSwconf("Filtering of logs for this logfile")
	public Filtering filtering = new Filtering();
	
	@CommentSwconf("Archiving of this log file")
	public ConfiguratorArchivingEntry archiving;
	
	public static class Filtering {
		
		@CommentSwconf("Min log level for save")
		public int minLevel = Integer.MIN_VALUE;
		
		@CommentSwconf("Loggers logs from which will be saved to logfile")
		public IExtendedList<String> loggerNames = CollectionsSWL.createExtendedList();
	}
	
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
	
	public IFunction1<LogEvent, Boolean> getLogCondition()
	{
		if (filtering != null)
		{
			return (evt) -> {
				LogInfo logInfo = evt.logInfo;
				ILogLevel level = logInfo.getLevel();
				
				if (level.getWeight() < filtering.minLevel)
					return false;
				
				if (!CollectionsSWL.isNullOrEmpty(filtering.loggerNames))
				{
					if (!filtering.loggerNames.contains(evt.logger.getName()))
						return false;
				}
				
				return true;
			};
		}
		
		return (f) -> true;
	}
}
