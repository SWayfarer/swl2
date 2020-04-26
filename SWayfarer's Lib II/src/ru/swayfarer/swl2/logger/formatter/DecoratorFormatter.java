package ru.swayfarer.swl2.logger.formatter;

import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LogInfo;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.StringUtils;

/** 
 * Форматтер, который декорирует лог
 * @author swayfarer
 *
 */
public class DecoratorFormatter implements IFunction2NoR<ILogger, LogInfo> {

	/** Дефотлное значение для {@link #decoratorSeq} */
	@InternalElement
	public static String defaultDecorator = "-";
	
	/** Последовательность символов, из которой будет составлен сплиттер декоратора */
	@InternalElement
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
			
			logInfo.content = logInfo.getLevel().getLogPrefix() + StringUtils.concat("\n", decorator, logInfo.getContent(), decorator, "\n");
		}
	}

}
