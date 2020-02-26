package ru.swayfarer.swl2.asm.transfomer.injection;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Отметить параметер как ссылку, по которой будет доступна работа с результатом работы метода-цели иньекции
 * <br> См {@link InjectionAsm} 
 * @author swayfarer
 *
 */
@Retention(RUNTIME)
@Target(PARAMETER)
public @interface ReturnRef {}
