package ru.swayfarer.swl2.resource.rlink;

import java.io.InputStream;
import java.net.URL;

import ru.swayfarer.swl2.binary.buffers.DynamicByteBuffer;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.resource.streams.DataInputStreamSWL;
import ru.swayfarer.swl2.resource.streams.StreamsUtils;
import ru.swayfarer.swl2.string.StringUtils;

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
	
	/** Получить строку в формате rlink */
	public String toRlinkString()
	{
		String last = type.getLastRegisteredPrefix();
		
		if (StringUtils.isEmpty(last))
			return content;
		
		return last + ":" + content;
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
	
	/** Получить все байты ресурса */
	public byte[] toBytes()
	{
		DataInputStreamSWL dataInputStreamSWL = toStream();
		
		return dataInputStreamSWL == null ? null : dataInputStreamSWL.readAllSafe();
	}
	
	/** Прочитать ресурс в буффер */
	public DynamicByteBuffer toByteBuffer()
	{
		return toByteBuffer(DynamicByteBuffer.allocateDirect());
	}
	
	/** Прочитать ресурс как одну строку */
	public String toSingleString()
	{
		DataInputStreamSWL dis = toStream();
		
		return dis == null ? null : dis.readAllAsUtf8();
	}
	
	/** Прочитать ресурс в буффер */
	public DynamicByteBuffer toByteBuffer(DynamicByteBuffer buf)
	{
		DataInputStreamSWL dataInputStreamSWL = toStream();
		
		if (dataInputStreamSWL == null)
			return null;
		
		if (buf == null)
			buf = DynamicByteBuffer.allocateDirect(dataInputStreamSWL.availableSafe());
		
		StreamsUtils.copyStreamSafe(dataInputStreamSWL, buf.toOutputStream(), true, true);
		
		return buf;
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
