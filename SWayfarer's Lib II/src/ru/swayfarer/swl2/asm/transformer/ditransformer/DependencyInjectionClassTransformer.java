package ru.swayfarer.swl2.asm.transformer.ditransformer;

import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.transformer.ditransformer.regetter.visitor.DIDynamicClassVisitor;
import ru.swayfarer.swl2.asm.transformer.ditransformer.visitor.DependencyInjectionClassVisitor;
import ru.swayfarer.swl2.asm.transformer.informated.InformatedClassTransformer;
import ru.swayfarer.swl2.ioc.DIManager.DISwL;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassReader;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassWriter;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;

public class DependencyInjectionClassTransformer extends InformatedClassTransformer {

	public static String DI_ANNOTATION_DESC = Type.getDescriptor(DISwL.class);
	
	@Override
	public void transform(String name, byte[] bytes, ClassReader reader, ClassWriter writer, ClassInfo info)
	{
		ClassVisitor cv = writer;
		
		if (info.fields.stream().anyMatch((field) -> field.hasAnnotation(DI_ANNOTATION_DESC)))
		{
			cv = new DependencyInjectionClassVisitor(cv, info);
		}
		
		cv = new DIDynamicClassVisitor(cv, info);
		
		acceptCV(reader, bytes, cv);
	}
	
}
