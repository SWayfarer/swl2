package ru.swayfarer.swl2.logger;

import java.util.logging.Level;

public interface ILogLevel {

	public String getPrefix();
	public Level getJavaLevel();
	public int getWeight();
	
	public static interface StandartLoggingLevels {

		public static final ILogLevel LEVEL_INFO = SimpleLogLevel.of("&{gr}Info&{}", Level.INFO, 1);
		public static final ILogLevel LEVEL_WARNING = SimpleLogLevel.of("&{yl}Warning&{}", Level.WARNING, 2);
		public static final ILogLevel LEVEL_ERROR = SimpleLogLevel.of("&{rd}Error&{}", Level.SEVERE, 3);
		public static final ILogLevel LEVEL_FATAL = SimpleLogLevel.of("&{yl}Fatal&{}", Level.SEVERE, 4);
		
	}
	
}
