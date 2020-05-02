package ru.swayfarer.swl2.logger.config.print;

import lombok.ToString;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.StringUtils;

/** 
 * Информация о выводе логгером логов в консоль
 * @author swayfarer
 *
 */
@ToString
public class PrintingInfo {

	/** Формат, в котором выводятся логи */
	@InternalElement
	public String format;
	
	/** Применить на логгер */
	public void apply(ILogger logger)
	{
		if (!StringUtils.isEmpty(format))
			logger.setLogFormat(format);
	}
	
}
