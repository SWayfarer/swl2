package ru.swayfarer.swl2.ioc;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

/**
 * Отметить этой аннотацией класс приложения, чтобы активировать сканирование компонентов в подпакетах.
 * @author swayfarer
 *
 */
@Retention(RUNTIME)
public @interface EnableDIScan
{
	public String[] roots() default "";
}
