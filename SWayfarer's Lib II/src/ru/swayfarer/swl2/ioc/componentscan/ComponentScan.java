package ru.swayfarer.swl2.ioc.componentscan;

import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.swayfarer.swl2.asm.informated.AnnotationInfo;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.collections.extended.IExtendedMap;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction0;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.ioc.DIManager;
import ru.swayfarer.swl2.ioc.DIManager.ContextElementType;
import ru.swayfarer.swl2.ioc.DIManager.DIContext;
import ru.swayfarer.swl2.ioc.DIManager.DIContextElementFromFun;
import ru.swayfarer.swl2.ioc.DIManager.IDIContextElement;
import ru.swayfarer.swl2.ioc.DIRegistry;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.Observables;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.resource.streams.BytesInputStreamSWL;
import ru.swayfarer.swl2.resource.streams.DataInputStreamSWL;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.threads.ThreadsUtils;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;

@SuppressWarnings("unchecked")
public class ComponentScan {

	public boolean isLoggingScan = false;
	
	public ScanInfo scanInfo = new ScanInfo();
	
	public static ILogger logger = LoggingManager.getLogger();
	
	public IFunction1<String, IExtendedMap<String, IFunction0<DataInputStreamSWL>>> streamCreationFun;
	public IExtendedList<String> packages = CollectionsSWL.createExtendedList();
	
	public IObservable<Object> eventComponentCreation = Observables.createObservable();
	
	public <T extends ComponentScan> T setStreamFun(IFunction1<String, IExtendedMap<String, IFunction0<DataInputStreamSWL>>> fun) 
	{
		this.streamCreationFun = fun;
		return (T) this;
	}
	
	public static IExtendedMap<String, IFunction0<DataInputStreamSWL>> sourceOfClassSource(String pkg, Class<?> cl)
	{
		URL location = cl.getProtectionDomain().getCodeSource().getLocation();
		
		if (location.getProtocol().equals("file"))
		{
			FileSWL fileByUrl = FileSWL.ofURL(location);
			return sourceOfDirectory(pkg, fileByUrl);
		}
		else if (location.getProtocol().equals("jar"))
		{
			URLConnection connection = logger.safeReturn(location::openConnection);
			
			if (connection != null && connection instanceof JarURLConnection)
			{
				JarURLConnection jarURLConnection = (JarURLConnection) connection;
				
				FileSWL file = FileSWL.ofURL(jarURLConnection.getJarFileURL());
				
				return sourceOfJar(pkg, file);
			}
		}
		
		return null;
	}
	
	public static IExtendedMap<String, IFunction0<DataInputStreamSWL>> sourceOfDirectory(String pkg, FileSWL rootDir)
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
	
	public static IFunction1<String, IExtendedMap<String, IFunction0<DataInputStreamSWL>>> streamFunOfClassSource(Class<?> cl)
	{
		return (s) -> sourceOfClassSource(s, cl);
	}
	
	public static IFunction1<String, IExtendedMap<String, IFunction0<DataInputStreamSWL>>> streamFunOfDirectory(FileSWL file)
	{
		return (s) -> sourceOfDirectory(s, file);
	}
	
	public static IFunction1<String, IExtendedMap<String, IFunction0<DataInputStreamSWL>>> streamFunOfJar(FileSWL jarFile)
	{
		return (s) -> sourceOfJar(s, jarFile);
	}
	
	public static IExtendedMap<String, IFunction0<DataInputStreamSWL>> sourceOfJar(String pkg, FileSWL jarFile)
	{
		return logger.safeReturn(() -> {
			
			IExtendedMap<String, IFunction0<DataInputStreamSWL>> ret = CollectionsSWL.createExtendedMap();
			
			String start = pkg.replace(".", "/");
			
			ZipFile file = jarFile.toZipFile();
			
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
		
		}, null, "Error while creating source from jarfile", jarFile.getAbsolutePath(), "with package", pkg);
	}
	
	public Object createNewObject(Class<?> cl, boolean isInject)
	{
		Object instance = ReflectionUtils.newInstanceOf(cl);
		
		if (instance == null)
		{
			logger.error("Can't create new instance of class", cl, "Maybe constructor without args is not exists?");
			return null;
		}
		
		eventComponentCreation.next(instance);
		
		ReflectionUtils.invokeMethods(new EventAnnotatedMethodFilter(ComponentEventType.PreInit), cl, instance, new Object[0]);
		
		if (isInject)
			DIManager.injectContextElements(instance);
		
		ReflectionUtils.invokeMethods(new EventAnnotatedMethodFilter(ComponentEventType.Init), cl, instance, new Object[0]);
		
		return instance;
	}
	
	public void scanClass(Class<?> cl, String contextName, String elementName, ContextElementType elementType) throws Throwable
	{
		if (isLoggingScan)
			logger.info("Scanning class", cl);
		
		IFunction0<Object> objectCreationFun = null;
		
		if (StringUtils.isEmpty(contextName))
		{
			contextName = DIRegistry.getClassContextName(cl);
		}
		
		if (elementType == null)
			elementType = ContextElementType.Singleton;
		
		switch(elementType)
		{
			case Prototype:
			{
				objectCreationFun = () -> createNewObject(cl, true);
				break;
			}
			case Singleton:
			{
				objectCreationFun = new IFunction0<Object>()
				{
					public Object instance;
					
					@Override
					public Object apply()
					{
						if (instance == null)
						{
							instance = createNewObject(cl, false);
							DIManager.injectContextElements(instance);
						}
						
						return instance;
					}
				};
				break;
			}
			case ThreadLocalPrototype:
			{
				objectCreationFun = () -> ThreadsUtils.getThreadLocal(() -> createNewObject(cl, true));
				break;
			}
			default:
				break;
		}
		
		if (objectCreationFun == null)
			return;
		
		DIManager.createIfNotFound(contextName);
		DIContext context = DIRegistry.getRegisteredContext(contextName);
		
		IDIContextElement elementToAdd = DIContextElementFromFun.of(cl, elementName, objectCreationFun);
		
		Map<Class<?>, IDIContextElement> map = context.getElementsForName(elementName, true);
		map.put(cl, elementToAdd);
	}
	
	public void scanClassByResource(String pkg, String name, IFunction0<DataInputStreamSWL> streamFun) throws Throwable
	{
		if (StringUtils.isEmpty(name) || streamFun == null)
		{
			if (isLoggingScan)
				logger.warning("Skiping class from", pkg, "named", name);
			
			return;
		}
		
		DataInputStreamSWL dis = streamFun.apply();
		
		if (dis == null)
		{
			if (isLoggingScan)
				logger.warning("Can't read class from package", pkg, "named", name);
			
			return;
		}
		
		ClassInfo classInfo = ClassInfo.valueOf(dis.readAllSafe());
		
		if (classInfo == null)
		{
			if (isLoggingScan)
				logger.warning("Can't read class from package", pkg, "named", name);
			
			return;
		}
		
		String componentAnnotationDesc = getComponentAnnotationDesc();
		AnnotationInfo componentAnnotationInfo = classInfo.getFirstAnnotation(componentAnnotationDesc);
		
		if (componentAnnotationInfo != null)
		{
			if (isLoggingScan)
				logger.info("Found class with component annotation:", name);
			
			String contextName = componentAnnotationInfo.getParam(getContextNameParameter());
			String elementName = componentAnnotationInfo.getParam(getElementNameParameter());
			String elementTypeStr = componentAnnotationInfo.getParam(getElementTypeParameter());
			ContextElementType elementType = null;
			
			if (StringUtils.isEmpty(elementTypeStr))
			{
				elementType = ContextElementType.Singleton;
			}
			else
			{
				elementType = ContextElementType.valueOf(elementTypeStr);
			}
			
			Class<?> cl = ReflectionUtils.findClass(name);
			scanClass(cl, contextName, elementName, elementType);
		}
		else
		{
			if (isLoggingScan)
			{
				logger.warning("Class", classInfo.name, "has not annotation", scanInfo.componentAnnotationDesc);
			}
		}
	}
	
	public String getElementNameParameter()
	{
		return scanInfo == null ? null : scanInfo.elementNameParameter;
	}
	
	public String getContextNameParameter()
	{
		return scanInfo == null ? null : scanInfo.contextNameParameter;
	}
	
	public String getElementTypeParameter()
	{
		return scanInfo == null ? null : scanInfo.elementTypeParameter;
	}
	
	public String getComponentAnnotationDesc()
	{
		return scanInfo == null ? null : scanInfo.componentAnnotationDesc;
	}
	
	public void scanInternal(String pkg) throws Throwable 
	{
		IExtendedMap<String, IFunction0<DataInputStreamSWL>> classStreamCreators = streamCreationFun.apply(pkg);
		
		if (!CollectionsSWL.isNullOrEmpty(classStreamCreators))
		{
			for (Map.Entry<String, IFunction0<DataInputStreamSWL>> e : classStreamCreators.entrySet())
			{
				e.getKey();
				scanClassByResource(pkg, e.getKey(), e.getValue());
			}
		}
	}
	
	public <T extends ComponentScan> T scan(String pkg) 
	{
		logger.safeOperation(() -> scanInternal(pkg), "Scanning package", pkg);
		
		return (T) this;
	}
	
	public static class EventAnnotatedMethodFilter implements IFunction1<Method, Boolean> {

		public IExtendedList<ComponentEventType> acceptableTypes = CollectionsSWL.createExtendedList();
		
		public EventAnnotatedMethodFilter()
		{
			
		}
		
		public EventAnnotatedMethodFilter(ComponentEventType... types)
		{
			acceptableTypes.addAll(types);
		}
		
		@Override
		public Boolean apply(Method method)
		{
			if (method == null)
				return false;
			
			DISwlComponentEvent annotation = method.getAnnotation(DISwlComponentEvent.class);
			
			if (annotation != null)
			{
				ComponentEventType value = annotation.value();
				
				if (acceptableTypes.contains(value))
				{
					return true;
				}
			}
			
			return false;
		}
	}
	
	@Data @Accessors(chain = true)
	public static class ScanInfo {
		
		public String componentAnnotationDesc = Type.getDescriptor(DISwlComponent.class);
		public String contextNameParameter = "context";
		public String elementNameParameter = "name";
		public String elementTypeParameter = "type";
		
	}
}
