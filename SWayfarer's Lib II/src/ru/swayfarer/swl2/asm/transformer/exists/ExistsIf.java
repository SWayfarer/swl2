package ru.swayfarer.swl2.asm.transformer.exists;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@Retention(RUNTIME)
public @interface ExistsIf
{
	public String[] value() default {};
}
