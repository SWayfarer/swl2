package ru.swayfarer.swl2.asm.classfinder.source;

import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import lombok.experimental.var;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.collections.extended.IExtendedMap;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction0;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.resource.streams.BytesInputStreamSWL;
import ru.swayfarer.swl2.resource.streams.DataInputStreamSWL;
import ru.swayfarer.swl2.string.StringUtils;

@SuppressWarnings("unchecked")
public class ClassSources {

	public static ILogger logger = LoggingManager.getLogger();
	
	public IExtendedList<IFunction1<URL, IFunction1<String, IExtendedMap<String, IFunction0<DataInputStreamSWL>>>>> registeredClassStreamCreators = CollectionsSWL.createExtendedList();
	
	public ClassSources()
	{
		registerDefaultStreamCreators();
	}
	
	public <T extends ClassSources> T registerDefaultStreamCreators()
	{
		registerStreamCreator((url) -> {
			
			if (url.getProtocol().equals("file"))
			{
				FileSWL fileByUrl = FileSWL.ofURL(url);
				
				ClassInfo classInfo = ClassInfo.valueOf(fileByUrl.getData());
				String targetClassName = classInfo.getName();
				int lastIndexOfSlash = targetClassName.lastIndexOf("/");
				
				String targetClassPackage = lastIndexOfSlash > 0 ? targetClassName.substring(0, lastIndexOfSlash).replace("/", ".") : "";
				
				logger.info("Target class package", targetClassPackage);
				
				return (pkg) -> ofDirectory(pkg, getClassfileRoot(targetClassPackage, fileByUrl));
			}
			
			return null;
		});
		
		registerStreamCreator((url) -> {
		
			if (url.getProtocol().equals("jar"))
			{
				URLConnection connection = logger.safeReturn(url::openConnection);
				
				if (connection != null && connection instanceof JarURLConnection)
				{
					JarURLConnection jarURLConnection = (JarURLConnection) connection;
					
					FileSWL file = FileSWL.ofURL(jarURLConnection.getJarFileURL());
					
					return (pkg) -> sourceOfJar(pkg, file);
				}
			}
			
			return null;
		});
		
		return (T) this;
	}
	
	public <T extends ClassSources> T registerStreamCreator(IFunction1<URL, IFunction1<String, IExtendedMap<String, IFunction0<DataInputStreamSWL>>>> fun)
	{
		registeredClassStreamCreators.addExclusive(fun);
		return (T) this;
	}
	
	public IExtendedMap<String, IFunction0<DataInputStreamSWL>> ofClassSource(String pkg, Class<?> cl)
	{
		URL location = cl.getClassLoader().getResource(cl.getName().replace(".", "/") + ".class");
		
		for (var streamCreationFun : registeredClassStreamCreators)
		{
			IFunction1<String, IExtendedMap<String, IFunction0<DataInputStreamSWL>>> result = streamCreationFun.apply(location);
			
			if (result != null)
				return result.apply(pkg);
		}
		
		return null;
	}
	
	public static FileSWL getClassfileRoot(String pkg, FileSWL classFile)
	{
		int backsCount = StringUtils.countMatches(pkg, ".") + 2;
		
		FileSWL rootDir = classFile;
		
		while (rootDir != null && backsCount > 0)
		{
			rootDir = rootDir.getParentFile();
			
			if (rootDir == null)
				return null;
			
			backsCount --;
		}
		
		return rootDir;
	}
	
	public IExtendedMap<String, IFunction0<DataInputStreamSWL>> ofDirectory(String pkg, FileSWL rootDir)
	{
		IExtendedMap<String, IFunction0<DataInputStreamSWL>> ret = CollectionsSWL.createExtendedMap();
		
		IExtendedList<FileSWL> subFiles = rootDir.getAllSubfiles();
		
		String start = pkg.replace(".", "/");
		
		for (FileSWL file : subFiles)
		{
			String path = file.getLocalPath(rootDir);
			
			if (file.isFile() && path.startsWith(start) && file.getName().endsWith(".class"))
			{
				String cleanName = path.replace("/", ".");
				cleanName = StringUtils.subString(0, -6, cleanName);
				
				ret.put(cleanName, file::toInputStream);
			}
		}
		
		return ret;
	}
	
	public IFunction1<String, IExtendedMap<String, IFunction0<DataInputStreamSWL>>> ofClassSource(Class<?> cl)
	{
		return (s) -> ofClassSource(s, cl);
	}
	
	public IFunction1<String, IExtendedMap<String, IFunction0<DataInputStreamSWL>>> ofJar(FileSWL jarFile)
	{
		return (s) -> sourceOfJar(s, jarFile);
	}
	
	public IFunction1<String, IExtendedMap<String, IFunction0<DataInputStreamSWL>>> ofJar(ZipFile jarFile)
	{
		return (s) -> sourceOfJar(s, jarFile);
	}
	
	public IFunction1<String, IExtendedMap<String, IFunction0<DataInputStreamSWL>>> ofDirectory(FileSWL rootDir)
	{
		return (s) -> ofDirectory(s, rootDir);
	}
	
	public static IExtendedMap<String, IFunction0<DataInputStreamSWL>> sourceOfJar(String pkg, FileSWL jarFile)
	{
		return sourceOfJar(pkg, jarFile.toZipFile());
	}
	
	public static IExtendedMap<String, IFunction0<DataInputStreamSWL>> sourceOfJar(String pkg, ZipFile jarFile)
	{
		return logger.safeReturn(() -> {
			
			IExtendedMap<String, IFunction0<DataInputStreamSWL>> ret = CollectionsSWL.createExtendedMap();
			
			String start = pkg.replace(".", "/");
			
			ZipFile file = jarFile;
			
			if (file == null)
				return null;
			
			Enumeration<? extends ZipEntry> entries = file.entries();
			
			while (entries.hasMoreElements())
			{
				ZipEntry entry = entries.nextElement();
				
				String entryName = entry.getName();
				
				if (entryName.startsWith(start) && entryName.endsWith(".class"))
				{
					String clearName = StringUtils.subString(0, -6, entryName).replace("/", ".");
					
					DataInputStreamSWL dis = DataInputStreamSWL.of(file.getInputStream(entry));
					byte[] bytes = dis.readAll();
					ret.put(clearName, () -> BytesInputStreamSWL.createStream(bytes));
				}
			}
			
			logger.safe(file::close);
			
			return ret;
		
		}, null, "Error while creating source from jarfile", jarFile, "with package", pkg);
	}
	
	public ComplexSourceFun ofClasspath()
	{
		IExtendedList<String> classPath = ReflectionUtils.getClasspath();
		
		ComplexSourceFun ret = new ComplexSourceFun();
		
		classPath.each((elem) -> {
			
			FileSWL file = new FileSWL(elem);
			
			IFunction1<String, IExtendedMap<String, IFunction0<DataInputStreamSWL>>> fun = null;
			
			if (file.isDirectory())
			{
				fun = ofDirectory(file);
			}
			else
			{
				fun = ofJar(file);
			}
			
			ret.addFun(fun);
		});
		
		return ret;
	}
	
	public static class ComplexSourceFun implements IFunction1<String, IExtendedMap<String, IFunction0<DataInputStreamSWL>>> {

		public IExtendedList<IFunction1<String, IExtendedMap<String, IFunction0<DataInputStreamSWL>>>> funs = CollectionsSWL.createExtendedList();
		
		@Override
		public IExtendedMap<String, IFunction0<DataInputStreamSWL>> apply(String pkg)
		{
			IExtendedMap<String, IFunction0<DataInputStreamSWL>> ret = null;
			
			for (var fun : funs)
			{
				IExtendedMap<String, IFunction0<DataInputStreamSWL>> current = fun.apply(pkg);
				
				if (!CollectionsSWL.isNullOrEmpty(current))
				{
					if (ret == null)
						ret = CollectionsSWL.createExtendedMap();
					
					ret.putAll(current);
				}
			}
			
			return ret;
		}
		
		public <T extends ComplexSourceFun> T addFun(IFunction1<String, IExtendedMap<String, IFunction0<DataInputStreamSWL>>> fun)
		{
			funs.add(fun);
			return (T) this;
		}
	}
}
