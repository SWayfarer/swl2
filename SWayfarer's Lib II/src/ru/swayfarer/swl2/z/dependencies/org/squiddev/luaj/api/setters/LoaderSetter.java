package ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.api.setters;

import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;
import ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.api.builder.APIClassLoader;
import ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.api.builder.BuilderException;
import ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.api.builder.IInjector;
import ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.api.builder.tree.LuaField;

import static ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Opcodes.GETSTATIC;
import static ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.api.builder.BuilderConstants.CLASS_LOADER;

/**
 * Sets the field to be the class loader
 */
public class LoaderSetter implements IInjector<LuaField> {
	/**
	 * Inject bytecode into a visitor
	 *
	 * @param visitor The method visitor to inject into
	 * @param field   The field that we are injecting with
	 */
	@Override
	public void inject(MethodVisitor visitor, LuaField field) {
		if (!field.field.getType().isAssignableFrom(APIClassLoader.class)) {
			throw new BuilderException("Cannot convert " + field.field.getType().getName() + " to APIClassLoader", field);
		}

		visitor.visitFieldInsn(GETSTATIC, field.klass.name, "LOADER", CLASS_LOADER);
	}
}
