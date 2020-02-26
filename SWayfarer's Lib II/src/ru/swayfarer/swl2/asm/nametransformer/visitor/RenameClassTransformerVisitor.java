package ru.swayfarer.swl2.asm.nametransformer.visitor;

import java.util.List;

import ru.swayfarer.swl2.asm.informated.AnnotationInfo;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.informated.FieldInfo;
import ru.swayfarer.swl2.asm.informated.MethodInfo;
import ru.swayfarer.swl2.asm.informated.visitor.InformatedClassVisitor;
import ru.swayfarer.swl2.asm.nametransformer.RenameAsm;
import ru.swayfarer.swl2.asm.nametransformer.RenameClassTransformer;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.FieldVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;

/**
 * Визитор для {@link RenameClassTransformer} 
 * @author swayfarer
 *
 */
public class RenameClassTransformerVisitor extends InformatedClassVisitor{

	public static final String ANNOTATION_DESC = Type.getDescriptor(RenameAsm.class);
	
	public RenameClassTransformerVisitor(ClassVisitor classVisitor, ClassInfo classInfo)
	{
		super(classVisitor, classInfo);
	}

	@Override
	public void visitClassInformated(ClassInfo classInfo, int version, int access, String name, String signature, String superName, String[] interfaces)
	{
		super.visitClassInformated(classInfo, version, access, getName(name, classInfo.annotations), signature, superName, interfaces);
	}
	
	@Override
	public MethodVisitor visitMethodInformated(MethodInfo info, int access, String name, String descriptor, String signature, String[] exceptions)
	{
		return super.visitMethodInformated(info, access, getName(name, info.annotations), descriptor, signature, exceptions);
	}
	
	@Override
	public FieldVisitor visitFieldInformated(FieldInfo info, int access, String name, String descriptor, String signature, Object value)
	{
		return super.visitFieldInformated(info, access, getName(name, info.annotations), descriptor, signature, value);
	}

	public String getName(String defaultName, List<AnnotationInfo> annotationsInfo)
	{
		for (AnnotationInfo annotationInfo : annotationsInfo)
		{
			if (annotationInfo.descritor.equals(ANNOTATION_DESC))
			{
				String value = annotationInfo.getParam("value");
				
				if (!StringUtils.isEmpty(value))
					return value;
			}
		}
		
		return defaultName;
	}
}
