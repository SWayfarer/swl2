package ru.swayfarer.swl2.asm.transformer.informated.visitor;

import java.util.ArrayList;
import java.util.List;

import ru.swayfarer.swl2.asm.informated.AnnotationInfo;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.AnnotationVisitor;

public class AnnotationScanner extends AnnotationVisitor{

	public AnnotationInfo info;
	
	public AnnotationScanner(AnnotationInfo info, AnnotationVisitor av)
	{
		super(ASM7, av);
		this.info = info;
	}
	
	@Override
	public void visit(String name, Object value)
	{
		info.params.put(name, value);
	}
	
	@Override
	public void visitEnum(String name, String descriptor, String value)
	{
		info.params.put(name, value);
	}
	
	@Override
	public AnnotationVisitor visitArray(String name)
	{
		List<?> arr = new ArrayList<>();
		info.params.put(name, arr);
		return new ArrayAnnotationVisitor(super.visitArray(name), arr);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static class ArrayAnnotationVisitor extends AnnotationVisitor {
 
		public List values;
		
		public ArrayAnnotationVisitor(AnnotationVisitor annotationVisitor, List values)
		{
			super(ASM7, annotationVisitor);
			this.values = values;
		}
		
		@Override
		public void visit(String name, Object value)
		{
			values.add(value);
			super.visit(name, value);
		}
		
	}
}
