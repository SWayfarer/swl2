package ru.swayfarer.swl2.asm;

/** Преобразователь классов */
public interface IClassTransformer {

	/**
	 * Преобразовать класс
	 * @param name Каноничное имя класса. То, под которым его знает класслоадер
	 * @param bytes Байты, которые будут трасформированы
	 * @param info Информация о трансформациях класса
	 * @return трансформированные байты класса, из которых он будет загружен
	 */
	public byte[] transform(String name, byte[] bytes, TransformedClassInfo info);
	
}
