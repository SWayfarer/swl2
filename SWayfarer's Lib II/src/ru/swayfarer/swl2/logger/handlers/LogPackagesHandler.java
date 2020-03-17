package ru.swayfarer.swl2.logger.handlers;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LogInfo;
import ru.swayfarer.swl2.logger.event.LogEvent;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.observable.subscription.ISubscription;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Редиректилка логгеров в зависимости от источника логов
 * 
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class LogPackagesHandler implements IFunction2NoR<ISubscription<LogEvent>, LogEvent>{

	/** Зарегистрированные редиректы */
	@InternalElement
	public IExtendedList<ILogRedirectInfo> redirectInfos = CollectionsSWL.createExtendedList();
	
	@Override
	public void applyNoR(ISubscription<LogEvent> sub, LogEvent evt)
	{
		redirectInfos.dataStream().each((e) -> {
			if (e.isAccepts(evt.logInfo))
			{
				ILogger targetLogger = e.getRedirectLogger();
				
				if (targetLogger != null && targetLogger != evt.logger)
				{
					targetLogger.log(evt.logInfo);
					evt.setCanceled(true);
				}
			}
		});
	}
	
	/** Добавить редирект */
	public <T extends LogPackagesHandler> T addRedirect(ILogger logger, String... packageMasks) 
	{
		redirectInfos.add(ILogRedirectInfo.of(logger, packageMasks));
		return (T) this;
	}
	
	/**
	 * Информация о редиректе лога
	 * @author swayfarer
	 *
	 */
	public static interface ILogRedirectInfo {
		
		/** Подходит ли лог для редиректа? */
		public boolean isAccepts(LogInfo logInfo);
		
		/** Получить логгер, на который будет происходить редирект */
		public ILogger getRedirectLogger();
		
		/** Получить редирект на указанный логгер из указанных пакетов */
		public static ILogRedirectInfo of(ILogger redirectTarget, String... packageMasks)
		{
			IExtendedList<String> masks = CollectionsSWL.createExtendedList(packageMasks);
			
			return new ILogRedirectInfo()
			{
				
				@Override
				public boolean isAccepts(LogInfo logInfo)
				{
					return masks.dataStream()
							.contains((e) -> StringUtils.isMatchesByMask(e, logInfo.callTrace.getFirstElement().getClassName()));
				}
				
				@Override
				public ILogger getRedirectLogger()
				{
					return redirectTarget;
				}
			};
		}
		
	}
}
