package ru.swayfarer.swl2.resource.rlink.types;

import java.net.URL;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction3;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.resource.rlink.RLUtils;
import ru.swayfarer.swl2.resource.rlink.ResourceLink;

public class FileAdjacentFinder implements IFunction3<URL, IFunction1<ResourceLink, Boolean>, Boolean, IExtendedList<ResourceLink>>{

	@Override
	public IExtendedList<ResourceLink> apply(URL url, IFunction1<ResourceLink, Boolean> filter, Boolean isDeep)
	{
		if (url.getProtocol().equals("file"))
		{
			FileSWL file = new FileSWL(url.getPath());
			
			if (file.exists())
			{
				file = file.getParentFile();
				
				if (file == null)
					return null;
				
				IExtendedList<ResourceLink> ret = CollectionsSWL.createExtendedList();
				
				addFileAdjacents(file, isDeep, ret);
				
				if (!ret.isEmpty())
					return ret;
			}
		}
		return null;
	}
	
	public static IExtendedList<ResourceLink> addFileAdjacents(FileSWL file, boolean isDeep, IExtendedList<ResourceLink> list)
	{
		if (list == null)
			list = CollectionsSWL.createExtendedList();
		
		list.add(RLUtils.file(file));
		
		if (isDeep && file.isDirectory())
		{
			for (FileSWL subfile : file.getSubfiles())
			{
				addFileAdjacents(subfile, isDeep, list);
			}
		}
		
		return list;
	}

}
