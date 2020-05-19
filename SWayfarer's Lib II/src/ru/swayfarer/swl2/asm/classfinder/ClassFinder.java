package ru.swayfarer.swl2.asm.classfinder;

import java.util.Map;
import java.util.zip.ZipFile;

import ru.swayfarer.swl2.asm.classfinder.source.ClassSources;
import ru.swayfarer.swl2.asm.classfinder.stream.ClassInfoStream;
import ru.swayfarer.swl2.asm.classfinder.stream.IClassInfoStream;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.collections.extended.IExtendedMap;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction0;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.Observables;
import ru.swayfarer.swl2.observable.subscription.ISubscription;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.resource.streams.DataInputStreamSWL;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Искалка классов по определенным параметрам
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class ClassFinder {

	/** Источники классов (в виде .class-файлов)  */
	@InternalElement
	public ClassSources classSources = new ClassSources();
	
	/** Логировать ли сканирование? */
	public boolean isLoggingScan = false;
	
	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Сканируемые пакеты */
	@InternalElement
	public IExtendedList<String> packages = CollectionsSWL.createExtendedList();
	
	/** Событие сканирования класса. Именно сюда нужно вешать кастомную логику */
	public IObservable<ClassInfo> eventScan = Observables.createObservable();
	
	/** Функция открытия стримов для пакета */
	@InternalElement
	public IFunction1<String, IExtendedMap<String, IFunction0<DataInputStreamSWL>>> classSourcesFun;
	
	/** Задать {@link #classSourcesFun}*/
	@InternalElement
	public <T extends ClassFinder> T setStreamFun(IFunction1<String, IExtendedMap<String, IFunction0<DataInputStreamSWL>>> fun) 
	{
		this.classSourcesFun = fun;
		return (T) this;
	}
	
	public <T extends ClassFinder> T useClasspath()
	{
		this.classSourcesFun = classSources.ofClasspath();
		return (T) this;
	}
	
	public <T extends ClassFinder> T useClassSource(Class<?> cl)
	{
		this.classSourcesFun = classSources.ofClassSource(cl);
		return (T) this;
	}
	
	public <T extends ClassFinder> T useJar(FileSWL file)
	{
		this.classSourcesFun = classSources.ofJar(file);
		return (T) this;
	}
	
	public <T extends ClassFinder> T useJar(ZipFile file)
	{
		this.classSourcesFun = classSources.ofJar(file);
		return (T) this;
	}
	
	public <T extends ClassFinder> T useDir(FileSWL file)
	{
		this.classSourcesFun = classSources.ofDirectory(file);
		return (T) this;
	}
	
	/**
	 * Сканировать пакет
	 * @param pkg Сканируемый пакет
	 * @return Оригинальный объект (this)
	 */
	public <T extends ClassFinder> T scan(String pkg) 
	{
		if (isLoggingScan)
			logger.safeOperation(() -> scanInternal(pkg), "Scanning package", pkg);
		else
			logger.safe(() -> scanInternal(pkg), "Error while scanning package", pkg);
		
		return (T) this;
	}
	
	/**
	 * Сканировать пакет (Внутренняя логика)
	 * @param pkg Сканируемый пакет
	 * @return Оригинальный объект (this)
	 */
	@InternalElement
	public void scanInternal(String pkg) throws Throwable 
	{
		IExtendedMap<String, IFunction0<DataInputStreamSWL>> classStreamCreators = classSourcesFun.apply(pkg);
		
		if (!CollectionsSWL.isNullOrEmpty(classStreamCreators))
		{
			for (Map.Entry<String, IFunction0<DataInputStreamSWL>> e : classStreamCreators.entrySet())
			{
				e.getKey();
				scanStream(pkg, e.getKey(), e.getValue());
			}
		}
		else
		{
			if (isLoggingScan)
				logger.warning("Empty stream creators!", classSourcesFun);
		}
	}
	
	/**
	 * Сканировать {@link DataInputStreamSWL} на уровне байткода
	 * @param pkg Сканируемый пакет 
	 * @param name Имя класса
	 * @param streamFun Функция, которая создает поток байт .class-файла по запросу
	 */
	@InternalElement
	public void scanStream(String pkg, String name, IFunction0<DataInputStreamSWL> streamFun) throws Throwable
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
		
		eventScan.next(classInfo);
	}
	
	public IClassInfoStream dataStream(String pkg)
	{
		return new ClassInfoStream(allClasses(pkg));
	}
	
	public IExtendedList<ClassInfo> allClasses(String pkg)
	{
		IExtendedList<ClassInfo> ret = CollectionsSWL.createExtendedList();
		ISubscription<ClassInfo> sub = eventScan.subscribe((info) -> ret.add(info));
		scan(pkg);
		sub.dispose();
		return ret;
	}
}
