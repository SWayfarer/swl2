package ru.swayfarer.swl2.logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils.StacktraceOffsets;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.logger.ILogLevel.StandartLoggingLevels;
import ru.swayfarer.swl2.logger.config.ILoggerConfigurator;
import ru.swayfarer.swl2.logger.config.SimpleLoggerConfigutaror;
import ru.swayfarer.swl2.logger.handlers.LogRedirectHandler;
import ru.swayfarer.swl2.logger.printers.SOutInterceptor;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.SimpleObservable;
import ru.swayfarer.swl2.resource.rlink.RLUtils;
import ru.swayfarer.swl2.resource.rlink.ResourceLink;
import ru.swayfarer.swl2.resource.streams.DataInputStreamSWL;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.swconf.utils.SwconfSerializationHelper;

/**
 * Менеджер логирования
 * <br> Позволяет настраивать логгеры, создаваемые через {@link #getLogger(String)}, для работы в одном режиме 
 * @author swayfarer
 */
public class LoggingManager {

	
	/** Закончена ли ранняя загрузка */
	@InternalElement
	public static boolean isLateinitCompleted;
	
	/** Событие создания логгера */
	public static IObservable<ILogger> eventCreation = new SimpleObservable<>();
	
	/** Функция, создающая новые логгеры, зная их имена */
	public static IFunction1<String, ILogger> loggerFactory = (name) -> new SimpleLoggerSWL(name);
	
	/** Созданные логгеры, кэшированные по их именам*/
	@InternalElement
	public static Map<String, ILogger> cachedLoggers = new HashMap<>();
	
	/** Логгеры, которые будут привязаны после инициализации {@link #rootLogger} */
	@InternalElement
	public static List<ILogger> lateinitLoggers = new ArrayList<>();
	
	/** Корневой логгер */
	@InternalElement
	public static ILogger rootLogger = new SimpleLoggerSWL().lateinit();
	
	/** Имя, с которым будет создан {@link #soutLogger} */
	public static String soutLoggerName = "SOUT";
	
	/** Формат, с которым будет создан {@link #soutLogger} */
	public static String soutLoggerFormat = "(%from%) [%logger%] -> %text%";
	
	/** Логгер, в который редиректится {@link System#out}, если вызван */
	@InternalElement
	public static ILogger soutLogger;
	
	/** Получить(или создать) настроенный логгер по имени класса, из которого вызван метод */
	public static ILogger getLogger()
	{
		return getLogger(ExceptionsUtils.getSimpleClassAt(StacktraceOffsets.OFFSET_CALLER), StacktraceOffsets.OFFSET_CALLER);
	}
	
	public static ILogger injectSOut()
	{
		if (soutLogger == null)
		{
			soutLogger = getLogger(soutLoggerName).setLogFormat(soutLoggerFormat);
			
			SOutInterceptor.inject(soutLogger, StandartLoggingLevels.LEVEL_INFO);
		}
		
		return soutLogger;
	}
	
	/** Получить(или создать) настроенный логгер по имени класса */
	public static ILogger getLogger(Class<?> cl)
	{
		if (cl == null)
			return null;
		
		return getLogger(cl.getName().toString(), StacktraceOffsets.OFFSET_CALLER);
	}
	
	/** Получить(или создать) настроенный логгер по имени */
	public static ILogger getLogger(String name)
	{
		return getLogger(name, StacktraceOffsets.OFFSET_CALLER);
	}
	
	/** Получить(или создать) настроенный логгер по имени */
	public static ILogger getLogger(String name, int stacktraceOffset)
	{
		if (name == null)
			return null;
		
		if (cachedLoggers.containsKey(name))
			return cachedLoggers.get(name);
		
		ILogger logger = createLogger(name, true);
		cachedLoggers.put(name, logger);
		
		return logger.setFrom(stacktraceOffset + 1).lateinit();
	}
	
	/** Создать логгер */
	@InternalElement
	public static ILogger createLogger(String name, boolean skipEvent)
	{
		ILogger logger = loggerFactory.apply(name);
		
		return logger.setFrom(StacktraceOffsets.OFFSET_CALLER);
	}
	
	public static void loadConfig(boolean isAutodetect)
	{
		ILogger logger = getLogger();
		
		logger.safe(() -> {
			
			
			
//			Class<?> cl = Class.forName(last.getClassName());
//			
//			String configPath = 
//			
//			URL url = cl.getClassLoader().getResource("");
//			
		}, "Error while loading logging confiduration");
	}
	
	/**
	 *  Получить корневой логгер
	 * <br> Корневой логгер будет использован, если все логгеры будут редиректиться к нему
	 */
	public static ILogger getRootLogger()
	{
		return rootLogger;
	}
	
	/**
	 * Включить всем логгерам, создаваемым через {@link #getLogger(String)}, режим ретрансляции в {@link #getRootLogger()}
	 * <br> Например, необходимо все свести к одному логгеру, как привыкли любители log4j
	 */
	public static void setAllToRootRedirecting()
	{
		eventCreation.subscribe((sub, logger) -> logger.evtLogging().subscribe(new LogRedirectHandler(rootLogger)));
	}
	
	public static void registerLateinitLogger(ILogger logger)
	{
		if (isLateinitCompleted)
		{
			eventCreation.next(logger);
		}
		else
		{
			lateinitLoggers.add(logger);
		}
	}
	
	public static void onInited()
	{
		isLateinitCompleted = true;
		lateinitLoggers.forEach(eventCreation::next);
		lateinitLoggers.clear();
	}
	
	public static void loadConfig()
	{
		String loggerConf = System.getProperty("swl.logging.conf");
		
		if (StringUtils.isEmpty(loggerConf))
			loggerConf = "/assets/swl/conf/logging/logger.json";
		
		ResourceLink rlink = RLUtils.createLink(loggerConf);
		loadConfig(rlink);
		
		if (rlink != null)
			rlink.getAdjacents(true).forEach(LoggingManager::loadConfig);
	}
	
	public static ILoggerConfigurator loadConfigurator(@ConcattedString Object... rlink)
	{
		ILogger logger = getRootLogger();
		
		ResourceLink link = RLUtils.createLink(StringUtils.concat(rlink));
		
		if (link != null && link.isExists())
		{
			return SwconfSerializationHelper.forRlink(link).readFromSwconf(link.toSingleString(), SimpleLoggerConfigutaror.class);
		}
		else
		{
			logger.warning("Can't load logger configurator from rlink", link, "because it's not exists!");
		}
		
		return null;
	}
	
	public static void loadConfig(ResourceLink rlink)
	{
		ILogger logger = getRootLogger();
		
		if (!rlink.isExists())
		{
			logger.warning("No config found at", rlink.content, ". Skiping configuration loading...");
			return;
		}
		
		DataInputStreamSWL stream = rlink.toStream();
		
		if (stream == null)
			return;
		
		String json = stream.readAllAsUtf8();
		
		logger.info("Loading logging configutaion: '\n", json, "\n'...");
		
		SimpleLoggerConfigutaror rootConfigutaror = SwconfSerializationHelper.json.readFromSwconf(json, SimpleLoggerConfigutaror.class);
		eventCreation.subscribe(rootConfigutaror);
	}
	
}
