package ru.swayfarer.swl2.resource.rlink;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import ru.swayfarer.swl2.binary.buffers.DynamicByteBuffer;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.resource.streams.DataInputStreamSWL;
import ru.swayfarer.swl2.resource.streams.DataOutputStreamSWL;
import ru.swayfarer.swl2.resource.streams.StreamsUtils;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Ссылка на ресурс
 * <br> Мой удобный способ быстро обратиться к ресурсу
 * @author swayfarer
 */
public class ResourceLink {
 
	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
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
		this.content = unwrapPkg(content);
	}
	
	public String unwrapPkg(String str)
	{
		if (str.startsWith("pkg:"))
		{
			IExtendedList<StackTraceElement> st = ExceptionsUtils.getThreadStacktrace(1);
			
			StackTraceElement element = st.getFirstElement();
			
			String pkg = getClass().getPackage().getName();
			
			for (StackTraceElement elem : st)
			{
				if (!elem.getClassName().startsWith(pkg))
				{
					element = elem;
					break;
				}
			}
			
			ExceptionsUtils.IfNull(element, IllegalStateException.class, "Caller class must not be in", pkg);
			
			String elemName = "/" + ExceptionsUtils.getClassPackage(element.getClassName()).replace(".", "/");
			
			return elemName + "/" + str.substring(4);
		}
		
		return str;
	}
	
	public String getSimpleNameWithoutExtension()
	{
		String name = getSimpleName();
		String extension = getExtension();
		
		if (extension.isEmpty())
			return name;
		
		return name.substring(0, name.length() - extension.length() - 1);
	}
	
	public static String getResourceParentPath(String resourceName)
	{
		String link = resourceName;
		int indexOfSlash = link.lastIndexOf("/");
		
		if (link.length() - 1 == indexOfSlash)
		{
			String tmp = StringUtils.subString(0, -1, link);
			
			indexOfSlash = tmp.lastIndexOf("/");
		}
		
		if (indexOfSlash > 0)
		{
			return link.substring(0, indexOfSlash);
		}
		
		return link;
	}
	
	public static String getExtension(String resourceName)
	{
		String name = getSimpleName(resourceName);
		
		String ret = "";
		
		char[] chars = name.toCharArray();
		
		for (char ch : chars)
		{
			if (ch == '.')
			{
				ret = "";
			}
			else
			{
				ret += ch;
			}
		}
		
		if (ret.equalsIgnoreCase(name))
			return "";
		
		return ret;
	}
	
	public static String getSimpleName(String resourceName)
	{
		String link = resourceName;
		int indexOfSlash = link.lastIndexOf("/");
		
		if (link.length() - 1 == indexOfSlash)
		{
			String tmp = StringUtils.subString(0, -1, link);
			
			indexOfSlash = tmp.lastIndexOf("/");
		}
		
		if (indexOfSlash > 0)
		{
			return StringUtils.subString(indexOfSlash + 1, 0, link);
		}
		
		return link;
	}
	
	/** Получить расширение файла */
	public String getExtension()
	{
		String name = getSimpleName();
		
		String ret = "";
		
		char[] chars = name.toCharArray();
		
		for (char ch : chars)
		{
			if (ch == '.')
			{
				ret = "";
			}
			else
			{
				ret += ch;
			}
		}
		
		if (ret.equalsIgnoreCase(name))
			return "";
		
		return ret;
	}
	
	public String getSimpleName()
	{
		String link = content;
		int indexOfSlash = content.lastIndexOf("/");
		
		if (link.length() - 1 == indexOfSlash)
		{
			String tmp = StringUtils.subString(0, -1, link);
			
			indexOfSlash = tmp.lastIndexOf("/");
		}
		
		if (indexOfSlash > 0)
		{
			return StringUtils.subString(indexOfSlash + 1, 0, link);
		}
		
		return link;
	}
	
	public String getParentName()
	{
		String link = content;
		int indexOfSlash = content.lastIndexOf("/");
		
		if (link.length() - 1 == indexOfSlash)
		{
			String tmp = StringUtils.subString(0, -1, link);
			
			indexOfSlash = tmp.lastIndexOf("/");
		}
		
		if (indexOfSlash > 0)
		{
			return link.substring(0, indexOfSlash);
		}
		
		return link;
	}
	
	/** Получить {@link BufferedImage} */
	public BufferedImage toImage()
	{
		return logger.safeReturn(() -> {
			return ImageIO.read(toStream());
		}, null, "Error while getting image from", this);
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
		InputStream stream = type.getInputStream(this);
		
		return stream == null ? null : DataInputStreamSWL.of(stream);
	}
	
	/** Получить поток для записи данных */
	public DataOutputStreamSWL toOutStream()
	{
		java.io.OutputStream stream = type.getOutStream(this);
		
		return stream == null ? null : DataOutputStreamSWL.of(stream);
	}
	
	/** Можно ли записывать в ресурс? */
	public boolean isWritable()
	{
		return type.isWritable(this);
	}
	
	/** Получить строку в формате rlink */
	public String toRlinkString()
	{
		String last = type.getLastRegisteredPrefix();
		
		if (StringUtils.isEmpty(last))
			return content;
		
		return last + content;
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
	
	/** Прочитать ресурс как одну строку */
	public String toSingleString(String encoding)
	{
		DataInputStreamSWL dis = toStream();
		
		return dis == null ? null : dis.readAllAsString(encoding);
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
