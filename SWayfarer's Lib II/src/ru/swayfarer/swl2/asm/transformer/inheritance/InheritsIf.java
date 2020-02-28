package ru.swayfarer.swl2.asm.transformer.inheritance;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Если класс отмечен этой аннотацией, то ему может быть переопределен парент и добавлены интерфейсы
 * <br> Неаккуратное использование может убить класс.
 * @author swayfarer
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface InheritsIf {

	public String parent() default "";
	public String[] interfaces() default {};
	public String[] conditions() default {};
	
}
