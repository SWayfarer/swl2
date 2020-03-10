package ru.swayfarer.swl2.logger.formatter;

import ru.swayfarer.swl2.ansi.Ansi;
import ru.swayfarer.swl2.ansi.AnsiFormatter;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LogInfo;
import ru.swayfarer.swl2.markers.InternalElement;

/**
 * См {@link AnsiFormatter} 
 * @author swayfarer
 *
 */
public class AnsiColorsFormatter extends AnsiFormatter implements IFunction2NoR<ILogger, LogInfo> {
	
	/** Экземляр для работы */
	@InternalElement
	public static AnsiColorsFormatter instance = new AnsiColorsFormatter();
	
	@Override
	public void applyNoR(ILogger logger, LogInfo info) {
		
		info.content = format(info.content) + Ansi.SANE;
	}

}
