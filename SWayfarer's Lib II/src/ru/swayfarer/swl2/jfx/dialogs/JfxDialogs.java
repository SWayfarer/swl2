package ru.swayfarer.swl2.jfx.dialogs;

import java.util.List;
import java.util.Optional;

import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2;
import ru.swayfarer.swl2.jfx.config.JfxLocale;
import ru.swayfarer.swl2.jfx.css.CssManager;
import ru.swayfarer.swl2.jfx.scene.controls.validation.TextPropertyValidators;
import ru.swayfarer.swl2.jfx.utils.JfxUtils;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Утилиты для работы с Jfx-диалогами
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class JfxDialogs {

	/** Получить double, введенную в диалог */
	public static double getInputDouble(String title, String context, double defaultValue)
	{
		String s = getInputString(title, context, TextPropertyValidators.DOUBLE_VALIDATOR);
		
		if (s == null)
			return defaultValue;
		
		return Double.valueOf(s);
	}
	
	/** Получить float, введенную в диалог */
	public static float getInputFloat(String title, String context, float defaultValue)
	{
		String s = getInputString(title, context, TextPropertyValidators.FLOAT_VALIDATOR);
		
		if (s == null)
			return defaultValue;
		
		return Float.valueOf(s);
	}
	
	/** Получить int, введенную в диалог */
	public static int getInputInteger(String title, String context, int defaultValue)
	{
		return getInputInteger(title, context, 0, defaultValue);
	}
	
	/** Получить int, введенную в диалог */
	public static int getInputInteger(String title, String context, int initialValue, int defaultValue)
	{
		String s = getInputString(title, context, initialValue+"", TextPropertyValidators.INTEGER_VALIDATOR);
		
		if (s == null)
			return defaultValue;
		
		return Integer.valueOf(s);
	}
	
	/** Получить int, введенную в диалог */
	public static String getInputString(String title, String context)
	{
		return getInputString(title, context, "");
	}

	/** Получить строку, введенную в диалог */
	public static String getInputString(String title, String context, IFunction2<String, String, Boolean>... validators)
	{
		return getInputString(title, context, "", validators);
	}
	
	/** Получить строку, введенную в диалог */
	public static String getInputString(String title, String context, String defaultValue, IFunction2<String, String, Boolean>... validators)
	{
		return getInputString(title, "", context, defaultValue, null, validators);
	}
	
	/** Получить строку, введенную в диалог */
	public static String getInputString(String title, String context, String defaultValue)
	{
		return getInputString(title, "", context, defaultValue, null);
	}
	
	/** Получить строку, введенную в диалог */
	public static String getInputString(String title, String label, String context, String defaultValue, List<String> css, IFunction2<String, String, Boolean>... validators)
	{
		TextInputDialog dialog = new TextInputDialog(defaultValue);
		 
		dialog.setTitle(title);
		dialog.setHeaderText(label);
		dialog.setContentText(context);
		 
		DialogPane scene = dialog.getDialogPane();
		
		CssManager.instance.addScene(scene.getScene());
		
		if (css != null)
			scene.getStylesheets().addAll(css);
		
		GridPane grid = (GridPane) CollectionsSWL.getLastElement(scene.getChildren());
		
		final TextField field = (TextField) grid.getChildren().get(1);
		
		TextPropertyValidators.validateBy(field, validators);
		
		Optional<String> result = dialog.showAndWait();
		
		return result == null ? null : result.isPresent() ? result.get() : null;
	}
	
	/** Получить файл для сохранения */
	public static FileSWL getSaveFile(String filterName, String initialDir, String... extensions)
	{
		return getSaveFile(new Stage(), filterName, initialDir, extensions);
	}
	
	/** Получить файл для сохранения */
	public static FileSWL getSaveFile(Stage stage, String filterName, String initialDir, String... extensions)
	{
		return getFile(false, stage, filterName, initialDir, extensions);
	}
	
	/** Получить файл для открытия */
	public static FileSWL getOpenFile(String filterName, String initialDir, String... extensions)
	{
		return getOpenFile(new Stage(), filterName, initialDir, extensions);
	}
	
	/** Получить файл для открытия */
	public static FileSWL getOpenFile(Stage stage, String filterName, String initialDir, String... extensions)
	{
		return getFile(true, stage, filterName, initialDir, extensions);
	}
	
	/** Получить папку для открытия */
	public static FileSWL getDirectory(Stage stage)
	{
		DirectoryChooser chooser = new DirectoryChooser();
		
		chooser.setTitle("Выберите папку");
		
		return FileSWL.of(chooser.showDialog(stage));
	}
	
	/** Получить файл для открытия */
	@InternalElement
	public static FileSWL getFile(boolean isOpen, Stage stage, String filterName, String initialDir, String... extensions)
	{
		FileChooser chooser = new FileChooser();
		
		ExceptionsUtils.safe(() -> {
			if (!StringUtils.isEmpty(initialDir))
				chooser.setInitialDirectory(new FileSWL(initialDir));
		});
		
		if (!(extensions == null || extensions.length == 0 || StringUtils.isEmpty(filterName)))
			chooser.setSelectedExtensionFilter(new ExtensionFilter(filterName, extensions));
		
		return FileSWL.of(isOpen ? chooser.showOpenDialog(stage) : chooser.showSaveDialog(stage));
	}
	
	/** Подтверждено ли действие из диалога? */
	public static boolean isConfirmed(String title, String context, Stage stage, List<String> css)
	{
		return message(AlertType.CONFIRMATION, title, context, stage, css).getResult() == ButtonType.OK;
	}
	
	/** Отобразить сообщение с информацией */
	public static void displayInfoMessage(String text, Stage parent)
	{
		message(AlertType.INFORMATION, null, text, parent, null);
	}
	
	/** Отобразить сообщение с ошибкой */
	public static void displayErrorMessage(String text, Stage parent)
	{
		message(AlertType.ERROR, null, text, parent, null);
	}

	/** Отобразить сообщение с предупреждением */
	public static void displayWarningMessage(String text, Stage parent)
	{
		message(AlertType.WARNING, null, text, parent, null);
	}
	
	/** Отобразить сообщение */
	@InternalElement
	public static Alert message(AlertType type, String headerText, String contentText, Stage parent, List<String> css)
	{
		Alert alert = new Alert(type);
		
		if (parent != null)
			try
			{
				alert.initOwner(parent);
			}
			catch (Throwable e){}
        
		DialogPane scene = alert.getDialogPane();
		
		if (css != null)
			scene.getStylesheets().addAll(css);
		
		CssManager.instance.apply(scene.getScene());
		
		String title = null;
		
		switch (type)
		{
			case WARNING:
				title = JfxLocale.instance.warningDialogTitle.get();
				break;
			case ERROR:
				title = JfxLocale.instance.errorDialogTitle.get();
				break;
			default:
				title = JfxLocale.instance.infoDialogTitle.get();
				break;
		}

        alert.setResizable(true);
        
        for (Node node : alert.getDialogPane().getChildren())
        {
        	if (node instanceof Label)
        	{
        		((Label) node).setMinWidth(Region.USE_PREF_SIZE);
        		((Label) node).setMinHeight(Region.USE_PREF_SIZE);
        	}
        }
        
        scene.setMinWidth(Region.USE_PREF_SIZE);
        scene.setMinHeight(Region.USE_PREF_SIZE);
        alert.setWidth(scene.getWidth());
        
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        
        Rectangle2D ret = JfxUtils.getScreenResolution();
        
        alert.setX(ret.getMaxX() / 2 - alert.getWidth() / 2);
        alert.setY(ret.getMaxY() / 2 - scene.getPrefHeight() / 2 - 25);

        alert.showAndWait();
        
        return alert;
	}
	
}
