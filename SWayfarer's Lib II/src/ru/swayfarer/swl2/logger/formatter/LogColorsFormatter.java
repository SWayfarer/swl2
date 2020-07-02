package ru.swayfarer.swl2.logger.formatter;

import ru.swayfarer.swl2.ansi.Ansi;
import ru.swayfarer.swl2.ansi.ConsoleColorsFormatter;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LogInfo;
import ru.swayfarer.swl2.markers.InternalElement;

/**
 * См {@link ConsoleColorsFormatter} 
 * @author swayfarer
 *
 */
public class LogColorsFormatter extends ConsoleColorsFormatter implements IFunction2NoR<ILogger, LogInfo> {
	
	public boolean isAppendsSane = true;
	
	/** Экземляр для работы */
	@InternalElement
	public static LogColorsFormatter instance = new LogColorsFormatter();
	
	@Override
	public void applyNoR(ILogger logger, LogInfo info) {
		
		info.content = format(info.content) + ( isAppendsSane ? Ansi.SANE : "") ;
	}

}
