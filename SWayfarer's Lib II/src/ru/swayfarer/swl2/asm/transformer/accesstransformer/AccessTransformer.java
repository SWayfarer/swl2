package ru.swayfarer.swl2.asm.transformer.accesstransformer;

import ru.swayfarer.swl2.asm.transformer.accesstransformer.visitor.AccessVisitor;
import ru.swayfarer.swl2.asm.transformer.basic.AbstractAsmTransformer;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassReader;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassWriter;

/**
 * Трансформер доступа к содержимому классов. 
 * <h1>Применение:</h1> При использовании на классе делает публичными все его поля и методы, а также снимает метки final там, где они расставлены.
 * @author swayfarer
 *
 */
public class AccessTransformer extends AbstractAsmTransformer{

	@Override
	public void transform(String name, byte[] bytes, ClassReader reader, ClassWriter writer)
	{
		acceptCV(reader, bytes, new AccessVisitor(writer));
	}
}
