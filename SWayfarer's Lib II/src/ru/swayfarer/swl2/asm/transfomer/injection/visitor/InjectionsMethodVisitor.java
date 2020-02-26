package ru.swayfarer.swl2.asm.transfomer.injection.visitor;

import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.informated.MethodInfo;
import ru.swayfarer.swl2.asm.transfomer.injection.IMethodInjection;
import ru.swayfarer.swl2.asm.transfomer.injection.InjectionsClassTransformer;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.commons.AdviceAdapter;

/** {@link MethodVisitor} трансформера {@link InjectionsClassTransformer} */
public class InjectionsMethodVisitor extends AdviceAdapter {

	public MethodInfo targetMethodInfo;
	public ClassInfo targetClassInfo;
	public IExtendedList<IMethodInjection> availableInjections;
	public boolean isAlreadyInCode;
	
	public InjectionsMethodVisitor(MethodVisitor methodVisitor, int access, String name, String descriptor, IExtendedList<IMethodInjection> availableInjections, ClassInfo targetClassInfo, MethodInfo targetMethodInfo)
	{
		super(ASM7, methodVisitor, access, name, descriptor);
		
		if (availableInjections == null)
			availableInjections = CollectionsSWL.createExtendedList();
		
		this.availableInjections = availableInjections;
		this.targetClassInfo = targetClassInfo;
		this.targetMethodInfo = targetMethodInfo;
	}
	
	@Override
	protected void onMethodEnter()
	{
		super.onMethodEnter();
		availableInjections.forEach((inj) -> inj.inject(targetClassInfo, targetMethodInfo, false, this));
	}
	
	@Override
	protected void onMethodExit(int opcode)
	{
		if (!isAlreadyInCode)
		{
			isAlreadyInCode = true;
			availableInjections.forEach((inj) -> inj.inject(targetClassInfo, targetMethodInfo, true, this));
			isAlreadyInCode = false;
			
			super.onMethodExit(opcode);
		}
	}
}
