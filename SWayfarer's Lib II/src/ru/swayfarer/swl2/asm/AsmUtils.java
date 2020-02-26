package ru.swayfarer.swl2.asm;

import java.util.ArrayList;
import java.util.List;

import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.informated.FieldInfo;
import ru.swayfarer.swl2.asm.informated.MethodInfo;
import ru.swayfarer.swl2.equals.EqualsUtils;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Opcodes;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;

public class AsmUtils implements Opcodes{

	/**
	 * Ex: path/to/class/TestClass
	 */
	public static String toInternalName(Type type)
	{
		return toInternalName(type.getDescriptor());
	}
	
	/**
	 * Ex: path/to/class/TestClass
	 */
	public static String toInternalName(String desc)
	{
		if (desc.length() == 1 && "CBSIJZDF".contains(desc))
			return Type.getType(desc).getInternalName();
		return toCanonicalName(desc).replace(".", "/");
	}
	
	public static boolean isPrimitive(Type type)
	{
		return EqualsUtils.objectEqualsSome
		(
			type, 
			Type.BOOLEAN_TYPE,
			Type.BYTE_TYPE,
			Type.CHAR_TYPE,
			Type.DOUBLE_TYPE,
			Type.FLOAT_TYPE,
			Type.INT_TYPE,
			Type.LONG_TYPE,
			Type.SHORT_TYPE,
			Type.VOID_TYPE
		);
	}
	
	public static String toDescName(String canonicalName)
	{
		return "L"+canonicalName.replace(".", "/")+";";
	}
	
	public static void createInstance(MethodVisitor mv, Type type)
	{
		newInstance(mv, type);
		initInstance(mv, type);
	}
	
	public static void initInstance(MethodVisitor mv, Type type)
	{
		initInstance(mv, toInternalName(type.getDescriptor()));
	}
	
	public static void initInstance(MethodVisitor mv, String desc)
	{
		mv.visitMethodInsn(INVOKESPECIAL, desc, "<init>", "()V", false);
	}
	
	public static void newInstance(MethodVisitor mv, Type type)
	{
		newInstance(mv, toInternalName(type.getDescriptor()));
	}
	
	public static void newInstance(MethodVisitor mv, String desc)
	{
		mv.visitTypeInsn(NEW, desc);
		mv.visitInsn(DUP);
	}
	
	public static void invokeObjectReturn(MethodVisitor mv, Type type)
	{
		Class<?> cl = null;
		String desc = null;
		
		if (type == Type.BOOLEAN_TYPE)
		{
			cl = Boolean.class;
			desc = "(Z)";
		}
		else if (type == Type.BYTE_TYPE)
		{
			cl = Byte.class;
			desc = "(B)";
		}
		else if (type == Type.SHORT_TYPE)
		{
			cl = Short.class;
			desc = "(S)";
		}
		else if (type == Type.INT_TYPE)
		{
			cl = Integer.class;
			desc = "(I)";
		}
		else if (type == Type.LONG_TYPE)
		{
			cl = Long.class;
			desc = "(L)";
		}
		else if (type == Type.FLOAT_TYPE)
		{
			cl = Float.class;
			desc = "(F)";
		}
		else if (type == Type.DOUBLE_TYPE)
		{
			cl = Double.class;
			desc = "(D)";
		}
		else if (type == Type.CHAR_TYPE)
		{
			cl = Character.class;
			desc = "(C)";
		}
		
		if (cl != null)
		{
			String iname = toInternalName(Type.getType(cl));
			mv.visitMethodInsn(INVOKESTATIC, iname, "valueOf", desc+"L"+iname+";", false);
		}
		
		mv.visitInsn(ARETURN);
	}
	
	public static void invokeObjectCheckcast(MethodVisitor mv, Type type)
	{
		Type checkcastType = type;
		String className , methodName, methodDesc;
		methodName = className = methodDesc = null;
		
		if (type == Type.INT_TYPE)
		{
			className = "Integer";
			methodName = "intValue";
			methodDesc = "()I";
			checkcastType = Type.getType(Integer.class);
		}
		else if (type == Type.BOOLEAN_TYPE)
		{
			className = "Boolean";
			methodName = "booleanValue";
			methodDesc = "()Z";
			checkcastType = Type.getType(Boolean.class);
		}
		else if (type == Type.SHORT_TYPE)
		{
			className = "Short";
			methodName = "shortValue";
			methodDesc = "()S";
			checkcastType = Type.getType(Short.class);
		}
		else if (type == Type.BYTE_TYPE)
		{
			className = "Byte";
			methodName = "byteValue";
			methodDesc = "()B";
			checkcastType = Type.getType(Byte.class);
		}
		else if (type == Type.FLOAT_TYPE)
		{
			className = "Float";
			methodName = "floatValue";
			methodDesc = "()F";
			checkcastType = Type.getType(Float.class);
		}
		else if (type == Type.DOUBLE_TYPE)
		{
			className = "Double";
			methodName = "doubleValue";
			methodDesc = "()D";
			checkcastType = Type.getType(Double.class);
		}
		else if (type == Type.LONG_TYPE)
		{
			className = "Long";
			methodName = "longValue";
			methodDesc = "()L";
			checkcastType = Type.getType(Long.class);
		}
		else if (type == Type.CHAR_TYPE)
		{
			className = "Character";
			methodName = "charValue";
			methodDesc = "()C";
			checkcastType = Type.getType(Character.class);
		}
		
		mv.visitTypeInsn(CHECKCAST, toInternalName(checkcastType));
		
		if (methodName != null)
		{
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/"+className, methodName, methodDesc, false);
		}
	}
	
	/**
	 * Ex: path.to.class.TestClass
	 */
	public static String toCanonicalName(String desc)
	{
		if (desc.length() == 1 && "CBSIJZDF".contains(desc))
			return Type.getType(desc).getClassName();
		
		desc = desc.replaceFirst("L", "");
		
		if (desc.endsWith(";"))
			desc = desc.substring(0, desc.length() - 1);
		
		return desc.replace("/", ".");
	}
	
	public static void invokeStore(MethodVisitor mv, Type type, int id)
	{
		if (type != Type.VOID_TYPE)
			mv.visitVarInsn(getStoreOpcode(type), id);
	}
	
	public static void invokeLoad(MethodVisitor mv, Type type, int id)
	{
		if (type != Type.VOID_TYPE)
			mv.visitVarInsn(getLoadOpcode(type), id);
	}
	
	public static void getField(MethodVisitor mv, FieldInfo fieldInfo)
	{
		ClassInfo classInfo = fieldInfo.getOwner();
		mv.visitFieldInsn(fieldInfo.isStatic() ? Opcodes.GETSTATIC : Opcodes.GETFIELD, classInfo.getType().getInternalName(), fieldInfo.name, fieldInfo.descriptor);
	}
	
	public static void putField(MethodVisitor mv, FieldInfo fieldInfo)
	{
		ClassInfo classInfo = fieldInfo.getOwner();
		mv.visitFieldInsn(fieldInfo.isStatic() ? Opcodes.PUTSTATIC : Opcodes.PUTFIELD, classInfo.getType().getInternalName(), fieldInfo.name, fieldInfo.descriptor);
	}
	
	public static void invokeMethod(MethodVisitor mv, MethodInfo methodInfo)
	{
		ClassInfo classInfo = methodInfo.getOwner();
		mv.visitMethodInsn(methodInfo.isStatic() ? Opcodes.INVOKESTATIC : Opcodes.INVOKEVIRTUAL, classInfo.getType().getInternalName(), methodInfo.name, methodInfo.descriptor, false);
	}
	
	public static void invokeReturn(MethodVisitor mv, Type type)
	{
		mv.visitInsn(getReturnOpcode(type));
	}
	
	public static int getMethodArgumentsCount(String methodDesc)
	{
		String regex = "L[^;]*;";
		
		methodDesc = methodDesc.substring(1, methodDesc.indexOf(")"));
		
		int count = StringUtils.countMatchesRegex(methodDesc, regex);
		
		methodDesc = methodDesc.replaceAll(regex, "");
		
		count += methodDesc.length();
		
		return count;
	}
	
	public static int getStoreOpcode(Type type)
	{
		int opcode = ASTORE;
		
		if (EqualsUtils.objectEqualsSome(type, Type.BOOLEAN_TYPE, Type.BYTE_TYPE, Type.CHAR_TYPE, Type.SHORT_TYPE, Type.INT_TYPE))
		{
			opcode = ISTORE;
		}
		else if (type == Type.LONG_TYPE)
		{
			opcode = LSTORE;
		}
		else if (type == Type.FLOAT_TYPE)
		{
			opcode = FSTORE;
		}
		else if (type == Type.DOUBLE_TYPE)
		{
			opcode = DSTORE;
		}
		
		return opcode;
	}
	
	public static int getLoadOpcode(Type type)
	{
		int opcode = ALOAD;
		
		if (EqualsUtils.objectEqualsSome(type, Type.BOOLEAN_TYPE, Type.BYTE_TYPE, Type.CHAR_TYPE, Type.SHORT_TYPE, Type.INT_TYPE))
		{
			opcode = ILOAD;
		}
		else if (type == Type.LONG_TYPE)
		{
			opcode = LLOAD;
		}
		else if (type == Type.FLOAT_TYPE)
		{
			opcode = FLOAD;
		}
		else if (type == Type.DOUBLE_TYPE)
		{
			opcode = DLOAD;
		}
		
		return opcode;
	}
	
	public static int getReturnOpcode(Type type)
	{
		int opcode = ARETURN;
		
		if (EqualsUtils.objectEqualsSome(type, Type.BOOLEAN_TYPE, Type.BYTE_TYPE, Type.CHAR_TYPE, Type.SHORT_TYPE, Type.INT_TYPE))
			opcode = IRETURN;
		else if (type == Type.FLOAT_TYPE)
			opcode = FRETURN;
		else if (type == Type.DOUBLE_TYPE)
			opcode = DRETURN;
		else if (type == Type.LONG_TYPE)
			opcode = LRETURN;
		else if (type == Type.VOID_TYPE)
			opcode = RETURN;
		
		return opcode;
	}
	
	public static void invokeStatic(MethodVisitor mv, String className, String methodName, String methodDesc)
	{
		mv.visitMethodInsn(INVOKESTATIC, className, methodName, methodDesc, false);
	}
	
	public static List<String> parseArgumentsFromDescriptor(String desc)
	{
		return parseArgumentsFromDescriptor(desc, new ArrayList<>());
	}
	
	public static List<String> parseArgumentsFromDescriptor(String desc, List<String> list)
	{
		if (desc == null)
			return null;
		
		if (desc.contains("("))
			desc = desc.substring(desc.indexOf("(") + 1, desc.indexOf(")"));
		
		int index;
		
		for (int i1 = 0; i1 < desc.length(); i1 ++)
		{
			if (desc.charAt(i1) == 'L' || (desc.charAt(i1) == '[' && desc.length() - 1 > i1 && desc.charAt(i1 + 1) == 'L'))
			{
				list.add(desc.substring(i1, index = desc.indexOf(';') ));
				return parseArgumentsFromDescriptor(desc.substring(index+1), list);
			}
			else if (desc.charAt(i1) == '[' && desc.length() - 1 > i1)
			{
				list.add(desc.charAt(i1)+desc.charAt(i1 + 1)+"");
				i1 ++;
			}
		}
		
		return list;
		
	}
	
}
