package ru.swayfarer.swl2.asm.transformer.remapper.visitor;

import ru.swayfarer.swl2.asm.transformer.remapper.RemapInfo;
import ru.swayfarer.swl2.asm.transformer.remapper.RemapperClassTransformer;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.FieldVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.commons.ClassRemapper;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.commons.Remapper;

/**
 * {@link ClassVisitor} для {@link RemapperClassTransformer} 
 * @author swayfarer
 *
 */
public class RemapperClassVisitor extends ClassRemapper {

	/** Информация о ремаппинге */
	@InternalElement
	public RemapInfo remapInfo;
	
	/** Новое имя класса */
	@InternalElement
	public String newName;
	
	/** Старое имя класса */
	@InternalElement
	public String oldName;
	
	public RemapperClassVisitor(ClassVisitor classVisitor, RemapInfo remapInfo, boolean fieldsEnabled, boolean classesEnabled, boolean methodsEnabled)
	{
		super(ASM7, classVisitor, new RemapInfoRemapper(remapInfo, fieldsEnabled, classesEnabled, methodsEnabled));
		this.remapInfo = remapInfo;
	}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
	{
		oldName = name;
		name = remapper.map(name);
		newName = name;
		super.visit(version, access, name, signature, superName, interfaces);
	}
	
	@Override
	public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value)
	{
		return super.visitField(access, name, descriptor, signature, value);
	}
	
	/** 
	 * Реализация ASM'овского {@link Remapper} для работы с {@link RemapInfo}
	 * @author swayfarer
	 *
	 */
	public static class RemapInfoRemapper extends Remapper {
	
		/** Информация о ремаппинге */
		@InternalElement
		public RemapInfo remapInfo;
		
		/** Настройки */
		public boolean fieldsEnabled, classesEnabled, methodsEnabled;
		
		/** Конструктор */
		public RemapInfoRemapper(RemapInfo remapInfo, boolean fieldsEnabled, boolean classesEnabled, boolean methodsEnabled)
		{
			super();
			this.remapInfo = remapInfo;
			this.fieldsEnabled = fieldsEnabled;
			this.classesEnabled = classesEnabled;
			this.methodsEnabled = methodsEnabled;
		}

		@Override
		public String mapMethodName(String owner, String name, String descriptor)
		{
			if (methodsEnabled)
			{
				String newName = remapInfo.getMethodMapping(name, owner, descriptor);
				
				if (!StringUtils.isEmpty(newName))
					name = newName;
			}
			
			return super.mapMethodName(owner, name, descriptor);
		}
		
		@Override
		public String mapFieldName(String owner, String name, String descriptor)
		{
			if (fieldsEnabled)
			{
				String newName = remapInfo.getFieldMapping(name, owner, descriptor);
				
				if (!StringUtils.isEmpty(newName))
					name = newName;
			}
			
			return super.mapFieldName(owner, name, descriptor);
		}
		
		@Override
		public String map(String internalName)
		{
			if (classesEnabled)
			{
				String newName = remapInfo.getClassMapping(internalName);
				
				if (!StringUtils.isEmpty(newName))
					internalName = newName;
			}
			
			return super.map(internalName);
		}
		
	}

}
