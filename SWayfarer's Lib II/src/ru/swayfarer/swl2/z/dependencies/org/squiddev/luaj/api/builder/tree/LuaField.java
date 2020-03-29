package ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.api.builder.tree;

import java.lang.reflect.Field;

import ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.api.builder.IInjector;

/**
 * A field of a Lua class
 */
public class LuaField {
	/**
	 * The parent method for this argument
	 */
	public final LuaClass klass;

	/**
	 * The parameter for this argument
	 */
	public final Field field;

	/**
	 * A method to call to set this value.
	 */
	public IInjector<LuaField> setup = null;

	public LuaField(LuaClass klass, Field field) {
		this.klass = klass;
		this.field = field;

		if (klass.settings.transformer != null) klass.settings.transformer.transform(this);
	}
}
