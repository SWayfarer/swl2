package ru.swayfarer.swl2.logger.formatter;

import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LogInfo;
import ru.swayfarer.swl2.string.StringUtils;

public class DecoratorFormatter implements IFunction2NoR<ILogger, LogInfo> {

	public static String defaultDecorator = "-";
	public String decoratorSeq = defaultDecorator;
	
	@Override
	public void applyNoR(ILogger logger, LogInfo logInfo)
	{
		if (logInfo.isDecorated)
		{
			int size = StringUtils.getMaxLineLenght(logInfo.getContent());
			
			if (StringUtils.isEmpty(decoratorSeq))
				decoratorSeq = defaultDecorator;
			
			String decorator = StringUtils.createSeq(decoratorSeq, size);
			
			logInfo.content = StringUtils.concat("\n", decorator, logInfo.getContent(), decorator, "\n");
		}
	}

}
