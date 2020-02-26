package ru.swayfarer.swl2.asm.nametransformer;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

/**
 * Переименовать. Во время ремаппинга имя этого элемента будет заменено на указанное в аннотации 
 * @author swayfarer
 *
 */
@Retention(RUNTIME)
public @interface RenameAsm
{
	public String value();
}
