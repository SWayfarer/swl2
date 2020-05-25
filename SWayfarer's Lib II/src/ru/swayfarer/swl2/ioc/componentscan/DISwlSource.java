package ru.swayfarer.swl2.ioc.componentscan;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Автоматически регистрируемый источник контекста 
 * @author swayfarer
 *
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface DISwlSource
{
	/** Контекст, в который будут добавлены элементы источника */
	public String context() default "";
	
	@Retention(RUNTIME)
	@Target(METHOD)
	public static @interface InitEvent {}
	
	@Retention(RUNTIME)
	@Target(METHOD)
	public static @interface PreInitEvent {}
}
