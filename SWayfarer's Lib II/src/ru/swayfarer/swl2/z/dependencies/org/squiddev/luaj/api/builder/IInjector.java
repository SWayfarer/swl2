package ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.api.builder;

import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;

/**
 * A bytecode item that can be injected into a {@link MethodVisitor}
 */
public interface IInjector<T> {
	/**
	 * Inject this into a visitor
	 *
	 * @param mv     The visitor to inject into
	 * @param object The object we are injecting with
	 */
	void inject(MethodVisitor mv, T object);
}
