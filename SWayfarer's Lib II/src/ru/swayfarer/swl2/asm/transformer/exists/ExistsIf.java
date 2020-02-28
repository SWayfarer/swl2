package ru.swayfarer.swl2.asm.transformer.exists;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

/**
 * Отмеченный этой аннотацией элемент будет существовать только в том случае, если выполняются все указанные в ней условия
 * <br> <br> Условия(содержание) настраиваются в {@link ExistsConditionContainer}'е, который задается при добавлении трансформера
 * @author swayfarer
 */
@Retention(RUNTIME)
public @interface ExistsIf
{
	public String[] value() default {};
}
