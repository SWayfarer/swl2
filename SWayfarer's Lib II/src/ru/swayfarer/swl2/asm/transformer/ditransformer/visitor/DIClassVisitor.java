package ru.swayfarer.swl2.asm.transformer.ditransformer.visitor;

import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.informated.visitor.InformatedClassVisitor;
import ru.swayfarer.swl2.ioc.DIManager;
import ru.swayfarer.swl2.ioc.DIManager.DISwL;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;

/**
 * Визитор классов, добавляющий в отмеченные {@link DISwL} поля иньекцию из контекста 
 * @author swayfarer
 */
public class DIClassVisitor extends InformatedClassVisitor {

	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Имя класса, к котрому поля будут обращаться за контекстом */
	public static String CONTEXT_GET_CLASS_NAME = Type.getInternalName(DIManager.class);
	
	/** Имя метода класса {@link #CONTEXT_GET_CLASS_NAME}, к которому поля будут обращаться за контекстом */
	public static String CONTEXT_GET_METHOD_NAME = "getContextElement";
	
	/** Конструктор */
	public DIClassVisitor(ClassVisitor classVisitor, ClassInfo classInfo)
	{
		super(classVisitor, classInfo);
	}
	
	/** Транформация метода */
	@Override
	public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions)
	{	
		MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
		
		return new DIMethodVisitor(mv, access, name, descriptor, classInfo);
	}

}
