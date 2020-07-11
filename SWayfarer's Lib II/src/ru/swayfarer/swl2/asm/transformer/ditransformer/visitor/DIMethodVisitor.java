package ru.swayfarer.swl2.asm.transformer.ditransformer.visitor;

import java.util.Map;

import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.transformer.ditransformer.DIClassTransformer;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.commons.AdviceAdapter;

/**
 * Посетитель методов, добавляюший загрузку DI в поля класса через конструкторы 
 * @author swayfarer
 *
 */
public class DIMethodVisitor extends AdviceAdapter {

	public DIClassTransformer diClassVisitor;
	
	public static ILogger logger = LoggingManager.getLogger();
	
	public IExtendedList<String> injectedConstructorDescs = CollectionsSWL.createExtendedList();
	public Map<String, Integer> invokeSpecialsReservedCounts = CollectionsSWL.createHashMap();
	
	/** Информация о классе */
	@InternalElement
	public ClassInfo classInfo;
	
	public String methodName;
	
	/** Конструктор */
	protected DIMethodVisitor(DIClassTransformer diClassVisitor, MethodVisitor methodVisitor, int access, String name, String descriptor, ClassInfo classInfo)
	{
		super(ASM7, methodVisitor, access, name, descriptor);
		this.classInfo = classInfo;
		this.methodName = name;
		this.diClassVisitor = diClassVisitor;
	}
	
	@Override
	public void visitMethodInsn(int opcodeAndSource, String owner, String name, String descriptor, boolean isInterface)
	{
		super.visitMethodInsn(opcodeAndSource, owner, name, descriptor, isInterface);
		
		if (methodName.equals("<init>") && opcodeAndSource == INVOKESPECIAL && name.equals("<init>"))
		{
			if (diClassVisitor.isLoggingBytecodeInjections)
				logger.info("Checking for injection constructor", classInfo.name, descriptor, invokeSpecialsReservedCounts.get(owner));
			
			if (!hasReservedInvokeSpecial(owner))
			{
				if (!isAlreadyInjectedTo(descriptor))
				{
					if (diClassVisitor.isLoggingBytecodeInjections)
					{
						logger.info("Injecting DIManager#injectContextElements to constructor", classInfo.name, methodDesc);
					}
					
					appendInjectMethod(this);
					
					setInjected(descriptor);
				}
				else
				{
					if (diClassVisitor.isLoggingBytecodeInjections)
						logger.warning("Is already injected to", classInfo.name, methodDesc);
				}
			}
			else
			{
				if (diClassVisitor.isLoggingBytecodeInjections)
					logger.warning("This invoke special is reserved by", owner);
			}
		}
	}
	
	
	
	public void reserveInvokeSpecial(String internalName)
	{
		Integer i = invokeSpecialsReservedCounts.get(internalName);
		invokeSpecialsReservedCounts.put(internalName, i == null ? 1 : i + 1);
	}
	
	public boolean hasReservedInvokeSpecial(String internalName)
	{
		Integer i = invokeSpecialsReservedCounts.get(internalName);
		invokeSpecialsReservedCounts.put(internalName, i == null ? 0 : i - 1);
		return i != null && i > 0;
	}
	
	public void appendInjectMethod(MethodVisitor mv)
	{
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESTATIC, diClassVisitor.contextClassInternalName, diClassVisitor.injectionMethodName, "(Ljava/lang/Object;)Ljava/lang/Object;", false);
		mv.visitInsn(POP);
	}
	
	public boolean isAlreadyInjectedTo(String desc)
	{
		return injectedConstructorDescs.contains(desc);
	}
	
	public void setInjected(String desc)
	{
		injectedConstructorDescs.addExclusive(desc);
	}
	
	@Override
	public void visitTypeInsn(int opcode, String type)
	{
		if (opcode == NEW)
			reserveInvokeSpecial(type);
		
		super.visitTypeInsn(opcode, type);
	}

}
