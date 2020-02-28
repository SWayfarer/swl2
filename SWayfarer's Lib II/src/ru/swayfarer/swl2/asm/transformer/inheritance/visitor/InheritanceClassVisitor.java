package ru.swayfarer.swl2.asm.transformer.inheritance.visitor;

import java.util.List;

import ru.swayfarer.swl2.asm.informated.AnnotationInfo;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.informated.MethodInfo;
import ru.swayfarer.swl2.asm.informated.visitor.InformatedClassVisitor;
import ru.swayfarer.swl2.asm.transformer.exists.ExistsConditionContainer;
import ru.swayfarer.swl2.asm.transformer.inheritance.InheritanceClassTransformer;
import ru.swayfarer.swl2.asm.transformer.inheritance.InheritsIf;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;

/**
 * {@link ClassVisitor} для {@link InheritanceClassTransformer}
 * @author swayfarer
 *
 */
public class InheritanceClassVisitor extends InformatedClassVisitor {

	/** Дескриптор аннотации {@link InheritsIf} */
	@InternalElement
	public static final String ANNOTATION_DESC = Type.getType(InheritsIf.class).getDescriptor();
	
	/** Контейнер с уловиями, по которым будет определяться наследование */
	@InternalElement
	public ExistsConditionContainer conditionContainer;
	
	/** Старый и новый типы парента */
	@InternalElement
	public String oldParentType, newParentType;
	
	/** Конструктор */
	public InheritanceClassVisitor(ClassVisitor classVisitor, ClassInfo classInfo, ExistsConditionContainer conditionContainer)
	{
		super(classVisitor, classInfo);
		this.conditionContainer = conditionContainer;
	}

	@Override
	public void visitClassInformated(ClassInfo classInfo, int version, int access, String name, String signature, String superName, String[] interfaces)
	{
		IExtendedList<String> newInterfaces = CollectionsSWL.createExtendedList();
		
		for (AnnotationInfo annotationInfo : classInfo.annotations)
		{
			if (annotationInfo.descritor.equals(ANNOTATION_DESC))
			{
				List<String> annotationConditions = annotationInfo.getParam("conditions");
				
				if (conditionContainer.isConditionsCompleted(annotationConditions))
				{
					List<String> annotationInterfaces = annotationInfo.getParam("interfaces");
				
					if (annotationInterfaces != null)
						newInterfaces.addAll(annotationInterfaces);
					
					String parent = annotationInfo.getParam("parent");
					
					if (!StringUtils.isEmpty(parent))
					{
						oldParentType = superName;
						newParentType = parent;
						superName = parent;
					}
				}
			}
		}
		
		super.visitClassInformated(classInfo, version, access, name, signature, superName, newInterfaces.toArray(String.class));
	}
	
	@Override
	public MethodVisitor visitMethodInformated(MethodInfo info, int access, String name, String descriptor, String signature, String[] exceptions)
	{
		MethodVisitor mv = super.visitMethodInformated(info, access, name, descriptor, signature, exceptions);
		
		if (!StringUtils.isSomeEmpty(newParentType, oldParentType))
		{
			mv = new InheritanceMethodVisitor(mv, oldParentType, newParentType);
		}
		
		return mv;
	}
}
