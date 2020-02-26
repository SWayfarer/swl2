package ru.swayfarer.swl2.asm;

/** Преобразователь классов */
public interface IClassTransformer {

	public byte[] transform(String name, byte[] bytes, TransformedClassInfo info);
	
}
