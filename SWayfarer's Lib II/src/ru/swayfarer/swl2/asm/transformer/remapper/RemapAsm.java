package ru.swayfarer.swl2.asm.transformer.remapper;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

/**
 * Отмеченные этой аннотацией элементы будут отремапены в новое, указанное в аннотации имя 
 * @author swayfarer
 *
 */
@Retention(RUNTIME)
public @interface RemapAsm
{
	/** Новое имя */
	public String value() default "";
}
