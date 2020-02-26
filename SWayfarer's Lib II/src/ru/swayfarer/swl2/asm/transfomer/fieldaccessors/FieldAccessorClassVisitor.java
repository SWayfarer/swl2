package ru.swayfarer.swl2.asm.transfomer.fieldaccessors;

import ru.swayfarer.swl2.asm.AsmUtils;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.informated.FieldInfo;
import ru.swayfarer.swl2.asm.informated.MethodInfo;
import ru.swayfarer.swl2.asm.informated.visitor.InformatedClassVisitor;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;

public class FieldAccessorClassVisitor extends InformatedClassVisitor {

	public FieldAccessorsClassTransformer classTransformer;
	public IExtendedList<String> fields;
	
	public FieldAccessorClassVisitor(ClassVisitor classVisitor, ClassInfo classInfo, FieldAccessorsClassTransformer classTransformer)
	{
		super(classVisitor, classInfo);
		this.classTransformer = classTransformer;
		fields = classTransformer.registeredFields.get(classInfo.getInternalName());
	}
	
	@Override
	public MethodVisitor visitMethodInformated(MethodInfo info, int access, String name, String descriptor, String signature, String[] exceptions)
	{
		MethodVisitor mv = super.visitMethodInformated(info, access, name, descriptor, signature, exceptions);
		return mv;
	}
	
	@Override
	public void visitEnd()
	{
		generateAccessors();
		super.visitEnd();
	}
	
	public void generateAccessors()
	{
		if (CollectionsSWL.isNullOrEmpty(fields))
			return;
		
		classInfo.fields.dataStream()
			.filter((f) -> fields.contains(f.getName()))
			.each(this::generateAccessors);
	}
	
	public void generateAccessors(FieldInfo info)
	{
		generateSetter(info);
		generateSetter(info);
	}
	
	public void generateGetter(FieldInfo info)
	{
		String methodName = classTransformer.fieldsGetterNameFun.apply(info);
		String methodDesc = "()" + info.descriptor;
		
		if (canGenerate(methodName, methodDesc))
		{
			MethodVisitor mv = visitMethod(ACC_PUBLIC, methodName, methodDesc, "", null);
			
			mv.visitCode();
			
			mv.visitVarInsn(ALOAD, 0);
			AsmUtils.getField(mv, info);
			AsmUtils.invokeReturn(mv, info.getType());
			
			mv.visitEnd();
		}
	}
	
	public void generateSetter(FieldInfo info)
	{
		String methodName = classTransformer.fieldsGetterNameFun.apply(info);
		String methodDesc = "(" + info.descriptor + ")Ljava/lang/Object;";
		
		if (canGenerate(methodName, methodDesc))
		{
			MethodVisitor mv = visitMethod(ACC_PUBLIC, methodName, methodDesc, "", null);
			
			mv.visitCode();
			
			mv.visitVarInsn(ALOAD, 0);
			AsmUtils.invokeLoad(mv, info.getType(), 1);
			AsmUtils.putField(mv, info);
			
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(ARETURN);
			
			mv.visitEnd();
		}
	}
	
	public boolean canGenerate(String methodName, String desc)
	{
		return classInfo.getMethod(methodName, desc) == null;
	}

}
