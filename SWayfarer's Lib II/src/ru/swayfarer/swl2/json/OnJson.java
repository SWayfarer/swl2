package ru.swayfarer.swl2.json;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

@Retention(RUNTIME)
public @interface OnJson
{
	public JsonStage stage() default JsonStage.Load_Completed;
}
