package ru.swayfarer.swl2.asm.injection.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;

/**
 * Позволяет получить результат деятельности метода. Использование неумолимо приводит к установке параметра injectOnExit в true по понятным причинам
 * @author swayfarer
 */
@Retention(RUNTIME)
public @interface ReturnValue
{
	public static final Type type = Type.getType(ReturnValue.class);
}
