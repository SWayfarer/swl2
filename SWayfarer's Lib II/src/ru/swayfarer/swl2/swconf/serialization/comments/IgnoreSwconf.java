package ru.swayfarer.swl2.swconf.serialization.comments;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import ru.swayfarer.swl2.swconf.serialization.SwconfSerialization;

/**
 * Отмеченное этой аннотацией поле не будет замечаться {@link SwconfSerialization}'ом
 * @author swayfarer
 *
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface IgnoreSwconf
{

}
