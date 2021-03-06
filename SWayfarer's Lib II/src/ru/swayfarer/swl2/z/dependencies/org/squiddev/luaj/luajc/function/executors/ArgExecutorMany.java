package ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.luajc.function.executors;

import ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.LuaValue;
import ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.Varargs;
import ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.luajc.function.FunctionExecutor;
import ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.luajc.function.FunctionWrapper;

/**
 * Function executor with many arguments
 */
public abstract class ArgExecutorMany extends FunctionExecutor {
	@Override
	public LuaValue execute(FunctionWrapper function) {
		return execute(function, (Varargs) LuaValue.NONE).arg1();
	}

	@Override
	public final LuaValue execute(FunctionWrapper function, LuaValue arg1) {
		return execute(function, (Varargs) arg1).arg1();
	}

	@Override
	public final LuaValue execute(FunctionWrapper function, LuaValue arg1, LuaValue arg2) {
		return execute(function, LuaValue.varargsOf(arg1, arg2)).arg1();
	}

	@Override
	public final LuaValue execute(FunctionWrapper function, LuaValue arg1, LuaValue arg2, LuaValue arg3) {
		return execute(function, LuaValue.varargsOf(arg1, arg2, arg3)).arg1();
	}
}
