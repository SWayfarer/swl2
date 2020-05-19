package ru.swayfarer.swl2.ioc.componentscan;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import ru.swayfarer.swl2.ioc.context.elements.ContextElementType;

@Retention(RUNTIME)
@Target(TYPE)
public @interface DISwlComponent
{
	public String name();
	public String context() default "";
	public ContextElementType type() default ContextElementType.Singleton;
	public Class<?> associated() default Object.class;
}
