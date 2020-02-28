package ru.swayfarer.swl2.asm.transformer.exists.visitor;

import java.util.List;

import ru.swayfarer.swl2.asm.informated.AnnotationInfo;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.informated.FieldInfo;
import ru.swayfarer.swl2.asm.informated.MethodInfo;
import ru.swayfarer.swl2.asm.informated.visitor.InformatedClassVisitor;
import ru.swayfarer.swl2.asm.transformer.exists.ExistsClassTransformer;
import ru.swayfarer.swl2.asm.transformer.exists.ExistsConditionContainer;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.FieldVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;

/**
 * {@link ClassVisitor} для {@link ExistsClassTransformer}
 * @author swayfarer
 *
 */
public class ExistsClassVisitor extends InformatedClassVisitor {

	/** Условия, по которым будет определяться существование объектов */
	@InternalElement
	public ExistsConditionContainer conditions;

	/** Конструктор */
	public ExistsClassVisitor(ClassVisitor classVisitor, ClassInfo classInfo, ExistsConditionContainer conditions)
	{
		super(classVisitor, classInfo);
		this.conditions = conditions;
	}
	
	@Override
	public void visitClassInformated(ClassInfo classInfo, int version, int access, String name, String signature, String superName, String[] interfaces)
	{
		if (isSkiped(classInfo.annotations))
			return;
		
		super.visitClassInformated(classInfo, version, access, name, signature, superName, interfaces);
	}
	
	@Override
	public FieldVisitor visitFieldInformated(FieldInfo fieldInfo, int access, String name, String descriptor, String signature, Object value)
	{
		if (isSkiped(fieldInfo.annotations))
			return null;
		
		return super.visitFieldInformated(fieldInfo, access, name, descriptor, signature, value);
	}
	
	@Override
	public MethodVisitor visitMethodInformated(MethodInfo methodInfo, int access, String name, String descriptor, String signature, String[] exceptions)
	{
		if (isSkiped(methodInfo.annotations))
			return null;
		
		return super.visitMethodInformated(methodInfo, access, name, descriptor, signature, exceptions);
	}
	
	/** Должен ли быть пропущен элемент, отмеченный указанными аннотациями? */
	public boolean isSkiped(List<AnnotationInfo> annotations)
	{
		for (AnnotationInfo annotationInfo : annotations)
		{
			if (annotationInfo != null && annotationInfo.descritor.equals(ExistsClassTransformer.ANNOTATION_DESC))
			{
				List<String> conditions = annotationInfo.getParam("value");
				
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
