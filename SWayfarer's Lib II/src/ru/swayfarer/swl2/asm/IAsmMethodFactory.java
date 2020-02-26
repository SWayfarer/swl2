package ru.swayfarer.swl2.asm;

import ru.swayfarer.swl2.asm.transformer.basic.AbstractAsmMethodTransformer.MethodClassVisitor;
import ru.swayfarer.swl2.asm.transformer.basic.AbstractAsmMethodTransformer.MethodMethodVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;

/**
 * Фабрика для трансформации методов
 * @author swayfarer
 */
public interface IAsmMethodFactory {
	
	/**
	 * Трансформировать метод
	 * @param methodVisitor MethodVisitor, который посещает класс
	 */
	public void transformMethod(MethodMethodVisitor methodVisitor);
	
	/**
	 * Посетить метод из ClassVisitor'а. 
	 * @param methodClassVisitor ClassVisitor, который посещает класс
	 * @return Кастомный или стандартный(оставить Null) MethodVisitor
	 */
	public MethodVisitor super_Visit(MethodClassVisitor methodClassVisitor);
}
