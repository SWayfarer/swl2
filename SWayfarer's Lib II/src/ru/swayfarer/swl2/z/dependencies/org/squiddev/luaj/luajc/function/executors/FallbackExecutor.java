package ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.luajc.function.executors;

import static ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.LuaValue.NILS;
import static ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.LuaValue.NONE;
import static ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.LuaValue.varargsOf;

import ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.LuaValue;
import ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.Prototype;
import ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.Varargs;
import ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.luajc.function.FunctionExecutor;
import ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.luajc.function.FunctionWrapper;
import ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.luajc.function.LuaVM;

/**
 * Rewrite of {@link ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.LuaClosure} which will never compile.
 */
public final class FallbackExecutor extends FunctionExecutor {
	public static final FallbackExecutor INSTANCE = new FallbackExecutor();

	@Override
	public final LuaValue execute(FunctionWrapper function) {
		int size = function.prototype.maxstacksize;

		LuaValue[] stack = new LuaValue[size];
		System.arraycopy(NILS, 0, stack, 0, size);

		return LuaVM.execute(function, stack, NONE, 0).arg1();
	}

	@Override
	public final LuaValue execute(FunctionWrapper function, LuaValue arg) {
		Prototype prototype = function.prototype;
		int size = prototype.maxstacksize;

		LuaValue[] stack = new LuaValue[size];
		System.arraycopy(NILS, 0, stack, 0, size);

		switch (prototype.numparams) {
			default:
				stack[0] = arg;
				return LuaVM.execute(function, stack, NONE, 0).arg1();
			case 0:
				return LuaVM.execute(function, stack, arg, 0).arg1();
		}
	}

	@Override
	public final LuaValue execute(FunctionWrapper function, LuaValue arg1, LuaValue arg2) {
		Prototype prototype = function.prototype;
		int size = prototype.maxstacksize;

		LuaValue[] stack = new LuaValue[size];
		System.arraycopy(NILS, 0, stack, 0, size);

		switch (prototype.numparams) {
			default:
				stack[0] = arg1;
				stack[1] = arg2;
				return LuaVM.execute(function, stack, NONE, 0).arg1();
			case 1:
				stack[0] = arg1;
				return LuaVM.execute(function, stack, arg2, 0).arg1();
			case 0:
				return LuaVM.execute(function, stack, prototype.is_vararg != 0 ? varargsOf(arg1, arg2) : NONE, 0).arg1();
		}
	}

	@Override
	public final LuaValue execute(FunctionWrapper function, LuaValue arg1, LuaValue arg2, LuaValue arg3) {
		Prototype prototype = function.prototype;
		int size = prototype.maxstacksize;

		LuaValue[] stack = new LuaValue[size];
		System.arraycopy(NILS, 0, stack, 0, size);

		switch (prototype.numparams) {
			default:
				stack[0] = arg1;
				stack[1] = arg2;
				stack[2] = arg3;
				return LuaVM.execute(function, stack, NONE, 0).arg1();
			case 2:
				stack[0] = arg1;
				stack[1] = arg2;
				return LuaVM.execute(function, stack, arg3, 0).arg1();
			case 1:
				stack[0] = arg1;
				return LuaVM.execute(function, stack, prototype.is_vararg != 0 ? varargsOf(arg2, arg3) : NONE, 0).arg1();
			case 0:
				return LuaVM.execute(function, stack, prototype.is_vararg != 0 ? varargsOf(arg1, arg2, arg3) : NONE, 0).arg1();
		}
	}

	@Override
	public final Varargs execute(FunctionWrapper function, Varargs varargs) {
		Prototype prototype = function.prototype;
		int size = prototype.maxstacksize;

		LuaValue[] stack = new LuaValue[size];
		System.arraycopy(NILS, 0, stack, 0, size);

		int numParams = prototype.numparams;
		for (int i = 0; i < numParams; i++) {
			stack[i] = varargs.arg(i + 1);
		}
		return LuaVM.execute(function, stack, prototype.is_vararg != 0 ? varargs.subargs(numParams + 1) : NONE, 0);
	}
}
