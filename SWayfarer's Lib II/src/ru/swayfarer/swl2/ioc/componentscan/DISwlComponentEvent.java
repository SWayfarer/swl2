package ru.swayfarer.swl2.ioc.componentscan;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Событие компонента DI <br>
 * События вызываются во время жизненного цикла компонента на разных стадиях <br>
 * Отмеченные методы становятся обработчиками этих событий
 * @author swayfarer
 *
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface DISwlComponentEvent
{
	/** Тип события */
	public ComponentEventType value() default ComponentEventType.Init;
}
