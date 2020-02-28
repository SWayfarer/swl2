package ru.swayfarer.swl2.asm.transformer.patcher.visitor;

import java.util.List;

import ru.swayfarer.swl2.asm.informated.AnnotationInfo;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.informated.FieldInfo;
import ru.swayfarer.swl2.asm.informated.MethodInfo;
import ru.swayfarer.swl2.asm.informated.visitor.InformatedClassVisitor;
import ru.swayfarer.swl2.asm.transformer.exists.ExistsClassTransformer;
import ru.swayfarer.swl2.asm.transformer.exists.ExistsConditionContainer;
import ru.swayfarer.swl2.asm.transformer.patcher.PatchAsm;
import ru.swayfarer.swl2.asm.transformer.patcher.PatcherClassTransformer;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.FieldVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;

/**
 * {@link ClassVisitor} для {@link PatcherClassTransformer}
 * @author swayfarer
 *
 */
public class PatcherClassVisitor extends InformatedClassVisitor {

	/** Условия, в зависимости от которых будут применяться патчи */
	@InternalElement
	public ExistsConditionContainer conditions;
	
	/** Дескриптор аннотации */
	@InternalElement
	public static final String ANNOTATION_DESC = Type.getType(PatchAsm.class).getDescriptor();
	
	/** Конструктор */
	public PatcherClassVisitor(ClassVisitor classVisitor, ClassInfo classInfo, ExistsConditionContainer conditions)
	{
		super(classVisitor, classInfo);
		this.conditions = conditions;
	}
	
	@Override
	public MethodVisitor visitMethodInformated(MethodInfo info, int access, String name, String descriptor, String signature, String[] exceptions)
	{
		if (isSkiped(info.annotations))
			return null;
		
		return super.visitMethodInformated(info, access, name, descriptor, signature, exceptions);
	}
	
	@Override
	public FieldVisitor visitFieldInformated(FieldInfo info, int access, String name, String descriptor, String signature, Object value)
	{
		if (isSkiped(info.annotations))
			return null;
		
		return super.visitFieldInformated(info, access, name, descriptor, signature, value);
	}
	
	/** Должен ли быть пропущен элемент, отмеченный указанными аннотациями? */
	public boolean isSkiped(List<AnnotationInfo> annotations)
	{
		for (AnnotationInfo annotationInfo : annotations)
		{
			if (annotationInfo != null && annotationInfo.descritor.equals(ExistsClassTransformer.ANNOTATION_DESC))
			{
				List<String> conditions = annotationInfo.getParam("conditions");
				
				if (conditions != null)
				{
					for (String condition : conditions)
					{
						if (!this.conditions.getCondition(condition))
							return true;
					}
				}
			}
		}
		
		return false;
	}
}
