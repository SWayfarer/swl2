package ru.swayfarer.swl2.asm.transformer.accesstransformer.visitor;

import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.FieldVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Opcodes;

public class AccessVisitor extends ClassVisitor{

	public boolean isSkiping = false;
	
	public AccessVisitor(ClassVisitor cv) 
	{
		super(Opcodes.ASM5, cv);
	}
	
	public int getAccess(int acc)
	{
		return isSkiping ? acc : AccessTypes.openAccess(acc);
	}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {

		isSkiping = AccessTypes.isInterface(access) || (signature != null && (signature.contains("Ljava/lang/Enum")));
		
		cv.visit(version, getAccess(access), name, signature, superName, interfaces);
	}
	
	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) 
	{
		return cv.visitField(getAccess(access), name, desc, signature, value);
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
	{
		return cv.visitMethod(getAccess(access), name, desc, signature, exceptions);
	}
	
}
