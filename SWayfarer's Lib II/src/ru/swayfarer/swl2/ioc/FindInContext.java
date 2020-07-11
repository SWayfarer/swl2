package ru.swayfarer.swl2.ioc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FindInContext {
	public String context();
}
