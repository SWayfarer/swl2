package ru.swayfarer.swl2.jfx.events;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1NoR;

/**
 * Утилиты для работы с Jfx-событиями
 * @author swayfarer
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class JfxEvents {

	/** Добавить дополнительный обработчик клика мышкой */
	public static <T extends AdditionalHandler<MouseEvent>> T addClickHandler(Parent parent, IFunction1NoR<? super MouseEvent> fun)
	{
		EventHandler<? super MouseEvent> clickHandler = parent.getOnMouseClicked();
		
		if (clickHandler == null)
			clickHandler = new AdditionalHandler<MouseEvent>(null).addHandler(fun);
		else if (clickHandler instanceof AdditionalHandler)
		{
			AdditionalHandler additionalHandler = (AdditionalHandler) clickHandler;
			additionalHandler.addHandler(fun);
		}
		
		return (T) clickHandler;
	}
	
}
