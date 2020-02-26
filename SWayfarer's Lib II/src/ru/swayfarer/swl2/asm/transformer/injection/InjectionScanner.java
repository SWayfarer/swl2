package ru.swayfarer.swl2.asm.transformer.injection;

import java.util.ArrayList;
import java.util.List;

import ru.swayfarer.swl2.asm.IClassTransformer;
import ru.swayfarer.swl2.asm.TransformedClassInfo;
import ru.swayfarer.swl2.asm.informated.AnnotationInfo;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.informated.MethodInfo;
import ru.swayfarer.swl2.asm.injection.IInjectionInfo;
import ru.swayfarer.swl2.asm.injection.MethodInjectionInfo;
import ru.swayfarer.swl2.asm.injection.annotations.InjectionAsm;
import ru.swayfarer.swl2.asm.transformer.informated.InformatedClassTransformer;
import ru.swayfarer.swl2.resource.rlink.RLUtils;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassReader;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassWriter;

/**
 * Сканнер вставок в контейнере.
 * <h1>Примение:</h1>
 * При использовании на классе находит все методы-вставки, обозначенные аннотацией {@link InjectionAsm}
 * @author swayfarer
 *
 */
public class InjectionScanner extends InformatedClassTransformer{

	
	
	public List<IInjectionInfo> injections = new ArrayList<>();
	
	@Override
	public void transform(String name, byte[] bytes, ClassReader reader, ClassWriter writer, ClassInfo info)
	{
		List<MethodInfo> methods = info.methods;
		AnnotationInfo annotation;
		for (MethodInfo mInfo : methods)
		{
			annotation = mInfo.getFirstAnnotation(InjectionAsm.type.getDescriptor());
			
			if (annotation != null)
			{
				MethodInjectionInfo injection = MethodInjectionInfo.valueOf(info, mInfo, annotation);
				
				injections.add(injection);
			}
		}
		
		acceptCV(reader, bytes, writer);
	}
	
	public static List<IInjectionInfo> getInjections(String classname, List<IClassTransformer> transformers)
	{
		byte[] bytes = RLUtils.toBytes("/"+classname.replace(".", "/")+".class");
		
		InjectionScanner scanner = new InjectionScanner();
		
		if (transformers != null)
		for (IClassTransformer transformer : transformers)
		{
			bytes = transformer.transform(classname, bytes, new TransformedClassInfo());
		}
		
		return scanner.getInjections(bytes);
	}
	
	public List<IInjectionInfo> getInjections(byte[] bytes)
	{
		transform("", bytes, new TransformedClassInfo());
		
		return injections;
	}
	
	public List<IInjectionInfo> getInjections()
	{
		return injections;
	}

}
