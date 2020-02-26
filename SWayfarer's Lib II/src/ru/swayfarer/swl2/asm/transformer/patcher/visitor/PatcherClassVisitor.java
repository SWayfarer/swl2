package ru.swayfarer.swl2.asm.transformer.patcher.visitor;

import java.util.List;

import ru.swayfarer.swl2.asm.informated.AnnotationInfo;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.informated.FieldInfo;
import ru.swayfarer.swl2.asm.informated.MethodInfo;
import ru.swayfarer.swl2.asm.informated.visitor.InformatedClassVisitor;
import ru.swayfarer.swl2.asm.transformer.patcher.PatchAsm;
import ru.swayfarer.swl2.version.SimpleVersion;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.FieldVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;

public class PatcherClassVisitor extends InformatedClassVisitor {

	public SimpleVersion version;
	
	public static final String ANNOTATION_DESC = Type.getType(PatchAsm.class).getDescriptor();
	
	public PatcherClassVisitor(ClassVisitor classVisitor, ClassInfo classInfo, SimpleVersion version)
	{
		super(classVisitor, classInfo);
		this.version = version;
	}
	
	@Override
	public MethodVisitor visitMethodInformated(MethodInfo info, int access, String name, String descriptor, String signature, String[] exceptions)
	{
		if (!isValid(info.annotations))
			return null;
		
		return super.visitMethodInformated(info, access, name, descriptor, signature, exceptions);
	}
	
	@Override
	public FieldVisitor visitFieldInformated(FieldInfo info, int access, String name, String descriptor, String signature, Object value)
	{
		if (!isValid(info.annotations))
			return null;
		
		return super.visitFieldInformated(info, access, name, descriptor, signature, value);
	}
	
//	@Override
//	public void visitClassInformated(ClassInfo classInfo, int version, int access, String name, String signature, String superName, String[] interfaces)
//	{
//		Console.info("Visiting...");
//		
//		if (!isValid(classInfo.annotations))
//		{
//			Console.info("Invalid");
//			return;
//		}
//		else
//		{
//			Console.info("Valid");
//			
//		}
//		
//		super.visitClassInformated(classInfo, version, access, name, signature, superName, interfaces);
//	}
	
	public boolean isValid(List<AnnotationInfo> annotations)
	{
		for (AnnotationInfo info : annotations)
		{
			if (info.descritor.equals(ANNOTATION_DESC))
			{
				return isValid((String) info.getParam("version"));
			}
		}
		
		return false;
	}
	
	public boolean isValid(String version)
	{
		return this.version == null || this.version.compareTo(version) >= 0;
	}

}
