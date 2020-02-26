package ru.swayfarer.swl2.asm.injection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sun.xml.internal.ws.wsdl.writer.document.ParamType;

import ru.swayfarer.swl2.asm.AsmUtils;
import ru.swayfarer.swl2.asm.informated.AnnotationInfo;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.informated.MethodInfo;
import ru.swayfarer.swl2.asm.informated.VariableInfo;
import ru.swayfarer.swl2.asm.injection.annotations.InjectionAsm;
import ru.swayfarer.swl2.asm.injection.annotations.ReturnController;
import ru.swayfarer.swl2.asm.injection.annotations.ReturnValue;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.json.NonJson;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.reference.SimpleReference;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Label;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Opcodes;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.commons.AdviceAdapter;

public class MethodInjectionInfo implements IInjectionInfo, Opcodes{

	@NonJson
	public static List<IMethodInjectionTranformer> tranformers = new ArrayList<>();
	
	@NonJson
	public static ILogger logger = LoggingManager.getLogger();
	
	@NonJson
	public static final String INJECT_ANNOTATION_DESC = Type.getDescriptor(InjectionAsm.class);
	
	// Параметры
	public boolean injectOnExit;
	public Type methodReturnType, targetReturnType;
	public boolean hasReturnParam = false;
	public boolean hasReturnController = false;
	
	public boolean isReturnValueFirst = false;
	
	// Цель
	public String targetDesc, targetName, targetClass;
	public boolean isTargetStatic = false;
	
	// Параметры метода
	public String methodClass, methodName, methodDesc;
	public List<Type> params = new ArrayList<>();
	
	public static Type returnControllerType = Type.getType(SimpleReference.class);

	public MethodInjectionInfo() {}
	
	public MethodInjectionInfo(boolean injectOnExit, Type methodReturnType, Type targetReturnType, boolean hasReturnParam, String targetDesc, String targetName, String targetClass, boolean isTargetStatic, String methodClass, String methodName, String methodDesc, List<Type> params)
	{
		super();
		this.injectOnExit = injectOnExit;
		this.methodReturnType = methodReturnType;
		this.targetReturnType = targetReturnType;
		this.hasReturnParam = hasReturnParam;
		this.targetDesc = targetDesc;
		this.targetName = targetName;
		this.targetClass = targetClass;
		this.isTargetStatic = isTargetStatic;
		this.methodClass = methodClass;
		this.methodName = methodName;
		this.methodDesc = methodDesc;
		this.params = params;
	}
	
	public boolean validate()
	{
		if (hasReturnParam && (targetReturnType.equals(Type.VOID_TYPE)))
		{
			logger.warning("Can't use return value for void target method! Aborting "+methodClass+"#"+methodName+" injection!");
			return false;
		}
		
		return true;
	}

	public void inject(AdviceAdapter mv, int opcode)
	{
		//Console.info("Injecting...", injectOnExit, hasReturnController);
		
		if (!validate())
			return;
		
		int returnId, controllerId;
		returnId = controllerId = -1;
		
		boolean isTargetVoid = targetReturnType.equals(Type.VOID_TYPE);
		
		if (injectOnExit && !isTargetVoid)
		{
			returnId = mv.newLocal(targetReturnType);
			mv.visitVarInsn(targetReturnType.getOpcode(Opcodes.ISTORE), returnId);
		}
		
		if (hasReturnController)
		{
			AsmUtils.createInstance(mv, returnControllerType);
			
			controllerId = mv.newLocal(returnControllerType);
			
			mv.visitVarInsn(returnControllerType.getOpcode(Opcodes.ISTORE), controllerId);
		}
		
		int nextId = 0;
		
		if (!isTargetStatic)
			mv.visitVarInsn(Type.getType("L"+targetClass+";").getOpcode(ILOAD), nextId++);
		else
			mv.visitInsn(ACONST_NULL);
		
		for (Type paramType : params)
		{
			mv.visitVarInsn(paramType.getOpcode(ILOAD), nextId);
			nextId += paramType.getSize();
		}
		
		if (isReturnValueFirst && hasReturnParam && returnId != -1)
			mv.visitVarInsn(targetReturnType.getOpcode(ILOAD), returnId);
		
		if (hasReturnController)
			mv.visitVarInsn(returnControllerType.getOpcode(ILOAD), controllerId);
		
		if (!isReturnValueFirst && hasReturnParam && returnId != -1)
			mv.visitVarInsn(targetReturnType.getOpcode(ILOAD), returnId);
		
		AsmUtils.invokeStatic(mv, methodClass, methodName, methodDesc);
		
		if (methodReturnType != Type.VOID_TYPE)
			mv.visitInsn(POP);
		
		if (hasReturnController)
		{
			mv.visitVarInsn(returnControllerType.getOpcode(ILOAD), controllerId);
			
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, AsmUtils.toInternalName(returnControllerType), "isSetted", "()Z", false);
			Label lbl = mv.newLabel();
			
			mv.visitJumpInsn(IFEQ, lbl);
			
			if (isTargetVoid)
			{
				mv.visitInsn(RETURN);
			}
			else
			{
				mv.visitVarInsn(returnControllerType.getOpcode(ILOAD), controllerId);
				mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, AsmUtils.toInternalName(returnControllerType), "getValue", "()Ljava/lang/Object;", false);
				
				//mv.visitTypeInsn(Opcodes.CHECKCAST, AsmUtils.toInternalName(targetReturnType));
				AsmUtils.invokeObjectCheckcast(mv, targetReturnType);
				mv.visitInsn(targetReturnType.getOpcode(IRETURN));
			}
			
			mv.visitLabel(lbl);
			
			if (injectOnExit)
			{
				if (isTargetVoid)
				{
					mv.visitInsn(RETURN);
				}
				else
				{
					mv.visitVarInsn(targetReturnType.getOpcode(ILOAD), returnId);
					mv.visitInsn(targetReturnType.getOpcode(IRETURN));
				}
			}
		}
		else if (injectOnExit && isTargetVoid)
			mv.visitInsn(RETURN);
		else if (injectOnExit && returnId != -1)
		{
			mv.visitVarInsn(targetReturnType.getOpcode(ILOAD), returnId);
			mv.visitInsn(targetReturnType.getOpcode(IRETURN));
		}
	}
	
	public void inject2(AdviceAdapter mv)
	{
		int resultLocalId = -1;
		int returnLocalId = -1;
		int retContainerLocalId = -1;
		
	/* Если нужно передать результат работы метода, то создаем под это дело переменную и заполнем ее */
	
		if (!validate())
			return;
		
		if ((hasReturnController || hasReturnParam) && targetReturnType != Type.VOID_TYPE)
		{
			returnLocalId = mv.newLocal(targetReturnType);
			mv.visitLocalVariable("returnValue", targetReturnType.getDescriptor(), null, mv.newLabel(), mv.newLabel(), returnLocalId);
		
		//	AsmUtils.invokeStore(mv, targetReturnType, returnLocalId);
			mv.visitVarInsn(targetReturnType.getOpcode(Opcodes.ISTORE), returnLocalId);
			injectOnExit = true;
		}
		
		if (hasReturnController)
		{
			retContainerLocalId = mv.newLocal(returnControllerType);
			mv.visitLocalVariable("returnContainer", returnControllerType.getDescriptor(), null, mv.newLabel(), mv.newLabel(), retContainerLocalId);
			
			AsmUtils.newInstance(mv, returnControllerType.getDescriptor());
			AsmUtils.initInstance(mv, returnControllerType.getDescriptor());
			AsmUtils.invokeStore(mv, returnControllerType, retContainerLocalId);
		}
	
	/* Загружаем переменные */
	
		int nextLoad = 0;
		
		// Для статических методов вместо this загружается null
		if (isTargetStatic)
		{
			mv.visitInsn(Opcodes.ACONST_NULL);
		}
		// Для не статических методов в первый параметр загружается this
		else
		{
			AsmUtils.invokeLoad(mv, Type.getType("L"+targetClass+";"), nextLoad++);
		}
		
		// Загружаем параметры на стек 
			for (Type type : params)
			{
				AsmUtils.invokeLoad(mv, type, nextLoad++);
			}

			
			
			// Если последним передается @ReturnValue, то сохряняем его тоже
		
			
		if (hasReturnParam)
			AsmUtils.invokeLoad(mv, targetReturnType, returnLocalId);
		
		if (hasReturnController)
			AsmUtils.invokeLoad(mv, returnControllerType, retContainerLocalId);
		
	/* Вызываем встроенный метод */
		
		AsmUtils.invokeStatic(mv, methodClass, methodName, methodDesc);
	
	/* Если была создана переменная, то сохраняем в нее результат и ретерним */
	
		if (hasReturnController)
		{
			String contName = AsmUtils.toInternalName(returnControllerType.getDescriptor());
			
			//mv.visitInsn(Opcodes.POP);
			AsmUtils.invokeLoad(mv, returnControllerType, retContainerLocalId);
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, contName, "isSetted", "()Z", false);
			
			Label lbl = mv.newLabel();
			
			mv.visitJumpInsn(Opcodes.IFEQ, lbl);
			
			if (targetReturnType != Type.VOID_TYPE)
			{
				AsmUtils.invokeLoad(mv, returnControllerType, retContainerLocalId);
				mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, contName, "getValue", "()Ljava/lang/Object;", false);
				//mv.visitTypeInsn(Opcodes.CHECKCAST, targetReturnType.getDescriptor());
				AsmUtils.invokeReturn(mv, returnControllerType);
			}
			else
			{
				AsmUtils.invokeReturn(mv, Type.VOID_TYPE);
			}
			
			mv.visitLabel(lbl);
			
		}
		
		if (returnLocalId != -1)
		{
			AsmUtils.invokeLoad(mv, targetReturnType, returnLocalId);
			AsmUtils.invokeReturn(mv, targetReturnType);
		}
		
		//mv.visitLabel(end);
		
		if (returnLocalId != -1)
		{
			//mv.visitLocalVariable("return_"+JavaUtils.getRandomString(22), targetReturnType.getDescriptor(), null, start, end, returnLocalId);
		}
		
		if (resultLocalId != -1)
		{
			//mv.visitLocalVariable("result_"+JavaUtils.getRandomString(22), methodReturnType.getDescriptor(), null, start, end, resultLocalId);
		}
			
	}
	
	@Override
	public boolean isInjectOnExit()
	{
		return injectOnExit;
	}

	@Override
	public String getTargetClass()
	{
		return targetClass;
	}

	@Override
	public String getTargetMethod()
	{
		return targetName;
	}

	@Override
	public String getTargetDescriptor()
	{
		return targetDesc;
	}
	
	public static MethodInjectionInfo valueOf(ClassInfo classInfo, MethodInfo method, AnnotationInfo annotation)
	{
		if (method == null || annotation == null)
			return null;
		
		String methodClass = AsmUtils.toInternalName(classInfo.name);
		String methodName = method.name;
		String methodDesc = method.descriptor;
		
		IExtendedList<VariableInfo> params = method.getParams();
		
		if (params.isEmpty())
			return null;
		
		String targetClassName = params.get(0).descriptor;
		
		params.removeLastElement();
		
		String targetMethodName = method.name;
		
		IExtendedList<Type> paramsTypes = CollectionsSWL.createExtendedList();
		
		boolean hasReturnValue = false;
		boolean hasReturnContainer = false;
		boolean injectOnExit = false;
		boolean isReturnValueFirst = false;
		
		params.printList(logger, "Params");
		
		for (int i1 = 0; i1 < 2; i1 ++)
		{
			if (!params.isEmpty())
			{
				VariableInfo lastParam = params.getLastElement();
				
				if (lastParam.getFirstAnnotation(ReturnController.type.getDescriptor()) != null)
				{
					hasReturnContainer = true;
					params.removeLastElement();
					
				}
				else if (lastParam.getFirstAnnotation(ReturnValue.type.getDescriptor()) != null)
				{
					isReturnValueFirst = hasReturnContainer;
					hasReturnValue = true;
					params.removeLastElement();
				}
			}
		}
		
		for (VariableInfo varInfo : params)
		{
			paramsTypes.add(varInfo.getType());
		}
		
		String targetDesc = "(";
		
		paramsTypes.removeFirstElement();
		
		for (Type type : paramsTypes)
		{
			targetDesc += type.getDescriptor();
		}
		
		targetDesc += ")";
		
		for (Map.Entry<String, Object> entry : annotation.params.entrySet())
		{
			switch (entry.getKey())
			{
				case "injectOnExit":
					injectOnExit = (boolean)entry.getValue();
					break;
				case "targetMethod":
					targetMethodName = ""+entry.getValue();
					break;
			}
		}
		
		MethodInjectionInfo info = new MethodInjectionInfo();
		
		info.hasReturnParam = hasReturnValue;
		info.hasReturnController = hasReturnContainer;
		info.isReturnValueFirst = isReturnValueFirst;
		info.injectOnExit = hasReturnValue ? true : injectOnExit;
		
		info.methodClass = methodClass;
		info.methodDesc = methodDesc;
		info.methodName = methodName;
		info.methodReturnType = Type.getType(method.descriptor.substring(method.descriptor.indexOf(")")+1));
		
		info.targetClass = AsmUtils.toInternalName(targetClassName);
		info.targetName = targetMethodName;
		info.targetDesc = targetDesc;
		
		info.params = paramsTypes;
		
		for (IMethodInjectionTranformer tranformer : tranformers)
		{
			try
			{
				info = tranformer.transform(info);
			}
			catch (Throwable e)
			{
				e.printStackTrace();
			}
		}
		
		AnnotationInfo injectionAnnotationInfo = method.getFirstAnnotation(INJECT_ANNOTATION_DESC);
		
		if (injectionAnnotationInfo != null)
		{
			String aClassName = injectionAnnotationInfo.getParam("targetClassInternal");
			String aMethodDesc = injectionAnnotationInfo.getParam("targetMethodDesc");
			String aMethodName = injectionAnnotationInfo.getParam("targetMethod");
			
			if (!StringUtils.isEmpty(aMethodName))
			{
				info.targetName = aMethodName;
				logger.info("Found target name", aMethodDesc);
			}
			
			if (!StringUtils.isEmpty(aMethodDesc))
			{
				info.targetDesc = aMethodDesc;

				logger.info("Found desc name", aMethodDesc);
			}
			
			if (!StringUtils.isEmpty(aClassName))
			{
				info.targetClass = aClassName;
				logger.info("Found classname name", aMethodDesc);
			}
		}
		
		logger.info(info);
		
		return info;
	}

	
	
	@Override
	public String toString()
	{
		return "MethodInjectionInfo [hasReturnContainer="+hasReturnController+", injectOnExit=" + injectOnExit + ", methodReturnType=" + methodReturnType + ", targetReturnType=" + targetReturnType + ", hasReturnParam=" + hasReturnParam
				+ ", targetDesc=" + targetDesc + ", targetName=" + targetName + ", targetClass=" + targetClass + ", isTargetStatic=" + isTargetStatic + ", methodClass=" + methodClass + ", methodName=" + methodName + ", methodDesc="
				+ methodDesc + ", params=" + params + "]";
	}

	@Override
	public void setTargetClassReturnType(Type type)
	{
		targetReturnType = type;
	}

	@Override
	public void setTargetClassStatic(boolean isStatic)
	{
		isTargetStatic = isStatic;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (hasReturnParam ? 1231 : 1237);
		result = prime * result + (injectOnExit ? 1231 : 1237);
		result = prime * result + (isTargetStatic ? 1231 : 1237);
		result = prime * result + ((methodClass == null) ? 0 : methodClass.hashCode());
		result = prime * result + ((methodDesc == null) ? 0 : methodDesc.hashCode());
		result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
		result = prime * result + ((methodReturnType == null) ? 0 : methodReturnType.hashCode());
		result = prime * result + ((params == null) ? 0 : params.hashCode());
		result = prime * result + ((targetClass == null) ? 0 : targetClass.hashCode());
		result = prime * result + ((targetDesc == null) ? 0 : targetDesc.hashCode());
		result = prime * result + ((targetName == null) ? 0 : targetName.hashCode());
		result = prime * result + ((targetReturnType == null) ? 0 : targetReturnType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MethodInjectionInfo other = (MethodInjectionInfo) obj;
		if (hasReturnParam != other.hasReturnParam)
			return false;
		if (injectOnExit != other.injectOnExit)
			return false;
		if (isTargetStatic != other.isTargetStatic)
			return false;
		if (methodClass == null)
		{
			if (other.methodClass != null)
				return false;
		}
		else if (!methodClass.equals(other.methodClass))
			return false;
		if (methodDesc == null)
		{
			if (other.methodDesc != null)
				return false;
		}
		else if (!methodDesc.equals(other.methodDesc))
			return false;
		if (methodName == null)
		{
			if (other.methodName != null)
				return false;
		}
		else if (!methodName.equals(other.methodName))
			return false;
		if (methodReturnType == null)
		{
			if (other.methodReturnType != null)
				return false;
		}
		else if (!methodReturnType.equals(other.methodReturnType))
			return false;
		if (params == null)
		{
			if (other.params != null)
				return false;
		}
		else if (!params.equals(other.params))
			return false;
		if (targetClass == null)
		{
			if (other.targetClass != null)
				return false;
		}
		else if (!targetClass.equals(other.targetClass))
			return false;
		if (targetDesc == null)
		{
			if (other.targetDesc != null)
				return false;
		}
		else if (!targetDesc.equals(other.targetDesc))
			return false;
		if (targetName == null)
		{
			if (other.targetName != null)
				return false;
		}
		else if (!targetName.equals(other.targetName))
			return false;
		if (targetReturnType == null)
		{
			if (other.targetReturnType != null)
				return false;
		}
		else if (!targetReturnType.equals(other.targetReturnType))
			return false;
		return true;
	}
	
	public static interface IMethodInjectionTranformer {
		public MethodInjectionInfo transform(MethodInjectionInfo info) throws Throwable;
	}
}
