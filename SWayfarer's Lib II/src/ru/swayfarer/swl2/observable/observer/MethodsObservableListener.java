package ru.swayfarer.swl2.observable.observer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.observable.subscription.ISubscription;

public class MethodsObservableListener<Event_Type> implements IFunction2NoR<ISubscription<Event_Type>, Event_Type>{

	public Object instance;
	public List<Method> methods = new ArrayList<>();
	
	public MethodsObservableListener(Object obj)
	{
		ExceptionsUtils.IfNull(obj, IllegalArgumentException.class, "Handler object can't be null!");
		
		Method[] methods = obj.getClass().getMethods();
		
		for (Method method : methods)
		{
			if (method.getAnnotation(ObsListener.class) != null)
			{
				if (method.getParameterCount() == 3)
				{
					
				}
			}
		}
	}
	
	@Override
	public void applyNoR(ISubscription<Event_Type> arg1, Event_Type arg2)
	{
		
	}

}
