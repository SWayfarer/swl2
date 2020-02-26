package ru.swayfarer.swl2.asm.transformer.ditransformer.visitor;

import static ru.swayfarer.swl2.asm.transformer.ditransformer.DIClassTransformer.DI_ANNOTATION_DESC;

import ru.swayfarer.swl2.asm.AsmUtils;
import ru.swayfarer.swl2.asm.informated.AnnotationInfo;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.informated.FieldInfo;
import ru.swayfarer.swl2.ioc.DIManager;
import ru.swayfarer.swl2.ioc.DIManager.DISwL;
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

	/** Будет ли при иньекции все происходить через {@link DIManager#injectContextElements(String, Object)}? */
	public static boolean injectByReflection = true;
	
	/** Информация о классе */
	@InternalElement
	public ClassInfo classInfo;
	
	/** Конструктор */
	protected DIMethodVisitor(MethodVisitor methodVisitor, int access, String name, String descriptor, ClassInfo classInfo)
	{
		super(ASM7, methodVisitor, access, name, descriptor);
		this.classInfo = classInfo;
	}
	
	/** Посещение инструкции байткода */
	@Override
	public void visitInsn(int opcode)
	{
		if (getName().equals("<init>") && opcode == RETURN)
		{
			appendFieldsInits(this);
		}
		
		super.visitInsn(opcode);
	}
	
	/** Добавить инициализацию всех отмеченных {@link DISwL} полей */
	public void appendFieldsInits(MethodVisitor mv)
	{
		if (injectByReflection && classInfo.fields.stream().anyMatch((field) -> field.hasAnnotation(DI_ANNOTATION_DESC)))
		{
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, DIClassVisitor.CONTEXT_GET_CLASS_NAME, "injectContextElements", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
			mv.visitInsn(POP);
		}
		else
		{
			for (FieldInfo fieldInfo : classInfo.fields)
			{
				appendFieldInit(mv, fieldInfo, true);
			}
		}
	}
	
	/** Дописать инициализацию в конкретное поле */
	public static void appendFieldInit(MethodVisitor mv, FieldInfo fieldInfo, boolean put)
	{
		if (!fieldInfo.isStatic() && fieldInfo.hasAnnotation(DI_ANNOTATION_DESC))
		{
			AnnotationInfo annotationInfo = fieldInfo.getFirstAnnotation(DI_ANNOTATION_DESC);
			
			if (annotationInfo != null)
			{
				System.out.println("Annotation not null for " + fieldInfo.name);
				
				String contextName = annotationInfo.getParam("context");
				
				String elementName = annotationInfo.getParam("name");
				
				String elementType = annotationInfo.getParam("type");
				
				if (StringUtils.isEmpty(contextName))
				{
					AnnotationInfo classAnnotationInfo = fieldInfo.owner.getFirstAnnotation(DI_ANNOTATION_DESC);
					
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
