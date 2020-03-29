package ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.luajc.function.executors;

import ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.LuaValue;
import ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.Varargs;
import ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.luajc.function.FunctionExecutor;
import ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.luajc.function.FunctionWrapper;

/**
 * Function executor with 0 arguments
 */
public abstract class ArgExecutor0 extends FunctionExecutor {
	@Override
	public final Varargs execute(FunctionWrapper function, Varargs varargs) {
		return execute(function);
	}

	@Override
	public final LuaValue execute(FunctionWrapper function, LuaValue arg1) {
		return execute(function);
	}

	@Override
	public final LuaValue execute(FunctionWrapper function, LuaValue arg1, LuaValue arg2) {
		return execute(function);
	}

	@Override
	public final LuaValue execute(FunctionWrapper function, LuaValue arg1, LuaValue arg2, LuaValue arg3) {
		return execute(function);
	}
}
