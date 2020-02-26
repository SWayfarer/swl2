package ru.swayfarer.swl2.asm.transfomer.injection;


import java.util.Map;

import ru.swayfarer.swl2.asm.TransformedClassInfo;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.transfomer.injection.visitor.InjectionsClassVisitor;
import ru.swayfarer.swl2.asm.transformer.informated.InformatedClassTransformer;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassReader;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassWriter;

/**
 * Класс-трансформер, обеспечивающий работу {@link InjectionAsm} 
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class InjectionsClassTransformer extends InformatedClassTransformer {
	
	/** Лист посещенных классов */
	@InternalElement
	public IExtendedList<String> visitedClasses = CollectionsSWL.createExtendedList();
	
	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Карта, где ключ - каноничное имя класса, значение - информация об иньекциях в него */
	@InternalElement
	public Map<String, ClassInjectionInfo> classInjections = CollectionsSWL.createHashMap();
	
	@Override
	public void transform(String name, byte[] bytes, ClassReader reader, ClassWriter writer, ClassInfo info, TransformedClassInfo transformedClassInfo)
	{
		ClassInjectionInfo classInjectionInfo = classInjections.get(name);
		
		ClassVisitor cv = writer;
		
		if (classInjectionInfo != null)
		{
			cv = new InjectionsClassVisitor(cv, info, classInjectionInfo);
		}
		
		acceptCV(reader, bytes, cv);
	}
	
	/** Зарегистрировать иньекцию в класс */
	public <T extends InjectionsClassTransformer> T registerClassInjection(String classNormalName, ClassInjectionInfo injectionInfo)
	{
		if (classInjections.containsKey(classNormalName))
			logger.warning("Overwriting", classNormalName, "");
	
		classInjections.put(classNormalName, injectionInfo);
		
		return (T) this;
	}
	
	/** Добавить класс с методами-иньекциями */
	public <T extends InjectionsClassTransformer> T addInjections(Class<?> cl)
	{
		return addInjections(cl.getName());
	}
	
	/** Добавить класс с методами-иньекциями */
	public <T extends InjectionsClassTransformer> T addInjections(String classNormalName)
	{
		if (visitedClasses.contains(classNormalName))
		{
			logger.warning("Skiping already visited class:", classNormalName);
			return (T) this;
		}
		
		visitedClasses.add(classNormalName);
		
		ClassInfo classInfo = ClassInfo.valueOf(classNormalName);
		
		if (classInfo == null)
		{
			logger.warning("Can't read class info for '", classNormalName, "'");
			return (T) this;
		}
		
		IExtendedList<MethodInjection> injections = MethodInjection.of(classInfo);
		
		if (injections == null)
		{
			logger.warning("No injections found in '", classNormalName, "'");
			return (T) this;
		}
		
		logger.info("Found", injections.size(), "injections at", classNormalName);
		
		injections.forEach(this::registerMethodInjection);
		
		return (T) this;
	}
	
	/** Зарегистрировать иньекцию, используя в качестве класса-цели {@link IMethodInjection#getTargetClassCanonicalName()} */
	public <T extends InjectionsClassTransformer> T registerMethodInjection(IMethodInjection info)
	{
		return registerMethodInjection(info.getTargetClassCanonicalName(), info);
	}
	
	/** Зарегистрировать иньекцию */
	public <T extends InjectionsClassTransformer> T registerMethodInjection(String classNormalName, IMethodInjection info)
	{
		ClassInjectionInfo classInjectionInfo = getClassInjectionInfo(classNormalName);
		classInjectionInfo.methodInjections.add(info);
		return (T) this;
	}
	
	/** Получить информанию об иньекциях в класс */
	public ClassInjectionInfo getClassInjectionInfo(String classNormalName)
	{
		ClassInjectionInfo ret = classInjections.get(classNormalName);
		
		if (ret == null)
		{
			ret = new ClassInjectionInfo(classNormalName, CollectionsSWL.createExtendedList());
			registerClassInjection(classNormalName, ret);
		}
		
		return ret;
	}

}
