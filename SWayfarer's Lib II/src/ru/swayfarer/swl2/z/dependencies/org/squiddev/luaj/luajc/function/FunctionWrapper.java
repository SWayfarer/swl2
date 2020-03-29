package ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.luajc.function;

import ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.LuaFunction;
import ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.LuaValue;
import ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.Prototype;
import ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.Varargs;
import ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.luajc.IGetPrototype;
import ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.luajc.analysis.ProtoInfo;
import ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.luajc.upvalue.AbstractUpvalue;

/**
 * Subclass of {@link LuaFunction} common to LuaJC compiled functions.
 */
public final class FunctionWrapper extends BasicLuaFunction implements IGetPrototype {
	public final ProtoInfo info;
	public final Prototype prototype;

	public final AbstractUpvalue[] upvalues;

	public FunctionWrapper(ProtoInfo info, LuaValue env) {
		super(env);
		this.info = info;
		this.prototype = info.prototype;
		this.upvalues = new AbstractUpvalue[prototype.nups];
	}

	@Override
	public Prototype getPrototype() {
		return info.prototype;
	}

	@Override
	public LuaValue call() {
		return info.executor.execute(this);
	}

	@Override
	public LuaValue call(LuaValue arg) {
		return info.executor.execute(this, arg);
	}

	@Override
	public LuaValue call(LuaValue arg1, LuaValue arg2) {
		return info.executor.execute(this, arg1, arg2);
	}

	@Override
	public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
		return info.executor.execute(this, arg1, arg2, arg3);
	}

	@Override
	public Varargs invoke(Varargs args) {
		return onInvoke(args).eval();
	}

	@Override
	public Varargs onInvoke(Varargs args) {
		return info.executor.execute(this, args);
	}
}
