package ru.swayfarer.swl2.asm.transformer.informated.visitor;

import ru.swayfarer.swl2.asm.AsmUtils;
import ru.swayfarer.swl2.asm.informated.AnnotationInfo;
import ru.swayfarer.swl2.asm.informated.MethodInfo;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.AnnotationVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Label;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Opcodes;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.commons.AdviceAdapter;

public class MethodScanner extends AdviceAdapter{

	public VarsScanner varScanner;
	public MethodInfo info;
	
	public MethodScanner(MethodVisitor methodVisitor, int access, String name, String descriptor, MethodInfo info)
	{
		super(ASM7, methodVisitor, access, name, descriptor);
		
		this.info = info;
		
		varScanner = new VarsScanner(info, methodVisitor, AsmUtils.getMethodArgumentsCount(descriptor), (access & Opcodes.ACC_STATIC) != 0);
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String descriptor, boolean visible)
	{
		AnnotationInfo info = new AnnotationInfo();
		info.descritor = descriptor;
		this.info.annotations.add(info);
		
		varScanner.visitAnnotation(descriptor, visible);
		
		return new AnnotationScanner(info, super.visitAnnotation(descriptor, visible));
	}
	
	@Override
	public void visitCode()
	{
		varScanner.visitCode();
		super.visitCode();
	}
	
	@Override
	public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible)
	{
		return varScanner.visitParameterAnnotation(parameter, descriptor, visible, super.visitParameterAnnotation(parameter, descriptor, visible));
	}
	
	@Override
	public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index)
	{
		varScanner.visitLocalVariable(name, descriptor, signature, start, end, index);
		super.visitLocalVariable(name, descriptor, signature, start, end, index);
	}
	
	@Override
	public void visitMaxs(int maxStack, int maxLocals)
	{
		varScanner.visitMaxs(maxStack, maxLocals);
		super.visitMaxs(maxStack, maxLocals);
	}

}
