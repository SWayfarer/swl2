package ru.swayfarer.swl2.logger.formatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LogInfo;
import ru.swayfarer.swl2.string.StringUtils;

public class DateFormatRule implements IFunction2NoR<ILogger, LogInfo>{

	public static final String DATE_REGEX = StringUtils.regex()
			.text("%date[")
				.something()
			.text("]%")
		.build();
	
	@Override
	public void applyNoR(ILogger logger, LogInfo logInfo)
	{
		logInfo.content = format(logInfo.content, new Date(logInfo.time));
	}
	
	public static String format(String s, Date d)
	{
		List<String> dates = StringUtils.getAllMatches(DATE_REGEX, s);
		
		if (dates != null)
		{
			for (String date : dates)
			{
				String format = date.substring(6, date.length() - 2);
				
				SimpleDateFormat dateFormat = new SimpleDateFormat(format);
				
				s = s.replace(date, dateFormat.format(d));
			}
		}
		
		return s;
	}

}
