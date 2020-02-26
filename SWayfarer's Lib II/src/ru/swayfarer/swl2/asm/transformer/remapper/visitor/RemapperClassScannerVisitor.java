package ru.swayfarer.swl2.asm.transformer.remapper.visitor;

import ru.swayfarer.swl2.asm.informated.AnnotationInfo;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.informated.FieldInfo;
import ru.swayfarer.swl2.asm.informated.MethodInfo;
import ru.swayfarer.swl2.asm.informated.visitor.InformatedClassVisitor;
import ru.swayfarer.swl2.asm.transformer.remapper.RemapAsm;
import ru.swayfarer.swl2.asm.transformer.remapper.RemapInfo;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.FieldVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;

public class RemapperClassScannerVisitor extends InformatedClassVisitor{

	public static final String ANNOTATION_DESC = Type.getType(RemapAsm.class).getDescriptor();
	
	public RemapInfo remapInfo;
	
	public RemapperClassScannerVisitor(ClassVisitor classVisitor, ClassInfo classInfo, RemapInfo remapInfo)
	{
		super(classVisitor, classInfo);
		this.remapInfo = remapInfo;
	}
	
	@Override
	public void visitClassInformated(ClassInfo classInfo, int version, int access, String name, String signature, String superName, String[] interfaces)
	{
		AnnotationInfo annotationInfo = classInfo.getFirstAnnotation(ANNOTATION_DESC);
		
		if (annotationInfo != null)
		{
			String newName = annotationInfo.getParam("value");
			
			if (!StringUtils.isEmpty(newName))
			{
				remapInfo.setClassMapping(className, newName);
			}
		}
		
		super.visitClassInformated(classInfo, version, access, name, signature, superName, interfaces);
	}
	
	@Override
	public FieldVisitor visitFieldInformated(FieldInfo info, int access, String name, String descriptor, String signature, Object value)
	{
		AnnotationInfo annotationInfo = info.getFirstAnnotation(ANNOTATION_DESC);
		
		if (annotationInfo != null)
		{
			String newName = annotationInfo.getParam("value");
			
			if (!StringUtils.isEmpty(newName))
			{
				remapInfo.setFieldMapping(className, name, descriptor, newName);
			}
		}
		
		return super.visitFieldInformated(info, access, name, descriptor, signature, value);
	}
	
	@Override
	public MethodVisitor visitMethodInformated(MethodInfo info, int access, String name, String descriptor, String signature, String[] exceptions)
	{
		AnnotationInfo annotationInfo = info.getFirstAnnotation(ANNOTATION_DESC);
		
		if (annotationInfo != null)
		{
			String newName = annotationInfo.getParam("value");
			
			if (!StringUtils.isEmpty(newName))
			{
				remapInfo.setMethodMapping(className, name, descriptor, newName);
			}
		}
		
		return super.visitMethodInformated(info, access, name, descriptor, signature, exceptions);
	}

}
