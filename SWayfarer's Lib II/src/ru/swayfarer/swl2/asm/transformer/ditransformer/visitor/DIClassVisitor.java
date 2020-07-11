package ru.swayfarer.swl2.asm.transformer.ditransformer.visitor;

import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.informated.visitor.InformatedClassVisitor;
import ru.swayfarer.swl2.asm.transformer.ditransformer.DIClassTransformer;
import ru.swayfarer.swl2.ioc.DIManager.DISwL;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;

/**
 * Визитор классов, добавляющий в отмеченные {@link DISwL} поля иньекцию из контекста 
 * @author swayfarer
 */
public class DIClassVisitor extends InformatedClassVisitor {

	public DIClassTransformer classFileTransformer;
	
	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Конструктор */
	public DIClassVisitor(DIClassTransformer classFileTransformer, ClassVisitor classVisitor, ClassInfo classInfo)
	{
		super(classVisitor, classInfo);
		this.classFileTransformer = classFileTransformer;
	}
	
	/** Транформация метода */
	@Override
	public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions)
	{	
		MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
		
		return new DIMethodVisitor(classFileTransformer, mv, access, name, descriptor, classInfo);
	}

}
