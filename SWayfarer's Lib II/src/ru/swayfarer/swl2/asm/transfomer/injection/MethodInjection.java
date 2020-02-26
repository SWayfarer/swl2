package ru.swayfarer.swl2.asm.transfomer.injection;

import com.sun.xml.internal.ws.org.objectweb.asm.Opcodes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.swayfarer.swl2.asm.AsmUtils;
import ru.swayfarer.swl2.asm.informated.AnnotationInfo;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.informated.MethodInfo;
import ru.swayfarer.swl2.asm.informated.VariableInfo;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.SimpleObservable;
import ru.swayfarer.swl2.reference.IReference;
import ru.swayfarer.swl2.reference.SimpleReference;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Label;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.commons.AdviceAdapter;

/**
 * Иньекция в метод
 */
public class MethodInjection implements IMethodInjection, Opcodes {

	/** Событие пост-создания иньекции */
	public static IObservable<InjectionCreationEvent> eventCreation = new SimpleObservable<>();
	
	/** Дескриптор аннотации, которой отмечаются иньектируемые методы */
	@InternalElement
	public static String INJECTION_ANNOTATION_DESC = Type.getDescriptor(InjectionAsm.class);
	
	/** Дескриптор аннотации, отмечающий ссылку на возвращаемое значение */
	@InternalElement
	public static String RETURN_REF_ANNOTATION_DESC = Type.getDescriptor(ReturnRef.class);
	
	public static Type RETURN_REF_INSTANCE_TYPE = Type.getType(SimpleReference.class);
	public static String RETURN_REF_INSTANCE_INTERNAL_NAME	= RETURN_REF_INSTANCE_TYPE.getInternalName();
	
	/** Дескриптор ссылки {@link IReference} */
	@InternalElement
	public static String REF_DESCRIPTOR = Type.getDescriptor(IReference.class);
	
	/** Логгер*/
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Имя метода, в который будет производиться иньекция */
	@InternalElement @Getter @Setter
	public String targetMethodName;
	
	/** Дескриптор метода, в который будет производиться иньекция */
	@InternalElement @Getter @Setter
	public String targetMethodDesc;
	
	/** Имя класса, в который будет производиться иньекция */
	@InternalElement @Getter @Setter
	public String targetClassInternalName;
	
	/** Информация о методе, который вставляется */
	@InternalElement
	public MethodInfo injectedMethodInfo;
	
	/** Есть ли ссылка на возвращаемое значение? */
	@InternalElement
	public boolean hasReturnRef;
	
	/** Вставлять ли на выходе? */
	@InternalElement
	public boolean injectOnExit;

	/** Иньекция метода через {@link MethodVisitor} */
	@InternalElement
	@Override
	public boolean inject(ClassInfo targetClassInfo, MethodInfo targetMethodInfo, boolean atEnd, AdviceAdapter methodVisitor)
	{
		if (atEnd != injectOnExit)
			return false;
		
		try
		{
			int currentParamId = 0;
			ReturnRefInfo returnRefInfo = null;
			
			if (hasReturnRef)
			{
				returnRefInfo = preInjectReturnRef(targetClassInfo, targetMethodInfo, atEnd, methodVisitor);
			}
			
			/*
			 * Если метод статический, то нужно подгрузить null в первый слот метода
			 */
			if (targetMethodInfo.isStatic())
			{
				methodVisitor.visitInsn(ACONST_NULL);
			}
			else
			{
				// Иначе кладем в первый слот this
				AsmUtils.invokeLoad(methodVisitor, targetClassInfo.getType(), currentParamId);
				currentParamId ++;
			}
			
			// Загружаем все параметры метода-цели на стек, чтобы передать их во втавляемый метод
			for (VariableInfo parameterInfo : targetMethodInfo.parameters)
			{
				System.out.println("Adding " + parameterInfo);
				methodVisitor.visitVarInsn(parameterInfo.getType().getOpcode(ILOAD), currentParamId ++);
			}
			
			if (hasReturnRef)
				methodVisitor.visitVarInsn(ALOAD, returnRefInfo.getReturnRefId());
			
			// Вызываем метод
			methodVisitor.visitMethodInsn(INVOKESTATIC, getInjectedClassInternalName(), getInjectedMethodName(), getInjectedMethodDesc(), false);
			
			// Тип, возвращаемый вставляемым методом
			Type injectedMethodReturnType = injectedMethodInfo.getReturnType();
			
			// Если вставляемый метод не был void, то нужно убрать со стека результат его работы 
			if (injectedMethodReturnType != Type.VOID_TYPE)
				methodVisitor.visitInsn(injectedMethodReturnType.getOpcode(POP));
			
			if (hasReturnRef)
				postInjectReturnRef(targetClassInfo, targetMethodInfo, atEnd, methodVisitor, returnRefInfo);
			
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while injecting method\n", injectedMethodInfo, "\nto", targetMethodInfo);
		}
		return false;
	}
	
	public ReturnRefInfo preInjectReturnRef(ClassInfo targetClassInfo, MethodInfo targetMethodInfo, boolean atEnd, AdviceAdapter methodVisitor)
	{
		Type methodReturnType = targetMethodInfo.getReturnType();
		
		int refVarId = methodVisitor.newLocal(Type.getType(SimpleReference.class));
		int returnVarId = -1;
		
		if (atEnd && !targetMethodInfo.isVoid())
		{
			returnVarId = methodVisitor.newLocal(targetMethodInfo.getReturnType());
			methodVisitor.visitVarInsn(methodReturnType.getOpcode(ISTORE), returnVarId);
		}
		
		methodVisitor.visitTypeInsn(NEW, "ru/swayfarer/swl2/reference/SimpleReference");
		methodVisitor.visitInsn(DUP);
		
		if (atEnd && !targetMethodInfo.isVoid())
		{
			methodVisitor.visitVarInsn(methodReturnType.getOpcode(ILOAD), returnVarId);
			methodVisitor.visitMethodInsn(INVOKESPECIAL, RETURN_REF_INSTANCE_INTERNAL_NAME, "<init>", "(Ljava/lang/Object;)V", false);
		}
		else
		{
			methodVisitor.visitMethodInsn(INVOKESPECIAL, RETURN_REF_INSTANCE_INTERNAL_NAME, "<init>", "()V", false);
		}
		
		methodVisitor.visitVarInsn(ASTORE, refVarId);
		
		return new ReturnRefInfo(refVarId, returnVarId);
	}

	public void postInjectReturnRef(ClassInfo targetClassInfo, MethodInfo targetMethodInfo, boolean atEnd, AdviceAdapter methodVisitor, ReturnRefInfo refInfo)
	{
		Label afterReturnLabel = new Label();
		Type methodReturnType = targetMethodInfo.getReturnType();
		methodVisitor.visitVarInsn(ALOAD, refInfo.returnRefId);
		methodVisitor.visitMethodInsn(INVOKEVIRTUAL, RETURN_REF_INSTANCE_INTERNAL_NAME, "isSetted", "()Z", false);
		methodVisitor.visitJumpInsn(IFEQ, afterReturnLabel);
		
		if (!targetMethodInfo.isVoid())
		{
			methodVisitor.visitVarInsn(methodReturnType.getOpcode(ILOAD), refInfo.returnRefId);
			methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "ru/swayfarer/swl2/reference/SimpleReference", "getValue", "()Ljava/lang/Object;", false);
			AsmUtils.invokeObjectCheckcast(methodVisitor, methodReturnType);
			methodVisitor.visitInsn(methodReturnType.getOpcode(IRETURN));
		}
		else
		{
			methodVisitor.visitInsn(RETURN);
		}
		
		methodVisitor.visitLabel(afterReturnLabel);
		
		if (atEnd)
		{
			if (!targetMethodInfo.isVoid())
			{
				System.out.println(refInfo.returnVarId);
				methodVisitor.visitVarInsn(methodReturnType.getOpcode(ILOAD), refInfo.returnVarId);
				methodVisitor.visitInsn(methodReturnType.getOpcode(IRETURN));
			}
			else
			{
				methodVisitor.visitInsn(RETURN);
			}
		}
	}
	
	@Override
	public String getInjectedMethodName()
	{
		return injectedMethodInfo.name;
	}

	@Override
	public String getInjectedMethodDesc()
	{
		return injectedMethodInfo.descriptor;
	}

	@Override
	public String getInjectedClassInternalName()
	{
		return injectedMethodInfo.owner.name;
	}
	
	public static IExtendedList<MethodInjection> of(ClassInfo classInfo)
	{
		IExtendedList<MethodInjection> ret = CollectionsSWL.createExtendedList();
		
		for (MethodInfo methodInfo : classInfo.methods)
		{
			AnnotationInfo injectionAnnotationInfo = methodInfo.getFirstAnnotation(INJECTION_ANNOTATION_DESC);
			
			// Если метод не отмечен аннотацией, то пропускаем его мимо
			if (injectionAnnotationInfo == null)
				continue;
			
			Boolean injectOnExit = injectionAnnotationInfo.getParam("injectOnExit");
			String targetMethodName = injectionAnnotationInfo.getParam("targetName");
			String targetMethodDesc = injectionAnnotationInfo.getParam("targetDesc");
			String targetMethodClass = injectionAnnotationInfo.getParam("targetClass");
			boolean hasReturnRef = false;
			IExtendedList<VariableInfo> methodParams = methodInfo.parameters.copy();
			
			if (injectOnExit == null)
				injectOnExit = false;
			
			if (StringUtils.isEmpty(targetMethodName))
				targetMethodName = methodInfo.name;
			
			// Если целевой класс не задан аннотацией, то ее надо взять из первого агрумента метода 
			if (StringUtils.isEmpty(targetMethodClass))
			{
				if (CollectionsSWL.isNullOrEmpty(methodParams))
				{
					logger.warning("Method", methodInfo, "must have target. Use InjectionAsm annotation's property 'targetClass' or add class type by first arg!");
					continue;
				}
				
				targetMethodClass = AsmUtils.toCanonicalName(methodParams.getFirstElement().descriptor);
			}
			
			// Если задан, то первый элемент гарантированно должен быть Object'ом, чтобы в него пришло то, что в дескрипторе 
			else if (!CollectionsSWL.isNullOrEmpty(methodParams))
			{
				VariableInfo firstParam = methodParams.getFirstElement();
				
				if (!firstParam.descriptor.equals(Type.getDescriptor(Object.class)))
				{
					logger.warning("For use 'targetClassDesc' param you must put java.lang.Object as first arg of your method!");
					continue;
				}
			}
			
			// Поиск ReturnRef
			if (!CollectionsSWL.isNullOrEmpty(methodParams))
			{
				VariableInfo lastParam = methodParams.getLastElement();
				
				// Если параметер типа IReference и либо его имя returnRef, либо он отмечен аннотацией @ReturnRef
				if (lastParam.descriptor.equals(REF_DESCRIPTOR) && (lastParam.name.equals("returnRef") || lastParam.hasAnnotation(RETURN_REF_ANNOTATION_DESC)))
				{
					methodParams.removeLastElement();
					hasReturnRef = true;
				}
			}
			
			methodParams.removeFirstElement();
			
			if (StringUtils.isEmpty(targetMethodDesc))
			{
				StringBuilder sb = new StringBuilder();
				
				sb.append("(");
				
				for (VariableInfo var : methodParams)
				{
					sb.append(var.descriptor);
				}
				
				sb.append(")");
				
				targetMethodDesc = sb.toString();
			}
			
			MethodInjection injection = new MethodInjection();
			injection.hasReturnRef = hasReturnRef;
			injection.injectedMethodInfo = methodInfo;
			injection.injectOnExit = injectOnExit;
			injection.targetClassInternalName = targetMethodClass;
			injection.targetMethodDesc = targetMethodDesc;
			injection.targetMethodName = targetMethodName;
			
			InjectionCreationEvent event = new InjectionCreationEvent(injection, classInfo);
			eventCreation.next(event);
			
			if (event.isCanceled())
				continue;
			
			ret.add(injection);
		}
		
		return ret;
	}

	@Override
	public String toString()
	{
		return "MethodInjection [targetMethodName=" + targetMethodName + ", targetMethodDesc=" + targetMethodDesc + ", targetClassInternalName=" + targetClassInternalName + ", injectedMethodInfo=" + injectedMethodInfo + ", hasReturnRef="
				+ hasReturnRef + ", injectOnExit=" + injectOnExit + "]";
	}
	
	@AllArgsConstructor @Data
	public static class ReturnRefInfo {
		
		public int returnRefId = -1;
		public int returnVarId = -1;
		
	}
}
