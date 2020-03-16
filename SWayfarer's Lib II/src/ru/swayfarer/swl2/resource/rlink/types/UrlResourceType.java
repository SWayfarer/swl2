package ru.swayfarer.swl2.resource.rlink.types;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction3;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.resource.rlink.ResourceLink;
import ru.swayfarer.swl2.resource.rlink.ResourceType;

/**
 * Тип ресурсов, расположенных по URL 
 * @author swayfarer
 */
@SuppressWarnings("unchecked")
public class UrlResourceType extends ResourceType{

	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Список генераторов подресурсов по {@link URL} */
	@InternalElement
	public static IExtendedList<IFunction3<URL, IFunction1<ResourceLink, Boolean>, Boolean, IExtendedList<ResourceLink>>> adjacentFinder = CollectionsSWL.createExtendedList();

	/** Получить {@link URL} из {@link ResourceLink} */
	public URL rlinkToURL(ResourceLink rlink)
	{
		if (rlink == null)
			return null;
		
		try
		{
			return new URL(rlink.content);
		}
		catch (IOException e)
		{
			logger.error(e, "Error while creating url from", rlink);
		}
		
		return null;
	}
	
	/** Получить источник ресурса */
	@Override
	public <T> T getSource(ResourceLink rlink)
	{
		return (T) rlinkToURL(rlink);
	}

	/** Существует ли ресурс? */
	@Override
	public boolean isExists(ResourceLink rlink)
	{
		URL url = rlinkToURL(rlink);
		
		if (url == null)
			return false;
		
		try (InputStream is = url.openStream())
		{
			return is != null;
		}
		catch (FileNotFoundException e)
		{
			return false;
		}
		catch (IOException e)
		{
			logger.error(e, "Error while checking url", url, "of rlink", rlink, "for exixsting!");
		}
		
		
		return false;
	}

	/** Получить поток данных из ресурса */
	@Override
	public <T extends InputStream> T getInputStream(ResourceLink rlink)
	{
		URL url = rlinkToURL(rlink);
		
		if (url == null)
			return null;
		
		try
		{
			return (T) url.openStream();
		}
		catch (IOException e)
		{
			logger.error(e, "Error while getting stream from url", url, "of rlink", rlink);
		}
		
		return null;
	}

	/** Получить файл из ресурса */
	@Override
	public FileSWL getFile(ResourceLink rlink)
	{
		URL url = rlinkToURL(rlink);
		
		if (url == null)
			return null;
		
		try
		{
			return new FileSWL(url.toURI());
		}
		catch (URISyntaxException e)
		{
			logger.error(e, "Error while getting file from url", url);
		}
		
		return null;
	}

	/** Получить URL из ресурса */
	@Override
	public URL getURL(ResourceLink rlink)
	{
		return rlinkToURL(rlink);
	}

	/** Получить подресурсы, удовлетворяющие фильтру */
	@Override
	public IExtendedList<ResourceLink> getAdjacentLinks(ResourceLink rlink, boolean isDeep, IFunction1<ResourceLink, Boolean> filter)
	{
		URL url = rlinkToURL(rlink);
		
		if (url == null)
			return null;
		
		IExtendedList<ResourceLink> ret = CollectionsSWL.createExtendedList();
		
		for (IFunction3<URL, IFunction1<ResourceLink, Boolean>, Boolean, IExtendedList<ResourceLink>> generator : adjacentFinder)
		{
			ret = generator.apply(url, filter, isDeep);
			
			if (ret != null && !ret.isEmpty())
				break;
		}
		
		return ret;
	}
	
	/** Зарегистрировать генератор подресурсов */
	public static void registerAdjacentFinder(IFunction3<URL, IFunction1<ResourceLink, Boolean>, Boolean, IExtendedList<ResourceLink>> generator)
	{
		adjacentFinder.addExclusive(generator);
	}
	
	/** Регистрация */
	public static void registerDefaultAdjacentFinders()
	{
		registerAdjacentFinder(new ZipAdjacentFinder());
		registerAdjacentFinder(new FileAdjacentFinder());
	}

	static {
		
		registerDefaultAdjacentFinders();
		
	}

	@Override
	public OutputStream getOutStream(ResourceLink rlink)
	{
		return null;
	}

	@Override
	public boolean isWritable(ResourceLink rlink)
	{
		return false;
	}
}
