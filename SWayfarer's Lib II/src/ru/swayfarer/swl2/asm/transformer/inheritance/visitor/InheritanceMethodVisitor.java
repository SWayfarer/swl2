package ru.swayfarer.swl2.asm.transformer.inheritance.visitor;

import ru.swayfarer.swl2.asm.transformer.inheritance.InheritanceClassTransformer;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;

/**
 * {@link MethodVisitor} для {@link InheritanceClassTransformer} 
 * @author swayfarer
 *
 */
public class InheritanceMethodVisitor extends MethodVisitor{

	/** Старый и новый типы парентов */
	public String oldParentType, newParentType;
	
	/** Сколько <init> нужно пропустить? */
	public int initsToSkip;
	
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
