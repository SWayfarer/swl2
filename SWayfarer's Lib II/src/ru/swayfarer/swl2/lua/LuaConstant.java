package ru.swayfarer.swl2.lua;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@Retention(RUNTIME)
public @interface LuaConstant
{
	public String name() default "";
}
