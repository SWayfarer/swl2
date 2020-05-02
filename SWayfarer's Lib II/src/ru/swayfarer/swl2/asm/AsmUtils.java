package ru.swayfarer.swl2.asm;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.informated.FieldInfo;
import ru.swayfarer.swl2.asm.informated.MethodInfo;
import ru.swayfarer.swl2.asm.informated.VariableInfo;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.equals.EqualsUtils;
import ru.swayfarer.swl2.string.DynamicString;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.string.reader.StringReaderSWL;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Opcodes;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;

/** 
 * Утилиты для работы с ObjectWeb ASM (https://asm.ow2.io/)
 */
public class AsmUtils implements Opcodes{

	/**
	 *  Является ли класс с этим доступом интерфесом? 
	 *  @param access Доступ в формате int см {@link Opcodes}
	 */
	public static boolean isInterface(int access)
	{
		return (access & ACC_INTERFACE) != 0;
	}
	
	/** 
	 * Является ли элемент с этим доступом финальным? (final) 
	 * @param Доступ в формате int см {@link Opcodes}
	 */
	public static boolean isFinal(int access)
	{
		return (access & ACC_FINAL) != 0;
	}
	
	/** 
	 * Является ли элемент с этим доступом защищенным? (protected)
	 * @param Доступ в формате int см {@link Opcodes}
	 */
	public static boolean isProtected(int access)
	{
		return (access & ACC_PROTECTED) != 0;
	}
	
	/** 
	 * Является ли элемент с этим доступом приватным? (provate) 
	 * @param Доступ в формате int см {@link Opcodes}
	 */
	public static boolean isPrivate(int access)
	{
		return (access & ACC_PRIVATE) != 0;
	}
	
	/** 
	 * Это опкод загрузки объекта? 
	 * @param Опкод в формате int см {@link Opcodes}
	 */
	public static boolean isObjectLoadOpcode(int opcode)
	{
		return EqualsUtils.objectEqualsSome(opcode, ALOAD, AALOAD, IALOAD, DALOAD, FALOAD, LALOAD);
	}
	
	/** 
	 * Открыть доступ. Сделать public и убрать final
	 * @param Доступ в формате int см {@link Opcodes}
	 */
	public static int openAccess(int access) {
		
		if (isFinal(access))
		{
			access = access ^ ACC_FINAL;
		}
		
		if (isPrivate(access))
		{
			access = (access ^ ACC_PRIVATE) | ACC_PUBLIC;
		}
		
		if (isProtected(access))
		{
			access = (access ^ ACC_PROTECTED) | ACC_PUBLIC;
		}
		
		return access;
		
	}
	
	/** 
	 * Получить "Internal name" класса. 
	 * <br> Например, для java.lang.String вернет java/lang/String
	 * @param type Тип, имя которого получаем 
	 * @return Имя типа 
	 */
	public static String toInternalName(Type type)
	{
		return type.getInternalName();
	}
	
	/** 
	 * Получить "Internal name" класса. 
	 * <br> Например, для java.lang.String вернет java/lang/String
	 * @param desc Дескриптор, имя которого получаем 
	 * @return Имя типа 
	 */
	public static String toInternalName(String desc)
	{
		if (desc.length() == 1 && "CBSIJZDF".contains(desc))
			return Type.getType(desc).getInternalName();
		return toCanonicalName(desc).replace(".", "/");
	}
	
	/**
	 * Является ли тип примитивным? (таким как int, boolean и т.п.)
	 * @param type Тип, который проверяется 
	 * @return True, если примитив 
	 */
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
	
	/** 
	 * Получить дескриптор из каноничного названия класса 
	 * @param canonicalName Каноническое имя класса, дескриптор которого получаем 
	 * @return Дескриптор класса 
	 */
	public static String toDescName(String canonicalName)
	{
		return "L"+canonicalName.replace(".", "/")+";";
	}
	
	/** 
	 * Создать объект указанного типа и вызвать пустой конструктор 
	 * @param mv {@link MethodVisitor}, через который создаётся новый экземпляр
	 * @param classType Тип создаваемого экземпляра 
	 */
	public static void createInstance(MethodVisitor mv, Type classType)
	{
		newInstance(mv, classType);
		initInstance(mv, classType);
	}
	
	/** 
	 * Вызвать пустой конструктор 
	 * @param mv {@link MethodVisitor}, через который запишется вызов конструктора 
	 * @param classType Тип объекта, чей конструктор вызывается
	 */
	public static void initInstance(MethodVisitor mv, Type classType)
	{
		initInstance(mv, toInternalName(classType.getDescriptor()));
	}
	
	/** 
	 * Вызвать пустой конструктор
	 * @param mv {@link MethodVisitor}, через который запишется вызов конструктора 
	 * @param classInternalName Имя объекта, чей конструктор вызывается
	 */
	public static void initInstance(MethodVisitor mv, String classInternalName)
	{
		mv.visitMethodInsn(INVOKESPECIAL, classInternalName, "<init>", "()V", false);
	}
	
	/** 
	 * Создать новый объект указанного типа 
	 * <br> Конструктор при этом вызван не будет
	 */
	public static void newInstance(MethodVisitor mv, Type type)
	{
		newInstance(mv, toInternalName(type.getDescriptor()));
	}
	
	/** 
	 * Создать новый объект указанного типа 
	 * <br> Конструктор при этом вызван не будет
	 */
	public static void newInstance(MethodVisitor mv, String typeInternalName)
	{
		mv.visitTypeInsn(NEW, typeInternalName);
		mv.visitInsn(DUP);
	}
	
	/** 
	 * Вызвать return указанного типа в {@link MethodVisitor}'е
	 * <br> Если указан примитивный тип, то будет возвращена его обертка 
	 */
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
	
	/**
	 * Вызвать каст к указанному типу 
	 * <br> Если указан примитивный тип, то каст произведется к его обертке 
	 */
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
	 * Превратить дескриптор в каноничное имя класса. 
	 * <h1> Пример:
	 * <br> Было Lpath/to/class/TestClass; - стало path.to.class.TestClass
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
	
	/** Вызывать хранение объекта со стека в переменную под указанным id */
	public static void invokeStore(MethodVisitor mv, Type type, int id)
	{
		if (type != Type.VOID_TYPE)
			mv.visitVarInsn(getStoreOpcode(type), id);
	}
	
	/** Вызывать загрузку объекта на стек из переменной с указанным id */ //
	public static void invokeStore(MethodVisitor mv, VariableInfo variable)
	{
		invokeStore(mv, variable.getType(), variable.getId());
	}
	
	/** Вызывать загрузку объекта на стек из переменной с указанным id */ //
	public static void invokeLoad(MethodVisitor mv, Type type, int id)
	{
		if (type != Type.VOID_TYPE)
			mv.visitVarInsn(getLoadOpcode(type), id);
	}
	
	/** Вызывать загрузку объекта на стек из переменной с указанным id */ //
	public static void invokeLoad(MethodVisitor mv, VariableInfo variable)
	{
		invokeLoad(mv, variable.getType(), variable.getId());
	}
	
	/** Получить все типы, упомянутые в дескрипторе. Для методов включая возращаемый тип в конце */
	public static IExtendedList<DescriptorTypeInfo> getDescriptiorTypes(String descStr)
	{
		IExtendedList<DescriptorTypeInfo> ret = CollectionsSWL.createExtendedList();
		
		StringReaderSWL readerSWL = new StringReaderSWL(descStr);
		DynamicString currentType = new DynamicString();
		boolean isReadingType = false;
		int arrCount = 0;
		
		while (readerSWL.hasNextElement())
		{
			if (readerSWL.skipSome("(") || readerSWL.skipSome(")"))
			{
				continue;
			}
			
			Character current = readerSWL.next();
			
			if (current.equals('['))
			{
				arrCount ++;
			}
			else 
			{
				if (!isReadingType && current.equals('L'))
					isReadingType = true;
				else if (current.equals(';'))
					isReadingType = false;
				else 
				{
					if (current.equals('['))
					{
						new Throwable().printStackTrace();
					}
					currentType.append(current);
				}
				
				if (!isReadingType)
				{
					ret.add(DescriptorTypeInfo.of(currentType.toString(), arrCount));
					currentType.clear();
					arrCount = 0;
				}
			}
		}
		
		readerSWL.close();
		
		return ret;
	}
	
	/** Вызов филда, исользуя его {@link FieldInfo} */
	public static void getField(MethodVisitor mv, FieldInfo fieldInfo)
	{
		ClassInfo classInfo = fieldInfo.getOwner();
		mv.visitFieldInsn(fieldInfo.isStatic() ? Opcodes.GETSTATIC : Opcodes.GETFIELD, classInfo.getType().getInternalName(), fieldInfo.name, fieldInfo.descriptor);
	}
	
	/** Вызов задания филда, используя его {@link FieldInfo} */
	public static void putField(MethodVisitor mv, FieldInfo fieldInfo)
	{
		ClassInfo classInfo = fieldInfo.getOwner();
		mv.visitFieldInsn(fieldInfo.isStatic() ? Opcodes.PUTSTATIC : Opcodes.PUTFIELD, classInfo.getType().getInternalName(), fieldInfo.name, fieldInfo.descriptor);
	}
	
	/** Вызов метода, используя его {@link MethodInfo} */
	public static void invokeMethod(MethodVisitor mv, MethodInfo methodInfo)
	{
		ClassInfo classInfo = methodInfo.getOwner();
		mv.visitMethodInsn(methodInfo.isStatic() ? Opcodes.INVOKESTATIC : Opcodes.INVOKEVIRTUAL, classInfo.getType().getInternalName(), methodInfo.name, methodInfo.descriptor, false);
	}
	
	/** Вызвать return указанного типа */
	public static void invokeReturn(MethodVisitor mv, Type type)
	{
		mv.visitInsn(getReturnOpcode(type));
	}
	
	/** Получить кол-во параметров функции по ее дескриптору */
	public static int getMethodArgumentsCount(String methodDesc)
	{
		String regex = "L[^;]*;";
		
		methodDesc = methodDesc.substring(1, methodDesc.indexOf(")"));
		
		int count = StringUtils.countMatchesRegex(methodDesc, regex);
		
		methodDesc = methodDesc.replaceAll(regex, "");
		
		count += methodDesc.length();
		
		return count;
	}
	
	/** Получить {@link Opcodes} хранения указанного типа */
	public static int getStoreOpcode(Type type)
	{
		return type.getOpcode(ISTORE);
	}
	
	/** Получить {@link Opcodes} загрузки указанного типа */
	public static int getLoadOpcode(Type type)
	{
		return type.getOpcode(ILOAD);
	}
	
	/** Получить {@link Opcodes} возврата указанного типа */
	public static int getReturnOpcode(Type type)
	{
		return type.getOpcode(IRETURN);
	}
	
	/** Статический вызов метода */
	public static void invokeStatic(MethodVisitor mv, String className, String methodName, String methodDesc)
	{
		mv.visitMethodInsn(INVOKESTATIC, className, methodName, methodDesc, false);
	}
	
	/**
	 * Информация об элементе дескриптора 
	 * @author swayfarer
	 *
	 */
	@Data @AllArgsConstructor(staticName = "of")
	public static class DescriptorTypeInfo {
		public String internalName;
		public int arrCount;
	}
}
