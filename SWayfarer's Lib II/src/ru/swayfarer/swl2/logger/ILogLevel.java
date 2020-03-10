package ru.swayfarer.swl2.logger;

import java.util.logging.Level;

public interface ILogLevel {

	public String getPrefix();
	public Level getJavaLevel();
	public int getWeight();
	
	public static interface StandartLoggingLevels {

		public static final ILogLevel LEVEL_INFO = SimpleLogLevel.of(() -> SimpleLogLevel.INFO_PREFIX_TEXT, Level.INFO, 1);
		public static final ILogLevel LEVEL_WARNING = SimpleLogLevel.of(() -> SimpleLogLevel.WARNING_PREFIX_TEXT, Level.WARNING, 2);
		public static final ILogLevel LEVEL_ERROR = SimpleLogLevel.of(() -> SimpleLogLevel.ERROR_PREFIX_TEXT, Level.SEVERE, 3);
		public static final ILogLevel LEVEL_FATAL = SimpleLogLevel.of(() -> SimpleLogLevel.FATAL_PREFIX_TEXT, Level.SEVERE, 4);
		
	}
	
}
