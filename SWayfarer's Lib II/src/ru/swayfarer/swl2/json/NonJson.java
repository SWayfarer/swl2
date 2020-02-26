package ru.swayfarer.swl2.json;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

/**
 * Игнорировать при (де)сериализации при помощи {@link JsonUtils} 
 * @author swayfarer
 *
 */
@Retention(RUNTIME)
public @interface NonJson
{

}
