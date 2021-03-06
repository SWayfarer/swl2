package ru.swayfarer.swl2.app;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.SneakyThrows;
import ru.swayfarer.swl2.asm.classloader.ClassLoaderSWL;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1NoR;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.ioc.DIManager;
import ru.swayfarer.swl2.ioc.EnableDIScan;
import ru.swayfarer.swl2.ioc.componentscan.DIScan;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.SimpleObservable;
import ru.swayfarer.swl2.options.Option;
import ru.swayfarer.swl2.options.OptionsParser;
import ru.swayfarer.swl2.string.StringUtils;

/** 
 * Стартер для приложений
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public abstract class ApplicationSWL {
	
	/** Время старта приложения */
	@InternalElement
	public long startTime = System.currentTimeMillis();
	
	/** Парсер опций */
	public OptionsParser optionsParser;
	
	/** Прочитанные из командной строки опции */
	@InternalElement
	public IExtendedList<Option> options;
	
	/** Логгер*/
	@InternalElement
	public ILogger logger = LoggingManager.getLogger(getClass().getSimpleName());
	
	/** Точка для подписки на событие выхода из приложения */
	public IObservable<ApplicationSWL> eventExit = new SimpleObservable<>(); 
	
	/** Показывать ли время работы приложения при его закрытии? */
	public boolean isShowingWorkTimeOnExit = true;
	
	/** Включен ли postStart? */
	public boolean isPostStartEnabled;
	
	/** Стартер для приложения */
	public IFunction2NoR<IExtendedList<String>, IFunction1NoR<IExtendedList<String>>> applicationStarter = (args, method) -> method.apply(args);

	/** Сканнер элементов DI, таких как компоненты и источники контекста */
	@InternalElement
	public DIScan diScan = new DIScan();
	
	/** Ланучер приложения, дла более гибкого старта (например, для стара с JavaFX) */
	@InternalElement
	public IFunction2NoR<List<String>, IFunction1NoR<List<String>>> launcherFun = (args, fun) -> fun.apply(args);
	
	/** 
	 * Настроить класслоадер, который будет грузить приложение 
	 * <h1> На этом этапе нельзя работать с полями приложения. 
	 * <br> Класслоадер создаст новый экземпляр, прежде чем выполнится {@link #preStart(IExtendedList)}!
	 * <br> Тут можно только настраивать класслоадер. </br>
	 * @param classLoader Загрузчик классов, который настраивается
	 */
	public void configureClassloader(ClassLoaderSWL classLoader) {}
	
	/** Настроить {@link #launcherFun} */
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
	
	/**
	 * Инициализация DI
	 * @param args Аргументы, переданные в main
	 * @return True, если DI должен быть заиньекчен в этот объект
	 */
	public boolean initDI(IExtendedList<String> args) { return false; }
	
	/**
	 * Безопасная версия метода {@link #start(IExtendedList)}, находящегося под try-catch
	 * @param args Аргументы, переданные в main
	 * @throws Throwable
	 */
	public void startSafe(IExtendedList<String> args) throws Throwable {}
	
	/** 
	 * Пост-загрузка приложения
	 * @param args Лист аргументов, переданных при старте
	 */
	public void postStart(IExtendedList<String> args) {}
	
	/** Отсканировать элементы DI внутри пакета приложения (см {@link #diScan}) */
	public void scanDIComponents()
	{
		diScan.scan(getClass().getPackage().getName());
	}

	/**
	 * Запустить при помощи лаунчера ({@link #launcherFun}) приложение
	 * @param optionsList Лист аргументов, переданных при старте
	 */
	public void launchByLauncher(List<String> optionsList)
	{
		configureLauncher();
		launcherFun.apply(optionsList, this::launch);
	}
	
	/**
	 * Использовать источник класса приложения для сканирования DI, вместо classpath'а <br>
	 * Быстрее, чем сканировать classpath(Иногда намного), но требует наличия всех DI элементов в одном источнике
	 * @return Это приложение (this)
	 */
	public <T extends ApplicationSWL> T useApplicationSourceForScan()
	{
		this.diScan.classFinder.useClassSource(getClass());
		return (T) this;
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
		
		EnableDIScan scanAnnotation = ReflectionUtils.findAnnotationRec(getClass(), EnableDIScan.class);
		
		boolean mustInject = initDI(options);
				
		if (scanAnnotation != null)
		{
			if (diScan.isAlreadyScanned.get())
			{
				logger.warning("Application marked by", EnableDIScan.class.getName(), ", but component scan is already done! Skiping...");
			}
			else
			{
				String[] roots = scanAnnotation.roots();
				
				if (CollectionsSWL.isNullOrEmpty(roots))
					roots = new String[] {""};
				
				for (String root : roots)
				{
					if (StringUtils.isBlank(root))
						root = getClass().getPackage().getName();

					diScan.scan(root, true);
				}
				
				mustInject = true;
			}
		}
		
		if (mustInject)
			DIManager.injectContextElements(this);
		
		this.options = optionsParser.parse(options);
		optionsParser.init(logger, this.options);
		
		AtomicInteger nextListenerId = new AtomicInteger();
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> eventExit.next(this), "ShutdownListener-" + nextListenerId.getAndIncrement()));
		
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
