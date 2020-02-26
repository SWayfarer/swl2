package ru.swayfarer.swl2.asm.transformer.patcher;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@Retention(RUNTIME)
public @interface PatchAsm
{
	public String version() default "1.0.0";
}
