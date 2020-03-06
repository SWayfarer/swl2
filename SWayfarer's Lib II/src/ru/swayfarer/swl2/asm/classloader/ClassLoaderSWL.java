package ru.swayfarer.swl2.asm.classloader;

import java.io.File;
import java.lang.reflect.ReflectPermission;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.security.AllPermission;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.security.Permissions;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Manifest;

import ru.swayfarer.swl2.asm.IClassTransformer;
import ru.swayfarer.swl2.asm.TransformedClassInfo;
import ru.swayfarer.swl2.asm.classloader.source.URLClassSource;
import ru.swayfarer.swl2.asm.transformer.dump.DumpClassTransformer;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.Alias;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.resource.rlink.RLUtils;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Класслоадер, который загружает классы, трансформируя их.
 * 
 * @author SWayfarer
 */

@SuppressWarnings("unchecked")
public class ClassLoaderSWL extends URLClassLoader {

	/** Карта с уже загруженными классами, где ключ - имя класса */
	@InternalElement
	public Map<String, Class<?>> loadedClasses = new HashMap<>();

	/**
	 * Самое вкусное. Лист с трансформерами, которые будут применены к классам
	 */
	@InternalElement
	public IExtendedList<IClassTransformer> transformers = CollectionsSWL.createExtendedList();

	/** Исключения, которые будут загружены стандартным загрузчиком */
	@InternalElement
	public IExtendedList<String> systemExclusions = CollectionsSWL.createExtendedList("java.");

	/** Исключения, которые будут загружены стандартным загрузчиком */
	@InternalElement
	public IExtendedList<String> exclusions = CollectionsSWL.createExtendedList();

	/** Исключения, которые будут принудительно загружены этим загрузчиком */
	@InternalElement
	public IExtendedList<String> inclusions = CollectionsSWL.createExtendedList();

	/** Показывать ли эксепшены, вылетающие при загрузке классов */
	@InternalElement
	public boolean showDefineStacktrace = false;

	/**
	 *  Источник информации о классе, из которого будет браться необходимая для загрузки информация
	 * <br> Позволяет изменить источник на любой понравившийся
	 */
	@InternalElement
	public IClassInfoSource classInfoSource = URLClassSource.valueOf(this);
	
	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Задефайненные пакеты */
	@InternalElement
	public IExtendedList<String> definedPackages = CollectionsSWL.createExtendedList();
	
	/** Родитель */
	@InternalElement
	public ClassLoader parent;

	/** Конструктор */
	public ClassLoaderSWL()
	{
		this(ClassLoaderSWL.class.getClassLoader());
	}

	/** Конструктор c указанием родительского класслоадера */
	public ClassLoaderSWL(ClassLoader parent)
	{
		super(new URL[] {});
		
		this.parent = parent;

		if (parent instanceof URLClassLoader)
		{
			URL[] urls = ((URLClassLoader) parent).getURLs();
			
			for (URL url : urls)
			{
				addURL(url);
			}
		}
		else
		{
			wrapParent();
		}
	}
	
	/** Добавить дамп классов по маске имени в директорию */
	@Alias("registerDump")
	public <T extends ClassLoaderSWL> T addDump(String classInternalNameMask, String dumpDir)
	{
		return registerDump(classInternalNameMask, dumpDir);
	}
	
	/** Добавить дамп классов по маске имени в директорию */
	public <T extends ClassLoaderSWL> T registerDump(String classInternalNameMask, String dumpDir)
	{
		addTransformer(new DumpClassTransformer(classInternalNameMask, dumpDir));
		return (T) this;
	}
	
	/** Использовать родительский класслоадер в качестве {@link #classInfoSource} */
	public <T extends ClassLoaderSWL> T wrapParent()
	{
		classInfoSource = URLClassSource.wrapClassloader(parent);
		return (T) this;
	}
	
	/** Получить {@link URL}'ы из класслоадера, который ими пользуется */
	@InternalElement
	public static List<URL> getUrls(ClassLoader classLoader)
	{
		if (classLoader instanceof URLClassLoader)
		{
			return CollectionsSWL.createExtendedList(((URLClassLoader) classLoader).getURLs());
		}

//		// jdk9 +
//		if (classLoader.getClass().getName().startsWith("jdk.internal.loader.ClassLoaders"))
//		{
//			try
//			{
//				return UnsafeUtils.getField(UnsafeUtils.<Object>getField(classLoader, "ucp"), "path");
//			}
//			catch (Exception e)
//			{
//				e.printStackTrace();
//				return null;
//			}
//		}
		return null;
	}
	
	/** 
	 * Начать новую цепочку действий в указанном методе класса с указанными аргументами.
	 * <br> Метод должен содержать параметер {@link List}<{@link String}>, в который будут помещены переданные аргументы 
	 * <br> Смотри {@link #startAt(String, String, Object...)}
	 */
	public void startAt(Class<?> cl, String methodname, String... args)
	{
		startAt(cl.getName(), methodname, (Object[]) args);
	}
	
	/**
	 * 
	 * Создает объект, дочерние объекты которого будут загружены через этот
	 * ClassLoader.
	 * <h1>Все хуки и трансформеры регистрируются ВНЕ этих объектов, иначе не
	 * заработают</h1>
	 * <h1></h1>
	 * 
	 * @param className
	 *            Путь до класса, объект которого будет создан
	 * @param methodname
	 *            Имя метода, который будет вызван. Метод должен принимать один
	 *            аргумент типа List<String>.
	 * @param args
	 *            Аргументы для старта
	 */
	public void startAt(String className, String methodname, Object... args)
	{
		Thread currentThread = Thread.currentThread();
		ClassLoader context = currentThread.getContextClassLoader();
		try
		{
			Object obj = this.findClass(className).getConstructor().newInstance();
			
			ReflectionUtils.invokeMethod(obj, methodname, Arrays.asList(args));
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}

		currentThread.setContextClassLoader(context);
	}

	/** Класс не должен быть загружен этим ClassLoader'ом? */
	@InternalElement
	public boolean isExclusion(String name)
	{
		if (StringUtils.isStringStartsWith(name, inclusions.toArray()))
			return false;
		
		return     (StringUtils.isStringStartsWith(name, systemExclusions.toArray()) 
				|| (StringUtils.isStringStartsWith(name, exclusions.toArray())));
	}

	/** Трансформация класса */
	@InternalElement
	public byte[] transform(String name, byte[] bytes, TransformedClassInfo transformedClassInfo)
	{
		for (IClassTransformer transformer : transformers)
		{
			try
			{
				bytes = transformer.transform(name, bytes, transformedClassInfo);
				name = transformedClassInfo.name;
			}
			catch (Throwable e)
			{
				e.printStackTrace();
			}
		}

		return bytes;
	}

	/**
	 * Добавит пакеты, которые будут принудительно включены в загрузку в обход
	 * исключений
	 */
	public void addInclusions(String... inclusions)
	{
		this.inclusions.addAll(CollectionsSWL.createExtendedList(inclusions));
	}

	/** Добавить исключения, которые будут загружены класслоадером-родителем */
	public void addExclusions(String... exclusions)
	{
		this.exclusions.addAll(CollectionsSWL.createExtendedList(exclusions));
	}

	/** Добавить трансформер классов */
	public <T extends ClassLoaderSWL> T addTransformer(IClassTransformer transformer)
	{
		if (!transformers.contains(transformer))
			transformers.add(transformer);
		
		return (T) this;
	}

	public Class<?> loadClass(Class<?> cl) throws ClassNotFoundException
	{
		return loadClass(cl.getCanonicalName());
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException
	{

		if (isExclusion(name))
			return parent.loadClass(name);

		return findClass(name);
	}

	@Override
	public Class<?> findClass(String name) throws ClassNotFoundException
	{
		
		if (loadedClasses.containsKey(name))
		{
			return loadedClasses.get(name);
		}

		Class<?> ret = null;

		if (!isExclusion(name))
		{
			try
			{
				byte[] bytes = classInfoSource.getClassBytes(name);

				if (bytes != null)
				{
					TransformedClassInfo transformedClassInfo = new TransformedClassInfo();
					transformedClassInfo.name = name;

					bytes = transform(name, bytes, transformedClassInfo);

					CodeSource source = classInfoSource.getClassCodeSource(name);

					ExceptionsUtils.safe(() -> {
						
						String pkgName = transformedClassInfo.name;
						
						if (pkgName.contains("."))
						{
							pkgName = pkgName.substring(0, pkgName.lastIndexOf("."));
							Package pkg = getPackage(pkgName);
							
							if (pkg != null)
								return;
							
							URLConnection connection = source.getLocation().openConnection();
							
							if (connection instanceof JarURLConnection)
							{
								Manifest man = ((JarURLConnection) connection).getManifest();
								definePackage(pkgName, man, source.getLocation());
							}
							else
							{
								definePackage(pkgName, null, null, null, null, null, null, null);
							}
						}
					});
					
					ret = defineClass(transformedClassInfo.name, bytes, 0, bytes.length, getDomain(source));
				}
				else
				{
					logger.warning("Resource not found for class", name, "! Falling back to default classloader!");
				}
			}
			catch (Throwable e)
			{
				if (showDefineStacktrace)
					logger.warning(e, "Error while defining class", name, "! Falling back to default loader!");
				else
				{
					logger.warning("Error while defining class " + name + "! Falling back to default loader!");
				}
			}
		}

		if (ret == null)
			ret = parent.loadClass(name);

		loadedClasses.put(name, ret);

		return ret;
	}
	
	private ProtectionDomain getDomain(CodeSource source)
	{
		return new ProtectionDomain(source, getPermissions());
	}

	private Permissions getPermissions()
	{
		Permissions permissions = new Permissions();
		permissions.add(new AllPermission());
		permissions.add(new ReflectPermission("suppressAccessChecks"));
		return permissions;
	}

	@Override
	public void addURL(URL url)
	{
		super.addURL(url);
	}

	public boolean closeSafe()
	{
		try
		{
			close();

			return true;
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			//logger.printThrowable(e, "Error while closing classloader");
		}

		return false;
	}

	public static interface IClassInfoSource {

		public byte[] getClassBytes(String name);

		public CodeSource getClassCodeSource(String name);

	}

	public static class RLinkClassInfoSource implements IClassInfoSource {

		public byte[] getClassBytes(String name)
		{
			String bytespath = "/" + name.replace(".", "/") + (".class");

			byte[] bytes = RLUtils.toBytes(bytespath);

			return bytes;
		}

		public CodeSource getClassCodeSource(String name)
		{
			String bytespath = "/" + name.replace(".", "/") + (".class");

			CodeSource source = null;

			Object obj = RLUtils.toSource(bytespath);

			if (obj != null)
			{
				if (obj instanceof File)
				{
					try
					{
						source = new CodeSource(((File) obj).toURI().toURL(), (CodeSigner[]) null);
					}
					catch (MalformedURLException e)
					{
						e.printStackTrace();
					}
				}
				else if (obj instanceof URL)
				{
					source = new CodeSource((URL) obj, (CodeSigner[]) null);
				}
			}

			return source;
		}

	}

}

