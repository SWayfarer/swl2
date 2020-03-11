package ru.swayfarer.swl2.collections.wrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Обертка на лист
 * <br> Позволяет добавлять новый функционал в листы, не изменяя их основных классов 
 * @author swayfarer
 */
public class ListWrapper<Element_Type> implements List<Element_Type>{

	/** Обернутый лист */
	public List<Element_Type> wrappedList;
	
	/** Конструктор */
	public ListWrapper()
	{
		this(null);
	}
	
	/** Конструктор */
	public ListWrapper(List<Element_Type> wrappedList)
	{
		if (wrappedList == null)
			wrappedList = new ArrayList<Element_Type>();
			
		this.wrappedList = wrappedList;
	}
	
	/** Размер листа */
	@Override
	public int size()
	{
		return wrappedList.size();
	}

	/** Пустой ли лист? */
	@Override
	public boolean isEmpty()
	{
		return wrappedList.isEmpty();
	}

	/** Содержит ли лист объект? */
	@Override
	public boolean contains(Object o)
	{
		return wrappedList.contains(o);
	}

	/** Получить массив элементов листа */
	@Override
	public Object[] toArray()
	{
		return wrappedList.toArray();
	}

	/** Превратить в массив */
	@Override
	public <T> T[] toArray(T[] a)
	{
		return wrappedList.toArray(a);
	}

	/** Добавить элемент */
	@Override
	public boolean add(Element_Type e)
	{
		return wrappedList.add(e);
	}

	/** Удалить объект */
	@Override
	public boolean remove(Object o)
	{
		return wrappedList.remove(o);
	}

	/** Содержит ли лист все элементы? */
	@Override
	public boolean containsAll(Collection<?> c)
	{
		return wrappedList.containsAll(c);
	}

	/** Добавить все элементы */
	@Override
	public boolean addAll(Collection<? extends Element_Type> c)
	{
		return wrappedList.addAll(c);
	}

	/** Добавить все элементы, вставив их после указанного индекса*/
	@Override
	public boolean addAll(int index, Collection<? extends Element_Type> c)
	{
		return wrappedList.addAll(index, c);
	}

	/** Удалить все элементы из листа */
	@Override
	public boolean removeAll(Collection<?> c)
	{
		return wrappedList.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		return wrappedList.retainAll(c);
	}

	/** Очистить лист */
	@Override
	public void clear()
	{
		wrappedList.clear();
	}

	/** Получить элемент листа */
	@Override
	public Element_Type get(int index)
	{
		return wrappedList.get(index);
	}

	/** Задать элемент листа */
	@Override
	public Element_Type set(int index, Element_Type element)
	{
		return wrappedList.set(index, element);
	}

	/** Добавить элемент по указанную позицию */
	@Override
	public void add(int index, Element_Type element)
	{
		wrappedList.add(index, element);
	}

	/** Удалить элемент под указанным номером */
	@Override
	public Element_Type remove(int index)
	{
		return wrappedList.remove(index);
	}

	/** Получить первый индекс эквивалентного элемента */
	@Override
	public int indexOf(Object o)
	{
		return wrappedList.indexOf(o);
	}

	/** Получить последний индекс эквивалентного элемента */
	@Override
	public int lastIndexOf(Object o)
	{
		return wrappedList.lastIndexOf(o);
	}

	@Override
	public ListIterator<Element_Type> listIterator()
	{
		return wrappedList.listIterator();
	}

	@Override
	public ListIterator<Element_Type> listIterator(int index)
	{
		return wrappedList.listIterator(index);
	}

	@Override
	public List<Element_Type> subList(int fromIndex, int toIndex)
	{
		return wrappedList.subList(fromIndex, toIndex);
	}

	@Override
	public Iterator<Element_Type> iterator()
	{
		return wrappedList.iterator();
	}
	
	@Override
	public String toString()
	{
		return getClass().getSimpleName() + ": " + wrappedList;
	}
}
