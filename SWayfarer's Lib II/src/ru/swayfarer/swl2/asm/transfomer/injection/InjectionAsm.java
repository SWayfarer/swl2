package ru.swayfarer.swl2.asm.transfomer.injection;

import ru.swayfarer.swl2.reference.SimpleReference;

/**
 * Создать иньекцию.
 * <br> Аннотация используется внутри классов-контейнеров с иньекциями
 * <br> Вызовы методов-инекций будут вписаны в нужное место целевых методов
 * <h1> Иньекции позволяют работать с возвращаемым значениеми. </h1>
 * Для этого укажите последним параметром {@link SimpleReference} с названием returnRef, либо отмеченный аннотацией {@link ReturnRef}
 * <br> Значение ссылки будет возвращаемым значением метода-цели, если {@link #injectOnExit()} выставлен в true. Иначе будет null.
 * <br> Для void-методов значение ссылки после работы иньекции игнорируются, вместо этого проверяется сам факт задавания любого значения. 
 * Проще говоря, чтобы заставить void сделать return, необходимо вызвать {@link SimpleReference#setValue(Object)} и передать что угодно
 * 
 * <br> <h1> Первым параметром иньекции обязательно должен идти объект, в который будет передан this из метода-цели. Если цель статическая, то передастся null
 * @author swayfarer
 *
 */
public @interface InjectionAsm {

	/** 
	 * Имя целевого метода 
	 * <br> Если не указать, то будет использовано имя метода иньекции.
	 * <br> Для конструкторов используется имя < init >, для статических блоков классов - < clinit >
	 */
	public String targetName() default "";
	
	/** 
	 * Дескриптор целевого метода 
	 * Если не указать, то будет собран из параметров метода-иньекции, исключая первый и, при наличии, returnRef
	 */
	public String targetDesc() default "";
	
	/**
	 * Каноничное имя целевого класса
	 * <br> Если не указывать, то будет взято из типа первого параметра метода-иньекции
	 */
	public String targetClass() default "";
	
	/** Вставлять ли в конец метода? По-умолчанию - false*/
	public boolean injectOnExit() default false;
	
}
