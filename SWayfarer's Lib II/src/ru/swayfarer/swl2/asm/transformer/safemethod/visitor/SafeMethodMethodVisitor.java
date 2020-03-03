package ru.swayfarer.swl2.asm.transformer.safemethod.visitor;

import ru.swayfarer.swl2.asm.AsmUtils;
import ru.swayfarer.swl2.asm.informated.AnnotationInfo;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.informated.FieldInfo;
import ru.swayfarer.swl2.asm.informated.MethodInfo;
import ru.swayfarer.swl2.asm.transformer.safemethod.SafeMethod;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Label;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.commons.AdviceAdapter;

/**
 * {@link MethodVisitor} для {@link SafeMethodClassVisitor} 
 * @author swayfarer
 *
 */
public class SafeMethodMethodVisitor extends AdviceAdapter {
	
	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Дефолтное сообщение, в которое будут подставляться актеры под конкретный {@link Throwable} */
	@InternalElement
	public static String DEFAULT_MESSAGE = "Error while runing method %className%::%methodName%[%methodDesc%]";
	
	/** Дефолтный тип отлавливаемого {@link Throwable}'а */
	@InternalElement
	public static Type DEFAULT_TYPE_OF_THROWABLE = Type.getType(Throwable.class);
	
	/** Дескриптор интерфейса {@link ILogger} */
	@InternalElement
	public static Type ILOGGER_TYPE = Type.getType(ILogger.class);
	
	/** Информация о трансформиуемом методе */
	@InternalElement
	public MethodInfo methodInfo;
	
	/** Информация об аннотации {@link SafeMethod}, которой отмечен метод */
	@InternalElement
	public AnnotationInfo annotationInfo;
	
	/** Начало блока try */
	@InternalElement
	public Label tryBlockStart = new Label();
	
	/** Конец блока try */
	@InternalElement
	public Label tryBlockEnd = new Label();
	
	/** Начало блока catch */
	@InternalElement
	public Label catchBlockStart = new Label();
	
	/** Конструктор */
	public SafeMethodMethodVisitor(MethodVisitor methodVisitor, int access, String name, String descriptor, MethodInfo methodInfo)
	{
		super(ASM7, methodVisitor, access, name, descriptor);
		this.methodInfo = methodInfo;
		annotationInfo = methodInfo.getFirstAnnotation(SafeMethodClassVisitor.SAFE_METHOD_ANNOTATION_DESCRIPTOR);
	}

	@Override
	public void visitCode()
	{
		Type typeOfThrowable = annotationInfo.getParam("typeOfThrowable", DEFAULT_TYPE_OF_THROWABLE);
		
		this.visitTryCatchBlock(tryBlockStart, tryBlockEnd, catchBlockStart, typeOfThrowable.getInternalName());
		this.visitLabel(tryBlockStart);
	}
	
	/** Добавить catch-блок */
	public void visitCatchBlock()
	{
		// Id последней на данный момент локалки (Это и есть Throwable) 
		int exceptionLocalId = nextLocal - 1;
		
		// Был ли сгенерирован лог через логгер? 
		boolean isLoggerGenerated = false;
		
		// Получаем сообщение, которое пойдет вместе с exception'ом в лог 
		String message = annotationInfo.getParam("message", DEFAULT_MESSAGE);
		
		message = message
			.replace("%methodName%", methodInfo.getName())
			.replace("%className%", methodInfo.getOwner().getCanonicalName())
			.replace("%methodDesc%", methodInfo.getDescriptor())
		;
		
		if (annotationInfo != null)
		{
			String loggerName = annotationInfo.getParam("logger", null);
			
			if (!StringUtils.isEmpty(loggerName))
			{
				ClassInfo ownerInfo = methodInfo.getOwner();
				FieldInfo loggerField = ownerInfo.findField(loggerName, ILOGGER_TYPE);
				
				// Если было найдено поле типа ILogger с указанным именем, то ошибка будет выводиться через него 
				if (loggerField == null)
				{
					logger.warning("Logger with name", loggerName, "not found in class", ownerInfo.getCanonicalName());
				}
				else
				{
					AsmUtils.getField(mv, loggerField);
					
					this.visitVarInsn(ALOAD, exceptionLocalId);
					this.visitInsn(ICONST_1);
					this.visitTypeInsn(ANEWARRAY, "java/lang/Object");
					this.visitInsn(DUP);
					this.visitInsn(ICONST_0);
					this.visitLdcInsn(message);
					this.visitInsn(AASTORE);
					this.visitMethodInsn(INVOKEINTERFACE, "ru/swayfarer/swl2/logger/ILogger", "error", "(Ljava/lang/Throwable;[Ljava/lang/Object;)V", true);
					
					isLoggerGenerated = true;
				}
			}
		}
		
		// Если не было сгенерировано вывода через логгер, то обратимся к стардартным средствам Java
		if (!isLoggerGenerated)
		{
			this.visitFieldInsn(GETSTATIC, "java/lang/System", "err", "Ljava/io/PrintStream;");
			this.visitLdcInsn(message);
			this.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
			
			this.visitVarInsn(ALOAD, exceptionLocalId);
			this.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Throwable", "printStackTrace", "()V", false);
		}
	}
	
	@Override
	public void visitMaxs(int maxStack, int maxLocals)
	{
		// На этом этапе, в не в visitEnd потому, что максы находятся с учетом try-catch'ей 
		
		this.visitLabel(tryBlockEnd);
		Label label3 = new Label();
		this.visitJumpInsn(GOTO, label3);
		this.visitLabel(catchBlockStart);
		this.visitVarInsn(ASTORE, Math.max(nextLocal - 1, 0));
		Label label4 = new Label();
		this.visitLabel(label4);
		
		visitCatchBlock();
		
		this.visitLabel(label3);
		
		AsmUtils.invokeReturn(this, getReturnType());
		
		super.visitMaxs(maxStack, maxLocals);
	}
}
