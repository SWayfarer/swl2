package ru.swayfarer.swl2.asm.transformer.nullsafe.visitor;

import ru.swayfarer.swl2.asm.AsmUtils;
import ru.swayfarer.swl2.asm.informated.AnnotationInfo;
import ru.swayfarer.swl2.asm.informated.MethodInfo;
import ru.swayfarer.swl2.asm.informated.VariableInfo;
import ru.swayfarer.swl2.asm.transformer.nullsafe.NullSafeClassTransformer;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.commons.AdviceAdapter;

/**
 * {@link MethodVisitor} для {@link NullSafeClassTransformer}'а
 * @author swayfarer
 *
 */
public class NullSafeMethodVisitor extends AdviceAdapter {

	/** Тип бросаемого исключения по умолчанию */
	@InternalElement
	public static Type DEFAULT_EXCEPTION_TYPE = Type.getType(IllegalArgumentException.class);
	
	/** Логгер */
	@InternalElement
	public ILogger logger = LoggingManager.getLogger();
	
	/** Информация об обрабатываемом методе  */
	@InternalElement
	public MethodInfo methodInfo;
	
	/** Конструтор */
	public NullSafeMethodVisitor(MethodInfo methodInfo, MethodVisitor methodVisitor, int access, String name, String descriptor)
	{
		super(ASM7, methodVisitor, access, name, descriptor);
		this.methodInfo = methodInfo;
	}

	@Override
	protected void onMethodEnter()
	{
		int startVarId = methodInfo.isStatic() ? 0 : 1;
		
		for (VariableInfo param : methodInfo.parameters)
		{
			AnnotationInfo annotationInfo = param.getFirstAnnotation(NullSafeClassVisitor.NULL_SAFE_ANNOTATION_DESC);
			
			if (annotationInfo != null)
			{
				
				Type exceptionType = annotationInfo.getParam("exception", DEFAULT_EXCEPTION_TYPE);
				String message = annotationInfo.getParam("message", null);
				
				if (AsmUtils.isPrimitive(param.getType()))
				{
					logger.warning("Parameter '" + param.name + "' of method '" + methodInfo.owner + "::" + methodInfo.name + "' must can't be primitive for @NullSafe!");
					continue;
				}
				
				visitVarInsn(ALOAD, startVarId);
				visitLdcInsn(exceptionType);
				visitInsn(ICONST_1);
				visitTypeInsn(ANEWARRAY, "java/lang/Object");
				visitInsn(DUP);
				visitInsn(ICONST_0);
				
				if (StringUtils.isEmpty(message))
				{
					visitLdcInsn(param.name);
					visitLdcInsn(methodInfo.name);
					visitLdcInsn(methodInfo.owner.name.replace("/", "."));
					visitMethodInsn(INVOKESTATIC, Type.getInternalName(NullSafeClassTransformer.class), "getFormattedMessage", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", false);
				}
				else
				{
					message = message
						.replace("%className%", methodInfo.getOwner().getCanonicalName())
						.replace("%methodName%", methodInfo.name)
						.replace("%parameterName%", param.getName())
						.replace("%methodDescriptor%", methodInfo.getDescriptor())
					;
					
					visitLdcInsn(message);
				}
				
				visitInsn(AASTORE);
				visitMethodInsn(INVOKESTATIC, "ru/swayfarer/swl2/exceptions/ExceptionsUtils", "IfNull", "(Ljava/lang/Object;Ljava/lang/Class;[Ljava/lang/Object;)V", false);
			}
			
			startVarId ++;
		}
	}
}
