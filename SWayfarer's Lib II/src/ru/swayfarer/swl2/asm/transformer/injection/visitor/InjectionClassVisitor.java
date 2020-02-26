package ru.swayfarer.swl2.asm.transformer.injection.visitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.swayfarer.swl2.asm.injection.IInjectionInfo;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;

public class InjectionClassVisitor extends ClassVisitor{

	public Map<String, List<IInjectionInfo>> injections = new HashMap<>();
	
	public String className;
	
	public InjectionClassVisitor(ClassVisitor classVisitor)
	{
		super(ASM7, classVisitor);
	}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
	{
		className = name;
		super.visit(version, access, name, signature, superName, interfaces);
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions)
	{
		List<IInjectionInfo> injections = findInjections(name);
		
		if (injections != null)
		{
			return new InjectionMethodVisitor(injections, super.visitMethod(access, name, descriptor, signature, exceptions), access, name, descriptor);
		}
		
		return super.visitMethod(access, name, descriptor, signature, exceptions);
	}

	public List<IInjectionInfo> findInjections(String name)
	{
		return injections.containsKey(name) ? injections.get(name) : null;
	}
	
	
	
}
