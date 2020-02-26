package ru.swayfarer.swl2.asm.nametransformer;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@Retention(RUNTIME)
public @interface RenameAsm
{
	public String value();
}
