package ru.swayfarer.swl2.asm.transfomer.threadlocal;

import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.transformer.informated.InformatedClassTransformer;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassReader;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassWriter;

/**
 * Обеспечивает работу аннотации {@link ThreadLocal} 
 * @author swayfarer
 *
 */
public class ThreadLocalClassTransformer extends InformatedClassTransformer {

	@Override
	public void transform(String name, byte[] bytes, ClassReader reader, ClassWriter writer, ClassInfo info)
	{
		acceptCV(reader, bytes, new ThreadLocalClassVisitor(writer, info));
	}
	
}
