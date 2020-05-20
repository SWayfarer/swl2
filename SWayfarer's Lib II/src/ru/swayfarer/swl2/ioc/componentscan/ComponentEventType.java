package ru.swayfarer.swl2.ioc.componentscan;

/**
 * Событие компонента контекста
 * @author swayfarer
 *
 */
public enum ComponentEventType
{
	/** Пре-инициализация, до иньекций из DI */
	PreInit,
	
	/** Инициализация, после иньекций из DI */
	Init,
	
//	PostInit
}
