package ru.swayfarer.swl2.asm.transformer.injection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.swayfarer.swl2.asm.IClassTransformer;
import ru.swayfarer.swl2.asm.injection.IInjectionInfo;
import ru.swayfarer.swl2.asm.transformer.basic.AbstractAsmTransformer;
import ru.swayfarer.swl2.asm.transformer.injection.visitor.InjectionClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassReader;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassWriter;

/**
 * 
 * Трансформер классов с вставками в методы.
 * <h1>Применение:</h1> При использовании на классе вставляет в его методы подходящие иньекции
 * 
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class InjectionClassTransformer extends AbstractAsmTransformer{

	public List<IClassTransformer> transformers = new ArrayList<>();
	public Map<String, Map<String, List<IInjectionInfo>>> injections = new HashMap<>();
	
	@Override
	public void transform(String name, byte[] bytes, ClassReader reader, ClassWriter writer)
	{
		name = name.replace(".", "/");
		
		InjectionClassVisitor cv = new InjectionClassVisitor(writer);
		cv.injections = getMethods(name);
		
		//Console.info("Transforming "+name+", Methods: "+cv.injections);
		
		acceptCV(reader, bytes, cv);
	}
	
	/** Добавить вставки из контейнера (напр. ru.swayfarer.container.HookContainer) */
	public InjectionClassTransformer addInjection(Class<?> containerClass)
	{
		return addInjection(containerClass.getName());
	}
	
	public <T extends InjectionClassTransformer> T addTransformer(IClassTransformer transformer) 
	{
		if (!transformers.contains(transformer))
			transformers.add(transformer);
		
		return (T) this;
	}
	
	/** Добавить вставки из контейнера (напр. ru.swayfarer.container.HookContainer) */
	public InjectionClassTransformer addInjection(String containerClassName)
	{
		List<IInjectionInfo> injections = InjectionScanner.getInjections(containerClassName, transformers);
		
		if (injections != null && !injections.isEmpty())
		for (IInjectionInfo injInfo : injections)
			addInjection(injInfo);
		
		return this;
	}
	
	public InjectionClassTransformer addInjection(IInjectionInfo injection)
	{
		if (injection == null)
			return null;
		
		List<IInjectionInfo> injections = getInjections(injection.getTargetClass(), injection.getTargetMethod());
		
		if (!injections.contains(injection))
			injections.add(injection);
		
		return this;
	}
	
	public Map<String, List<IInjectionInfo>> getMethods(String className)
	{
		Map<String, List<IInjectionInfo>> methods = this.injections.get(className);
		
		if (methods == null)
		{
			methods = new HashMap<>();
			this.injections.put(className, methods);
		}
		
		return methods;
	}
	
	public List<IInjectionInfo> getInjections(String className, String methodName)
	{
		Map<String, List<IInjectionInfo>> methods = getMethods(className);
		
		List<IInjectionInfo> injections = methods.get(methodName);
		
		if (injections == null)
		{
			injections = new ArrayList<>();
			methods.put(methodName, injections);
		}
		
		return injections;
	}

}
