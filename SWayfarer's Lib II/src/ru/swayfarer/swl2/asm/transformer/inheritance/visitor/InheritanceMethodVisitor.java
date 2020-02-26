package ru.swayfarer.swl2.asm.transformer.inheritance.visitor;

import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;

public class InheritanceMethodVisitor extends MethodVisitor{

	public String oldParentType, newParentType;
	public int initsToSkip;
	
	//methodVisitor.visitTypeInsn(NEW, "Codetest");
	//methodVisitor.visitMethodInsn(INVOKESPECIAL, "Codetest", "<init>", "()V", false);
	
	public InheritanceMethodVisitor(MethodVisitor methodVisitor, String oldParentType, String newParentType)
	{
		super(ASM7, methodVisitor);
		this.oldParentType = oldParentType;
		this.newParentType = newParentType;
	}
	
	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface)
	{
		if (opcode == INVOKESPECIAL && owner.equals(oldParentType) && descriptor.endsWith("V") && isInterface == false)
		{
			if (name.equals("<init>"))
			{
				if (initsToSkip > 0)
					initsToSkip --;
				else
					owner = newParentType;
			}
			else
			{
				owner = newParentType;
			}
		}
		
		super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
	}
	
	@Override
	public void visitTypeInsn(int opcode, String type)
	{
		if (opcode == NEW && type.equals(oldParentType))
			initsToSkip ++;
		
		super.visitTypeInsn(opcode, type);
	}
	

}
