package ru.swayfarer.swl2.logger;

/**
 * Содель конфига для логов
 * 
 * @author swayfarer
 */
public class LoggingConfig {

	/** Класс логгера, который будет создаваться */
	public String loggerType = SimpleLoggerSWL.class.getName();

	/** Шаблон, по которому будут генерится логи */
	public String loggingTemplate = "[%thread%] [%date[HH:mm:ss-DD.MM.YYYY]%] [%logger%] [%level%] -> %text%";

	/** Минимальный вес лога, чтобы он отобразился */
	public int minLogWeight = -1;

	/** Применить конфиг */
	public void apply()
	{
		try
		{
			LoggingManager.loggerFactory = (name) -> {

				try
				{
					return (ILogger) Class.forName(loggerType).getDeclaredConstructor(String.class).newInstance(name);
				}
				catch (Throwable e)
				{
					System.err.println("Error while creating logger with class " + loggerType + ".Maybe default constructor does not exist?");
					e.printStackTrace();
				}

				return null;
			};
		}
		catch (Throwable e)
		{
			System.err.println("Error while applying logging configutation!");
			e.printStackTrace();
		}
	}

}
