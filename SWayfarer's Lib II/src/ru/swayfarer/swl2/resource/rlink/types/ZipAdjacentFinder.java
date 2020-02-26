package ru.swayfarer.swl2.resource.rlink.types;

import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction3;
import ru.swayfarer.swl2.resource.rlink.RLUtils;
import ru.swayfarer.swl2.resource.rlink.ResourceLink;

public class ZipAdjacentFinder implements IFunction3<URL, IFunction1<ResourceLink, Boolean>, Boolean, IExtendedList<ResourceLink>>{

	@Override
	public IExtendedList<ResourceLink> apply(URL url, IFunction1<ResourceLink, Boolean> filter, Boolean isDeep)
	{
		try
		{
			if (url.getProtocol().equals("jar"))
			{
				
				IExtendedList<ResourceLink> ret = CollectionsSWL.createExtendedList();
				
				JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
				
				JarFile jarFile = jarURLConnection.getJarFile();
				
				String resourcePath = url.getPath();
				
				int resStartIndex = resourcePath.indexOf("!/") + 2;
				
				if (resStartIndex >= resourcePath.length())
					return null;
				
				String filePath = resourcePath.substring(0, resStartIndex);
				
				resourcePath = resourcePath.substring(resStartIndex);
				
				int slashIndex = resourcePath.lastIndexOf("/");
				
				if (slashIndex > 0)
					resourcePath = resourcePath.substring(0, slashIndex);
				else
					return null;
				
				Enumeration<JarEntry> entries = jarFile.entries();
				
				while (entries.hasMoreElements())
				{
					JarEntry entry  = entries.nextElement();
					
					String entryName = entry.getName();
					
					if (entryName.startsWith(resourcePath))
					{
						entryName = entryName.replace(resourcePath + "/", "");
						
						slashIndex = entryName.indexOf("/");
						
						if (isDeep || slashIndex < 0)
						{
							ResourceLink rlink = RLUtils.createLink("u:"+"jar:"+filePath+"!/"+entry.getName());
							
							if (filter.apply(rlink))
							{
								ret.add(rlink);
							}
						}
					}
				}
				
				if (!ret.isEmpty())
					return ret;
			}

		}
		catch (Throwable e)
		{
			System.err.println("Error while getting children for " + url);
			e.printStackTrace();
		}
		
		return null;
	}

}
