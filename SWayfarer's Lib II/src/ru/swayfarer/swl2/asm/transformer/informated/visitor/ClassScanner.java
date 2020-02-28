package ru.swayfarer.swl2.asm.transformer.informated.visitor;

import java.util.ArrayList;

import ru.swayfarer.swl2.asm.informated.AnnotationInfo;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.informated.FieldInfo;
import ru.swayfarer.swl2.asm.informated.MethodInfo;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.AnnotationVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.FieldVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;

/**
 * Сканнер классов, который производит {@link ClassInfo} для указанных целей
 * @author swayfarer
 *
 */
public class ClassScanner extends ClassVisitor{

	/** Информация о классе, которую билдим */
	@InternalElement
	public ClassInfo info = new ClassInfo();
	
	/** Конструктор */
	public ClassScanner(ClassVisitor classVisitor)
	{
		super(ASM7, classVisitor);
	}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
	{
		info.access = access;
		info.version = version;
		info.name = name;
		info.signature = signature;
		info.superName = superName;
		info.interfaces = new ArrayList<>();
		
		super.visit(version, access, name, signature, superName, interfaces);
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String descriptor, boolean visible)
	{
		AnnotationInfo info = new AnnotationInfo();
		info.descritor = descriptor;
		this.info.annotations.add(info);
		
		return new AnnotationScanner(info, super.visitAnnotation(descriptor, visible));
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions)
	{	
		MethodInfo info = new MethodInfo();
		this.info.methods.add(info);
		info.owner = this.info;
		info.access = access;
		info.name = name;
		info.descriptor = descriptor;
		info.signature = signature;
		info.exceptions = CollectionsSWL.createExtendedList();
		
		return new MethodScanner(null, access, name, descriptor, info);
	}
	
	@Override
	public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value)
	{
		FieldInfo info = new FieldInfo();
		
		info.access = access;
		info.name = name;
		info.descriptor = descriptor;
		info.signature = signature;
		info.value = value;
		info.owner = this.info;
		
		this.info.fields.add(info);
		
		return new FieldScanner(info, super.visitField(access, name, descriptor, signature, value));
	}

}
