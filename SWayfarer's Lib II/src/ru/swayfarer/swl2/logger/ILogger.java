package ru.swayfarer.swl2.logger;

import java.util.Vector;

import ru.swayfarer.swl2.exceptions.IUnsafeRunnable;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.logger.ILogLevel.StandartLoggingLevels;
import ru.swayfarer.swl2.logger.event.LogEvent;
import ru.swayfarer.swl2.logger.formatter.DecoratorFormatter;
import ru.swayfarer.swl2.logger.formatter.TemplateLogFormatter;
import ru.swayfarer.swl2.logger.handlers.LogRedirectHandler;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.string.StringUtils;

/** 
 * Логгер
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public interface ILogger {

	/*
	 * Логирование
	 */
	
	/** Отлогировать */
	public void log(LogInfo logInfo);
	
	/*
	 * Стандартные
	 */
	
	/**
	 * Лог на уровне {@link StandartLoggingLevels#LEVEL_INFO}
	 * @param text Логируемый текст
	 */
	public void info(@ConcattedString Object... text);
	
	/**
	 * Лог на уровне {@link StandartLoggingLevels#LEVEL_WARNING}
	 * @param text Логируемый текст
	 */
	public void warning(@ConcattedString Object... text);
	
	/**
	 * Лог на уровне {@link StandartLoggingLevels#LEVEL_WARNING}
	 * @param text Логируемый текст
	 */
	public void error(@ConcattedString Object... text);
	
	/**
	 * Лог на уровне {@link StandartLoggingLevels#LEVEL_FATAL}
	 * @param text Логируемый текст
	 */
	public void fatal(@ConcattedString Object... text);
	
	/**
	 * Лог на указанном уровне
	 * @param text Логируемый текст
	 * @param lvl Уровень лога 
	 */
	public void log(ILogLevel lvl, @ConcattedString Object... text);
	
	/*
	 * Throwable
	 */
	
	/**
	 * Лог на уровне {@link StandartLoggingLevels#LEVEL_INFO}
	 * @param text Логируемый текст
	 * @param e {@link Throwable}, сопровождающий лог
	 */
	public void info(Throwable e, @ConcattedString Object... text);
	
	/**
	 * Лог на уровне {@link StandartLoggingLevels#LEVEL_WARNING}
	 * @param text Логируемый текст
	 * @param e {@link Throwable}, сопровождающий лог
	 */
	public void warning(Throwable e, @ConcattedString Object... text);
	
	/**
	 * Лог на уровне {@link StandartLoggingLevels#LEVEL_ERROR}
	 * @param text Логируемый текст
	 * @param e {@link Throwable}, сопровождающий лог
	 */
	public void error(Throwable e, @ConcattedString Object... text);
	
	/**
	 * Лог на уровне {@link StandartLoggingLevels#LEVEL_FATAL}
	 * @param text Логируемый текст
	 * @param e {@link Throwable}, сопровождающий лог
	 */
	public void fatal(Throwable e, @ConcattedString Object... text);
	
	/**
	 * Лог на указанном уровне
	 * @param lvl Уровень лога
	 * @param text Логируемый текст
	 * @param e {@link Throwable}, сопровождающий лог
	 */
	public void log(ILogLevel lvl, Throwable e, @ConcattedString Object... text);
	
	
	public <T extends ILogger> T child(String name);
	
	/*
	 * Форматирование
	 */
	
	/**
	 * Задать форматирующую функцию для логгера 
	 */
	public <T extends ILogger> T setFormatter(IFunction2NoR<ILogger, LogInfo> formatter);
	
	/**
	 * Задать логирующую функцию для логгера 
	 */
	public <T extends ILogger> T setPrinter(IFunction2NoR<ILogger, LogInfo> formatter);
	
	/**
	 * Получить форматирующую функцию
	 */
	public IFunction2NoR<ILogger, LogInfo> getFormatter();
	
	/**
	 * Получить логирующую функцию
	 */
	public IFunction2NoR<ILogger, LogInfo> getPrinter();
	
	/**
	 * Получить имя логгера
	 */
	public String getName();
	
	/** Получить место, из которого логгер был создан */
	public Vector<StackTraceElement> getFrom();
	
	/** Задать место, из которого был создан логгер */
	public <T extends ILogger> T setFrom(Vector<StackTraceElement> from);
	
	/** Задать место, из которого был создан логгер */
	public default <T extends ILogger> T setFrom(StackTraceElement[] stackTraceElements)
	{
		Vector<StackTraceElement> creationStacktrace = new Vector<>();
		
		for (StackTraceElement stackTraceElement : stackTraceElements)
			creationStacktrace.add(stackTraceElement);
		
		setFrom(creationStacktrace);
		
		return setFrom(creationStacktrace);
	}
	
	public default <T extends ILogger> T setDecoratorSeq(@ConcattedString Object... text)
	{
		DecoratorFormatter formatter = new DecoratorFormatter();
		formatter.decoratorSeq = StringUtils.concat(text);
		
		this.setFormatter(getFormatter().andThan(formatter));
		return (T) this;
	}
	
	public default <T extends ILogger> T setFrom(int stacktraceOffset)
	{
		stacktraceOffset += 1;
		
		StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
		
		if (stacktraceOffset >= stackTraceElements.length)
		{
			System.out.println("Сорян");
			return (T) this;
		}
		
		Vector<StackTraceElement> creationStacktrace = new Vector<>();
		
		for (int i1 = stacktraceOffset; i1 < stackTraceElements.length; i1 ++)
		{
			creationStacktrace.add(stackTraceElements[i1]);
		}
		
		return setFrom(creationStacktrace);
	}
	
	/**
	 * Задать форматирование логгера в формате {@link TemplateLogFormatter}
	 * @param format Формат
	 */
	public default <T extends ILogger> T setLogFormat(String format)
	{
		setFormatter(TemplateLogFormatter.of(format));
		return (T) this;
	}
	
	/** Перенаправлять логи в другой логгер. (Форматировать будет тоже другой) */
	public default <T extends ILogger> T redirect(ILogger logger)
	{
		if (logger == null)
			return (T) this;
		
		evtLogging().subscribe(new LogRedirectHandler(logger));
		return (T) this;
	}
	
	/** Безопасно выполнить действие и отлогировать ошибку, если будет */
	public default Throwable safe(IUnsafeRunnable run)
	{
		return safe(run, "Error while processing safe action");
	}
	
	/** Безопасно выполнить действие и отлогировать ошибку, если будет */
	public default Throwable safe(IUnsafeRunnable run, @ConcattedString Object... text)
	{
		try
		{
			run.run();
		}
		catch (Throwable e)
		{
			error(e, text);
			return e;
		}
		
		return null;
	}
	
	/** Установить позднюю загрузку через {@link LoggingManager} */
	public default <T extends ILogger> T lateinit()
	{
		LoggingManager.registerLateinitLogger(this);
		return (T) this;
	}
	
	/**
	 * Событие для подключения наблюдателей
	 * @return {@link IObservable}, или null, если события не поддерживаются. 
	 */
	public IObservable<LogEvent> evtLogging();
	
	/**
	 * Событие для подключения наблюдателей после логирования 
	 * @return {@link IObservable}, или null, если события не поддерживаются. 
	 */
	public IObservable<LogEvent> evtPostLogging();
}
