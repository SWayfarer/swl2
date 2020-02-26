package ru.swayfarer.swl2.asm.transformer.ditransformer.regetter.visitor;

import ru.swayfarer.swl2.asm.AsmUtils;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.informated.FieldInfo;
import ru.swayfarer.swl2.asm.informated.MethodInfo;
import ru.swayfarer.swl2.asm.informated.visitor.InformatedClassVisitor;
import ru.swayfarer.swl2.asm.transformer.ditransformer.visitor.DIMethodVisitor;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;

public class DIDynamicClassVisitor extends InformatedClassVisitor {

	public static ILogger logger = LoggingManager.getLogger();
	
	public IExtendedList<String> fieldsToGenerate = CollectionsSWL.createExtendedList();
	
	public DIDynamicClassVisitor(ClassVisitor classVisitor, ClassInfo classInfo)
	{
		super(classVisitor, classInfo);
	}
	
	@Override
	public MethodVisitor visitMethodInformated(MethodInfo info, int access, String name, String descriptor, String signature, String[] exceptions)
	{
		MethodVisitor cv = super.visitMethodInformated(info, access, name, descriptor, signature, exceptions);
		cv = new DIDynamicMethodVisitor(cv, access, name, descriptor, this);
		return cv;
	}
	
	@Override
	public void visitEnd()
	{
		generateGetters();
		super.visitEnd();
	}
	
	public void addGenFieldInfo(FieldInfo fieldInfo)
	{
		fieldsToGenerate.addExclusive(fieldInfo.getName());
	}
	
	public void generateGetters()
	{
		fieldsToGenerate.dataStream()
			.mapped((s) -> classInfo.getField(s))
			.each(this::generateMethod);
	}
	
	public boolean generateMethod(FieldInfo field)
	{
		String methodName = getMethodName(field);
		String methodDesc = getMethodDesc(field);
		
		if (classInfo.getMethod(methodName, methodDesc) != null)
			return false;
		
		MethodVisitor mv = visitMethod(ACC_PUBLIC, methodName, methodDesc, "", null);
		
		mv.visitCode();
			DIMethodVisitor.appendFieldInit(mv, field, false);
			AsmUtils.invokeReturn(mv, field.getType());
			mv.visitMaxs(2, 2);
		mv.visitEnd();
		
		return true;
	}
	
	public String getMethodName(FieldInfo fieldInfo)
	{
		return StringUtils.toCamelCase(true, "get", "updated", fieldInfo.name, "internal");
	}
	
	public String getMethodDesc(FieldInfo fieldInfo)
	{
		return "()" + fieldInfo.descriptor;
	}

}
