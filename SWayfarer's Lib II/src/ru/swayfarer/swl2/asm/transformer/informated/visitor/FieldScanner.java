package ru.swayfarer.swl2.asm.transformer.informated.visitor;

import ru.swayfarer.swl2.asm.informated.AnnotationInfo;
import ru.swayfarer.swl2.asm.informated.FieldInfo;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.AnnotationVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.FieldVisitor;

public class FieldScanner extends FieldVisitor{

	public FieldInfo info;
	
	public FieldScanner(FieldInfo info, FieldVisitor fv)
	{
		super(ASM7, fv);
		this.info = info;
	}

	@Override
	public AnnotationVisitor visitAnnotation(String descriptor, boolean visible)
	{
		AnnotationInfo info = new AnnotationInfo();
		info.descritor = descriptor;
		this.info.annotations.add(info);
		
		return new AnnotationScanner(info, super.visitAnnotation(descriptor, visible));
	}
	
}
