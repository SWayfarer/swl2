package ru.swayfarer.swl2.asm.transformer.injection.visitor;

import java.util.List;

import ru.swayfarer.swl2.asm.injection.IInjectionInfo;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.commons.AdviceAdapter;

public class InjectionMethodVisitor extends AdviceAdapter{

	public List<IInjectionInfo> injections;
	public boolean isAlreadyInCode = false;
	
	public InjectionMethodVisitor(List<IInjectionInfo> injections, MethodVisitor methodVisitor, int access, String name, String descriptor)
	{
		super(ASM7, methodVisitor, access, name, descriptor);
		this.injections = injections;
	}
	
	@Override
	public void visitMaxs(int maxStack, int maxLocals)
	{
		super.visitMaxs(maxStack, maxLocals);
	}
	
	@Override
	protected void onMethodEnter()
	{
		super.onMethodEnter();
		
		inject(false, -1);
	}
	
	public void inject(boolean isExit, int opcode)
	{
		if (!isAlreadyInCode)
		{
			for (IInjectionInfo injInfo : injections)
			{
//				Console.printCalltrace();
//				Console.info(injInfo.getTargetMethod(), (injInfo.isInjectOnExit() == isExit), methodDesc, injInfo.getTargetDescriptor(), methodDesc.startsWith(injInfo.getTargetDescriptor()));
//				
				System.out.println(methodDesc.substring(0, methodDesc.indexOf(")")+1));
				System.out.println(injInfo.getTargetDescriptor());
				if ((injInfo.isInjectOnExit() == isExit) && methodDesc.substring(0, methodDesc.indexOf(")")+1).equals(injInfo.getTargetDescriptor()))
				{
					isAlreadyInCode = true;
					injInfo.setTargetClassReturnType(getReturnType());
					injInfo.setTargetClassStatic((methodAccess & ACC_STATIC) != 0);
					injInfo.inject(this, opcode);
					isAlreadyInCode = false;
				}
			}
		}
	}
	
	@Override
	protected void onMethodExit(int opcode)
	{
		super.onMethodExit(opcode);
		
		inject(true, opcode);
	}
}
