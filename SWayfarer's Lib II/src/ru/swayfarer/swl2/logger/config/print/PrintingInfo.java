package ru.swayfarer.swl2.logger.config.print;

import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.string.StringUtils;

public class PrintingInfo {

	public String format;
	
	public void apply(ILogger logger)
	{
		if (!StringUtils.isEmpty(format))
			logger.setLogFormat(format);
	}
	
}
