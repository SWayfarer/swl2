package ru.swayfarer.swl2.markers;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Эти объекты будут преобразованы в строку через соединие. 
 * <br> В некоторых случаях, например, при логгинге, будут использованы пробелы между объектами
 * @author swayfarer
 *
 */
@Retention(RUNTIME)
@Target({ FIELD, LOCAL_VARIABLE, ElementType.PARAMETER})
public @interface ConcattedString
{

}
