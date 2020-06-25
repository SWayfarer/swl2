package ru.swayfarer.swl2.asm.transformer.ditransformer.visitor;

import static ru.swayfarer.swl2.asm.transformer.ditransformer.DIClassTransformer.DI_ANNOTATION_DESC;

import java.util.Map;

import ru.swayfarer.swl2.asm.AsmUtils;
import ru.swayfarer.swl2.asm.informated.AnnotationInfo;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.informated.FieldInfo;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.ioc.DIManager;
import ru.swayfarer.swl2.ioc.DIRegistry;
import ru.swayfarer.swl2.ioc.DIManager.DISwL;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.commons.AdviceAdapter;

/**
 * Посетитель методов, добавляюший загрузку DI в поля класса через конструкторы 
 * @author swayfarer
 *
 */
public class DIMethodVisitor extends AdviceAdapter {

	public static ILogger logger = LoggingManager.getLogger();
	
	public IExtendedList<String> injectedConstructorDescs = CollectionsSWL.createExtendedList();
	public Map<String, Integer> invokeSpecialsReservedCounts = CollectionsSWL.createHashMap();
	
	/** Будет ли при иньекции все происходить через {@link DIManager#injectContextElements(String, Object)}? */
	public static boolean injectByReflection = true;
	
	/** Информация о классе */
	@InternalElement
	public ClassInfo classInfo;
	
	public String methodName;
	
	/** Конструктор */
	protected DIMethodVisitor(MethodVisitor methodVisitor, int access, String name, String descriptor, ClassInfo classInfo)
	{
		super(ASM7, methodVisitor, access, name, descriptor);
		this.classInfo = classInfo;
		this.methodName = name;
	}
	
	@Override
	public void visitMethodInsn(int opcodeAndSource, String owner, String name, String descriptor, boolean isInterface)
	{
		super.visitMethodInsn(opcodeAndSource, owner, name, descriptor, isInterface);
		
		if (methodName.equals("<init>") && opcodeAndSource == INVOKESPECIAL && name.equals("<init>"))
		{
			if (DIRegistry.isLoggingBytecodeInjections)
				logger.info("Checking for injection constructor", classInfo.name, descriptor, invokeSpecialsReservedCounts.get(owner));
			
			if (!hasReservedInvokeSpecial(owner))
			{
				if (!isAlreadyInjectedTo(descriptor))
				{
					if (DIRegistry.isLoggingBytecodeInjections)
					{
						logger.info("Injecting DIManager#injectContextElements to constructor", classInfo.name, methodDesc);
					}
					
					appendFieldsInits(this);
					
					setInjected(descriptor);
				}
				else
				{
					if (DIRegistry.isLoggingBytecodeInjections)
						logger.warning("Is already injected to", classInfo.name, methodDesc);
				}
			}
			else
			{
				if (DIRegistry.isLoggingBytecodeInjections)
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
	
//	/** Посещение инструкции байткода */
//	@Override
//	public void visitInsn(int opcode)
//	{
//		if (getName().equals("<init>") && opcode == RETURN)
//		{
//			appendFieldsInits(this);
//		}
//		
//		super.visitInsn(opcode);
//	}
	
	/** Добавить инициализацию всех отмеченных {@link DISwL} полей */
	public void appendFieldsInits(MethodVisitor mv)
	{
		if (classInfo.fields.stream().anyMatch((field) -> field.findAnnotationRec(DI_ANNOTATION_DESC) != null))
		{
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, DIClassVisitor.CONTEXT_GET_CLASS_NAME, "injectContextElements", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
			mv.visitInsn(POP);
		}
	}
	
	public boolean isAlreadyInjectedTo(String desc)
	{
		return injectedConstructorDescs.contains(desc);
	}
	
	public void setInjected(String desc)
	{
		injectedConstructorDescs.addExclusive(desc);
	}
	
	/** Дописать инициализацию в конкретное поле */
	public static void appendFieldInit(MethodVisitor mv, FieldInfo fieldInfo, boolean put)
	{
		if (!fieldInfo.isStatic())
		{
			AnnotationInfo annotationInfo = fieldInfo.findAnnotationRec(DI_ANNOTATION_DESC);
			
			if (annotationInfo != null)
			{
				String contextName = annotationInfo.getParam("context");
				
				String elementName = annotationInfo.getParam("name");
				
				String elementType = annotationInfo.getParam("type");
				
				if (StringUtils.isEmpty(contextName))
				{
					AnnotationInfo classAnnotationInfo = fieldInfo.owner.findAnnotationRec(DI_ANNOTATION_DESC);
					
					if (classAnnotationInfo != null)
					{
						contextName = classAnnotationInfo.getParam("context");
					}
				}
				
				if (StringUtils.isEmpty(contextName))
					contextName = "default";
				
				if (StringUtils.isEmpty(elementName))
					elementName = fieldInfo.name;
				
				if (StringUtils.isEmpty(elementType))
					elementType = fieldInfo.descriptor;
				else
					elementType = AsmUtils.toInternalName(elementType);
				
				appendFieldInit(mv, fieldInfo, elementType, elementName, contextName, put);
			}
		}
	}
	
	@Override
	public void visitTypeInsn(int opcode, String type)
	{
		if (opcode == NEW)
			reserveInvokeSpecial(type);
		
		super.visitTypeInsn(opcode, type);
	}
	
	/** Дописать инициализацию в конкретное поле */
	public static void appendFieldInit(MethodVisitor mv, FieldInfo fieldInfo, String type, String elementName, String contextName, boolean put)
	{
		mv.visitVarInsn(fieldInfo.owner.getType().getOpcode(ILOAD), 0);
		mv.visitLdcInsn(contextName);
		mv.visitLdcInsn(elementName);
		
		String fieldInternalName = type.substring(1, type.length() - 1);
		String fieldCanonicalName = fieldInternalName.replace("/", ".");
		
		mv.visitLdcInsn(fieldCanonicalName);
		mv.visitMethodInsn(INVOKESTATIC, DIClassVisitor.CONTEXT_GET_CLASS_NAME, DIClassVisitor.CONTEXT_GET_METHOD_NAME, "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;", false);
		mv.visitTypeInsn(CHECKCAST, fieldInternalName);
		
		if (put)
			mv.visitFieldInsn(PUTFIELD, fieldInfo.owner.name, fieldInfo.name, fieldInfo.descriptor);
	}

}
