package ru.swayfarer.swl2.asm.transformer.patcher;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

/**
 * Аннотация используется для снятия снимков патчей <br>
 * 1) Отмечаем этой аннотацией все необходимые элементы (Методы, классы, филды) <br> 
 * 2) Собирам jar'ник и сканируем его через {@link PatcherInJarScanner} <br> 
 * 3) Профит, получаем патчи, которые можно применять через {@link PatcherClassTransformer}
 * <br> <br>
 * Т.е., проще говоря, можно понаписать чего-нибудь в целевом классе, и через патчи атоматически вставлять это в те, к которым прямого доступа нет.
 * @author swayfarer
 *
 */
@Retention(RUNTIME)
public @interface PatchAsm
{
	public String[] conditions() default {};
}
