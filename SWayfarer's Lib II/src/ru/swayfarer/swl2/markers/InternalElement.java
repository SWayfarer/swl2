package ru.swayfarer.swl2.markers;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Внутренний элемент.
 * <br> Лучше не трогать это напрямую, если не понимаешь, что делаешь.
 * <br> В этой библиотеке вообще не используется private и protected. Это делается для наибольшей гибкости конечной jar'ки, но требует от пользователя болшего внимания.
 * <br> Этой аннотацией отмечается то, что лучше понимать, прежде, чем трогать
 * <h1> Маркер </h1>
 * @author swayfarer
 *
 */
@Retention(RUNTIME)
@Target({ TYPE, FIELD, METHOD, CONSTRUCTOR })
public @interface InternalElement
{

}
