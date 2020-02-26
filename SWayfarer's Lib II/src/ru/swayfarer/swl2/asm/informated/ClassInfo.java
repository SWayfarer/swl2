package ru.swayfarer.swl2.asm.informated;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import ru.swayfarer.swl2.asm.TransformedClassInfo;
import ru.swayfarer.swl2.asm.transformer.informated.ClassScannerTransformer;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.resource.rlink.RLUtils;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;

/**
 * Информация о классе
 * @author swayfarer
 */
@Data
public class ClassInfo {

	/** Версия класса. На какой Java скомпилирован */
	public int version; 
	
	/** Доступ к классу через битовые опкоды */
	public int access; 
	
	/** Имя класса */
	public String name; 
	
	/** Сигнатура класса */
	public String signature; 
	
	/** Имя суперкласса */
	public String superName;
	
	/** Интерфейсы класса */
	public List<String> interfaces;
	
	/** Лист информации о полях */
	public IExtendedList<FieldInfo> fields = CollectionsSWL.createExtendedList();
	
	/** Лист информации о методах */
	public IExtendedList<MethodInfo> methods = CollectionsSWL.createExtendedList();
	
	/** Лист информации об аннотациях */
	public IExtendedList<AnnotationInfo> annotations = CollectionsSWL.createExtendedList();

	
	@Override
	public String toString()
	{
		return "ClassInfo [version=" + version + ", access=" + access + ", name=" + name + ", signature=" + signature + ", superName=" + superName + ", interfaces=" + interfaces + ", fields=" + fields + ", methods=" + methods
				+ ", annotations=" + annotations + "]";
	}
	
	public String getCanonicalName()
	{
		return getType().getClassName();
	}
	
	public String getInternalName()
	{
		return getType().getInternalName();
	}
	
	public FieldInfo getField(String name)
	{
		return fields.dataStream().find((var) -> var.name.equals(name));
	}
	
	/**
	 * Прочитать информацию о классе из его байтов
	 * @param bytes Байты класса
	 * @return Информация о классе. Null, если произошла ошибка
	 */
	public static ClassInfo valueOf(byte[] bytes)
	{
		TransformedClassInfo info = new TransformedClassInfo();
		ClassScannerTransformer tr = new ClassScannerTransformer();
		tr.transform("", bytes, info);
		return tr.info;
	}
	
	/**
	 * Имеет ли класс интерфейс?
	 * @param desc Дескриптор интерфейса
	 * @return Имеет ли?
	 */
	public boolean hasInterface(String desc)
	{
		return interfaces.contains(desc);
	}
	
	/**
	 * Имеет ли класс аннотацию?
	 * @param desc Дескриптор аннотации
	 * @return Имеет ли?
	 */
	public boolean hasAnnotation(String desc)
	{
		return getFirstAnnotation(desc) == null;
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
	
	public MethodInfo getMethod(String name, String desc)
	{
		return methods.dataStream()
				.find((method) -> method.name.equals(name) && method.descriptor.equals(desc));
	}
	
	public Type getType()
	{
		return Type.getType("L" + name + ";");
	}
	
	/** См {@link #valueOf(String)}*/
	public static ClassInfo valueOf(Class<?> cl)
	{
		return valueOf(cl.getCanonicalName());
	}
	
	public static ClassInfo valueOf(String classCanonicalName)
	{
		TransformedClassInfo info = new TransformedClassInfo();
		ClassScannerTransformer transformer = new ClassScannerTransformer();
		transformer.transform(classCanonicalName, RLUtils.toBytes("/"+classCanonicalName.replace(".", "/")+".class"), info);
		return transformer.info;
	}
}
