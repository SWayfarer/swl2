package ru.swayfarer.swl2.resource.rlink;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import ru.swayfarer.swl2.collections.streams.DataStream;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.reference.IReference;
import ru.swayfarer.swl2.reference.ParameterizedReference;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.resource.pathtransformers.PathTransforms;
import ru.swayfarer.swl2.resource.rlink.types.ClassPathResourceType;
import ru.swayfarer.swl2.resource.rlink.types.FileResourceType;
import ru.swayfarer.swl2.resource.rlink.types.UrlResourceType;
import ru.swayfarer.swl2.resource.streams.DataInputStreamSWL;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Утилиты для работы с {@link ResourceLink}
 * @author swayfarer
 *
 */
public class RLUtils {

	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Зарегистрированные типы ресурсов относительно их префиксов */
	public static Map<String, ResourceType> registeredResourceTypes = new HashMap<>();
	
	/** Создать ссылку */
	public static ResourceLink file(String path)
	{
		return createLink("f:" + path);
	}
	
	public static <T> T toSource(@ConcattedString Object... text)
	{
		ResourceLink resourceLink = createLink(StringUtils.concat(text));
		
		return resourceLink == null ? null : resourceLink.getSource();
	}
	
	public static String toSingleString(@ConcattedString Object... path)
	{
		ResourceLink rlink = createLink(StringUtils.concat(path));
		return rlink == null ? null : rlink.toSingleString();
	}
	
	/** Создать ссылку */
	public static ResourceLink file(File file)
	{
		if (file == null)
			return null;
		
		return file(file.getAbsolutePath());
	}
	
	public static boolean exists(@ConcattedString Object... text)
	{
		ResourceLink resourceLink = createLink(StringUtils.concat(text));
		
		return resourceLink != null ? resourceLink.isExists() : false;
	}
	
	/** Создать ссылку */
	public static ResourceLink url(String path)
	{
		return createLink("u:" + path);
	}
	
	/** Прочитать все байты ресурса */
	public static byte[] toBytes(@ConcattedString Object... text)
	{
		DataInputStreamSWL stream = toStream(text);
		
		return stream == null ? null : stream.readAllSafe();
	}
	
	/** Получить поток ресурса */
	public static DataInputStreamSWL toStream(@ConcattedString Object... text)
	{
		ResourceLink rlink = createLink(StringUtils.concat(text));
		
		return rlink == null ? null : rlink.toStream();
	}
	
	/** Получить ресурс в том же пакете, что и класс, откуда вызван метод */
	public static ResourceLink pkg(@ConcattedString Object... text)
	{
		ResourceLink rlink = createLink("/" + ExceptionsUtils.getPackageAt(1).replace(".", "/") + "/" + StringUtils.concat(text));
		return rlink;
	}
	
	/** Создать ссылку */
	public static ResourceLink createLink(String path)
	{
		if (StringUtils.isEmpty(path))
			return null;
		
		String prefix = getPathPrefix(path);
		
		ResourceType resourceType = null;
		
		if (StringUtils.isEmpty(prefix))
			resourceType = new ClassPathResourceType();
		else
		{
			resourceType = registeredResourceTypes.get(prefix);
			path = path.substring(prefix.length());
		}
		
		ExceptionsUtils.IfNull(resourceType, IllegalArgumentException.class, "The path must contain a registered prefix! Please, register prefix", prefix, "with regiterResourceType method! ");
		
		path = PathTransforms.transform(path);
		
		return ResourceLink.of(path, resourceType);
	}
	
	public static FileSWL getSourceFile(String searchedResource)
	{
		try
		{
			if (StringUtils.isEmpty(searchedResource))
				return null;
			
			StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
			StackTraceElement last = stackTraceElements[stackTraceElements.length - 1];
			
			Class<?> cl = Class.forName(last.getClassName());
			
			searchedResource = PathTransforms.fixSlashes(searchedResource);
			
			if (searchedResource.startsWith("/"))
				searchedResource = searchedResource.substring(1);
			
			if (searchedResource.endsWith("/"))
				searchedResource = searchedResource.substring(0, searchedResource.length() - 1);
			
			URL url = cl.getClassLoader().getResource(searchedResource);
			
			return getSourceFile(searchedResource, url);
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while searching resource", searchedResource);
		}
		
		return null;
	}
	
	public static boolean exists(ResourceLink rlink)
	{
		return rlink != null && rlink.isExists();
	}
	
	public static FileSWL getSourceFile(String searchedResource, URL url)
	{
		try
		{
			String protocol = url.getProtocol();
			String path = url.getPath();
			
			switch (protocol)
			{
				case "jar":
				{
					return new FileSWL(path.substring(0, path.indexOf("!")));
				}
				case "file":
				{
					FileSWL file = new FileSWL(path);
					
					searchedResource = PathTransforms.fixSlashes(searchedResource);
					
					if (searchedResource.startsWith("/"))
						searchedResource = searchedResource.substring(1);
					
					if (searchedResource.endsWith("/"))
						searchedResource = searchedResource.substring(0, searchedResource.length() - 1);
					
					int backSteps = StringUtils.countMatches( searchedResource, "/") + 1;
					
					while (backSteps > 0 && file != null)
					{
						file = file.getParentFile();
						backSteps --;
					}
					
					return file;
				}
			}
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while source from", url, "!");
		}
		
		return null;
	}
	
	/** Получить префикс из ссылки */
	@InternalElement
	public static String getPathPrefix(String path)
	{
		if (StringUtils.isEmpty(path))
			return null;
		
		int index = path.indexOf(":");
		
		return index > 0 ? path.substring(0, index + 1) : null;
	}
	
	/** Зарегистрировать источник ресурсов */
	public static boolean registerResourceType(ResourceType type, @ConcattedString Object... name)
	{
		ExceptionsUtils.IfNull(type, IllegalArgumentException.class, "Resource type can't be null!");
		ExceptionsUtils.IfEmpty(name, IllegalArgumentException.class, "Resource type registered name can't be empty or null!");
		
		String s = StringUtils.concat(name) + ":";
		
		if (registeredResourceTypes.containsKey(s))
			return false;
		
		type.lastRegisteredPrefix = s;
		registeredResourceTypes.put(s, type);
		
		return true;
	}
	
	/** Получить префикс ресурса */
	public static String getResourcePrefix(ResourceType type)
	{
		IReference<String> prefix = new ParameterizedReference<>();
		
		DataStream.of(registeredResourceTypes.entrySet())
			.each((e) -> {
				if (e.getValue().equals(type))
					prefix.setValue(e.getKey());
					
			});
		
		return prefix.getValue();
	}
	
	/** Регистрация стандартных типов ресурсов */
	public static void registerDefaultTypes()
	{
		try
		{
			registerResourceType(new FileResourceType(), "f");
			registerResourceType(new UrlResourceType(), "u");
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while registering default resource types!");
		}
	}
	
	/** Блок, выполняемый при загрузке класса */
	static
	{
		registerDefaultTypes();
	}
}
