package ru.swayfarer.swl2.ioc.context.elements;

import ru.swayfarer.swl2.asm.transformer.ditransformer.regetter.visitor.DynamicDI;

/** Типы элементов контектов */
public enum ContextElementType
{

	/**
	 * Синглтон <br>
	 * Единственный объект
	 */
	Singleton,

	/**
	 * Прототип <br>
	 * Каждый раз новый элемент <br>
	 * Если отметить иньектируемое поле {@link DynamicDI}, то каждое
	 * обращение к этому полю внутри класа будет возвращать новое значение
	 */
	Prototype,

	/**
	 * {@link ThreadLocal}-синглтон <br>
	 * Возвращает новый INSTANCE для каждого потока
	 */
	ThreadLocalPrototype
}