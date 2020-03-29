package ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.api.setters;

import ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.LuaTable;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;
import ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.api.builder.BuilderException;
import ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.api.builder.IInjector;
import ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.api.builder.tree.LuaField;

import static ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Opcodes.ALOAD;
import static ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Opcodes.GETFIELD;
import static ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.api.builder.BuilderConstants.CLASS_LUATABLE;

/**
 * Sets the field to be the API's table
 */
public class TableSetter implements IInjector<LuaField> {
	/**
	 * Inject bytecode into a visitor
	 *
	 * @param visitor The method visitor to inject into
	 * @param field   The field that we are injecting with
	 */
	@Override
	public void inject(MethodVisitor visitor, LuaField field) {
		if (!field.field.getType().isAssignableFrom(LuaTable.class)) {
			throw new BuilderException("Cannot convert " + field.field.getType().getName() + " to LuaTable", field);
		}

		visitor.visitVarInsn(ALOAD, 0);
		visitor.visitFieldInsn(GETFIELD, field.klass.name, "table", CLASS_LUATABLE);
	}
}
