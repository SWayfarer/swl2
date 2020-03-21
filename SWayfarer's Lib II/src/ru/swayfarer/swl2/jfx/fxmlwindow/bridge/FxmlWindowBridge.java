package ru.swayfarer.swl2.jfx.fxmlwindow.bridge;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.jfx.fxmlwindow.FxmlWindow;
import ru.swayfarer.swl2.jfx.fxmlwindow.bridge.handlers.InjectJfxConntectionHandler;
import ru.swayfarer.swl2.jfx.fxmlwindow.bridge.handlers.OnClickConnectionHandler;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;

/**
 * Мост между контроллером от fxml-файла и {@link FxmlWindow} 
 * @author User
 *
 */
@SuppressWarnings("unchecked")
public class FxmlWindowBridge {

	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	@InternalElement
	/** Зарегистированные обработчики соединения контроллера и окна */
	public IExtendedList<IFunction2NoR<FxmlWindow, Object>> registeredConntectHandlers = CollectionsSWL.createExtendedList();
	
	/** Конструктор */
	public FxmlWindowBridge()
	{
		registerDefaultHandlers();
	}
	
	/** Соединить окно и контроллер */
	public void connect(FxmlWindow window, Object controller)
	{
		registeredConntectHandlers.dataStream().each((e) -> {
			logger.safe(() -> {
				e.apply(window, controller);
			}, "Error while applying conntection handler", e, "to window", window, "and controller", controller);
		});
	}
	
	/** Зарегистрировать обработчик соединения окна и контроллера */
	public <T extends FxmlWindowBridge> T registerHandler(IFunction2NoR<FxmlWindow, Object> fun)
	{
		registeredConntectHandlers.addExclusive(fun);
		return (T) this;
	}
	
	/** Зарегистрировать стандартные обработчики соединения окна и контроллера */
	public void registerDefaultHandlers()
	{
		registerHandler(new InjectJfxConntectionHandler());
		registerHandler(new OnClickConnectionHandler());
	}
	
}
