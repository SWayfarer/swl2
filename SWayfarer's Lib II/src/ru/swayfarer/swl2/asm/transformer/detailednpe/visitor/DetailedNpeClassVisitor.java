package ru.swayfarer.swl2.asm.transformer.detailednpe.visitor;

import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassVisitor;

public class DetailedNpeClassVisitor extends ClassVisitor {

	public DetailedNpeClassVisitor(ClassVisitor classVisitor)
	{
		super(classVisitor);
	}
}
