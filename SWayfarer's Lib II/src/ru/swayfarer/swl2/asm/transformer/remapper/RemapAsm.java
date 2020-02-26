package ru.swayfarer.swl2.asm.transformer.remapper;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@Retention(RUNTIME)
public @interface RemapAsm
{
	public String value() default "";
}
