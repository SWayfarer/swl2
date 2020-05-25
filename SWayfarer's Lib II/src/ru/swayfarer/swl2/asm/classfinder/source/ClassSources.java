package ru.swayfarer.swl2.asm.classfinder.source;

import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import lombok.var;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.collections.extended.IExtendedMap;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction0;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.resource.streams.BytesInputStreamSWL;
import ru.swayfarer.swl2.resource.streams.DataInputStreamSWL;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Источники .class-ресурсов
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class ClassSources {

	/**
	 * Функция, возвращающая результат работы дочерних функций
	 * @author swayfarer
	 *
	 */
	public static class ComplexSourceFun implements IFunction1<String, IExtendedMap<String, IFunction0<DataInputStreamSWL>>> {

		/** Дочерние функции */
		@InternalElement
		public IExtendedList<IFunction1<String, IExtendedMap<String, IFunction0<DataInputStreamSWL>>>> funs = CollectionsSWL.createExtendedList();
		
		/** Добавить дочернюю функцию */
		public <T extends ComplexSourceFun> T addFun(IFunction1<String, IExtendedMap<String, IFunction0<DataInputStreamSWL>>> fun)
		{
			funs.add(fun);
			return (T) this;
		}
		
		/** Применить функцию */
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
	}
	
	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/**
	 * Получить папку, в которой находится default package (с пустым именем) для класс-файла, лежащего в определенном пакете
	 * @param pkg Пакет, в котором находится class-файл
	 * @param classFile Class-файл, для которого ищется расположение default package 
	 * @return Папка, в которой находится default package
	 */
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
	
	/**
	 * Зарегистрированные функции, возвращающие генераторы .class-ресурсов по URL класса, для которого ищестся ресурс <br>
	 * Для обеспечения работы {@link #ofClassSource(Class)}
	 */
	@InternalElement
	public IExtendedList<IFunction1<URL, IFunction1<String, IExtendedMap<String, IFunction0<DataInputStreamSWL>>>>> registeredClassStreamCreators = CollectionsSWL.createExtendedList();
	
	/** Конструктор */
	public ClassSources()
	{
		registerDefaultStreamCreators();
	}
	
	/**
	 * Получить функцию, генерирующую информацию о классах в указанном пакете, где ключ - имя класса, а значение - функция-генератор потока его байт
	 * @param cl Класс, лежащий в источнике классов, в котором будет производиться поиск
	 * @return Требуемая функция
	 */
	public IFunction1<String, IExtendedMap<String, IFunction0<DataInputStreamSWL>>> ofClassSource(Class<?> cl)
	{
		return (s) -> classesOfClassSource(s, cl);
	}
	
	/**
	 * Получить карту, где ключ - имя класса, а значение - генератор потока данных(его байт) для него 
	 * из источника, в котором находится указанный класс
	 * @param pkg Пакет, для которого производится поиск классов 
	 * @param cl Класс, который является частью сканируемого источника данных
	 * @return Искомая карта
	 */
	public IExtendedMap<String, IFunction0<DataInputStreamSWL>> classesOfClassSource(String pkg, Class<?> cl)
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
	
	/**
	 * Получить карту, где ключ - имя класса, а значение - генератор потока данных(его байт) для него 
	 * @param pkg Пакет, для которого производится поиск
	 * @param jarFile Jar-файл, в котором производится поиск
	 * @return Искомая карта
	 */
	public IExtendedMap<String, IFunction0<DataInputStreamSWL>> classesOfJar(String pkg, FileSWL jarFile)
	{
		return classesOfJar(pkg, jarFile.toZipFile());
	}
	
	/**
	 * Получить карту, где ключ - имя класса, а значение - генератор потока данных(его байт) для него 
	 * @param pkg Пакет, для которого производится поиск
	 * @param jarFile Jar-файл, в котором производится поиск
	 * @return Искомая карта
	 */
	public IExtendedMap<String, IFunction0<DataInputStreamSWL>> classesOfJar(String pkg, ZipFile jarFile)
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
	
	/**
	 * Функция, возвращающая информацию о классах внутри classpath
	 */
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
	
	/**
	 * Получить функцию, генерирующую информацию о классах в указанном пакете, где ключ - имя класса, а значение - функция-генератор потока его байт
	 * @param rootDir Директория, в котором будет производиться поиск классов
	 * @return Требуемая функция
	 */
	public IFunction1<String, IExtendedMap<String, IFunction0<DataInputStreamSWL>>> ofDirectory(FileSWL rootDir)
	{
		return (s) -> ofDirectory(s, rootDir);
	}
	
	/**
	 * Получить информацию о классах в пакете 
	 * @param pkg Пакет, для которого производится поиск
	 * @param rootDir Директория, которая ассоциируется с default package
	 * @return Карта, где ключ - имя класса, а значение - функция, генерирующая поток данных(его байт) для него
	 */
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


	/**
	 * Получить функцию, генерирующую информацию о классах в указанном пакете, где ключ - имя класса, а значение - функция-генератор потока его байт
	 * @param jarFile Jar-файл, в котором будет производиться поиск классов
	 * @return Требуемая функция
	 */
	public IFunction1<String, IExtendedMap<String, IFunction0<DataInputStreamSWL>>> ofJar(FileSWL jarFile)
	{
		return (s) -> classesOfJar(s, jarFile);
	}
	
	/**
	 * Получить функцию, генерирующую информацию о классах в указанном пакете, где ключ - имя класса, а значение - функция-генератор потока его байт
	 * @param jarFile Jar-файл, в котором будет производиться поиск классов
	 * @return Требуемая функция
	 */
	public IFunction1<String, IExtendedMap<String, IFunction0<DataInputStreamSWL>>> ofJar(ZipFile jarFile)
	{
		return (s) -> classesOfJar(s, jarFile);
	}
	
	/**
	 * Регистрация стандартных источков .class-ресурсов по URL класса в одном из них
	 * @return Этот объект (this)
	 */
	@InternalElement
	public <T extends ClassSources> T registerDefaultStreamCreators()
	{
		// Генератор ресурсов для классов, которые находятся в виде отдельных файлов внутри некой папки
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
		
		// Генератор ресурсов для классов, находящихся внутри jar-архива
		registerStreamCreator((url) -> {
			return logger.safeReturn(() -> {
				if (url.getProtocol().equals("jar"))
				{
					URLConnection connection = logger.safeReturn(url::openConnection);
					
					if (connection != null && connection instanceof JarURLConnection)
					{
						JarURLConnection jarURLConnection = (JarURLConnection) connection;
						JarFile jarFile = jarURLConnection.getJarFile();
						
						return (pkg) -> classesOfJar(pkg, jarFile);
					}
				}
				
				return null;
				
			}, null, "Error while getting jarfile of url", url);
		});
		
		return (T) this;
	}
	
	/**
	 * Зарегистрировать источник .class-ресурсов
	 * @param fun Функция, возвращающая генератор ресурсов для источника, частью которого был указанный URL
	 * @return Этот объект (this)
	 */
	public <T extends ClassSources> T registerStreamCreator(IFunction1<URL, IFunction1<String, IExtendedMap<String, IFunction0<DataInputStreamSWL>>>> fun)
	{
		registeredClassStreamCreators.addExclusive(fun);
		return (T) this;
	}
}
