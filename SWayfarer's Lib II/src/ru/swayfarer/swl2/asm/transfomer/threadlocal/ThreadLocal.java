package ru.swayfarer.swl2.asm.transfomer.threadlocal;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Отмеченный этой аннотацией метод всегада кэширует результат своей работы для каждого потока, а затем возвращаем его 
 * @author swayfarer
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ThreadLocal { }
