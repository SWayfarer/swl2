package ru.swayfarer.swl2.resource.rlink;

import java.io.InputStream;
import java.net.URL;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.resource.streams.DataInputStreamSWL;

/**
 * Ссылка на ресурс
 * <br> Мой удобный способ быстро обратиться к ресурсу
 * @author swayfarer
 */
public class ResourceLink {
 
	/** Тип ресурса */
	@InternalElement
	public ResourceType type;
	
	/** Текст ссылки */
	@InternalElement
	public String content;
	
	/** Конструктор */
	public ResourceLink(ResourceType type, String content)
	{
		super();
		ExceptionsUtils.IfNull(type, IllegalArgumentException.class, "The resource type can't be null!");
		ExceptionsUtils.IfEmpty(content, IllegalArgumentException.class, "The resource type can't be null!");
		
		this.type = type;
		this.content = content;
	}
	
	public <T> T getSource()
	{
		return type.getSource(this);
	}
	
	/** Существует ли? */
	public boolean isExists()
	{
		return type.isExists(this);
	}

	/** Статический метод-фабрика */
	public static ResourceLink of(String path, ResourceType type)
	{
		return new ResourceLink(type, path);
	}
	
	/** Получить источник ресурса */
	public <T> T toSource()
	{
		return type.getSource(this);
	}
	
	/** Получить поток данных из ресурса */
	public DataInputStreamSWL toStream()
	{
		InputStream stream = type.getStream(this);
		
		return stream == null ? null : DataInputStreamSWL.of(stream);
	}
	
	/** Получить {@link URL} из ресурса */
	public URL toURL()
	{
		return type.getURL(this);
	}
	
	/** Получить ресурс как файл*/
	public FileSWL toFile()
	{
		return type.getFile(this);
	}
	
	/** Получить соседние ссылки */
	public IExtendedList<ResourceLink> getAdjacents()
	{
		return type.getAdjacentLinks(this, false, (f) -> true);
	}
	
	/** Получить соседние ссылки */
	public IExtendedList<ResourceLink> getAdjacents(boolean isDeep)
	{
		IExtendedList<ResourceLink> ret = type.getAdjacentLinks(this, isDeep, (f) -> true);
		return ret == null ? CollectionsSWL.createExtendedList() : ret;
	}
	
	/** Получить соседние ссылки */
	public IExtendedList<ResourceLink> getAdjacents(boolean isDeep, IFunction1<ResourceLink, Boolean> filter)
	{
		return type.getAdjacentLinks(this, isDeep, filter);
	}

	@Override
	public String toString()
	{
		return "ResourceLink [type=" + type + ", content=" + content + "]";
	}
}
