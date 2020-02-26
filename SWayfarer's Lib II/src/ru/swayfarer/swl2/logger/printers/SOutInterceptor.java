package ru.swayfarer.swl2.logger.printers;

import lombok.AllArgsConstructor;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.logger.ILogLevel;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LogInfo;

@AllArgsConstructor(staticName = "of")
public class SOutInterceptor {

	public PrintStreamWrapper printStreamWrapper;
	public ILogger logger;
	public ILogLevel level;
	
	public static SOutInterceptor inject(ILogger logger, ILogLevel lvl)
	{
		if (logger == null)
			ExceptionsUtils.IfNull(logger, IllegalArgumentException.class, "Injecting logger can't be null!");
		
		PrintStreamWrapper wrapper = PrintStreamWrapper.of(System.out);
		
		SOutInterceptor interceptor = new SOutInterceptor(wrapper, logger, lvl);

		logger.setPrinter((ilogger, logEvent) -> System.err.println(logEvent.getContent()));
		
		wrapper.eventPrintLn.subscribe((sub, evt) -> {
			
			LogInfo logInfo = LogInfo.of(interceptor.logger, interceptor.level, 8, evt.content);
			logger.log(logInfo);
			evt.setCanceled(true);
			
		});
		
		System.setOut(wrapper);
		
		return interceptor;
	}
	
}
