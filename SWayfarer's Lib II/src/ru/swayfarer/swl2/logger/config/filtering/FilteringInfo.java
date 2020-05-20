package ru.swayfarer.swl2.logger.config.filtering;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.handlers.LogLevelFilteringHandler;

/**
 * Информация о фильтрации логов 
 * @author swayfarer
 *
 */
@ToString
@AllArgsConstructor(staticName = "of") @Data
public class FilteringInfo {
	
	/** Минимальный вес лога, чтобы его пропустили */
	public int levelWeight;
	
	/** Игнорируемые уровни логов */
	public List<String> ignoreLevels;
	
	/** Конструктор */
	public FilteringInfo() {}
	
	/** Применить на логгер */
	public void apply(ILogger logger)
	{
		if (levelWeight != Short.MIN_VALUE)
			logger.evtLogging().subscribe(new LogLevelFilteringHandler(levelWeight));
		
		if (!CollectionsSWL.isNullOrEmpty(ignoreLevels))
			logger.evtLogging().subscribe((sub, logEvent) -> logEvent.setCanceled(ignoreLevels.contains(logEvent.logInfo.level.getPrefix())));
	}
}