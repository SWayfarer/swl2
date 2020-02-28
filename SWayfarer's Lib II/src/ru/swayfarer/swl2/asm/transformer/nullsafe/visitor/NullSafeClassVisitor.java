package ru.swayfarer.swl2.asm.transformer.nullsafe.visitor;

import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.informated.MethodInfo;
import ru.swayfarer.swl2.asm.informated.VariableInfo;
import ru.swayfarer.swl2.asm.informated.visitor.InformatedClassVisitor;
import ru.swayfarer.swl2.asm.transformer.nullsafe.NullSafe;
import ru.swayfarer.swl2.asm.transformer.nullsafe.NullSafeClassTransformer;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;

/**
 * {@link ClassVisitor} для {@link NullSafeClassTransformer}'а
 * @author swayfarer
 *
 */
public class NullSafeClassVisitor extends InformatedClassVisitor {

	/** Дескриптор аннотации {@link NullSafe} */
	public static String NULL_SAFE_ANNOTATION_DESC = Type.getDescriptor(NullSafe.class);

	public NullSafeClassVisitor(ClassVisitor classVisitor, ClassInfo classInfo)
	{
		super(classVisitor, classInfo);
	}

	@Override
	public MethodVisitor visitMethodInformated(MethodInfo info, int access, String name, String descriptor, String signature, String[] exceptions)
	{
		MethodVisitor mv = super.visitMethodInformated(info, access, name, descriptor, signature, exceptions);
		
		if (hasNullsafe(info))
		{
			mv = new NullSafeMethodVisitor(info, mv, access, name, descriptor);
		}
		
		return mv;
	}
	
	/** Есть ли хотя бы в одном параметре метода аннотация {@link NullSafe} */
	public boolean hasNullsafe(MethodInfo methodInfo)
	{
		for (VariableInfo param : methodInfo.parameters)
		{
			if (param.hasAnnotation(NULL_SAFE_ANNOTATION_DESC))
				return true;
		}
		
		return false;
	}
}
