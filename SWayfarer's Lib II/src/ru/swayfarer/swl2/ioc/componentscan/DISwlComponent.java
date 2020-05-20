package ru.swayfarer.swl2.ioc.componentscan;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import ru.swayfarer.swl2.ioc.context.elements.ContextElementType;

/**
 * Компонент DI <br> 
 * Компоненты DI находятся DI-сканнерами, создаются и добавляются в контекст автоматически. <br>
 * Самосоздающийся бин, можно скзазать =)
 * @author swayfarer
 *
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface DISwlComponent
{
	/** Имя, под которым элемент будет создан */
	public String name();
	
	/** Контекст, в котором будет создан элемент */
	public String context() default "";
	
	/** Тип создаваемого элемента контекста */
	public ContextElementType type() default ContextElementType.Singleton;
	
	/** Класс, с которым элемент будет ассоциирован */
	public Class<?> associated() default Object.class;
}
