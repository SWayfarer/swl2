package ru.swayfarer.swl2.jfx.controls.validation;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;
import ru.swayfarer.swl2.equals.EqualsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Валидаторы текста 
 * @author swayfarer
 *
 */
public class TextPropertyValidators {

	/** Валидатор int'ов */
	public static IFunction2<String, String, Boolean> INTEGER_VALIDATOR = (oldValue, newValue) -> {
		return oldValue != null && newValue != null && (EqualsUtils.objectEqualsSome(newValue, "", "-") || newValue.matches("-[0-9]") || (oldValue.isEmpty() && newValue.equals("-")) || StringUtils.isInteger(newValue));
	};
	
	/** Валидатор float'ов */
	public static IFunction2<String, String, Boolean> FLOAT_VALIDATOR = (oldValue, newValue) -> {
		return oldValue != null && newValue != null && (EqualsUtils.objectEqualsSome(newValue, "", "-") || newValue.matches("-[0-9]") || (oldValue.isEmpty() && newValue.equals("-")) || StringUtils.isFloat(newValue));
	};
	
	/** Валидатор double'ов */
	public static IFunction2<String, String, Boolean> DOUBLE_VALIDATOR = (oldValue, newValue) -> {
		return oldValue != null && newValue != null && (EqualsUtils.objectEqualsSome(newValue, "", "-") || newValue.matches("-[0-9]") || (oldValue.isEmpty() && newValue.equals("-")) || StringUtils.isDouble(newValue));
	};
	
	/** Валидатор имен файлов */
	public static IFunction2<String, String, Boolean> FILENAME_VALIDATOR = (oldValue, newValue) -> {
		return oldValue != null && newValue != null && (newValue.isEmpty() || !(newValue.contains("/") || newValue.contains("\\")));
	};
	
	/** Вызов валидации текстовой проперти */
	@SuppressWarnings("unchecked")
	public static StringProperty validateBy(StringProperty property, IFunction2<String, String, Boolean>... validators)
	{
		if (property == null)
			return null;
		
		property.addListener((obs, oldValue, newValue) -> {
			
			for (IFunction2<String, String, Boolean> validator : validators)
			{
				if (!validator.apply(oldValue, newValue))
					property.setValue(oldValue);
			}
		});
		
		return property;
	}
	
	/** Вызов валидации текстового поля */
	@SuppressWarnings("unchecked")
	public static TextField validateBy(TextField textField, IFunction2<String, String, Boolean>... validators)
	{
		if (textField == null)
			return null;
		
		validateBy(textField.textProperty(), validators);
		return textField;
	}

}
