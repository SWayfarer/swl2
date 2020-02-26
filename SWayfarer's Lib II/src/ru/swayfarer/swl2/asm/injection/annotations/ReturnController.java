package ru.swayfarer.swl2.asm.injection.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;

/**
 * Позволяет управлять возвратом результата деятельности метода. 
 * @author swayfarer
 */
@Retention(RUNTIME)
public @interface ReturnController
{
	public static Type type = Type.getType(ReturnController.class);
}
