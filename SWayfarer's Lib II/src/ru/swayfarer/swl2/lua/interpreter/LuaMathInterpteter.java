package ru.swayfarer.swl2.lua.interpreter;

import ru.swayfarer.swl2.lua.LuaElement;
import ru.swayfarer.swl2.lua.LuaInterpreter;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.math.MathUtils;
import ru.swayfarer.swl2.resource.rlink.RLUtils;
import ru.swayfarer.swl2.threads.ThreadsUtils;

/**
 * Математический интерпретатор lua
 * @author swayfarer
 *
 */
public class LuaMathInterpteter {
	
	/** Экземпляр интерпретеатора для выполнения скриптов */
	@InternalElement
	public static final String script = RLUtils.pkg("math.lua").toSingleString();;
	
	/** Выполнить выражение */
	public double process(String s)
	{
		LuaInterpreter instance = getMathInterpeter();
		instance.processCustomScript(script.replace("text", s));
		return instance.globalsTable.get("process").checkdouble();
	}
	
	/** Получить экземпляр интерпретатора для текущего потока */
	@InternalElement
	public static LuaInterpreter getMathInterpeter()
	{
		return ThreadsUtils.getThreadLocal(() -> new LuaInterpreter().registerContentSources(new LuaMathModule()));
	}
	
	/** 
	 * Модуль, содержащий математические функции lua 
	 * @author swayfarer
	 *
	 */
	@InternalElement
	public static class LuaMathModule {
		
		/** Перевести число из 10-чной СС в base-ную*/
		@LuaElement
		public String base(double d, int base)
		{
			return MathUtils.toBase(d, base);
		}
		
		
		/** Перевести в 10-чную СС из base-чной*/
		@LuaElement
		public double dex(String s, double fromBase)
		{
			return MathUtils.toDex(s, (int)fromBase);
		}
		
		/** Перевести число из указанной СС в base-ную*/
		@LuaElement
		public String base2i(String s, double fromBase, double toBase)
		{
			return MathUtils.toBase(s, (int)fromBase, (int)toBase);
		}
		
		/** Получить десятичный логарифм числа */
		@LuaElement
		public double lg(double d)
		{
			return Math.log10(d);
		}
	}
}
