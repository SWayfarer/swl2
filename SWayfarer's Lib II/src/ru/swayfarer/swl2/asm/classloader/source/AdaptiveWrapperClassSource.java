package ru.swayfarer.swl2.asm.classloader.source;

import java.net.URL;

import ru.swayfarer.swl2.asm.classloader.ClassLoaderSWL;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Адаптивный источник информации о классах для {@link ClassLoaderSWL}, основанный на вытягивании источников из родительского {@link ClassLoader}'а <br> <br>
 * 
 * При поиске нового ресурса источник сначала обойдет все зарегистрированные в нем {@link ClassLoader}'ы, и если в них не найдется ресурса для нужного класса, то он будет загружен и его класслоадер будет добавлен к остальным
 * 
 * @author swayfarer
 *
 */
public class AdaptiveWrapperClassSource extends URLClassSource{

	/** Родительские {@link ClassLoader}'ы*/
	@InternalElement
	public IExtendedList<ClassLoader> loaders = CollectionsSWL.createExtendedList();
	
	/**
	 * Конструктор
	 */
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
