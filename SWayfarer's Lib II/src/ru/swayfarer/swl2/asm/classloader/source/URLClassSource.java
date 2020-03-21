package ru.swayfarer.swl2.asm.classloader.source;

import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.security.cert.Certificate;
import java.util.jar.JarFile;

import ru.swayfarer.swl2.asm.classloader.ClassLoaderSWL.IClassInfoSource;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.resource.streams.StreamsUtils;

/**
 * Источник информации о классах, достающий ее по их URL 
 * @author swayfarer
 */
public class URLClassSource implements IClassInfoSource {

	/** Логгер */
	@InternalElement
	public ILogger logger = LoggingManager.getLogger();
	
	/** Источник */
	public IFunction1<String, URL> urlSource = (name) -> {
		logger.warning("URL source fun was not setted! Returning null...");
		return null;
	};
	
	/** Получить байты класса */
	@Override
	public byte[] getClassBytes(String name)
	{
		name = name.replace(".", "/") + ".class";
		
		URL source = urlSource.apply(name);
		
		if (source != null)
		{
			try
			{
				return StreamsUtils.readAllAndClose(source.openStream());
			}
			catch (Throwable e)
			{
				logger.error(e, "Error while getting class bytes for", name);
			}
		}
		
		return null;
	}

	/** Получить источник класса */
	@Override
	public CodeSource getClassCodeSource(String name)
	{
		name = name.replace(".", "/") + ".class";
		
		URL source = urlSource.apply(name);
		
		if (source != null)
		{
			try
			{
				URLConnection connection = source.openConnection();
				
				if (connection != null)
				{
					if (connection instanceof JarURLConnection)
					{
						JarFile jarFile = ((JarURLConnection) connection).getJarFile();
						
						if (jarFile != null)
						{
							JarURLConnection connection2 = (JarURLConnection) connection;
							
							CodeSigner[] h = jarFile.getJarEntry(name).getCodeSigners();
							
							return new CodeSource(connection2.getJarFileURL(), h);
						}
					}
				}
			}
			catch (Throwable e)
			{
				logger.error(e, "Error while getting code source for", name);
			}
		}
		
		return new CodeSource(source, (Certificate[]) null);
	}
	
	/** Получить источник классов из {@link URLClassLoader'а} */
	public static URLClassSource valueOf(URLClassLoader urlClassLoader)
	{
		URLClassSource ret = new URLClassSource();
		ret.urlSource = (name) -> urlClassLoader.findResource(name);
		
		return ret;
	}
	
	/** Получить источник классов, обращающийся к другому класслоадеру */
	public static URLClassSource wrapClassloaders(ClassLoader... loaders)
	{
		URLClassSource ret = new URLClassSource();
		
		ret.urlSource = (name) -> {
			
			for (ClassLoader loader : loaders)
			{
				URL url = loader.getResource(name);
				
				if (url != null)
					return url;
			}
			
			return null;
			
		};
		return ret;
	}

}

