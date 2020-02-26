package ru.swayfarer.swl2.asm.transfomer.injection;

public @interface InjectionAsm {

	public String targetName() default "";
	public String targetDesc() default "";
	public String targetClass() default "";
	public boolean injectOnExit() default false;
	
}
