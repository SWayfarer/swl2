package ru.swayfarer.swl2.asm.transformer.ditransformer;

import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.transformer.ditransformer.regetter.visitor.DIDynamicClassVisitor;
import ru.swayfarer.swl2.asm.transformer.ditransformer.regetter.visitor.DynamicDI;
import ru.swayfarer.swl2.asm.transformer.ditransformer.visitor.DIClassVisitor;
import ru.swayfarer.swl2.asm.transformer.informated.InformatedClassTransformer;
import ru.swayfarer.swl2.ioc.DIManager.DISwL;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassReader;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassWriter;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;

/**
 * Класс-трансформер, осуществляющий работу:
 * <br> {@link DISwL},
 * <br> {@link DynamicDI} 
 * 
 * @author swayfarer
 *
 */
public class DIClassTransformer extends InformatedClassTransformer {

	public static String DI_ANNOTATION_DESC = Type.getDescriptor(DISwL.class);
	
	@Override
	public void transform(String name, byte[] bytes, ClassReader reader, ClassWriter writer, ClassInfo info)
	{
		ClassVisitor cv = writer;
		
		if (info.fields.stream().anyMatch((field) -> field.findAnnotationRec(DI_ANNOTATION_DESC) != null))
		{
			cv = new DIClassVisitor(cv, info);
		}
		
		cv = new DIDynamicClassVisitor(cv, info);
		
		acceptCV(reader, bytes, cv);
	}
	
}
