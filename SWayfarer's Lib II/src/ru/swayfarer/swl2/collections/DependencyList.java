package ru.swayfarer.swl2.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicInteger;

import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.collections.wrapper.ListWrapper;
import ru.swayfarer.swl2.markers.InternalElement;

/**
 * Лист с зависимостями
 * <br> Лист с зависимостями считает, что элементы зависимостей - его элементы, но редактировать их не может. 
 * Никаих {@link Exception}'ов, просто проигнорит. 
 * @author swayfarer
 *
 * @param <Element_Type> Тип элемента листа 
 */
@SuppressWarnings("unchecked")
public class DependencyList<Element_Type> extends ListWrapper<Element_Type> {

	/** Зависимости */
	@InternalElement
	public IExtendedList<List<? extends Element_Type>> dependencies = CollectionsSWL.createWeakList();

	public <T extends DependencyList<Element_Type>> T addDependency(List<? extends Element_Type> e) 
	{
		this.dependencies.addExclusive(e);
		return (T) this;
	}
	
	@Override
	public int size()
	{
		AtomicInteger i = new AtomicInteger(0);
		
		dependencies.dataStream().each((e) -> i.incrementAndGet());
		
		return i.get();
	}
	
	@Override
	public boolean isEmpty()
	{
		return dependencies.dataStream().contains(List::isEmpty) || super.isEmpty();
	}

	@Override
	public boolean contains(Object o)
	{
		return dependencies.dataStream().contains((e) -> e.contains(o)) || super.contains(o);
	}

	@Override
	public Iterator<Element_Type> iterator()
	{
		return getListWithDependenciesElements().iterator();
	}

	@Override
	public Object[] toArray()
	{
		return getListWithDependenciesElements().toArray();
	}

	@Override
	public <T> T[] toArray(T[] a)
	{
		return getListWithDependenciesElements().toArray(a);
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		return getListWithDependenciesElements().containsAll(c);
	}
	
	public IExtendedList<Element_Type> getListWithDependenciesElements()
	{
		IExtendedList<Element_Type> list = CollectionsSWL.createExtendedList(wrappedList);
		dependencies.dataStream().each((e) -> list.addAll(e));
		return list;
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		return getListWithDependenciesElements().retainAll(c);
	}

	@Override
	public Element_Type get(int index)
	{
		int offset = 0;
		
		if (isWrappedListIndex(index))
			return super.get(index);
		
		for (List<? extends Element_Type> list : dependencies)
		{
			offset += list.size();
			
			if (offset > index)
				return list.get(offset - index);
		}
		
		return super.get(index);
	}
	
	/** Является ли индекс частью обернутого листа? */
	public boolean isWrappedListIndex(int index)
	{
		return index < wrappedList.size();
	}

	@Override
	public Element_Type set(int index, Element_Type element)
	{
		if (isWrappedListIndex(index))
			return super.set(index, element);

		int offset = 0;
		
		for (List<? extends Element_Type> list : dependencies)
		{
			offset += list.size();
			
			if (offset > index)
				return list.get(offset - index);
		}
		
		return null;
	}

	@Override
	public void add(int index, Element_Type element)
	{
		if (isWrappedListIndex(index))
			super.add(index, element);
	}

	@Override
	public Element_Type remove(int index)
	{
		if (isWrappedListIndex(index))
			return super.remove(index);
		
		return null;
	}

	@Override
	public int indexOf(Object o)
	{
		int i1 = 0;
		int ret = wrappedList.indexOf(o);
		
		if (ret < 0)
			for (List<? extends Element_Type> list : dependencies)
			{
				ret = list.indexOf(o);
				
				if (ret < 0)
					i1 += list.size();
				else
					return ret + i1;
			}
		
		return -1;
	}

	@Override
	public int lastIndexOf(Object o)
	{
		int i1 = 0;
		int ret = wrappedList.lastIndexOf(o);
		
		if (ret < 0)
			for (List<? extends Element_Type> list : dependencies)
			{
				ret = list.lastIndexOf(o);
				
				if (ret < 0)
					i1 += list.size();
				else
					return ret + i1;
			}
		
		return -1;
	}

	@Override
	public ListIterator<Element_Type> listIterator()
	{
		IExtendedList<Element_Type> list = getListWithDependenciesElements();
		return list.listIterator();
	}

	@Override
	public ListIterator<Element_Type> listIterator(int index)
	{
		IExtendedList<Element_Type> list = getListWithDependenciesElements();
		return list.listIterator();
	}

	@Override
	public List<Element_Type> subList(int fromIndex, int toIndex)
	{
		IExtendedList<Element_Type> list = getListWithDependenciesElements();
		return list.subList(fromIndex, toIndex);
	}
	
}
