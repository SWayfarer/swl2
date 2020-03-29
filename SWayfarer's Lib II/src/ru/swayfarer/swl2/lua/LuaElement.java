package ru.swayfarer.swl2.lua;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Аннотация, отмечающая элементы, которые будут представлены {@link LuaInterpreter}'ом как элементы скрипта. 
 * @author swayfarer
 *
 */
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
public @interface LuaElement
{
	/** Имя элемента */
	public String name() default "";
	
	/** Альтернативные имена элемента (не перекрывает {@link #name()}) */
	public String[] alternates() default {};
}
