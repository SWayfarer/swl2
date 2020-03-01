package ru.swayfarer.swl2.asm.transfomer.threadlocal;

import java.util.UUID;

import ru.swayfarer.swl2.asm.AsmUtils;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.informated.MethodInfo;
import ru.swayfarer.swl2.asm.informated.VariableInfo;
import ru.swayfarer.swl2.asm.informated.visitor.InformatedClassVisitor;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Label;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Opcodes;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;

/**
 * {@link ClassVisitor} для {@link ThreadLocalClassTransformer} 
 * @author swayfarer
 *
 */
public class ThreadLocalClassVisitor extends InformatedClassVisitor {

	/** Функция, генерирующая название метода, генерящего {@link ThreadLocal} значение */
	@InternalElement
	public IFunction1<String, String> threadLocalMethodNameFun = (name) -> "threadLocal_" + name;
	
	/** Дескриптор аннотации {@link ThreadLocal} */
	@InternalElement
	public static String THREAD_LOCAL_ANNOTATION_DESC = Type.getDescriptor(ThreadLocal.class);
	
	/** Есть ли методы, отмеченные {@link ThreadLocal} */
	@InternalElement
	public boolean hasThreadLocalMethods;
	
	/** "Ключи" для методов (см {@link #getMethodKey(String, String, String)}), по которым методы проверяются на аннотированность {@link #THREAD_LOCAL_ANNOTATION_DESC} */
	@InternalElement
	public IExtendedList<String> annotatedMethodKeys = CollectionsSWL.createExtendedList();
	
	@InternalElement
	public IExtendedList<MethodInfo> annotatedMethodsInfo = CollectionsSWL.createExtendedList();
	
	/** Началась ли генерация методов */
	@InternalElement
	public boolean isGenetationStarted = false;
	
	/** Конструктор */
	public ThreadLocalClassVisitor(ClassVisitor classVisitor, ClassInfo classInfo)
	{
		super(classVisitor, classInfo);
		
		for (MethodInfo methodInfo : classInfo.methods)
		{
			if (methodInfo.hasAnnotation(THREAD_LOCAL_ANNOTATION_DESC))
			{
				hasThreadLocalMethods = true;
				annotatedMethodsInfo.add(methodInfo);
				annotatedMethodKeys.add(getMethodKey(methodInfo.owner.getInternalName(), methodInfo.name, methodInfo.descriptor)); 
			}
		}
	}
	
	/** Получить преобразованное имя метода (на этом этапе проиходит редирект некоторых методов) */
	public String getNewMethodName(String owner, String name, String desc)
	{
		String key = getMethodKey(owner, name, desc);
		
		if (annotatedMethodKeys.contains(key))
		{
			return threadLocalMethodNameFun.apply(name);
		}
		
		return name;
	}
	
	/** Получить ключ метода для хранения в списке {@link #annotatedMethodKeys} */
	public String getMethodKey(String owner, String name, String desc)
	{
		return StringUtils.concat("::", owner, name, desc);
	}

	@Override
	public MethodVisitor visitMethodInformated(MethodInfo info, int access, String name, String descriptor, String signature, String[] exceptions)
	{
		MethodVisitor mv = super.visitMethodInformated(info, access, name, descriptor, signature, exceptions);
		
		if (!isGenetationStarted && hasThreadLocalMethods)
			mv = new ThreadLocalMethodVisitor(mv, access, name, descriptor, this);
		
		return mv;
	}
	
	@Override
	public void visitEnd()
	{
		generateThreadLocalAccesors();
	}
	
	/** Генерация методов-аксессоров для {@link ThreadLocal}'ов */
	public void generateThreadLocalAccesors()
	{
		isGenetationStarted = true;
		annotatedMethodsInfo.forEach(this::generateThreadLocalAccesor);
		isGenetationStarted = false;
	}
	
	/** Сгенерировать аксессор для метода */
	public void generateThreadLocalAccesor(MethodInfo methodInfo)
	{
		int invokeOpcode = methodInfo.isStatic() ? Opcodes.INVOKESTATIC : Opcodes.INVOKEVIRTUAL;
		
		String key = UUID.randomUUID().toString() + "_" + UUID.randomUUID().toString();
		int nextId = 0;
		
		String ownerInternalName = methodInfo.owner.getInternalName();
		String name = threadLocalMethodNameFun.apply(methodInfo.name);
		String desc = methodInfo.descriptor;
		
		MethodVisitor mv = visitMethod(methodInfo.isStatic() ? ACC_PUBLIC | ACC_STATIC : ACC_PUBLIC, name, desc, null, null);
		mv.visitCode();
		Label label0 = new Label();
		mv.visitLabel(label0);
		mv.visitLdcInsn(key);
		mv.visitMethodInsn(INVOKESTATIC, "ru/swayfarer/swl2/asm/transfomer/threadlocal/ThreadLocalContainer", "hasRegisteredThreadLocal", "(Ljava/lang/String;)Z", false);
		Label label1 = new Label();
		mv.visitJumpInsn(IFNE, label1);
		Label label2 = new Label();
		mv.visitLabel(label2);
		mv.visitLdcInsn(key);
		
		if (!methodInfo.isStatic())
			AsmUtils.invokeLoad(mv, methodInfo.owner.getType(), nextId ++);
		
		for (VariableInfo param : methodInfo.parameters)
		{
			AsmUtils.invokeLoad(mv, param.getType(), nextId ++);
			AsmUtils.invokeObjectCheckcast(mv, param.getType());
		}
		
		mv.visitMethodInsn(invokeOpcode, ownerInternalName, methodInfo.name, methodInfo.descriptor, false);
		mv.visitMethodInsn(INVOKESTATIC, "ru/swayfarer/swl2/asm/transfomer/threadlocal/ThreadLocalContainer", "setThreadLocalObject", "(Ljava/lang/String;Ljava/lang/Object;)V", false);
		mv.visitLabel(label1);
		mv.visitLdcInsn(key);
		mv.visitMethodInsn(INVOKESTATIC, "ru/swayfarer/swl2/asm/transfomer/threadlocal/ThreadLocalContainer", "getThreadLocalObject", "(Ljava/lang/String;)Ljava/lang/Object;", false);
		
		AsmUtils.invokeObjectCheckcast(mv, methodInfo.getReturnType());
		mv.visitInsn(ARETURN);
		
		mv.visitMaxs(methodInfo.maxStack, methodInfo.maxLocals);
		mv.visitCode();
	}

}
