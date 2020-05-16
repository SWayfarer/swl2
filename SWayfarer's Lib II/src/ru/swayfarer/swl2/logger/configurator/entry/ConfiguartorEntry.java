package ru.swayfarer.swl2.logger.configurator.entry;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LogInfo;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.swconf.serialization.comments.CommentSwconf;

public class ConfiguartorEntry {

	@CommentSwconf("Packages filtering for this configuration")
	public IExtendedList<String> applySources = CollectionsSWL.createExtendedList();
	
	@CommentSwconf("Console printing settings")
	public ConfiguratorPrintingEntry printing = new ConfiguratorPrintingEntry();
	
	@CommentSwconf("Files saving settings")
	public IExtendedList<ConfiguratorFileEntry> files = CollectionsSWL.createExtendedList();
	
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
	
	public boolean isMatchesBy(String source, String logSource)
	{
		if (StringUtils.isEmpty(source))
			return true;
		
		if (source.startsWith("regex:"))
		{
			source = source.substring(6);
			return StringUtils.isMatchesByRegex(source, logSource);
		}
		else if (source.startsWith("mask:"))
		{
			source = source.substring(5);
			return StringUtils.isMatchesByMask(source, logSource);
		}
		
		return logSource.startsWith(source);
	}
}
