package ru.swayfarer.swl2.asm.transfomer.injection.visitor;

import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.informated.MethodInfo;
import ru.swayfarer.swl2.asm.informated.visitor.InformatedClassVisitor;
import ru.swayfarer.swl2.asm.transfomer.injection.ClassInjectionInfo;
import ru.swayfarer.swl2.asm.transfomer.injection.IMethodInjection;
import ru.swayfarer.swl2.asm.transfomer.injection.InjectionsClassTransformer;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;

/**
 * {@link ClassVisitor} {@link InjectionsClassTransformer}'а 
 * @author swayfarer
 *
 */
public class InjectionsClassVisitor extends InformatedClassVisitor {

	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Информация о классе, по которой будет проведена иньекция. Не путать с {@link MethodInfo}  */
	@InternalElement
	public ClassInjectionInfo classInjectionInfo;
	
	/** Конструктор */
	public InjectionsClassVisitor(ClassVisitor classVisitor, ClassInfo classInfo, ClassInjectionInfo classInjectionInfo)
	{
		super(classVisitor, classInfo);
		this.classInjectionInfo = classInjectionInfo;
	}
	
	@Override
	public MethodVisitor visitMethodInformated(MethodInfo info, int access, String name, String descriptor, String signature, String[] exceptions)
	{
		MethodVisitor mv = super.visitMethodInformated(info, access, name, descriptor, signature, exceptions);
		IExtendedList<IMethodInjection> injectionsForMethod = classInjectionInfo.getMethodInjections(name, descriptor);
		
		if (CollectionsSWL.isNullOrEmpty(injectionsForMethod))
			return mv;
		
		mv = new InjectionsMethodVisitor(mv, access, name, descriptor, injectionsForMethod, classInfo, info);
		
		return mv;
	}

}
