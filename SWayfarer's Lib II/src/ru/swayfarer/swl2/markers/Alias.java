package ru.swayfarer.swl2.markers;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@Retention(RUNTIME)
public @interface Alias
{
	public String value();
}
