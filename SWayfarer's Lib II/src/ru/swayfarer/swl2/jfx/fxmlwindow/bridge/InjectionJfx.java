package ru.swayfarer.swl2.jfx.fxmlwindow.bridge;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * В поле, отмеченное этой аннотацией, будет присоединен соостветствующий элемент fxml-файла,
 * имя которого будет взято либо из {@link #name()}, если задано, или по имени поля.
 * @author swayfarer
 *
 */
@Retention(CLASS)
@Target(FIELD)
public @interface InjectionJfx
{
	/** Имя элемента fxml */
	public String name() default "";
}
