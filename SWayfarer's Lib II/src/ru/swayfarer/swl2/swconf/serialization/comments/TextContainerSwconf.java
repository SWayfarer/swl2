package ru.swayfarer.swl2.swconf.serialization.comments;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Указывает на кодировку, в которой будет храниться конфиг 
 * @author swayfarer
 *
 */
@Retention(RUNTIME)
@Target(ElementType.TYPE)
public @interface TextContainerSwconf
{
	public String encoding() default "UTF-8";
	public String lineSplitter() default "";
}
