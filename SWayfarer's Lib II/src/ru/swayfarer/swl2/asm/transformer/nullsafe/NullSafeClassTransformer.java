package ru.swayfarer.swl2.asm.transformer.nullsafe;

import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.transformer.informated.InformatedClassTransformer;
import ru.swayfarer.swl2.asm.transformer.nullsafe.visitor.NullSafeClassVisitor;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction3;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassReader;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassWriter;

public class NullSafeClassTransformer extends InformatedClassTransformer {
	
	/** Шаблонированное сообщение о нулевом аргументе метода */
	public static String nullSafeMessage = "Param '%name%' of method '%classSimpleName%::%methodName%' can't be null!";
	
	/** Форматтер сообщения о нулевом аргументе метода */
	public static IFunction3<String, String, String, String> formatterFun = (className, methodName, paramName) -> {
		
		String message = nullSafeMessage;
		message = message
				.replace("%name%", paramName)
				.replace("%methodName%", methodName)
				.replace("%className%", className)
				.replace("%classSimpleName%", ExceptionsUtils.getClassSimpleName(className))
		;
		
		return message;
	};
	
	
	public static String getFormattedMessage(String className, String methodName, String paramName)
	{
		return formatterFun.apply(className, methodName, paramName);
	}
	
	@Override
	public void transform(String name, byte[] bytes, ClassReader reader, ClassWriter writer, ClassInfo info)
	{
		acceptCV(reader, bytes, new NullSafeClassVisitor(writer, info));
	}
}
