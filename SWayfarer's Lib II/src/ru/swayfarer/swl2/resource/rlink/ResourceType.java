package ru.swayfarer.swl2.resource.rlink;

import java.io.InputStream;
import java.net.URL;

import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Тип ресурсов. Позволяет взаимодействовать с ресурсами из {@link ResourceLink}
 * @author swayfarer
 *
 */
public abstract class ResourceType {

	/** Имя источника ресурсов */
	@InternalElement
	public String name;
	
	/**
	 * Констуктор, выбирающий в качестве имени источника имя класса
	 */
	public ResourceType()
	{
		name = getClass().getName();
	}
	
	/** Конструктор */
	public ResourceType(@ConcattedString Object... name)
	{
		String s = StringUtils.concat(name);
		
		if (s == null || s.isEmpty())
		{
			this.name = getClass().getName();
		}
	}
	
	/** Получить имя источника ресурсов */
	public String getName()
	{
		return name;
	}
	
	/** Получить файл из ресурса */
	public abstract FileSWL getFile(ResourceLink rlink);
	
	/** Получить URL из ресурса */
	public abstract URL getURL(ResourceLink rlink);
	
	/** Получить источник ресурса */
	public abstract <T> T getSource(ResourceLink rlink);
	
	/** Существует ли ресурс? */
	public abstract boolean isExists(ResourceLink rlink);
	
	/** Получить поток данных из ресурса */
	public abstract <T extends InputStream> T getStream(ResourceLink rlink);
	
	/** Получить соседние ресурсы  */
	public abstract IExtendedList<ResourceLink> getAdjacentLinks(ResourceLink rlink, boolean isDeep, IFunction1<ResourceLink, Boolean> filter);
	
}
