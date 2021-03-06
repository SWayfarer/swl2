package ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.luajc.upvalue;

import ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.LuaValue;

/**
 * A upvalue that delegates to another upvalue.
 */
public final class ProxyUpvalue extends AbstractUpvalue {
	public AbstractUpvalue upvalue;

	public ProxyUpvalue(AbstractUpvalue upvalue) {
		this.upvalue = upvalue;
	}

	@Override
	public void setUpvalue(LuaValue value) {
		upvalue.setUpvalue(value);
	}

	@Override
	public LuaValue getUpvalue() {
		return upvalue.getUpvalue();
	}

	@Override
	public void close() {
		if (!(upvalue instanceof ReferenceUpvalue)) {
			upvalue = new ReferenceUpvalue(upvalue.getUpvalue());
		}
	}
}
