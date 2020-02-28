package ru.swayfarer.swl2.collections.extended;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

import ru.swayfarer.swl2.classes.ReflectionUtils;
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
	
	/** Создать лист из массива */
	@SuppressWarnings("rawtypes")
	public static <T> ExtendedListWrapper<T> valueOf(Object obj)
	{
		if (obj == null)
			return null;
		
		ExtendedListWrapper list = null;

		if (obj.getClass() == char[].class)
			list = valueOf((char[]) obj);
		else if (obj.getClass() == boolean[].class)
			list = valueOf((boolean[]) obj);
		else if (obj.getClass() == byte[].class)
			list = valueOf((byte[]) obj);
		else if (obj.getClass() == short[].class)
			list = valueOf((short[]) obj);
		else if (obj.getClass() == int[].class)
			list = valueOf((int[]) obj);
		else if (obj.getClass() == long[].class)
			list = valueOf((long) obj);
		else if (obj.getClass() == float[].class)
			list = valueOf((float) obj);
		else if (obj.getClass() == double[].class)
			list = valueOf((double) obj);
		
		else if (ReflectionUtils.isArray(obj))
			list = new ExtendedListWrapper<>(new ArrayList<>(Arrays.asList(((Object[]) obj))));
		else
		{
			list = new ExtendedListWrapper<>();
			list.add(obj);
		}
		
		return list;
	}
	
	/** Создать лист из массива */
	public static ExtendedListWrapper<Boolean> valueOf(boolean[] arr)
	{
		ExtendedListWrapper<Boolean> list = new ExtendedListWrapper<>();
		
		for (boolean i : arr)
			list.add(i);
		
		return list;
	}

	/** Создать лист из массива */
	public static ExtendedListWrapper<Character> valueOf(char[] arr)
	{
		ExtendedListWrapper<Character> list = new ExtendedListWrapper<>();
		
		for (char i : arr)
			list.add(i);
		
		return list;
	}

	/** Создать лист из массива */
	public static ExtendedListWrapper<Integer> valueOf(byte[] arr)
	{
		ExtendedListWrapper<Integer> list = new ExtendedListWrapper<>();
		
		for (int i : arr)
			list.add(i);
		
		return list;
	}
	
	/** Создать лист из массива */
	public static ExtendedListWrapper<Short> valueOf(short[] arr)
	{
		ExtendedListWrapper<Short> list = new ExtendedListWrapper<>();
		
		for (short i : arr)
			list.add(i);
		
		return list;
	}
	
	/** Создать лист из массива */
	public static ExtendedListWrapper<Integer> valueOf(int[] arr)
	{
		ExtendedListWrapper<Integer> list = new ExtendedListWrapper<>();
		
		for (int i : arr)
			list.add(i);
		
		return list;
	}
	
	/** Создать лист из массива */
	public static ExtendedListWrapper<Long> valueOf(long[] arr)
	{
		ExtendedListWrapper<Long> list = new ExtendedListWrapper<>();
		
		for (long i : arr)
			list.add(i);
		
		return list;
	}
	
	/** Создать лист из массива */
	public static ExtendedListWrapper<Float> valueOf(float[] arr)
	{
		ExtendedListWrapper<Float> list = new ExtendedListWrapper<>();
		
		for (float i : arr)
			list.add(i);
		
		return list;
	}
	
	/** Создать лист из массива */
	public static ExtendedListWrapper<Double> valueOf(double[] arr)
	{
		ExtendedListWrapper<Double> list = new ExtendedListWrapper<>();
		
		for (double i : arr)
			list.add(i);
		
		return list;
	}

}
