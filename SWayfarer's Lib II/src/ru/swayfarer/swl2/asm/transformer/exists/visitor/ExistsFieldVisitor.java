package ru.swayfarer.swl2.asm.transformer.exists.visitor;

import java.util.List;

import ru.swayfarer.swl2.asm.informated.AnnotationInfo;
import ru.swayfarer.swl2.asm.transformer.exists.ExistsClassTransformer;
import ru.swayfarer.swl2.asm.transformer.exists.ExistsConditionContainer;
import ru.swayfarer.swl2.asm.transformer.informated.visitor.AnnotationScanner;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.AnnotationVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.FieldVisitor;

public class ExistsFieldVisitor extends FieldVisitor{

	public AnnotationInfo lastAnnotationInfo;
	public ExistsConditionContainer conditions;
	
	public ExistsFieldVisitor(FieldVisitor fieldVisitor)
	{
		super(ASM7, fieldVisitor);
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String descriptor, boolean visible)
	{
		if (descriptor.equals(ExistsClassTransformer.ANNOTATION_DESC))
		{
			lastAnnotationInfo = new AnnotationInfo(descriptor);
			return new AnnotationScanner(lastAnnotationInfo, super.visitAnnotation(descriptor, visible));
		}
		
		return super.visitAnnotation(descriptor, visible);
	}

	public boolean isSkiped()
	{
		if (lastAnnotationInfo == null)
			return false;
		
		List<String> conditions = lastAnnotationInfo.getParam("condition");
		
		if (conditions != null)
		{
			for (String condition : conditions)
			{
				if (!this.conditions.getCondition(condition))
				{
					lastAnnotationInfo = null;
					return true;
				}
			}
		}
		
		lastAnnotationInfo = null;
		return false;
	}
	
}
