package ru.swayfarer.swl2.asm.informated;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import ru.swayfarer.swl2.asm.AsmUtils;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Opcodes;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;

/**
 * Информация о поле
 * @author swayfarer
 */
@Data
public class FieldInfo {

	/** Доступ к полю через Opcodes */
	public int access;
	
	/** Имя поля */
	public String name;
	
	/** Дескриптор поля */
	public String descriptor;
	
	/** Сигнатура поля */
	public String signature;
	
	/** Начальное значение поля */
	public Object value;
	
	/** Аннотации поля */
	public List<AnnotationInfo> annotations = new ArrayList<>();
	
	public ClassInfo owner;

	@Override
	public String toString()
	{
		return "FieldInfo [access=" + access + ", name=" + name + ", descriptor=" + descriptor + ", signature=" + signature + ", value=" + value + ", annotations=" + annotations + "]";
	}
	
	public Type getType()
	{
		return Type.getType(descriptor);
	}
	
	public boolean isPrimitive()
	{
		return AsmUtils.isPrimitive(getType());
	}
	
	/**
	 * Имеет ли класс аннотацию?
	 * @param desc Дескриптор аннотации
	 * @return Имеет ли?
	 */
	public boolean hasAnnotation(String desc)
	{
		return getFirstAnnotation(desc) != null;
	}
	
	/**
	 * Получить все аннотации с по дескриптору
	 * @param desc Дескриптор аннотации
	 * @return Лист аннотаций на классе
	 */
	public List<AnnotationInfo> getAnnotationList(String desc)
	{
		List<AnnotationInfo> ret = new ArrayList<>();
		
		for (AnnotationInfo info : annotations)
			if (info.descritor.equals(desc))
				ret.add(info);
		
		return ret;
	}
	
	/**
	 * Получить первую аннотацию по дескриптору
	 * @param desc Дескриптор аннотации
	 * @return Первая аннотация. Null, если таковой нет
	 */
	public AnnotationInfo getFirstAnnotation(String desc)
	{
		for (AnnotationInfo info : annotations)
			if (info.descritor.equals(desc))
				return info;
		
		return null;
	}
	
	/** Является ли метод статическим? */
	public boolean isStatic()
	{
		return (access & Opcodes.ACC_STATIC) != 0;
	}
}
