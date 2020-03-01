package ru.swayfarer.swl2.asm.transfomer.threadlocal;

import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.commons.AdviceAdapter;

/**
 * {@link MethodVisitor} для {@link ThreadLocalClassTransformer}'а 
 * @author swayfarer
 *
 */
public class ThreadLocalMethodVisitor extends AdviceAdapter {

	/** Визитор классов, к которому прикреплен этот визитор*/
	@InternalElement
	public ThreadLocalClassVisitor classVisitor;
	
	public ThreadLocalMethodVisitor(MethodVisitor methodVisitor, int access, String name, String descriptor, ThreadLocalClassVisitor classVisitor)
	{
		super(ASM7, methodVisitor, access, name, descriptor);
		this.classVisitor = classVisitor;
	}

	@Override
	public void visitMethodInsn(int opcodeAndSource, String owner, String name, String descriptor, boolean isInterface)
	{
		super.visitMethodInsn(opcodeAndSource, owner, classVisitor.getNewMethodName(owner, name, descriptor), descriptor, isInterface);
	}
}
