package ru.swayfarer.swl2.resource.rlink.types;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.resource.rlink.ResourceLink;
import ru.swayfarer.swl2.resource.rlink.ResourceType;

/**
 * Файловый тип ресурса 
 * <br> Проще говоря, файл
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class FileResourceType extends ResourceType {
	
	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Получить источник */
	@Override
	public <T> T getSource(ResourceLink rlink)
	{
		File file = new File(rlink.content);
		return file.exists() ? (T) file : null;
	}

	/** Существует ли? */
	@Override
	public boolean isExists(ResourceLink rlink)
	{
		return new File(rlink.content).exists();
	}

	/** Получить входящий поток */
	@Override
	public <T extends InputStream> T getInputStream(ResourceLink rlink)
	{
		File file = new File(rlink.content);
		
		if (!file.exists())
			return null;
		
		try
		{
			return (T) new FileInputStream(file);
		}
		catch (FileNotFoundException e)
		{
			logger.error(e, "Error while creating stream for file", file.getAbsolutePath());
		}
		
		return null;
	}

	/** Получить файл */
	@Override
	public FileSWL getFile(ResourceLink rlink)
	{
		return new FileSWL(rlink.content);
	}

	/** Получить URL */
	@Override
	public URL getURL(ResourceLink rlink)
	{
		File file = getFile(rlink);
		
		if (file == null)
			return null;
		
		try
		{
			return file.toURI().toURL();
		}
		catch (IOException e)
		{
			logger.error(e, "Error while getting url from file", file);
		}
		
		return null;
	}

	/** Получить ссылки на соседние файлы */
	@Override
	public IExtendedList<ResourceLink> getAdjacentLinks(ResourceLink rlink, boolean isDeep, IFunction1<ResourceLink, Boolean> filter)
	{
		IExtendedList<ResourceLink> ret = CollectionsSWL.createExtendedList();
		
		rlink.toFile().getParentFile().getSubfiles().forEach((file) -> FileAdjacentFinder.addFileAdjacents(file, isDeep, ret));
		
		return ret;
	}

	@Override
	public OutputStream getOutStream(ResourceLink rlink)
	{
		return rlink != null ? rlink.toFile().toOutputStream() : null;
	}

	@Override
	public boolean isWritable(ResourceLink rlink)
	{
		return rlink != null;
	}

}
