package ru.swayfarer.swl2.logger.config;

import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.observable.subscription.ISubscription;

/**
 * Настройщик логгеров 
 * @author swayfarer
 */
public interface ILoggerConfigurator extends IFunction2NoR<ISubscription<ILogger>, ILogger>{

	/** Подходит ли логгер для настройки этим конфигуратором? */
	public default boolean isLoggerAcceptable(ILogger logger) { return true; }
	
	/** Настроить логгер */
	public void configure(ILogger logger);
	
	/** Настроить логгер, если он подходит */
	@Override
	default void applyNoR(ISubscription<ILogger> sub, ILogger logger)
	{
		if (isLoggerAcceptable(logger))
			configure(logger);
	}
	
}
