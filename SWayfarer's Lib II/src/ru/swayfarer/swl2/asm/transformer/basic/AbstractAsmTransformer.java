package ru.swayfarer.swl2.asm.transformer.basic;

import ru.swayfarer.swl2.asm.IClassTransformer;
import ru.swayfarer.swl2.asm.TransformedClassInfo;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassReader;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassWriter;

public abstract class AbstractAsmTransformer implements IClassTransformer {

	public ILogger logger = LoggingManager.getLogger();
	
	public void transform(String name, byte[] bytes, ClassReader reader, ClassWriter writer, TransformedClassInfo info)
	{
		transform(name, bytes, reader, writer);
	}
	
	public void transform(String name, byte[] bytes, ClassReader reader, ClassWriter writer)
	{
		
	}
	
	@Override
	public byte[] transform(String name, byte[] bytes, TransformedClassInfo info) 
	{
		try
		{
			ClassReader reader = createClassReader(bytes);
			ClassWriter writer = createClassWriter(reader, bytes);
			
			transform(name, bytes, reader, writer, info);
			
			byte[] ret = writer.toByteArray();
			
			return ret;
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while transforming class", name, "by", this);
		}
		
		return bytes;
	}
	
	protected ClassReader createClassReader(byte[] bytecode)
	{
		return new ClassReader(bytecode);
	}
	
	protected ClassWriter createClassWriter(ClassReader reader, int flags)
	{
		return new ClassWriter(reader, flags);
	}
	
	protected void acceptCV(ClassReader reader, byte[] bytes, ClassVisitor visit)
	{
		reader.accept(visit, isJava7(bytes) ? ClassReader.SKIP_FRAMES : ClassReader.EXPAND_FRAMES);
	}
	
	protected ClassWriter createClassWriter(ClassReader reader, byte[] bytes)
	{
		return createClassWriter(reader, isJava7(bytes) ? ClassWriter.COMPUTE_FRAMES : ClassWriter.COMPUTE_MAXS);
	}
	
	protected boolean isJava7(byte[] bytes)
	{
		return ((((bytes[6] & 0xFF) << 8) | (bytes[7] & 0xFF)) > 50);
	}
	
}
