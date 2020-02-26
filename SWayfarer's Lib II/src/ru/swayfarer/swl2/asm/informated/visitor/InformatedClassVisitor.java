package ru.swayfarer.swl2.asm.informated.visitor;

import java.util.HashMap;
import java.util.Map;

import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.informated.FieldInfo;
import ru.swayfarer.swl2.asm.informated.MethodInfo;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.FieldVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Opcodes;

/**
 * {@link ClassVisitor}, знающий информацио о посещаемом классе 
 * @author swayfarer
 *
 */
public class InformatedClassVisitor extends ClassVisitor implements Opcodes {

	/** Информация о классе */
	public ClassInfo classInfo;
	
	/** Имя класса */
	public String className;
	
	/** Имя + дескриптор метода в информацио о методе */
	public Map<String, MethodInfo> methodToInfo = new HashMap<>();
	
	/** Имя + дескриптор поля в информацио о поле */
	public Map<String, FieldInfo> fieldToInfo = new HashMap<>();
	
	public InformatedClassVisitor(ClassVisitor classVisitor, ClassInfo classInfo)
	{
		super(classVisitor);
		this.classInfo = classInfo;
		
		for (FieldInfo fieldInfo : classInfo.fields)
		{
			String fieldName = getFieldKey(fieldInfo.name, fieldInfo.descriptor);
			fieldToInfo.put(fieldName, fieldInfo);
		}
		
		for (MethodInfo methodInfo : classInfo.methods)
		{
			String methodName = getMethodKey(methodInfo.name, methodInfo.descriptor);
			methodToInfo.put(methodName, methodInfo);
		}
	}
	
	/** Получить ключ для поля в карте {@link #fieldToInfo}*/
	public String getFieldKey(String name, String desc)
	{
		return name + ":" + desc;
	}
	
	/** Получить ключ для метода в карте {@link #methodToInfo}*/
	public String getMethodKey(String name, String desc)
	{
		return name + ":" + desc;
	}
	
	/** 
	 * Посетить метод, зная информацию о нем
	 * @param info - Информаця о методе
	 * @param access - Доступ к методу
	 * @param name - Имя метода
	 * @param descriptor - Дескриптор метода 
	 * @param signature - Сигнатура метода 
	 * @param exceptions - Исклюения метода
	 * @return {@link MethodVisitor}, который будет посещать следующим 
	 */
	public MethodVisitor visitMethodInformated(MethodInfo info, int access, String name, String descriptor, String signature, String[] exceptions)
	{
		return super.visitMethod(access, name, descriptor, signature, exceptions);
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions)
	{
		return visitMethodInformated(methodToInfo.get(getMethodKey(name, descriptor)), access, name, descriptor, signature, exceptions);
	}
	
	/** 
	 * Посетить поле, зная информацию о нем
	 * @param info - Информаця о поле
	 * @param access - Доступ к полю
	 * @param name - Имя метода
	 * @param descriptor - Дескриптор поля 
	 * @param signature - Сигнатура поля 
	 * @return {@link FieldVisitor}, который будет посещать следующим 
	 */
	public FieldVisitor visitFieldInformated(FieldInfo info, int access, String name, String descriptor, String signature, Object value)
	{
		return super.visitField(access, name, descriptor, signature, value);
	}
	
	@Override
	public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value)
	{
		return visitFieldInformated(fieldToInfo.get(getFieldKey(name, descriptor)), access, name, descriptor, signature, value);
	}
	

	/**
	 * Посетить класс, зная информацию о нем
	 * @param classInfo - Информация о классе
	 * @param version - Версия класса
	 * @param access - Доступ к классу 
	 * @param name - Имя класса
	 * @param signature - Сигнатура класса 
	 * @param superName
	 * @param interfaces
	 */
	public void visitClassInformated(ClassInfo classInfo, int version, int access, String name, String signature, String superName, String[] interfaces)
	{
		super.visit(version, access, name, signature, superName, interfaces);
	}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
	{
		className = name;
		visitClassInformated(classInfo, version, access, name, signature, superName, interfaces);
	}

}
