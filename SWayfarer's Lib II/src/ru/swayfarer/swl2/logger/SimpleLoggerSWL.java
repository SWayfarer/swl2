package ru.swayfarer.swl2.logger;

import java.util.Vector;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils.StacktraceOffsets;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.logger.ILogLevel.StandartLoggingLevels;
import ru.swayfarer.swl2.logger.event.LogEvent;
import ru.swayfarer.swl2.logger.formatter.AnsiColorsFormatter;
import ru.swayfarer.swl2.logger.formatter.TemplateLogFormatter;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.SimpleObservable;
import ru.swayfarer.swl2.string.StringUtils;;

@SuppressWarnings("unchecked")
public class SimpleLoggerSWL implements ILogger, StandartLoggingLevels, StacktraceOffsets{

	public IFunction1<Throwable, IExtendedList<StackTraceElement>> stacktraceDecorator = (e) -> CollectionsSWL.createExtendedList(e.getStackTrace());
	public static String defaultDecoratorSeq = "=-";
	public static String defaultFormat = "[%thread%/%level%]&{153} (%from%) &{h:1}[%logger%] -> %text%";
	
	public IFunction1<StackTraceElement, String> stacktraceToStringFun = String::valueOf;
	
	public static IFunction2NoR<ILogger, LogInfo> defaultPrinter = (logger, info) -> System.out.println(info.getContent());
	
	public static IFunction2NoR<ILogger, LogInfo> defaultFormatter = new TemplateLogFormatter()
			.setFormat(defaultFormat);
	
	public String name;
	
	public Vector<StackTraceElement> from;
	
	public IFunction2NoR<ILogger, LogInfo> formatter, printer;
	
	public IObservable<LogEvent> eventLogging = new SimpleObservable<>();
	public IObservable<LogEvent> eventPostLogging = new SimpleObservable<>();
	
	public static boolean coloringEnabledByDefault = true;
	
	private AnsiColorsFormatter ansiFormatter = AnsiColorsFormatter.instance;
	
	public SimpleLoggerSWL()
	{
		this(ExceptionsUtils.getSimpleClassAt(OFFSET_CALLER));
		setFrom(OFFSET_CALLER);
	}
	
	public SimpleLoggerSWL(String name)
	{
		this.name = name;
		formatter = defaultFormatter;
		printer = defaultPrinter;
		setFrom(OFFSET_CALLER);
		setDecoratorSeq(defaultDecoratorSeq);
	}
	
	@Override
	public void log(LogInfo logInfo)
	{
		LogEvent event = LogEvent.of(logInfo, this);
		
		evtLogging().next(event);
		if (event.isCanceled())
			return;
		
		if (logInfo.attachedThrowable != null)
			logInfo.content = getStringFromThrowable(logInfo.attachedThrowable, logInfo.content);
		
		if (!logInfo.isFormatted() && formatter != null)
		{
			formatter.apply(this, logInfo);
			logInfo.setFormatted(true);
		}
		
		if (ansiFormatter != null)
		{
			ansiFormatter.apply(this, logInfo);
		}
		else
		{
			System.out.println("Colors formatter is null!");
		}
		
		if (printer != null)
			printer.apply(this, logInfo);
		
		event = LogEvent.of(logInfo, this);
		evtPostLogging().next(event);
	}

	@Override
	public <T extends ILogger> T hideColors()
	{
		ansiFormatter = new ClearAnsiFormatter();
		return (T) this;
	}
	
	@Override
	public void info(@ConcattedString Object... text)
	{
		log(LogInfo.of(this, LEVEL_INFO, OFFSET_CALLER, text));
	}

	@Override
	public void warning(@ConcattedString Object... text)
	{
		log(LogInfo.of(this, LEVEL_WARNING, OFFSET_CALLER, text));
	}

	@Override
	public void error(@ConcattedString Object... text)
	{
		log(LogInfo.of(this, LEVEL_ERROR, OFFSET_CALLER, text));
	}

	@Override
	public void fatal(@ConcattedString Object... text)
	{
		log(LogInfo.of(this, LEVEL_FATAL, OFFSET_CALLER, text));
	}

	@Override
	public void log(ILogLevel lvl, Object... text)
	{
		log(LogInfo.of(this, lvl, OFFSET_CALLER, text));
	}

	@Override
	public void info(Throwable e, @ConcattedString Object... text)
	{
		log(LogInfo.of(this, LEVEL_INFO, OFFSET_CALLER, text).setAttachedThrowable(e).setDecorated(true));
	}

	@Override
	public void warning(Throwable e, @ConcattedString Object... text)
	{
		log(LogInfo.of(this, LEVEL_WARNING, OFFSET_CALLER, text).setAttachedThrowable(e).setDecorated(true));
	}

	@Override
	public void error(Throwable e, @ConcattedString Object... text)
	{
		log(LogInfo.of(this, LEVEL_ERROR, OFFSET_CALLER, text).setAttachedThrowable(e).setDecorated(true));
	}

	@Override
	public void fatal(Throwable e, @ConcattedString Object... text)
	{
		log(LogInfo.of(this, LEVEL_FATAL, OFFSET_CALLER, text).setAttachedThrowable(e).setDecorated(true));
	}

	@Override
	public void log(ILogLevel lvl, Throwable e, Object... text)
	{
		log(LogInfo.of(this, lvl, 0, text).setAttachedThrowable(e).setDecorated(true));
	}

	@Override
	public <T extends ILogger> T setFormatter(IFunction2NoR<ILogger, LogInfo> formatter)
	{
		this.formatter = formatter;
		return (T) this;
	}

	@Override
	public <T extends ILogger> T setPrinter(IFunction2NoR<ILogger, LogInfo> printer)
	{
		this.printer = printer;
		return (T) this;
	}

	@Override
	public IFunction2NoR<ILogger, LogInfo> getFormatter()
	{
		return formatter;
	}

	@Override
	public IFunction2NoR<ILogger, LogInfo> getPrinter()
	{
		return printer;
	}
	
	public IExtendedList<StackTraceElement> getStacktrace(Throwable e)
	{
		return stacktraceDecorator == null ? CollectionsSWL.createExtendedList(e.getStackTrace()) : stacktraceDecorator.apply(e);
	}
	
	@InternalElement
	public String getStringFromThrowable(Throwable e, @ConcattedString Object... text)
	{
		String message = StringUtils.concatWithSpaces(text);
		StringBuilder builder = new StringBuilder();
		
		while (e != null)
		{
			if (message != null)
			{
				builder.append(message);
				builder.append("\n");
			}
			
			builder.append(e);
			builder.append('\n');
			
			boolean needsNewLine = false;
			
			for (StackTraceElement stackTraceElement : getStacktrace(e))
			{
				if (needsNewLine)
					builder.append('\n');
					
				builder.append("	at ");
				builder.append(stacktraceToStringFun.apply(stackTraceElement));
				
				needsNewLine = true;
			}
			
			e = e.getCause();
			
			if (e != null)
			{
				message = "\nCaused by: ";
			}
		}
		
		return builder.toString();
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public <T extends ILogger> T child(String name)
	{
		return null;
	}

	@Override
	public IObservable<LogEvent> evtLogging()
	{
		return eventLogging;
	}

	@Override
	public IObservable<LogEvent> evtPostLogging()
	{
		return eventPostLogging;
	}

	@Override
	public Vector<StackTraceElement> getFrom()
	{
		return from;
	}

	@Override
	public <T extends ILogger> T setFrom(Vector<StackTraceElement> from)
	{
		this.from = from;
		return (T) this;
	}

	@Override
	public <T extends ILogger> T setStacktraceDecorator(IFunction1<Throwable, IExtendedList<StackTraceElement>> fun)
	{
		this.stacktraceDecorator = fun;
		return (T) this;
	}

	@Override
	public IFunction1<Throwable, IExtendedList<StackTraceElement>> getStacktraceDecorator()
	{
		return stacktraceDecorator;
	}

	@Override
	public IFunction1<StackTraceElement, String> getStacktraceToStringFun()
	{
		return stacktraceToStringFun;
	}

	@Override
	public <T extends ILogger> T setStacktraceToStringFun(IFunction1<StackTraceElement, String> fun)
	{
		stacktraceToStringFun = fun;
		return (T) this;
	}
	
	public static class ClearAnsiFormatter extends AnsiColorsFormatter {
		
		public ClearAnsiFormatter()
		{
			isAppendsSane = false;
		}
		
		@Override
		public String getColor(String code, String background, String style)
		{
			return "";
		}
	}

	@Override
	public void dev(Object... text)
	{
		log(LogInfo.of(this, LEVEL_DEV, OFFSET_CALLER, text));
	}

	@Override
	public void dev(Throwable e, Object... text)
	{
		log(LogInfo.of(this, LEVEL_DEV, OFFSET_CALLER, text)
				.setAttachedThrowable(e)
				.setDecorated(true)
		);
	}
}

