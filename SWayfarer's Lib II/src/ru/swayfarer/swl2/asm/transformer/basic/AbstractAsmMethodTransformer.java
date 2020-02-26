package ru.swayfarer.swl2.asm.transformer.basic;

import java.util.ArrayList;
import java.util.List;

import ru.swayfarer.swl2.asm.IAsmMethodFactory;
import ru.swayfarer.swl2.asm.TransformedClassInfo;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassReader;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassWriter;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Opcodes;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.commons.AdviceAdapter;

public abstract class AbstractAsmMethodTransformer extends AbstractAsmTransformer implements IAsmMethodFactory, Opcodes{

	public List<String> classNames = new ArrayList<>();
	public List<String> methodNames = new ArrayList<>();
	
	public AbstractAsmMethodTransformer(String className, String methodName)
	{
		classNames.add(className);
		methodNames.add(methodName);
	}
	
	@Override
	public byte[] transform(String name, byte[] bytes, TransformedClassInfo transformedClassInfo)
	{
		if (name.toLowerCase().endsWith(".class"))
			name = name.substring(0, name.length() - ".class".length());
		
		if (!classNames.contains(name))
		{
			return bytes;
		}
		
		return super.transform(name, bytes, transformedClassInfo);
	}
	
	@Override
	public void transform(String name, byte[] bytes, ClassReader reader, ClassWriter writer)
	{
		{
			try
			{
				MethodClassVisitor cv = MethodClassVisitor.class.getConstructor(ClassVisitor.class).newInstance(writer);
				cv.classNames = classNames;
				cv.methodNames = methodNames;
				cv.factory = this;
				
				acceptCV(reader, bytes, cv);
			}
			catch (Throwable e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static class MethodClassVisitor extends ClassVisitor {

		public List<String> classNames;
		public List<String> methodNames; 
		
		public IAsmMethodFactory factory;
		
		boolean isCurrentClass = false;
		
		public MethodClassVisitor(ClassVisitor cv)
		{
			super(ASM5, cv);
		}
		
		@Override
		public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
		{
			isCurrentClass =  classNames.contains(name);
			
			super.visit(version, access, name, signature, superName, interfaces);
		}
		
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
		{
			MethodVisitor mv2 = factory.super_Visit(this);
			return isCurrentClass && methodNames.contains(name) ? new MethodMethodVisitor(ASM5, mv2 != null ? mv2 : cv.visitMethod(access, name, desc, signature, exceptions), access, name, desc, factory) : super.visitMethod(access, name, desc, signature, exceptions);
		}
		
	}
	
	public static class MethodMethodVisitor extends AdviceAdapter{

		public IAsmMethodFactory factory;
		
		protected MethodMethodVisitor(int api, MethodVisitor mv, int access, String name, String desc, IAsmMethodFactory fact)
		{
			super(api, mv, access, name, desc);
			this.factory = fact;
		}
		
		@Override
		protected void onMethodEnter()
		{
			factory.transformMethod(this);
		}
	}

}
