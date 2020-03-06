package ru.swayfarer.swl2.collections.observable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import ru.swayfarer.swl2.collections.extended.ExtendedListWrapper;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.SimpleObservable;
import ru.swayfarer.swl2.reference.IReference;
import ru.swayfarer.swl2.reference.SimpleReference;

/**
 * Обертка на лист, позволяющая использовать функционал {@link IObservableList}
 * <h1> Медленнее, чем простой лист из-за обработки событий </h1>
 * @author swayfarer
 */
@SuppressWarnings("unchecked")
public class ObservableListWrapper<Element_Type> extends ExtendedListWrapper<Element_Type> implements IObservableList<Element_Type> {

	/** 
	 * Точка для подписки на pre-версию событий. 
	 * <br> На этом этапе {@link ListEvent#setCanceled(boolean)} может повлиять на отмену операции
	 */
	@InternalElement
	public IObservable<ListEvent<Element_Type>> eventUpdate = new SimpleObservable<>();
	
	/** 
	 * Точка для подписки на post-версию событий. 
	 * <br> На этом этапе {@link ListEvent#setCanceled(boolean)} НЕ может повлиять на отмену операции
	 */
	@InternalElement
	public IObservable<ListEvent<Element_Type>> eventPostUpdate = new SimpleObservable<>();
	
	
	public ObservableListWrapper()
	{
		super();
	}

	public ObservableListWrapper(List<Element_Type> wrappedList)
	{
		super(wrappedList);
	}

	public boolean hasNoListeners()
	{
		return eventPostUpdate.isEmpty() && eventUpdate.isEmpty();
	}
	
	@Override
	public boolean add(Element_Type e)
	{
		if (hasNoListeners())
			return super.add(e);
		
		ListEvent<Element_Type> event = ListEvent.of(size(), e, this, ListEvent.EventType.Add);
		eventUpdate.next(event);
		
		if (event.isCanceled())
			return false;
		
		boolean ret = super.add(e);
		
		event = ListEvent.of(size(), e, this, ListEvent.EventType.Add);
		eventPostUpdate.next(event);
		
		return ret;
	}
	
	@Override
	public void add(int index, Element_Type element)
	{
		if (hasNoListeners())
			super.add(index, element);
		
		ListEvent<Element_Type> event = ListEvent.of(index, element, this, ListEvent.EventType.Add);
		eventUpdate.next(event);
		
		if (event.isCanceled())
			return;
		
		super.add(index, element);
		
		event = ListEvent.of(index, element, this, ListEvent.EventType.Add);
		eventPostUpdate.next(event);
	}
	
	@Override
	public boolean addAll(Collection<? extends Element_Type> c)
	{
		boolean isSomeModified = false;
		
		for (Element_Type element : c)
		{
			if (add(element))
				isSomeModified  = true;
		}
		
		return isSomeModified;
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends Element_Type> c)
	{
		boolean isSomeModified = false;
		
		for (Element_Type element : c)
		{
			ListEvent<Element_Type> event = ListEvent.of(index, element, this, ListEvent.EventType.Add);
			eventUpdate.next(event);
			
			if (event.isCanceled())
			{
				isSomeModified = true;
				
				super.add(index++, element);
				
				event = ListEvent.of(index, element, this, ListEvent.EventType.Add);
				eventPostUpdate.next(event);
			}
		}
		
		return isSomeModified;
	}
	
	@Override
	public Element_Type set(int index, Element_Type element)
	{
		Element_Type oldElement = get(index);
		
		ListEvent<Element_Type> event = ListEvent.of(index, element, this, ListEvent.EventType.Set);
		eventUpdate.next(event);
		
		if (!event.isCanceled())
		{
			super.set(index, element);
			
			event = ListEvent.of(index, element, this, ListEvent.EventType.Set);
			eventPostUpdate.next(event);
		}
		
		return oldElement;
	}
	
	@Override
	public void clear()
	{
		ListEvent<Element_Type> event = ListEvent.of(-1, null, this, ListEvent.EventType.Clear);
		eventUpdate.next(event);
		
		if (!event.isCanceled())
		{
			super.clear();
			
			event = ListEvent.of(-1, null, this, ListEvent.EventType.Clear);
			eventPostUpdate.next(event);
		}
	}
	
	@Override
	public Element_Type remove(int index)
	{
		Element_Type element = get(index);
		ListEvent<Element_Type> event = ListEvent.of(index, element, this, ListEvent.EventType.Remove);
		eventUpdate.next(event);
		
		if (!event.isCanceled())
		{
			super.remove(index);
			
			event = ListEvent.of(index, element, this, ListEvent.EventType.Remove);
			eventPostUpdate.next(event);
		}
		
		return element;
	}
	
	@Override
	public boolean remove(Object o)
	{
		Element_Type element = (Element_Type) o;
		int index = indexOf(o);
		
		ListEvent<Element_Type> event = ListEvent.of(index, element, this, ListEvent.EventType.Remove);
		eventUpdate.next(event);
		
		if (!event.isCanceled())
		{
			boolean isModified = super.remove(o);
			
			event = ListEvent.of(index, element, this, ListEvent.EventType.Remove);
			eventPostUpdate.next(event);
			
			return isModified;
		}
		
		return false;
	}
	
	@Override
	public boolean removeAll(Collection<?> c)
	{
		boolean isSomeModified = false;
		
		for (Object obj : c)
		{
			if (remove(obj))
				isSomeModified = true;
		}
		
		return isSomeModified;
	}
	
	@Override
	public boolean removeIf(Predicate<? super Element_Type> filter)
	{
		SimpleReference isModified = new SimpleReference(false);
		
		stream().forEach((e) -> {
			if (filter.test(e))
			{
				remove(e);
				isModified.setValue(true);
			}
		});
		
		return isModified.getValue();
	}
	
	@Override
	public void replaceAll(UnaryOperator<Element_Type> operator)
	{
		int size = size();
		
		for (int i1 = 0; i1 < size; i1 ++)
		{
			set(i1, operator.apply(get(i1)));
		}
	}
	
	@Override
	public boolean retainAll(Collection<?> c)
	{
		boolean isSomeModified = false;
		
		Iterator<Element_Type> $i = super.iterator();
		
		while ($i.hasNext())
		{
			Element_Type element = $i.next();
			
			if (!c.contains(element))
				$i.remove();
		}
		
		return isSomeModified;
	}
	
	@Override
	public IObservable<ListEvent<Element_Type>> evtUpdate()
	{
		return eventUpdate;
	}

	@Override
	public IObservable<ListEvent<Element_Type>> evtPostUpdate()
	{
		return eventPostUpdate;
	}

}
