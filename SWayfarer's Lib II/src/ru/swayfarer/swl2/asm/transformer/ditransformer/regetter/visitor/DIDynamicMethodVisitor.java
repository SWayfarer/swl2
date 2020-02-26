package ru.swayfarer.swl2.asm.transformer.ditransformer.regetter.visitor;

import ru.swayfarer.swl2.asm.informated.AnnotationInfo;
import ru.swayfarer.swl2.asm.informated.FieldInfo;
import ru.swayfarer.swl2.asm.transformer.ditransformer.DIClassTransformer;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.commons.AdviceAdapter;

/**
 * {@link MethodVisitor} трансформера {@link DIClassTransformer}
 * @author swayfarer
 *
 */
public class DIDynamicMethodVisitor extends AdviceAdapter {

	/** {@link ClassVisitor}, для которого был создан этот {@link MethodVisitor} */
	@InternalElement
	public DIDynamicClassVisitor classVisitor;
	
	/** Дескриптор аннотации {@link DynamicDI} */
	@InternalElement
	public static String UPDATE_ANNOTATION_DESC = Type.getDescriptor(DynamicDI.class);
	
	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Конструктор */
	public DIDynamicMethodVisitor(MethodVisitor methodVisitor, int access, String name, String descriptor, DIDynamicClassVisitor classVisitor)
	{
		super(ASM7, methodVisitor, access, name, descriptor);
		this.classVisitor = classVisitor;
	}

	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String descriptor)
	{
		if (opcode == GETFIELD)	
		{
			FieldInfo field = classVisitor.classInfo.getField(name);
			
			if (field != null && !field.isPrimitive())
			{
				AnnotationInfo updateAnnotation = field.getFirstAnnotation(UPDATE_ANNOTATION_DESC);
				
				if (updateAnnotation != null)
				{
					visitMethodInsn(INVOKEVIRTUAL, owner, classVisitor.getMethodName(field), classVisitor.getMethodDesc(field), false);
					classVisitor.addGenFieldInfo(field);
					
					return;
				}
			}
		}
		
		super.visitFieldInsn(opcode, owner, name, descriptor);
	}
}
