package ru.swayfarer.swl2.recursive;

import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction0NoR;
import ru.swayfarer.swl2.markers.InternalElement;

/**
 * Контейнер, защищающий от рекурсивного вызова. 
 * <br> При работе через него задача не сможет вызвать сама себя
 * @author swayfarer
 */
public class RecursiveBreaker {

	/** В процессе ли задача */
	@InternalElement
	public boolean isInProcess = false;
	
	/** Запустить задачу */
	public void run(IFunction0NoR fun)
	{
		ExceptionsUtils.IfNullArg(fun, "Fun can't be null!");
		
		if (isInProcess)
			return;
		
		isInProcess = true;
		
		fun.apply();
		
		isInProcess = false;
	}
	
}
