package ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.luajc.utils;

import static ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Opcodes.ALOAD;
import static ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Opcodes.BIPUSH;
import static ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Opcodes.DCONST_0;
import static ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Opcodes.DCONST_1;
import static ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Opcodes.ICONST_0;
import static ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Opcodes.RETURN;
import static ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Opcodes.SIPUSH;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassReader;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.util.CheckClassAdapter;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.util.TraceClassVisitor;

/**
 * Utilities for writing Java bytecode
 */
public final class AsmUtils {
	private AsmUtils() {
	}

	/**
	 * Get the appropriate constant opcode
	 *
	 * @param number The opcode number
	 * @return ICONST_n
	 */
	public static int getConstOpcode(int number) {
		return ICONST_0 + number;
	}

	/**
	 * Insert the correct Opcode for Java constants
	 *
	 * @param mv     The {@link MethodVisitor}
	 * @param number The constant to insert
	 */
	public static void constantOpcode(MethodVisitor mv, int number) {
		if (number >= -1 && number <= 5) {
			mv.visitInsn(getConstOpcode(number));
		} else if (number >= -128 && number <= 127) {
			mv.visitIntInsn(BIPUSH, number);
		} else if (number >= -32768 && number <= 32767) {
			mv.visitIntInsn(SIPUSH, (short) number);
		} else {
			mv.visitLdcInsn(number);
		}
	}

	public static void constantOpcode(MethodVisitor mv, double number) {
		if (number == 0.0D) {
			mv.visitInsn(DCONST_0);
		} else if (number == 1.0D) {
			mv.visitInsn(DCONST_1);
		} else {
			mv.visitLdcInsn(number);
		}
	}

	/**
	 * Validate a generated class
	 *
	 * @param reader The class to read
	 * @param loader The appropriate class loader
	 * @throws RuntimeException On bad validation
	 */
	public static void validateClass(ClassReader reader, ClassLoader loader) {
		StringWriter writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);

		Exception error = null;
		try {
			CheckClassAdapter.verify(reader, loader, false, printWriter);
		} catch (Exception e) {
			error = e;
		}

		String contents = writer.toString();
		if (error != null || contents.length() > 0) {
			throw new ValidationException(reader, writer.toString(), error);
		}
	}

	/**
	 * Validate a generated class
	 *
	 * @param bytes  The class bytes to read
	 * @param loader The appropriate class loader
	 * @see #validateClass(ClassReader, ClassLoader)
	 */
	public static void validateClass(byte[] bytes, ClassLoader loader) {
		validateClass(new ClassReader(bytes), loader);
	}

	public static void dump(byte[] bytes, OutputStream out) {
		dump(bytes, new PrintWriter(out, true));
	}

	public static void dump(byte[] bytes, PrintWriter out) {
		ClassReader reader = new ClassReader(bytes);
		reader.accept(new TraceClassVisitor(out), 0);
	}

	public static void dump(byte[] bytes) {
		dump(bytes, System.out);
	}


	public static void writeSuperConstructor(MethodVisitor visitor, String name) {
		visitor.visitVarInsn(ALOAD, 0);
		visitor.visitMethodInsn(INVOKESPECIAL, name, "<init>", "()V", false);
	}

	public static void writeDefaultConstructor(ClassVisitor visitor, String name) {
		MethodVisitor constructor = visitor.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
		constructor.visitCode();

		writeSuperConstructor(constructor, name);

		constructor.visitInsn(RETURN);
		constructor.visitMaxs(1, 1);
		constructor.visitEnd();
	}
}
