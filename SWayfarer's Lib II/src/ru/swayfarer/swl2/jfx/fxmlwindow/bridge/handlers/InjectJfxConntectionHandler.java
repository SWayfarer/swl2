package ru.swayfarer.swl2.jfx.fxmlwindow.bridge.handlers;

import java.lang.reflect.Field;
import java.util.Map;

import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.jfx.fxmlwindow.FxmlWindow;
import ru.swayfarer.swl2.jfx.fxmlwindow.bridge.InjectionJfx;
import ru.swayfarer.swl2.jfx.wrappers.IFxmlWrapper;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Обработчик соединения контроллера fxml и {@link FxmlWindow} 
 * <br> Иньектит элементы fxml-файла в поля, отмеченные {@link InjectionJfx} 
 * @author swayfarer
 *
 */
public class InjectJfxConntectionHandler implements IFunction2NoR<FxmlWindow, Object> {

	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	@Override
	public void applyNoR(FxmlWindow window, Object controller)
	{
		ExceptionsUtils.IfNullArg(controller, "Controller can't be null!");
		// Карта имя - поле для полей контроллера 
		Map<String, Field> controllerFields = ReflectionUtils.getAccessibleFields(controller);
		
		ReflectionUtils.forEachField(window, (windowField, instance, windowFieldOldValue) -> {
			
			InjectionJfx injectionJfx = windowField.getAnnotation(InjectionJfx.class);
			
			if (injectionJfx != null)
			{
				String fieldName = injectionJfx.name();
				
				if (StringUtils.isEmpty(fieldName))
					fieldName = windowField.getName();
				
				Field controllerField = controllerFields.get(fieldName);
				
				if (controllerField != null)
				{
					// Нельзя заменять значения, которые уже заданы
					if (windowFieldOldValue != null)
					{
						logger.warning("Skiping window's", window, "field", windowField, "because it's already has value!");
					}
					else
					{
						logger.safe(() -> {
							
							Class<?> windowFieldType = windowField.getType();
							
							Object windowFieldNewValue = controllerField.get(controller);
							
							// Если там раппер, то оборачиваем в него 
							if (IFxmlWrapper.class.isAssignableFrom(windowFieldType))
							{
								windowFieldNewValue = ReflectionUtils.newInstanceOf(windowFieldType, windowFieldNewValue);
							}
							
							windowField.set(instance, windowFieldNewValue);
							
						}, "Error while injecting controller's", controller, "field", controllerField, "to window's", window, "field", windowField);
					}
				}
			}
		});
	}
}
