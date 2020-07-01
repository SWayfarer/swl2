package ru.swayfarer.swl2.ioc.componentscan;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.ioc.DIManager;

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
	
	/**
	 * Зарегистрировать метод как постпроцессор элементов указанного контекста. <br>
	 * см. {@link DIManager#registerPostProcessor(String, IFunction1)}
	 * @author swayfarer
	 *
	 */
	@Retention(RUNTIME)
	@Target(METHOD)
	public @interface ElementPostprocessor {}
}
