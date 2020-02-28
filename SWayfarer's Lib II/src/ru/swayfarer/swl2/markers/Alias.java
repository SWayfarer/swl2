package ru.swayfarer.swl2.markers;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

/**
 * Аватар. Редирект. "Алиас".
 * <br> Метод, адресующийся на другой, который был добавлен ради удобства писать (мб кому-то нравится писать at вместо get)
 * <h1> Маркер </h1>
 * @author swayfarer
 *
 */
@Retention(RUNTIME)
public @interface Alias
{
	public String value();
}
