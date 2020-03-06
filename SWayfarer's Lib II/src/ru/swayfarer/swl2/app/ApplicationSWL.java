package ru.swayfarer.swl2.app;

import java.util.List;

import lombok.SneakyThrows;
import ru.swayfarer.swl2.asm.classloader.ClassLoaderSWL;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.SimpleObservable;
import ru.swayfarer.swl2.options.Option;
import ru.swayfarer.swl2.options.OptionsParser;

/** 
 * Стартер для приложений
 * @author swayfarer
 *
 */
public abstract class ApplicationSWL {
	
	/** Парсер опций */
	public OptionsParser optionsParser;
	
	public IExtendedList<Option> options;
	
	/** Логгер*/
	public ILogger logger = LoggingManager.getLogger(getClass().getSimpleName());
	
	/** Точка для подписки на событие выхода из приложения */
	public IObservable<Void> eventExit = new SimpleObservable<>(); 

	/** 
	 * Настроить класслоадер, который будет грузить приложение 
	 * <h1> На этом этапе нельзя работать с полями приложения. 
	 * <br> Класслоадер создаст новый экземпляр, прежде чем выполнится {@link #preStart(IExtendedList)}!
	 * <br> Тут можно только настраивать класслоадер. </br>
	 */
	public void configureClassloader(ClassLoaderSWL classLoader) {}
	
	/** Предзагрузка приложения */
	public void preStart(IExtendedList<String> args) {}
	
	/** Загрузка приложения */
	public void start(IExtendedList<String> args) {}
	
	/** Пост-загрузка приложения */
	public void postStart(IExtendedList<String> args) {}

	/** Старт приложения */
	@InternalElement
	public void launch(List<String> optionsList)
	{
		IExtendedList<String> options = CollectionsSWL.createExtendedList(optionsList);
		optionsParser = new OptionsParser();
		preStart(options);
		this.options = optionsParser.parse(options);
		optionsParser.init(logger, this.options);
		start(options);
		postStart(options);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> eventExit.next(null)));
	}
	
	/** Вызов стартера приложения из указанного класса */
	@SneakyThrows
	public static void startApplication(String[] args)
	{
		startApplication(Class.forName(ExceptionsUtils.getCallerStacktrace().getClassName()), args);
	}
	
	/** Вызов стартера приложения из указанного класса */
	public static void startApplication(Class<?> cl, String[] args)
	{
		ILogger logger = LoggingManager.getLogger();
		
		ApplicationSWL applicationSWL = ReflectionUtils.newInstanceOf(cl);
		
		if (applicationSWL == null)
		{
			logger.error("Can't start null application!");
			return;
		}
		
		logger.safe(() -> {
			
			ClassLoaderSWL classLoaderSWL = new ClassLoaderSWL();
			
			applicationSWL.configureClassloader(classLoaderSWL);
			
			classLoaderSWL.startAt(cl, "launch", args);
			
		}, "Error while starting application");
	}
}
