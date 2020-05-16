package ru.swayfarer.swl2.app;

import java.util.List;

import lombok.SneakyThrows;
import ru.swayfarer.swl2.asm.classloader.ClassLoaderSWL;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1NoR;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.ioc.DIManager;
import ru.swayfarer.swl2.ioc.componentscan.ComponentScan;
import ru.swayfarer.swl2.ioc.componentscan.DISwlComponent;
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
	
	/** Время старта приложения */
	public long startTime = System.currentTimeMillis();
	
	/** Парсер опций */
	public OptionsParser optionsParser;
	
	public IExtendedList<Option> options;
	
	/** Логгер*/
	public ILogger logger = LoggingManager.getLogger(getClass().getSimpleName());
	
	/** Точка для подписки на событие выхода из приложения */
	public IObservable<Void> eventExit = new SimpleObservable<>(); 
	
	/** Показывать ли время работы приложения при его закрытии? */
	public boolean isShowingWorkTimeOnExit = true;
	
	/** Включен ли postStart? */
	public boolean isPostStartEnabled;
	
	/** Стартер для приложения */
	public IFunction2NoR<IExtendedList<String>, IFunction1NoR<IExtendedList<String>>> applicationStarter = (args, method) -> method.apply(args);

	public ComponentScan componentScan = new ComponentScan().setStreamFun(ComponentScan.streamFunOfClassSource(getClass()));

	/** 
	 * Настроить класслоадер, который будет грузить приложение 
	 * <h1> На этом этапе нельзя работать с полями приложения. 
	 * <br> Класслоадер создаст новый экземпляр, прежде чем выполнится {@link #preStart(IExtendedList)}!
	 * <br> Тут можно только настраивать класслоадер. </br>
	 * @param classLoader Загрузчик классов, который настраивается
	 */
	public void configureClassloader(ClassLoaderSWL classLoader) {}
	
	public void configureLauncher() {};
	
	/**
	 *  Предзагрузка приложения 
	 *  @param args Лист аргументов, переданных при старте
	 */
	public void preStart(IExtendedList<String> args) {}
	
	/** 
	 *  Загрузка приложения 
	 *  @param args Лист аргументов, переданных при старте
	 */
	public void start(IExtendedList<String> args) {}
	
	public boolean initDI(IExtendedList<String> args) { return false; }
	
	public void startSafe(IExtendedList<String> args) throws Throwable {}
	
	/** 
	 * Пост-загрузка приложения
	 * @param args Лист аргументов, переданных при старте
	 */
	public void postStart(IExtendedList<String> args) {}
	
	/** Отсканировать DI-компоненты, отмеченные аннтотацией {@link DISwlComponent}*/
	public void scanDIComponents()
	{
		componentScan.scan(getClass().getPackage().getName());
	}
	
	public IFunction2NoR<List<String>, IFunction1NoR<List<String>>> launcherFun = (args, fun) -> fun.apply(args);

	public void launchByLauncher(List<String> optionsList)
	{
		configureLauncher();
		launcherFun.apply(optionsList, this::launch);
	}
	
	/** 
	 * Старт приложения 
	 * @param optionsList Лист аргументов, переданных при старте
	 */
	@InternalElement
	public void launch(List<String> optionsList)
	{
		IExtendedList<String> options = CollectionsSWL.createExtendedList(optionsList);
		optionsParser = new OptionsParser();
		preStart(options);
		
		if (initDI(options))
			DIManager.injectContextElements(this);
		
		this.options = optionsParser.parse(options);
		optionsParser.init(logger, this.options);
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> eventExit.next(null)));
		
		eventExit.subscribe((e) -> {
			if (isShowingWorkTimeOnExit)
				logger.info("Application work time", System.currentTimeMillis() - startTime, "milisis");
		});
		
		applicationStarter.apply(options, this::start);
		applicationStarter.apply(options, (e) -> logger.safe(() -> startSafe(e)));
		
		if (isPostStartEnabled)
			applicationStarter.apply(options, this::postStart);
	}
	
	/** 
	 * Вызов стартера приложения из указанного класса 
	 * @param args Аргументы из метода main
	 */
	@SneakyThrows
	public static Object startApplication(String[] args)
	{
		return startApplication(Class.forName(ExceptionsUtils.getCallerStacktrace().getClassName()), args);
	}
	
	/** 
	 * Вызов стартера приложения из указанного класса
	 * @param cl Класс приложения
	 * @param args Аргументы из метода main
	 */
	public static Object startApplication(Class<?> cl, String[] args)
	{
		ILogger logger = LoggingManager.getLogger();
		
		ApplicationSWL applicationSWL = ReflectionUtils.newInstanceOf(cl);
		
		if (applicationSWL == null)
		{
			logger.error("Can't start null application!");
			return null;
		}
		
		return logger.safeReturn(() -> {
			
			ClassLoaderSWL classLoaderSWL = new ClassLoaderSWL();
			
			applicationSWL.configureClassloader(classLoaderSWL);
			
			return classLoaderSWL.startAt(cl, "launchByLauncher", args);
			
		}, null, "Error while starting application");
	}
}
