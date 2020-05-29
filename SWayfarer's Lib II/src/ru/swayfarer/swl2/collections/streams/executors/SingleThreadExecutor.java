package ru.swayfarer.swl2.collections.streams.executors;

import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;

/**
 * Экзекьютор, выполняющий таски в однопоточном режиме
 * @author swayfarer
 *
 * @param <Element_Type> Тип элементов
 */
public class SingleThreadExecutor<Element_Type> implements IFunction2NoR<IFunction2NoR<Integer, ? super Element_Type>, IExtendedList<Element_Type>> {

	@Override
	public void applyNoR(IFunction2NoR<Integer, ? super Element_Type> fun, IExtendedList<Element_Type> elements)
	{
		int next = 0;
		
		for (Element_Type elem: elements)
		{
			fun.apply(next ++, elem);
		}
	}

}
