package ru.swayfarer.swl2.asm.informated;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.ToString;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;

/**
 * Информация о переменной
 * @author swayfarer
 */
@Data
@ToString
public class VariableInfo {

	/** Id переменной */
	public int id;
	
	/** Имя переменной */
	public String name;
	
	/** Дескриптор переменной */
	public String descriptor;
	
	/** Сигнатура переменной */
	public String signature;
	
	/** Аннотации переменной */
	public List<AnnotationInfo> annotations = CollectionsSWL.createExtendedList();
	
	/**
	 * Информация о переменной
	 * @param id Id переменной 
	 * @param name Имя переменной
	 * @param descriptor Дескриптор переменной
	 * @param signature Сигатура переменной 
	 */
	public VariableInfo(int id, String name, String descriptor, String signature)
	{
		super();
		this.name = name;
		this.descriptor = descriptor;
		this.signature = signature;
		this.id = id;
	}
	
	/** Получить тип переменной */
	public Type getType()
	{
		return Type.getType(descriptor);
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
	
	/**
	 * Имеет ли класс аннотацию?
	 * @param desc Дескриптор аннотации
	 * @return Имеет ли?
	 */
	public boolean isAnnotated(String desc)
	{
		return hasAnnotation(desc);
	}
	
	/** Является ли объектом (не-примитивом)*/
	public boolean isObject()
	{
		return descriptor.endsWith(";");
	}
	
	/** Является ли примитивом (long, double, int, char, boolean и т.п.) */
	public boolean isPrimitive()
	{
		return !isObject();
	}
	
	/** Является ли массивом */
	public boolean isArray()
	{
		return descriptor.startsWith("[");
	}
}
