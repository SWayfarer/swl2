package ru.swayfarer.swl2.asm.transformer.detailednpe.visitor;

import ru.swayfarer.swl2.asm.AsmUtils;
import ru.swayfarer.swl2.asm.informated.MethodInfo;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.commons.AdviceAdapter;

public class DetailedNpeMethodVisitor extends AdviceAdapter{

	public MethodInfo methodInfo;
	
	public String lastLoadedName;
	
	public int varsOffset;
	
	public DetailedNpeMethodVisitor(MethodVisitor methodVisitor, int access, String name, String descriptor)
	{
		super(ASM7, methodVisitor, access, name, descriptor);
	}

	@Override
	protected void onMethodEnter()
	{
		
	}
	
	@Override
	public void visitMethodInsn(int opcodeAndSource, String owner, String name, String descriptor, boolean isInterface)
	{
		if (opcodeAndSource == INVOKESPECIAL)
		{
			int id = newLocal(Type.getType(owner));
			
		}
		
		super.visitMethodInsn(opcodeAndSource, owner, name, descriptor, isInterface);
	}
	
	@Override
	public void visitVarInsn(int opcode, int var)
	{
		if (AsmUtils.isObjectLoadOpcode(opcode))
		{
			lastLoadedName = methodInfo.localIdToName.get(opcode);
		}
		
		super.visitVarInsn(opcode, var);
	}
}
