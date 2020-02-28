package ru.swayfarer.swl2.asm.transformer.remapper;

import ru.swayfarer.swl2.asm.TransformedClassInfo;
import ru.swayfarer.swl2.asm.transformer.basic.AbstractAsmTransformer;
import ru.swayfarer.swl2.asm.transformer.remapper.visitor.RemapperClassVisitor;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassReader;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassWriter;

/**
 * Класс-трансформер, обеспечивающий ремаппинг элементов на основе переданного {@link RemapInfo}
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class RemapperClassTransformer extends AbstractAsmTransformer{

	/** Информация о ремаппинге */
	@InternalElement
	public RemapInfo remapInfo;
	
	/** Ремаппер классов */
	@InternalElement
	public ClassRemapperTransformer classTransformer;
	
	/** Ремаппер полей */
	@InternalElement
	public FieldRemapperTransformer fieldTransformer;
	
	/** Репаммер методов */
	@InternalElement
	public MethodRemapperTransformer methodTransformer;
	
	/** Конструктор с пустым {@link RemapInfo} */
	public RemapperClassTransformer()
	{
		this(new RemapInfo());
	}
	
	/** Конструктор */
	public RemapperClassTransformer(RemapInfo remapInfo)
	{
		super();
		this.remapInfo = remapInfo;
		classTransformer = new ClassRemapperTransformer(remapInfo);
		fieldTransformer = new FieldRemapperTransformer(remapInfo);
		methodTransformer = new MethodRemapperTransformer(remapInfo);
	}
	
	@Override
	public byte[] transform(String name, byte[] bytes, TransformedClassInfo info)
	{
		bytes = fieldTransformer.transform(name, bytes, info);
		//bytes = methodTransformer.transform(name, bytes, info);
		bytes = classTransformer.transform(name, bytes, info);
		
		return bytes;
	}
	
	/** Получить маппинг поля */
	public String getFieldMapping(String name, String owner, String desc)
	{
		return remapInfo.getFieldMapping(name, owner, desc);
	}
	
	/** Получить маппинг метода */
	public String getMethodMapping(String name, String owner, String desc)
	{
		return remapInfo.getMethodMapping(name, owner, desc);
	}
	
	/** Получить маппинг класса */
	public String getClassMapping(String name)
	{
		return remapInfo.getClassMapping(name);
	}
	
	/** Задать маппинг класса */
	public <T extends RemapperClassTransformer> T setClassMapping(String before, String after)
	{
		remapInfo.setClassMapping(before, after);
		return (T) this;
	}
	
	/** Задать маппинг поля */
	public <T extends RemapperClassTransformer> T setFieldMapping(String owner, String name, String desc, String newName)
	{
		remapInfo.setFieldMapping(owner, name, desc, newName);
		return (T) this;
	}
	
	/** Задать маппинг метода */
	public <T extends RemapperClassTransformer> T setMethodMapping(String owner, String name, String desc, String newName)
	{
		remapInfo.setMethodMapping(owner, name, desc, newName);
		return (T) this;
	}
	
	/** Репмаппер классов */
	@InternalElement
	public static class ClassRemapperTransformer extends AbstractAsmTransformer {
		
		/** Информация о ремаппинге */
		@InternalElement
		public RemapInfo remapInfo;
		
		/** Конструктор */
		public ClassRemapperTransformer(RemapInfo remapInfo)
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
	
	/** Репмаппер методов */
	@InternalElement
	public static class MethodRemapperTransformer extends AbstractAsmTransformer {
		
		/** Информация о ремаппинге */
		@InternalElement
		public RemapInfo remapInfo;
		
		/** Конструктор */
		public MethodRemapperTransformer(RemapInfo remapInfo)
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
	
	/** Репмаппер полей */
	@InternalElement
	public static class FieldRemapperTransformer extends AbstractAsmTransformer {
		
		/** Информация о ремаппинге */
		@InternalElement
		public RemapInfo remapInfo;

		/** Конструктор */
		public FieldRemapperTransformer(RemapInfo remapInfo)
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
