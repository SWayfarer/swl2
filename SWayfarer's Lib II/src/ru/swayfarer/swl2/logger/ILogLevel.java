package ru.swayfarer.swl2.logger;

import java.util.logging.Level;

public interface ILogLevel {

	public String getPrefix();
	public Level getJavaLevel();
	public int getWeight();
	
	public static interface StandartLoggingLevels {

		public static final ILogLevel LEVEL_INFO = SimpleLogLevel.of("Info", Level.INFO, 1);
		public static final ILogLevel LEVEL_WARNING = SimpleLogLevel.of("Warning", Level.WARNING, 2);
		public static final ILogLevel LEVEL_ERROR = SimpleLogLevel.of("Error", Level.SEVERE, 3);
		public static final ILogLevel LEVEL_FATAL = SimpleLogLevel.of("Fatal", Level.SEVERE, 4);
		
	}
	
}
