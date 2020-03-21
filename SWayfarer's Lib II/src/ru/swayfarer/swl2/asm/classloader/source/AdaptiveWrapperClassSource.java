package ru.swayfarer.swl2.asm.classloader.source;

import java.net.URL;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.string.StringUtils;

public class AdaptiveWrapperClassSource extends URLClassSource{

	public IExtendedList<ClassLoader> loaders = CollectionsSWL.createExtendedList();
	
	public AdaptiveWrapperClassSource()
	{
		urlSource = (s) -> {
			
			for (int i1 = 0; i1 < 2; i1 ++)
			{
				String name = s;
				
				for (ClassLoader loader : loaders)
				{
					URL url = loader.getResource(name);
					
					if (url != null)
						return url;
				}
				
				if (i1 > 0)
					break;
				
				try
				{
					if (name.contains("/") || name.endsWith(".class"))
					{
						name = name.replace("/", ".");
						name = StringUtils.subString(0, -6, name);
					}
					
					Class<?> cl = Class.forName(name);
					ClassLoader loader = cl.getClassLoader();
					
					if (loader != null)
					{
						loaders.add(loader);
					}
					else
					{
						break;
					}
				}
				catch (Throwable e)
				{
					break;
				}
			}
			
			return null;
			
		};
	}

}
