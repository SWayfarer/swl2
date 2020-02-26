package ru.swayfarer.swl2.asm.transformer.nullsafe;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface NullSafe {

	public String message() default "%name% can't be null";
	public Class<?> exception() default IllegalArgumentException.class;
}
