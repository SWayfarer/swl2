package ru.swayfarer.swl2.jfx.fxmlwindow;

import java.util.List;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.resource.rlink.RLUtils;
import ru.swayfarer.swl2.resource.rlink.ResourceLink;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Искалка fxml-файлов 
 * @author swayfarer
 *
 */
public class FxmlFileFinder {

	public static ILogger logger = LoggingManager.getLogger();
	
	/** Возможные папки с fxml-файлами */
	@InternalElement
	public static List<String> fxmlFolderPatters = CollectionsSWL.createExtendedList(
			"/assets/fxml/", 
			"/res/fxml/", 
			"/fxml/"
	);

	/** Возможные шаблоны для имен fxml-файлов  */
	@InternalElement
	public static List<String> fxmlFilePatterns = CollectionsSWL.createExtendedList(
			"%lname%", 
			"%name%", 
			"gui%name%",
			"gui_%lname%",
			"gui_%name%", 
			"%lname%_gui"
	);

	/** Возможные расширения для fxml-файлов  */
	@InternalElement
	public static List<String> fxmlFileExtensions = CollectionsSWL.createExtendedList(
			".fxml"
	);
	
	/** Найти {@link ResourceLink} fxml-файла по его имени */
	public static ResourceLink findFxmlPathByName(String name)
	{
		String lowerCaseName = name.toLowerCase();
		
		ResourceLink ret;
		
		if ((ret = findFxmlPath(name, lowerCaseName)) != null)
		{
			return ret;
		}
		else if (lowerCaseName.endsWith("gui") && name.length() > 3)
		{
			name = StringUtils.subString(0, -3, name);
			
			lowerCaseName = name.toLowerCase();
			
			ret = findFxmlPath(name, lowerCaseName);
			
			if (ret != null)
			{
				return ret;
			}
		}
		else if (lowerCaseName.startsWith("gui") && name.length() > 3)
		{
			ret = findFxmlPath(name.substring(3), lowerCaseName.substring(3));
			
			if (ret != null)
			{
				return ret;
			}
		}
		
		return null;
	}
	
	/** Найти {@link ResourceLink} fxml-файла по его имени */
	@InternalElement
	public static ResourceLink findFxmlPath(String name, String lowerCaseName)
	{
		List<String> filePatterns = CollectionsSWL.createExtendedList(fxmlFilePatterns.size());
		List<String> folderPatters = CollectionsSWL.createExtendedList(fxmlFolderPatters.size());
		
		for (String pattern : fxmlFilePatterns)
			filePatterns.add(pattern.replace("%name%", name).replace("%lname%", lowerCaseName));
		
		for (String pattern : fxmlFolderPatters)
			folderPatters.add(pattern.replace("%name%", name).replace("%lname%", lowerCaseName));
		
		return findFxmlPath(StringUtils.unite(folderPatters, filePatterns, fxmlFileExtensions).toArray());
	}

	/** Найти {@link ResourceLink} fxml-файла по набору возможных расположений */
	@InternalElement
	public static ResourceLink findFxmlPath(Object... objects)
	{
		for (Object obj : objects)
		{
			ResourceLink link = RLUtils.createLink(obj+"");
			
			if (link.isExists())
			{
				return link;
			}
		}
		
		return null;
	}

}
