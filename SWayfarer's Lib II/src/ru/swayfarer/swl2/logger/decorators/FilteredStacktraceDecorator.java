package ru.swayfarer.swl2.logger.decorators;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;

@SuppressWarnings("unchecked")
public class FilteredStacktraceDecorator implements IFunction1<Throwable, IExtendedList<StackTraceElement>> {

	public IExtendedList<IFunction1<StackTraceElement, Boolean>> filters = CollectionsSWL.createExtendedList();
	
	@Override
	public IExtendedList<StackTraceElement> apply(Throwable e)
	{
		IExtendedList<StackTraceElement> ret = CollectionsSWL.createExtendedList();
		
		for (IFunction1<StackTraceElement, Boolean> filter : filters)
		{
			ret.filter(filter);
		}
		
		return ret;
	}
	
	public <T extends FilteredStacktraceDecorator> T addFilter(IFunction1<StackTraceElement, Boolean> filter) 
	{
		this.filters.add(filter);
		return (T) this;
	}

	public <T extends FilteredStacktraceDecorator> T clear() 
	{
		this.filters.clear();
		return (T) this;
	}
}
