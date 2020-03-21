package ru.swayfarer.swl2.jfx.fxmlwindow.bridge.handlers;

import java.lang.reflect.Method;
import java.util.Map;

import javafx.scene.control.Control;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.jfx.events.JfxEvents;
import ru.swayfarer.swl2.jfx.fxmlwindow.FxmlWindow;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Обработчик соединения контроллера fxml и {@link FxmlWindow} 
 * <br> Позволяет писать методы-обработчики событий, вроде onBtnSomethingClicked для элемента btnSomething
 * @author swayfarer
 *
 */
public class OnClickConnectionHandler implements IFunction2NoR<FxmlWindow, Object> {

	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	@Override
	public void applyNoR(FxmlWindow window, Object controller)
	{
		Map<String, Method> window0ArgsMethods = ReflectionUtils.getAccessibleMethods(window)
				.dataStream()
				.filter((e) -> e.getParameterCount() <= 1)
				.toMap((e) -> e.getName(), (e) -> e);
		
		ReflectionUtils.forEachField(window, (field, instance, value) -> {
			
			Class<?> fieldType = field.getType();
			
			if (Control.class.isAssignableFrom(fieldType))
			{
				String onClickMethodName = StringUtils.toCamelCase(false, "on", field.getName(), "clicked");
				
				Method onClickMethod = window0ArgsMethods.get(onClickMethodName);
				
				if (onClickMethod != null)
				{
					Control control = (Control) value;
					
					JfxEvents.addClickHandler(control, (event) -> {
						
						logger.safe(() -> {
							if (onClickMethod.getParameterCount() == 1)
								onClickMethod.invoke(window, event);
							else
								onClickMethod.invoke(window);
						}, "Error while invoking on click event handling by mehtod", onClickMethod);
						
					});
					
				}
			}
		});
	}

}
