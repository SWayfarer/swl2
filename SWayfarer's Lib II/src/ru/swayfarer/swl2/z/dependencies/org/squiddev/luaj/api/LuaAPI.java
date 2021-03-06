package ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.LuaValue;

/**
 * Show that this function is a Lua API
 *
 * This doesn't need to be explicitly used on your class unless you plan on returning it from a {@link LuaFunction}
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LuaAPI {
	/**
	 * The names of the Lua API
	 *
	 * @return Array of names this API should be set to
	 * @see LuaObject#bind(LuaValue)
	 */
	String[] value() default "";
}
