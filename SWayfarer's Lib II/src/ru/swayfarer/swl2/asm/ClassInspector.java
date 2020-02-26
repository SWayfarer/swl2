package ru.swayfarer.swl2.asm;

import ru.swayfarer.swl2.asm.transformer.basic.AbstractAsmTransformer;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.AnnotationVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassReader;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassWriter;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.FieldVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;

/**
 * Инспектор, выводящий в консоль информацию о классе
 * @author swayfarer
 */
public class ClassInspector extends AbstractAsmTransformer{

	public static ILogger logger = LoggingManager.getLogger();
	
	@Override
	public void transform(String name, byte[] bytes, ClassReader reader, ClassWriter writer)
	{
		acceptCV(reader, bytes, new ClassInspectorVisitor(writer));
	}
	
	public static class ClassInspectorVisitor extends ClassVisitor {

		public boolean showMethods = true;
		public boolean showClasses = true;
		public boolean showAnnotations = true;
		public boolean showField = true;
		
		public ClassInspectorVisitor(ClassVisitor cv)
		{
			super(ASM5, cv);
		}
		
		public ClassInspectorVisitor(ClassVisitor cv, boolean showMethods, boolean showClasses, boolean showAnnotations, boolean showField)
		{
			super(ASM5, cv);
			this.showMethods = showMethods;
			this.showClasses = showClasses;
			this.showAnnotations = showAnnotations;
			this.showField = showField;
		}

		@Override
		public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
		{
			if (showClasses)
				logger.info("Class, version: '", version, "' name: '", name, "' signature: '", signature, "'  superName: '", superName, "' interfaces: '{", StringUtils.concatWithSpaces((Object[])interfaces), "}'");
			
			super.visit(version, access, name, signature, superName, interfaces);
		}
		
		@Override
		public AnnotationVisitor visitAnnotation(String desc, boolean visible)
		{
			if (showAnnotations)
				logger.info("Annotation, desc: '", desc, "' visible: '", visible, "'");
			return super.visitAnnotation(desc, visible);
		}
		
		@Override
		public FieldVisitor visitField(int access, String name, String desc, String signature, Object value)
		{
			if (showField)
				logger.info("Field, access: '", access, "' name: '", name, "' desc: '", desc, "' signature: '", signature, "' value: '", value);
			
			return super.visitField(access, name, desc, signature, value);
		}
		
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
		{
			if (showMethods)
				logger.info("Method, name: '", name, "' desc: '", desc, "' signature: '", signature, "' exceptions: '{", StringUtils.concatWithSpaces((Object[])exceptions), "}'");
			
			return super.visitMethod(access, name, desc, signature, exceptions);
		}
	}

}
