package ru.swayfarer.swl2.swconf.serialization.comments;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <h1> Если отмечен класс: </h1>
 * Все сериализуемые через рефлексию объекты будут получать коментарий из аннотации, если ею не
 * <h1> Отмечено поле: </h1>
 * Сериализованное значение поля получает коментарий из аннотации 
 * @author User
 *
 */
@Retention(RUNTIME)
@Target({ TYPE, FIELD })
public @interface CommentSwconf
{
	/** Комментарий, получаемый элементом */
	public String value();
}
