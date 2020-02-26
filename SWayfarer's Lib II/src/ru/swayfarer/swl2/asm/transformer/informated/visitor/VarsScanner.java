package ru.swayfarer.swl2.asm.transformer.informated.visitor;

import ru.swayfarer.swl2.asm.informated.AnnotationInfo;
import ru.swayfarer.swl2.asm.informated.MethodInfo;
import ru.swayfarer.swl2.asm.informated.VariableInfo;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.AnnotationVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Label;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;

public class VarsScanner extends MethodVisitor{
	
	public int paramsCount;
	public MethodInfo info;
	public boolean isStatic;
	
	public int nextParamIndex;
	
	public VarsScanner(MethodInfo info, MethodVisitor mv, int paramsCount, boolean isStatic)
	{
		super(ASM7, mv);
		this.info = info;
		this.paramsCount = paramsCount;
		this.isStatic = isStatic;
		
		info.paramsCount = paramsCount;
	}
	
	@Override
	public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index)
	{
		VariableInfo info = new VariableInfo(index, name, descriptor, signature);
		
		if (isThisVar())
		{
			nextParamIndex ++;
		}
		else if (isNextLocalVarParam())
		{
			this.info.addParam(nextParamIndex, info);
			nextParamIndex ++;
		}
		
		this.info.addVariable(info);
	}
	
	public boolean isThisVar()
	{
		return !isStatic && nextParamIndex == 0;
	}
	
	public boolean isNextLocalVarParam()
	{
		if (isStatic)
			return nextParamIndex < paramsCount;
		
		return nextParamIndex <= paramsCount;
	}
	
	public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible, AnnotationVisitor av)
	{
		AnnotationInfo info = new AnnotationInfo();
		info.descritor = descriptor;
		
		this.info.cacheAnnotation(isStatic ? parameter : parameter + 1, info);
			
		AnnotationScanner scanner = new AnnotationScanner(info, av);
		
		return scanner;
	}
	
	@Override
	public void visitMaxs(int maxStack, int maxLocals)
	{
		this.info.maxStack = maxStack;
		this.info.maxLocals = maxLocals;
	}

}
