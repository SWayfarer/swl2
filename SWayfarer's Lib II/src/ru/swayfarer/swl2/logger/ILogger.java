package ru.swayfarer.swl2.logger;

import java.util.Vector;

import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.exceptions.IUnsafeRunnable;
import ru.swayfarer.swl2.exceptions.IUnsafeRunnable.IUnsafeRunnableWithReturn;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction0NoR;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.logger.ILogLevel.StandartLoggingLevels;
import ru.swayfarer.swl2.logger.decorators.FilteredStacktraceDecorator;
import ru.swayfarer.swl2.logger.decorators.FormattedStacktraceDecorator;
import ru.swayfarer.swl2.logger.event.LogEvent;
import ru.swayfarer.swl2.logger.formatter.AnsiColorsFormatter;
import ru.swayfarer.swl2.logger.formatter.DecoratorFormatter;
import ru.swayfarer.swl2.logger.formatter.TemplateLogFormatter;
import ru.swayfarer.swl2.logger.handlers.LogFileHandler;
import ru.swayfarer.swl2.logger.handlers.LogRedirectHandler;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.reference.IReference;
import ru.swayfarer.swl2.reference.ParameterizedReference;
import ru.swayfarer.swl2.resource.file.FileSWL;
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
	
	/** Получить дочерний логгер */
	public <T extends ILogger> T child(String name);
	
	public default <T extends ILogger> T setStacktraceElementFormat(String format)
	{
		setStacktraceToStringFun(new FormattedStacktraceDecorator(format));
		return (T) this;
	}
	
	public default <T extends ILogger> T addStackstraceBlocker(String... packageStarts)
	{
		return addStacktraceFilter((st) -> !StringUtils.isStringStartsWith(st.getClassName(), (Object[])packageStarts));
	}
	
	/** Отлогировать время выполнения операции */
	public default <T extends ILogger> T operation(IFunction0NoR fun, @ConcattedString Object... text)
	{
		return operation(fun, (str, time) -> info(str + (time > 0 ? ". Tooks " + time + " milisis." : "...")), text);
	}
	
	public IFunction1<StackTraceElement, String> getStacktraceToStringFun();
	public <T extends ILogger> T setStacktraceToStringFun(IFunction1<StackTraceElement, String> fun);
	
	/** Отлогировать время выполнения операции с функцией логирования */
	public default <T extends ILogger> T operation(IFunction0NoR fun, IFunction2NoR<String, Long> logFun, @ConcattedString Object... text)
	{
		String s = StringUtils.concatWithSpaces(text);
		
		logFun.$(s, -1l);
		
		long start = System.currentTimeMillis();
		fun.$();
		long end = System.currentTimeMillis();
		
		logFun.$(s, end - start);
		
		return (T) this;
	}
	
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
	
	/** Добавить форматтер, не удаляя текущий */
	public default <T extends ILogger> T appendFormatter(IFunction2NoR<ILogger, LogInfo> newFormatter)
	{
		IFunction2NoR<ILogger, LogInfo> formatter = getFormatter();
		setFormatter(formatter == null ? newFormatter : formatter.andThan(newFormatter));
		return (T) this;
	}
	
	/** Включить поддержку цветов через {@link AnsiColorsFormatter#instance} */
	public default <T extends ILogger> T enableColoring()
	{
		return appendFormatter(AnsiColorsFormatter.instance);
	}
	
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
	
	public <T extends ILogger> T setStacktraceDecorator(IFunction1<Throwable, IExtendedList<StackTraceElement>> fun);

	public IFunction1<Throwable, IExtendedList<StackTraceElement>> getStacktraceDecorator();
	
	public default <T extends ILogger> T addStacktraceFilter(IFunction1<StackTraceElement, Boolean> filter)
	{
		IFunction1<Throwable, IExtendedList<StackTraceElement>> stacktraceDecorator = getStacktraceDecorator();
		
		FilteredStacktraceDecorator decorator = null;
		
		if (stacktraceDecorator == null)
		{
			decorator = new FilteredStacktraceDecorator();
			setStacktraceDecorator(decorator);
			decorator.addFilter(filter);
		}
		else if (stacktraceDecorator instanceof FilteredStacktraceDecorator)
		{
			decorator = (FilteredStacktraceDecorator) stacktraceDecorator;
			decorator.addFilter(filter);
		}
		else 
		{
			decorator = new FilteredStacktraceDecorator();
			setStacktraceDecorator(stacktraceDecorator.andApply((list) -> list.filter(filter)));
		}
		
		return (T) this;
	}
	
	@InternalElement
	/** Установить декоратор */
	public default <T extends ILogger> T setDecoratorSeq(@ConcattedString Object... text)
	{
		DecoratorFormatter formatter = new DecoratorFormatter();
		formatter.decoratorSeq = StringUtils.concat(text);
		
		if (getFormatter() == null)
			this.setFormatter(formatter);
		else
			this.setFormatter(getFormatter().andThan(formatter));
		
		return (T) this;
	}
	
	/** Завершать работу приложения, если попадается Fatal-лог */
	public default <T extends ILogger> T setExitingOnFatal()
	{
		evtPostLogging().subscribe((sub, log) -> {
			if (log.logInfo.level == StandartLoggingLevels.LEVEL_FATAL)
				System.exit(1);
		});
		
		return (T) this;
	}
	
	@InternalElement
	/** Задать место создания логгера*/
	public default <T extends ILogger> T setFrom(int stacktraceOffset)
	{
		stacktraceOffset += 1;
		
		StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
		
		if (stacktraceOffset >= stackTraceElements.length)
		{
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
	
	/**
	 * Задать директорию, в которую будут сохраняться логи
	 */
	public default <T extends ILogger> T setLogsDir(String dir)
	{
		return setLogsDir(dir, null);
	}
	
	/**
	 * Задать директорию, в которую будут сохраняться логи
	 */
	public default <T extends ILogger> T setLogsDir(String dir, ILogLevel minLogLevel)
	{
		LogFileHandler logFileHandler = LogFileHandler.of(new FileSWL(), (event) -> minLogLevel == null || event.logInfo.level.getWeight() >= minLogLevel.getWeight());
		evtPostLogging().subscribe(logFileHandler);
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
	
	public default <T> T safeOperationReturn(IUnsafeRunnableWithReturn<T> operation, T defaultValue, @ConcattedString Object... text)
	{
		IReference<T> ref = new ParameterizedReference<>();
		
		operation(() -> {
			try
			{
				ref.setValue(operation.run());
			}
			catch (Throwable e)
			{
				String errorText = "Error while " + StringUtils.concatWithSpaces(text);
				error(e, errorText);
			}
		}, text);
		
		return ref.isSetted() ? ref.getValue() : defaultValue;
	}
	
	/** Безопасно выполнить действие и отлогировать ошибку, если будет */
	public default Throwable safeOperation(IUnsafeRunnable operation, @ConcattedString Object... text)
	{
		IReference<Throwable> ref = new ParameterizedReference<>();
		
		operation(() -> {
			try
			{
				operation.run();
			}
			catch (Throwable e)
			{
				ref.setValue(e);
				String errorText = "Error while " + StringUtils.concatWithSpaces(text);
				error(e, errorText);
			}
		}, text);
		
		return ref.getValue();
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
	
	/** Безопасно выпонить и вернуть резуьтат */
	public default <T> T safeReturn(IUnsafeRunnableWithReturn<T> fun, @ConcattedString Object... text)
	{
		return safeReturn(fun, null, text);
	}
	
	/** Безопасно выпонить и вернуть резуьтат */
	public default <T> T safeReturn(IUnsafeRunnableWithReturn<T> fun, T ifnull, @ConcattedString Object... text)
	{
		try
		{
			return fun.run();
		}
		catch (Throwable e)
		{
			error(e, text);
		}
		
		return ifnull;
	}
	
	public String getStringFromThrowable(Throwable e, @ConcattedString Object... text);
	
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
