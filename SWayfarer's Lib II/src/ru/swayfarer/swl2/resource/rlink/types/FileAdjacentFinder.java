package ru.swayfarer.swl2.resource.rlink.types;

import java.net.URL;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction3;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.resource.rlink.RLUtils;
import ru.swayfarer.swl2.resource.rlink.ResourceLink;

/**
 * Искалка соседних URL для файлов
 * @author swayfarer
 *
 */

public class FileAdjacentFinder implements IFunction3<URL, IFunction1<ResourceLink, Boolean>, Boolean, IExtendedList<ResourceLink>>{

	public static ILogger logger = LoggingManager.getLogger();
	
	@Override
	public IExtendedList<ResourceLink> apply(URL url, IFunction1<ResourceLink, Boolean> filter, Boolean isDeep)
	{
		return logger.safeReturn(() -> {
			
			if (url.getProtocol().equals("file"))
			{
				FileSWL file = FileSWL.ofURL(url);
				
				if (file != null)
				{
					file = file.getParentFile();
					
					if (file == null)
					{
						logger.warning("Parent file is null!");
						return null;
					}
					
					file = file.canonize();
					
					IExtendedList<ResourceLink> ret = CollectionsSWL.createExtendedList();
					
					addFileAdjacents(file, isDeep, ret);
					
					if (!ret.isEmpty())
						return ret;
				}
			}
			
			return null;
		}, null, "Error while getting adjacents for url", url);
	}
	
	public static IExtendedList<ResourceLink> addFileAdjacents(FileSWL file, boolean isDeep, IExtendedList<ResourceLink> list)
	{
		if (list == null)
			list = CollectionsSWL.createExtendedList();
		
		for (FileSWL subfile : file.getSubfiles())
		{
			if (isDeep)
			{
				addFileAdjacents(subfile, isDeep, list);
			}
			
			list.add(subfile.toRlink());
		}
		
		return list;
	}

}
