package ru.swayfarer.swl2.ioc.context.elements;

import java.util.concurrent.atomic.AtomicBoolean;

import ru.swayfarer.swl2.markers.InternalElement;

/**
 * Синглтон-элемент контекста. Всегда возвращает одно и то же значение
 * 
 * @author swayfarer
 *
 */
public class DIContextElementSingleton extends DIContextElementFromFun {

	/** Созданное значение элемента */
	@InternalElement
	public Object instance;
	
	/** Было ли уже создано значение элемента? */
	@InternalElement
	public AtomicBoolean isInstanceCreated = new AtomicBoolean(false);
	
	@Override
	public Object getValue()
	{
		if (!isInstanceCreated.get())
		{
			isInstanceCreated.set(true);
			instance = objectCreationFun.apply();
		}
		
		return instance;
	}
	
	@Override
	public String toString()
	{
		return "singleton: " + objectCreationFun;
	}
}