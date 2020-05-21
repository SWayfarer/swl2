package ru.swayfarer.swl2.logger.configurator.entry;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.logger.ILogLevel;
import ru.swayfarer.swl2.logger.LogInfo;
import ru.swayfarer.swl2.logger.event.LogEvent;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.swconf.serialization.comments.CommentSwconf;
import ru.swayfarer.swl2.swconf.serialization.comments.IgnoreSwconf;

/**
 * Фильтрация логов
 * @author swayfarer
 *
 */
@InternalElement
public class LogFiltering {
	
	@IgnoreSwconf
	public static String colorRegex = StringUtils.regex()
		.text("&{")
		.some()
		.not("}")
		.text("}")
	.build();
		
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
	
	/** Уровни логов, которые будут показаны при логировании */
	@CommentSwconf("Levels thats will be ignored by logger")
	public IExtendedList<String> acceptLevels = CollectionsSWL.createExtendedList();
	
	public boolean isAccepts(LogEvent evt)
	{
		LogInfo logInfo = evt.logInfo;
		ILogLevel level = logInfo.getLevel();
		
		int logWeight = level.getWeight();
		
		if (logWeight < this.minLevel)
			return false;
		
		if (logWeight > this.maxLevel)
			return false;
		
		if (!CollectionsSWL.isNullOrEmpty(this.loggerNames))
		{
			if (!this.loggerNames.contains(evt.logger.getName()))
				return false;
			
		}
		
		String prefix = evt.logInfo.getLevel().getPrefix();
		prefix = prefix.replaceAll(colorRegex, "");
		
		if (!CollectionsSWL.isNullOrEmpty(this.ignoreLevels))
		{
			
			if (this.ignoreLevels.contains(prefix))
				return false;
		}
		
		if (!CollectionsSWL.isNullOrEmpty(this.acceptLevels))
		{
			if (!this.acceptLevels.contains(prefix))
				return false;
		}
		
		return true;
	}
}