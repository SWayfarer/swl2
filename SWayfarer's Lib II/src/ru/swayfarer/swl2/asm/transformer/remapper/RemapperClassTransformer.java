package ru.swayfarer.swl2.asm.transformer.remapper;

import ru.swayfarer.swl2.asm.TransformedClassInfo;
import ru.swayfarer.swl2.asm.transformer.basic.AbstractAsmTransformer;
import ru.swayfarer.swl2.asm.transformer.remapper.visitor.RemapperClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassReader;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassWriter;

@SuppressWarnings("unchecked")
public class RemapperClassTransformer extends AbstractAsmTransformer{

	public RemapInfo remapInfo;
	
	public ClassTransformer classTransformer;
	public FieldTransformer fieldTransformer;
	public MethodTransformer methodTransformer;
	
	public RemapperClassTransformer()
	{
		this(new RemapInfo());
	}
	
	public RemapperClassTransformer(RemapInfo remapInfo)
	{
		super();
		this.remapInfo = remapInfo;
		classTransformer = new ClassTransformer(remapInfo);
		fieldTransformer = new FieldTransformer(remapInfo);
		methodTransformer = new MethodTransformer(remapInfo);
	}
	
	@Override
	public byte[] transform(String name, byte[] bytes, TransformedClassInfo info)
	{
		bytes = fieldTransformer.transform(name, bytes, info);
		//bytes = methodTransformer.transform(name, bytes, info);
		bytes = classTransformer.transform(name, bytes, info);
		
		return bytes;
	}
	
	public String getFieldMapping(String name, String owner, String desc)
	{
		return remapInfo.getFieldMapping(name, owner, desc);
	}
	
	public String getMethodMapping(String name, String owner, String desc)
	{
		return remapInfo.getMethodMapping(name, owner, desc);
	}
	
	public String getClassMapping(String name)
	{
		return remapInfo.getClassMapping(name);
	}
	
	public <RemapperClassTransformer_Type extends RemapperClassTransformer> RemapperClassTransformer_Type setClassMapping(String before, String after)
	{
		remapInfo.setClassMapping(before, after);
		return (RemapperClassTransformer_Type) this;
	}
	
	public <RemapperClassTransformer_Type extends RemapperClassTransformer> RemapperClassTransformer_Type setFieldMapping(String owner, String name, String desc, String newName)
	{
		remapInfo.setFieldMapping(owner, name, desc, newName);
		return (RemapperClassTransformer_Type) this;
	}
	
	public <RemapperClassTransformer_Type extends RemapperClassTransformer> RemapperClassTransformer_Type setMethodMapping(String owner, String name, String desc, String newName)
	{
		remapInfo.setMethodMapping(owner, name, desc, newName);
		return (RemapperClassTransformer_Type) this;
	}
	
	protected static class ClassTransformer extends AbstractAsmTransformer {
		
		public RemapInfo remapInfo;
		
		public ClassTransformer(RemapInfo remapInfo)
		{
			super();
			this.remapInfo = remapInfo;
		}

		@Override
		public void transform(String name, byte[] bytes, ClassReader reader, ClassWriter writer, TransformedClassInfo info)
		{
			RemapperClassVisitor cv = new RemapperClassVisitor(writer, remapInfo, false, true, false);
			acceptCV(reader, bytes, cv);
			info.name = cv.newName.replace("/", ".");
		}
		
	}
	
	protected static class MethodTransformer extends AbstractAsmTransformer {
		
		public RemapInfo remapInfo;
		
		public MethodTransformer(RemapInfo remapInfo)
		{
			super();
			this.remapInfo = remapInfo;
		}

		@Override
		public void transform(String name, byte[] bytes, ClassReader reader, ClassWriter writer)
		{
			acceptCV(reader, bytes, new RemapperClassVisitor(writer, remapInfo, false, false, true));
		}
		
	}
	
	protected static class FieldTransformer extends AbstractAsmTransformer {
		
		public RemapInfo remapInfo;
		
		public FieldTransformer(RemapInfo remapInfo)
		{
			super();
			this.remapInfo = remapInfo;
		}

		@Override
		public void transform(String name, byte[] bytes, ClassReader reader, ClassWriter writer)
		{
			acceptCV(reader, bytes, new RemapperClassVisitor(writer, remapInfo, true, false, true));
		}
		
	}
	
}
