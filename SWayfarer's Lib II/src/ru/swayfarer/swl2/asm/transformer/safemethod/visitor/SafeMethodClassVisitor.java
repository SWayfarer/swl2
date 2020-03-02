package ru.swayfarer.swl2.asm.transformer.safemethod.visitor;

import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.informated.MethodInfo;
import ru.swayfarer.swl2.asm.informated.visitor.InformatedClassVisitor;
import ru.swayfarer.swl2.asm.transformer.safemethod.SafeMethod;
import ru.swayfarer.swl2.asm.transformer.safemethod.SafeMethodsTransformer;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;

/**
 * {@link ClassVisitor} для {@link SafeMethodsTransformer} 
 * @author swayfarer
 *
 */
public class SafeMethodClassVisitor extends InformatedClassVisitor {

	/** Дескриптор аннотации {@link SafeMethod} */
	@InternalElement
	public static String SAFE_METHOD_ANNOTATION_DESCRIPTOR = Type.getDescriptor(SafeMethod.class);
	
	/** Конструктор */
	public SafeMethodClassVisitor(ClassVisitor classVisitor, ClassInfo classInfo)
	{
		super(classVisitor, classInfo);
	}
	
	@Override
	public MethodVisitor visitMethodInformated(MethodInfo info, int access, String name, String descriptor, String signature, String[] exceptions)
	{
		MethodVisitor mv = super.visitMethodInformated(info, access, name, descriptor, signature, exceptions);
		
		if (info.hasAnnotation(SAFE_METHOD_ANNOTATION_DESCRIPTOR))
			mv = new SafeMethodMethodVisitor(mv, access, name, descriptor, info);
		
		return mv;
	}

}
