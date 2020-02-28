package ru.swayfarer.swl2.asm.transformer.informated;

import ru.swayfarer.swl2.asm.TransformedClassInfo;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.transformer.basic.AbstractAsmTransformer;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassReader;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassWriter;

/** 
 * Трансформер классов, обладающий информацией о трансформируемом объекте 
 * @author swayfarer
 *
 */
public abstract class InformatedClassTransformer extends AbstractAsmTransformer{

	
	@Deprecated
	@Override
	public void transform(String name, byte[] bytes, ClassReader reader, ClassWriter writer, TransformedClassInfo info)
	{
		transform(name, bytes, reader, writer, ClassInfo.valueOf(bytes), info);
	}
	
	/**
	 * Преобразовать класс
	 * @param name Каноничное имя класса. То, под которым его знает класслоадер
	 * @param bytes Байты, которые будут трасформированы
	 * @param reader Читалка классов, которая аццептит визиторов
	 * @param writer Записывалка классов, которую нужно передавать в свои визиторы
	 * @param info Информация о классе
	 * @param transformedClassInfo Информация о трансформированном классе 
	 * @return трансформированные байты класса, из которых он будет загружен
	 */
	public void transform(String name, byte[] bytes, ClassReader reader, ClassWriter writer, ClassInfo info, TransformedClassInfo transformedClassInfo)
	{
		transform(name, bytes, reader, writer, info);
	}
	
	/**
	 * Преобразовать класс
	 * @param name Каноничное имя класса. То, под которым его знает класслоадер
	 * @param bytes Байты, которые будут трасформированы
	 * @param reader Читалка классов, которая аццептит визиторов
	 * @param writer Записывалка классов, которую нужно передавать в свои визиторы
	 * @param info Информация о классе
	 * @return трансформированные байты класса, из которых он будет загружен
	 */
	public void transform(String name, byte[] bytes, ClassReader reader, ClassWriter writer, ClassInfo info) {}

}
