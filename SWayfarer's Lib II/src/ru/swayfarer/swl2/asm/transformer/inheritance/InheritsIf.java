package ru.swayfarer.swl2.asm.transformer.inheritance;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface InheritsIf {

	public String parent() default "";
	public String[] interfaces() default {};
	public String[] conditions() default {};
	
}
