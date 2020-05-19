package ru.swayfarer.swl2.logger;

import java.util.logging.Level;

import lombok.Data;
import ru.swayfarer.swl2.ansi.AnsiFormatter;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction0;
import ru.swayfarer.swl2.markers.InternalElement;

/**
 * Простой уровень лога
 * @author swayfarer
 *
 */
@Data
public class SimpleLogLevel implements ILogLevel {

	/** Название уровня {@link StandartLoggingLevels#LEVEL_INFO}*/
	@InternalElement
	public static String INFO_PREFIX_TEXT = "&{green}Info&{h:1}";
	
	/** Название уровня {@link StandartLoggingLevels#LEVEL_WARNING}*/
	@InternalElement
	public static String WARNING_PREFIX_TEXT = "&{yellow}Warning&{h:1}";
	
	/** Название уровня {@link StandartLoggingLevels#LEVEL_ERROR}*/
	@InternalElement
	public static String ERROR_PREFIX_TEXT = "&{203}Error&{h:1}";
	
	/** Название уровня {@link StandartLoggingLevels#LEVEL_FATAL}*/
	@InternalElement
	public static String FATAL_PREFIX_TEXT = "&{9}Fatal&{h:1}";
	
	/** Цветовой префикс уровня {@link StandartLoggingLevels#LEVEL_FATAL}*/
	@InternalElement
	public static String INFO_LOG_COLOR_PREFIX = "&{85}";
	
	/** Цветовой префикс уровня {@link StandartLoggingLevels#LEVEL_FATAL}*/
	@InternalElement
	public static String ERROR_LOG_COLOR_PREFIX = "&{208}";
	
	/** Цветовой префикс уровня {@link StandartLoggingLevels#LEVEL_FATAL}*/
	@InternalElement
	public static String WARNING_LOG_COLOR_PREFIX = "&{229}";
	
	/** Цветовой префикс уровня {@link StandartLoggingLevels#LEVEL_FATAL}*/
	@InternalElement
	public static String FATAL_LOG_COLOR_PREFIX = "&{203}";
	
	/** Функция, возвращающая префикс уровня */
	@InternalElement
	public IFunction0<String> prefixFun, logPrefixFun;
	
	/** Уровень Java-логирования, соответствующий этому уровню  */
	@InternalElement
	public Level javaLevel;
	
	/** Важность лога  */
	@InternalElement
	public int weight;
	
	/** Получить уровень по заданным параметрам */
	public static SimpleLogLevel of(String prefix, String logPrefix, Level javaLevel, int weight)
	{
		String formattedPrefix = AnsiFormatter.getInstance().format(prefix);
		String formattedLogPrefix = AnsiFormatter.getInstance().format(logPrefix);
		return of(() -> formattedPrefix, () -> formattedLogPrefix, javaLevel, weight);
	}
	
	/** Получить уровень по заданным параметрам */
	public static SimpleLogLevel of(IFunction0<String> prefixFun, IFunction0<String> logPrefixFun, Level javaLevel, int weight)
	{
		SimpleLogLevel level = new SimpleLogLevel();
		level.prefixFun = prefixFun;
		level.logPrefixFun = logPrefixFun;
		level.javaLevel = javaLevel;
		level.weight = weight;
		
		return level;
	}

	/** Получить префикс */
	@Override
	public String getPrefix() {
		return prefixFun.apply();
	}

	@Override
	public String getLogPrefix()
	{
		return logPrefixFun.apply();
	}
}
