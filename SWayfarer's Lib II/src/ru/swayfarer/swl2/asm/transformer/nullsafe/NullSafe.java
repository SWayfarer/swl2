package ru.swayfarer.swl2.asm.transformer.nullsafe;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Аннотация, обеспечивающая автоматическую проверку на null отмеченных элементов 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface NullSafe {

	/** Сообщение, которое будет записано в указанный {@link Exception} */
	public String message() default "%name% can't be null";
	
	/** Тип {@link Exception}'а, который будет брошен при невыполнении условия */
	public Class<?> exception() default IllegalArgumentException.class;
}
