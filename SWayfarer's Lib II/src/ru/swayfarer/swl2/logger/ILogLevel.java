package ru.swayfarer.swl2.logger;

import java.util.logging.Level;

public interface ILogLevel {

	public String getPrefix();
	public Level getJavaLevel();
	public int getWeight();
	public String getLogPrefix();
	
	public static interface StandartLoggingLevels {

		public static final ILogLevel LEVEL_INFO = SimpleLogLevel.of
		(
				() -> SimpleLogLevel.INFO_PREFIX_TEXT,
				() -> SimpleLogLevel.INFO_LOG_COLOR_PREFIX,
				Level.INFO,
				1
		);
		
		public static final ILogLevel LEVEL_DEV = SimpleLogLevel.of
		(
				() -> SimpleLogLevel.DEV_PREFIX_TEXT,
				() -> SimpleLogLevel.DEV_LOG_COLOR_PREFIX,
				Level.FINE,
				1
		);
		
		public static final ILogLevel LEVEL_WARNING = SimpleLogLevel.of
		(
				() -> SimpleLogLevel.WARNING_PREFIX_TEXT,
				() -> SimpleLogLevel.WARNING_LOG_COLOR_PREFIX,
				Level.WARNING,
				2
		);
		
		public static final ILogLevel LEVEL_ERROR = SimpleLogLevel.of
		(
				() -> SimpleLogLevel.ERROR_PREFIX_TEXT,
				() -> SimpleLogLevel.ERROR_LOG_COLOR_PREFIX,
				Level.SEVERE,
				3
		);
		
		public static final ILogLevel LEVEL_FATAL = SimpleLogLevel.of
		(
				() -> SimpleLogLevel.FATAL_PREFIX_TEXT,
				() -> SimpleLogLevel.FATAL_LOG_COLOR_PREFIX,
				Level.SEVERE,
				4
		);
		
	}
}
