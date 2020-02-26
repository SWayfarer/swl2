package ru.swayfarer.swl2.asm.transfomer.fieldaccessors;

import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.commons.AdviceAdapter;

public class FieldAccessorMethodVisitor extends AdviceAdapter {

	public FieldAccessorClassVisitor classVisitor;
	
	protected FieldAccessorMethodVisitor(MethodVisitor methodVisitor, int access, String name, String descriptor, FieldAccessorClassVisitor classVisitor)
	{
		super(ASM7, methodVisitor, access, name, descriptor);
		this.classVisitor = classVisitor;
	}
}
