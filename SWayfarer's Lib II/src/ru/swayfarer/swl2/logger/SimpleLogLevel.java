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

	/** Текст уровня {@link StandartLoggingLevels#LEVEL_INFO}*/
	@InternalElement
	public static String INFO_PREFIX_TEXT = "&{gr, 1}Info&{}";
	
	/** Текст уровня {@link StandartLoggingLevels#LEVEL_WARNING}*/
	@InternalElement
	public static String WARNING_PREFIX_TEXT = "&{yl, 1}Warning&{}";
	
	/** Текст уровня {@link StandartLoggingLevels#LEVEL_ERROR}*/
	@InternalElement
	public static String ERROR_PREFIX_TEXT = "&{rd, 1}Error&{}";
	
	/** Текст уровня {@link StandartLoggingLevels#LEVEL_FATAL}*/
	@InternalElement
	public static String FATAL_PREFIX_TEXT = "&{yl, 1}Fatal&{}";
	
	/** Функция, возвращающая префикс уровня */
	@InternalElement
	public IFunction0<String> prefixFun;
	
	/** Уровень Java-логирования, соответствующий этому уровню  */
	@InternalElement
	public Level javaLevel;
	
	/** Важность лога  */
	@InternalElement
	public int weight;
	
	/** Получить уровень по заданным параметрам */
	public static SimpleLogLevel of(String prefix, Level javaLevel, int weight)
	{
		String formattedPrefix = AnsiFormatter.instance.format(prefix);
		return of(() -> formattedPrefix, javaLevel, weight);
	}
	
	/** Получить уровень по заданным параметрам */
	public static SimpleLogLevel of(IFunction0<String> prefixFun, Level javaLevel, int weight)
	{
		SimpleLogLevel level = new SimpleLogLevel();
		level.prefixFun = prefixFun;
		level.javaLevel = javaLevel;
		level.weight = weight;
		
		return level;
	}

	/** Получить префикс */
	@Override
	public String getPrefix() {
		return prefixFun.apply();
	}
}
