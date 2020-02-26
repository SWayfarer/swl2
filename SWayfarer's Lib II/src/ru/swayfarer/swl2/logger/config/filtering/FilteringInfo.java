package ru.swayfarer.swl2.logger.config.filtering;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.handlers.LogLevelFilteringHandler;

@AllArgsConstructor(staticName = "of") @Data
public class FilteringInfo {
	
	public int levelWeight;
	public List<String> ignoreLevels;
	
	public FilteringInfo()
	{
		
	}
	
	public void apply(ILogger logger)
	{
		if (levelWeight != Short.MIN_VALUE)
			logger.evtLogging().subscribe(new LogLevelFilteringHandler(levelWeight));
		
		if (!CollectionsSWL.isNullOrEmpty(ignoreLevels))
			logger.evtLogging().subscribe((sub, logEvent) -> logEvent.setCanceled(ignoreLevels.contains(logEvent.logInfo.level.getPrefix())));
	}
}