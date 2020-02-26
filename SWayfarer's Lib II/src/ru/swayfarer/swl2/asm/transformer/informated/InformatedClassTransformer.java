package ru.swayfarer.swl2.asm.transformer.informated;

import ru.swayfarer.swl2.asm.TransformedClassInfo;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.transformer.basic.AbstractAsmTransformer;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassReader;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassWriter;

public abstract class InformatedClassTransformer extends AbstractAsmTransformer{

	@Deprecated
	@Override
	public void transform(String name, byte[] bytes, ClassReader reader, ClassWriter writer, TransformedClassInfo info)
	{
		transform(name, bytes, reader, writer, ClassInfo.valueOf(bytes), info);
	}
	
	public void transform(String name, byte[] bytes, ClassReader reader, ClassWriter writer, ClassInfo info, TransformedClassInfo transformedClassInfo)
	{
		transform(name, bytes, reader, writer, info);
	}
	
	public void transform(String name, byte[] bytes, ClassReader reader, ClassWriter writer, ClassInfo info) {}

}
