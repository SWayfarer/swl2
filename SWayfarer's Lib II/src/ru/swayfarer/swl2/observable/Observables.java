package ru.swayfarer.swl2.observable;

import ru.swayfarer.swl2.observable.property.ObservableProperty;

public class Observables {

	public static <T> ObservableProperty<T> createProperty()
	{
		return createProperty(null);
	}
	
	public static <T> ObservableProperty<T> createProperty(T obj)
	{
		return new ObservableProperty<T>(obj);
	}
	
}
