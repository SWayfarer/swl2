package ru.swayfarer.swl2.json;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

/** Указанный тип Json для (де)сериализации */
@Retention(RUNTIME)
public @interface JsonType
{
	public String as() default "";
	public String from() default "";
}
