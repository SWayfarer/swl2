package ru.swayfarer.swl2.asm.transformer.safemethod;

import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.transformer.informated.InformatedClassTransformer;
import ru.swayfarer.swl2.asm.transformer.safemethod.visitor.SafeMethodClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassReader;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassWriter;

/**
 * Класс-трансформер, обеспечивающий работу {@link SafeMethod} 
 * @author swayfarer
 *
 */
public class SafeMethodsTransformer extends InformatedClassTransformer {

	@Override
	public void transform(String name, byte[] bytes, ClassReader reader, ClassWriter writer, ClassInfo info)
	{
		acceptCV(reader, bytes, new SafeMethodClassVisitor(writer, info));
	}
	
}
