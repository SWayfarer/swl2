package ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.luajc.upvalue;

import ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.LuaValue;

/**
 * An upvalue that is backed by a variable.
 */
public final class ReferenceUpvalue extends AbstractUpvalue {
	private LuaValue value;

	public ReferenceUpvalue() {
	}

	public ReferenceUpvalue(LuaValue value) {
		this.value = value;
	}

	@Override
	public void setUpvalue(LuaValue value) {
		this.value = value;
	}

	@Override
	public LuaValue getUpvalue() {
		return value;
	}
}
