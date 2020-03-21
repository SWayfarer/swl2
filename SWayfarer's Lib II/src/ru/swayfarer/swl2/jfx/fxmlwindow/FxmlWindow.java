package ru.swayfarer.swl2.jfx.fxmlwindow;

import java.nio.charset.Charset;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.jfx.css.CssManager;
import ru.swayfarer.swl2.jfx.events.AdditionalHandler;
import ru.swayfarer.swl2.jfx.fxmlwindow.bridge.FxmlWindowBridge;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.SimpleObservable;
import ru.swayfarer.swl2.observable.property.ObservableProperty;
import ru.swayfarer.swl2.resource.rlink.RLUtils;
import ru.swayfarer.swl2.resource.rlink.ResourceLink;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Умный контроллер для fxml-файла
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public abstract class FxmlWindow {

	/** Логгер */
	@InternalElement
	public ILogger logger = LoggingManager.getLogger(getClass().getSimpleName());
	
	/** Контроллер */
	@InternalElement
	public Object controller;
	
	/** Сцена, на которой отображается окно */
	@InternalElement
	public Scene scene;
	
	/** {@link Stage}, через которую отображается окно */
	@InternalElement
	public Stage stage;
	
	/** Ссылка на fxml-ресурс */
	@InternalElement
	public ResourceLink fxmlLink;
	
	/** Декодирование fxml-файла */
	public String encoding;
	
	/** Кодировка, применяемая к читаемым через {@link FxmlWindow} fxml-файлам по-умолчанию */
	@InternalElement
	public Charset DEFAULT_CHARSET = StringUtils.getCharset("UTF-8");
	
	/** Мост между автоматически сгенерированным под fxml-файл контроллером и этим окном */
	@InternalElement
	public FxmlWindowBridge controllerConnectionBridge = new FxmlWindowBridge();
	
	/** Было ли проинициализировано окно? */
	@InternalElement
	public boolean isInited;
	
	/** События окна */
	@InternalElement
	public Events events = new Events();
	
	/** Показывается ли окно? */
	@InternalElement
	public ObservableProperty<Boolean> isShowing = new ObservableProperty<>();
	
	/** Показать окно и дождаться его закрытия */
	public <T extends FxmlWindow> T showAndWait()
	{
		return show(true);
	}
	
	/** Показать окно и дождаться его закрытия в том случае, если ни одного окна с этим классом не показывается в данный момент*/
	public boolean showAndWaitOnce()
	{
		if (isSomeShowing())
			return false;
		
		FxmlWindowsTracker.setWindowShowing(this, true);
		showAndWait();
		
		return true;
	}
	
	/** Показать окно */
	public <T extends FxmlWindow> T show()
	{
		return show(false);
	}
	
	/** Показать только в том случае, если ни одного окна с этим классом не показывается в данный момент */
	public boolean showOnce()
	{
		if (!isSomeShowing())
		{
			FxmlWindowsTracker.setWindowShowing(this, true);
			show();
			return true;
		}
		
		return false;
	}
	
	/** Показать окно в текущей {@link #stage}, или в новой, если она пуста */
	@InternalElement
	public <T extends FxmlWindow> T show(boolean isWait)
	{
		if (stage == null)
			stage = new Stage();
		
		if (!isInited)
		{
			isInited = true;
			initWindowInternal();
		}
		
		stage.setScene(scene);
		
		if (isWait)
			stage.showAndWait();
		else
			stage.show();
		
		return (T) this;
	}
	
	/** Показывается ли хотя бы одно окно с этим классом? */
	@InternalElement
	public boolean isSomeShowing()
	{
		return FxmlWindowsTracker.isWindowTypeAlreadyShowing(this);
	}
	
	/** Сделать окно перекрывающим указанное */
	public void setModal(Stage stage)
	{
		if (this.stage == null)
			this.stage = null;
		
		this.stage.initOwner(stage);
		this.stage.initModality(Modality.WINDOW_MODAL);
	}
	
	/** Задать заголовок окна */
	public <T extends FxmlWindow> T setTitle(@ConcattedString Object... text)
	{
		this.stage.setTitle(StringUtils.concatWithSpaces(text));
		return (T) this;
	}
	
	/** Будет ли окно всегда поверх других? */
	public <T extends FxmlWindow> T setAlwaysOnTop(boolean isAlwaysOnTop)
	{
		this.stage.setAlwaysOnTop(isAlwaysOnTop);
		return (T) this;
	}
	
	/** Сделать окно перекрывающим указанное */
	public void setModal(FxmlWindow window)
	{
		if (window == null)
			return;
		
		setModal(window.stage);
	}
	
	/** Загрузить окно из fxml-файла */
	public <T extends FxmlWindow> T loadFromFxml(ResourceLink fxmlRlink)
	{
		this.fxmlLink = fxmlRlink;
		
		ExceptionsUtils.IfNotExists(fxmlLink, "Fxml file for rlink", fxmlRlink, "does not exists! Skiping load from this fxml!");
		
		logger.safe(() -> {
			
			FXMLLoader loader = new FXMLLoader(StringUtils.getCharset(encoding, DEFAULT_CHARSET));
			
			Object controller = InternalControllerFactory.generateController(fxmlRlink);
			
			logger.info("Controller is", controller);
			
			loader.setController(controller);
			
			Parent root = loader.load(fxmlRlink.toStream());
			
			Scene scene = null;
			
			if (root instanceof Region)
			{
				Region rg = (Region) root;
				scene = new Scene(root, rg.prefWidthProperty().get(), rg.prefHeightProperty().get());
			}
			else
			{
				scene = new Scene(root);
			}
			
			logger.info(root.getClass());
			logger.info("Loaded scene with", scene.widthProperty().get(), scene.heightProperty().get(), "dimenstions!");
			CssManager.instance.addScene(scene);
			setScene(scene);
			setController(controller);
			
		}, "Error while loading", this, "from fxml at", fxmlRlink);
		
		return (T) this;
	}
	
	/** Задать сцену, на которой будет отображаться окно */
	public <T extends FxmlWindow> T setScene(Scene scene)
	{
		this.scene = scene;
		CssManager.instance.addWindow(this);
		return (T) this;
	}
	
	/** Задать {@link #stage} */
	public <T extends FxmlWindow> T setStage(Stage stage)
	{
		this.stage = stage;
		return (T) this;
	}
	
	/** Задать {@link #controller} */
	public <T extends FxmlWindow> T setController(Object controller)
	{
		this.controller = controller;
		return (T) this;
	}
	
	/** Получить {@link #controller} */
	public <T> T getController()
	{
		return (T) controller;
	}
	
	/** Инициализация окна*/
	@InternalElement
	public <T extends FxmlWindow> T initWindowInternal()
	{
		logger.safe(() -> {

			ResourceLink rlink = getFxmlRlink();
			loadFromFxml(rlink);
			preInitWindow();
			controllerConnectionBridge.connect(this, controller);
			initWindow();
			initCloseHandlerInternal();
			
		}, "Error while initializing fxml window for", fxmlLink);
		
		return (T) this;
	}
	
	@InternalElement
	/** Настроить обработчик закрытия окна */
	public void initCloseHandlerInternal()
	{
		AdditionalHandler<WindowEvent> closeHandler = new AdditionalHandler<WindowEvent>(stage.getOnCloseRequest());
		
		closeHandler.addHandler((evt) -> {
			
			CloseEvent event = new CloseEvent(this);
			events.eventCloseRequested.next(event);
			
			if (event.isCanceled())
				return;
			
			events.eventClose.next(event);
		});
		
		// Привязка события закрытия окна к проперти 
		events.eventClose.subscribe((e) -> isShowing.setValue(false));
		
		// Привязка события закрытия окна к FxmlWindowsTracker
		events.eventClose.subscribe((e) -> FxmlWindowsTracker.setWindowShowing(this, true));
		
		stage.setOnCloseRequest(closeHandler);
	}
	
	/** Получить ссылку на fxml-файл */
	@InternalElement
	public ResourceLink getFxmlRlink()
	{
		ResourceLink rlink = RLUtils.createLink(getFxmlPath());
		
		// Если вручную fxml-путь не задан, то попробуем найти его согласно имени класса 
		if (rlink == null)
		{
			rlink = findExistingFxmlPath();
		}
		
		// Если даже это не помогло, то говорим, что ничего не находится =(
		if (rlink == null)
		{
			logger.error("Fxml for window", this, "was not found! Please, override method getFxmlPath() or put fxml file into assets folder!");
		}
		
		return rlink;
	}
	
	/** Найти fxml внутри ресурсов по имени класса */
	public ResourceLink findExistingFxmlPath()
	{
		return FxmlFileFinder.findFxmlPathByName(getClass().getSimpleName());
	}
	
	/** Получить заданную руками ссылку на fxml-файл */
	public String getFxmlPath() { return null; }
	
	/** Инициализация окна */
	public abstract void initWindow();
	
	/** Пре-инициализация окна */
	public void preInitWindow() {}
	
	public static class Events {
		
		/** Событие закрытия окна */
		@InternalElement
		public IObservable<CloseEvent> eventCloseRequested = new SimpleObservable<>();
		
		/** Событие закрытия окна */
		@InternalElement
		public IObservable<CloseEvent> eventClose = new SimpleObservable<>();
		
	}
}
