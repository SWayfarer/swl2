package ru.swayfarer.swl2.asm.injection.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;

/**
 * Отмечает метод как метод-вставку
 * @author swayfarer
 *
 */
@Retention(RUNTIME)
public @interface InjectionAsm
{
	public static final Type type = Type.getType(InjectionAsm.class);
	
	/** Вставлять ли метод на выходе? */
	public boolean injectOnExit() default false;
	
	/** Целевой класс. При использовании укажите {@link Object} первым аргументом */
	public String targetClass() default "";
	
	public String targetClassInternal() default "";
	public String targetMethodDesc() default "";
	
	/** Имя целевого метода */
	public String targetMethod() default "";
	
	//public Funct
}
