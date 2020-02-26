package ru.swayfarer.swl2.asm.transformer.ditransformer.regetter.visitor;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Динамическое обновление элемента контекста. 
 * <br> Если отмечено поле, и элемент контекста не-Singleton, то при каждом обращении к нему будет возвращаться новый объект
 * @author swayfarer
 *
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface DynamicDI
{

}
