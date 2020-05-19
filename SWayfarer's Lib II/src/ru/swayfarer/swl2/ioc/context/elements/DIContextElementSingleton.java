package ru.swayfarer.swl2.ioc.context.elements;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Синглтон-элемент контекста. Всегда возвращает одно и то же значение
 * 
 * @author swayfarer
 *
 */
public class DIContextElementSingleton extends DIContextElementFromFun {

	public Object instance;
	public AtomicBoolean isInstanceCreated = new AtomicBoolean(false);
	
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