package ru.swayfarer.swl2.collections.extended;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

import ru.swayfarer.swl2.collections.wrapper.ListWrapper;
import ru.swayfarer.swl2.markers.InternalElement;

/**
 * Обертка на лист, позволяющая добавить ему функционал {@link IExtendedList}
 * @author swayfarer
 */
@SuppressWarnings("unchecked")
public class ExtendedListWrapper<Element_Type> extends ListWrapper<Element_Type> implements IExtendedList<Element_Type> {

	/** Является ли лист конкуррентным? */
	@InternalElement
	public boolean isConcurrent = false;
	
	/** Конструктор */
	public ExtendedListWrapper()
	{
		super();
	}

	/** 
	 * Конструктор
	 * @param wrappedList Обернутый лист
	 */
	public ExtendedListWrapper(List<Element_Type> wrappedList)
	{
		super(wrappedList);
	}

	/**
	 * Получить {@link Iterator}, чтобы не бросался {@link ConcurrentModificationException}
	 */
	@InternalElement @Override
	public Iterator<Element_Type> getConcurrentIterator()
	{
		return new ArrayList<Element_Type>(this).iterator();
	}

	/** Бросает ли {@link ConcurrentModificationException}? */
	@Override
	public boolean isConcurrent()
	{
		return isConcurrent;
	}

	/** Будет ли бросать {@link ConcurrentModificationException}? */
	@Override
	public <T extends IExtendedList<Element_Type>> T setConcurrent(boolean isConcurrent)
	{
		this.isConcurrent = isConcurrent;
		return (T) this;
	}
	
	/** Итератор */
	@Override
	public Iterator<Element_Type> iterator()
	{
		return isConcurrent() ? getConcurrentIterator() : super.iterator();
	}

	/** Добавить все элементы */
	@Override
	public <T extends IExtendedList<Element_Type>> T addAll(Element_Type... elements)
	{
		for (Element_Type element : elements)
			add(element);
		
		return (T) this;
	}

	/** Cодержит ли все элементы? */
	@Override
	public boolean containsAll(Element_Type... elements)
	{
		for (Element_Type element : elements)
			if (!contains(element))
				return false;
		
		return true;
	}

	/**
	 * Добавить элемент, если такой не найден в листе
	 */
	@Override
	public <T extends IExtendedList<Element_Type>> T addExclusive(Element_Type type)
	{
		if (!contains(type))
			add(type);
		
		return (T) this;
	}

}
