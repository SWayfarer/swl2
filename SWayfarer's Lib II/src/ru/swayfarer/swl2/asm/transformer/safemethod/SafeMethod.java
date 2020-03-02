package ru.swayfarer.swl2.asm.transformer.safemethod;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Отмеченный этой аннотацией метод оборачивается в блок try-catch 
 * @author swayfarer
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface SafeMethod
{
	/** Логгер, через который будет выполен вывод */
	public String logger() default "";
	
	/** Сообщение, с которым будет выполнен вывод */
	public String message() default "Error while running method %method%";
	
	/** Класс, {@link Throwable}'ы которого которого будет кетчиться. 
	 * <h1> Предупреждение: </h1>
	 * Если укзать класс, которого нет на этапе кетча, то будет битый класс. Аккуратнее с этим 
	 */
	public Class<? extends Throwable> typeOfThrowable() default Throwable.class;
}
