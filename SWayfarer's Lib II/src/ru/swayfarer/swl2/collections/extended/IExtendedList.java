package ru.swayfarer.swl2.collections.extended;

import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.streams.DataStream;
import ru.swayfarer.swl2.collections.streams.IDataStream;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1NoR;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2;
import ru.swayfarer.swl2.logger.ILogLevel.StandartLoggingLevels;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LogInfo;
import ru.swayfarer.swl2.markers.Alias;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Расширенный лист
 * @author swayfarer
 */
@SuppressWarnings("unchecked")
public interface IExtendedList<Element_Type> extends List<Element_Type>{

	/** Получить итератор, при параллельном использовании которого с основным не полетит {@link ConcurrentModificationException} */
	public Iterator<Element_Type> getConcurrentIterator();
	
	/** Бросает ли {@link ConcurrentModificationException} при параллельном использовании {@link Iterator}'а? */
	public boolean isConcurrent();
	
	/** Будет ли бросаться */
	public <T extends IExtendedList<Element_Type>> T setConcurrent(boolean isConcurrent);
	
	/** Задать все элементы */
	public default <E extends Element_Type, T extends IExtendedList<Element_Type>> T setAll(E... content)
	{
		clear();
		
		for (E e : content)
		{
			add(e);
		}
		
		return (T) this;
	}
	
	/** Задать все элементы */
	public default <E extends Element_Type, T extends IExtendedList<Element_Type>> T setAll(Iterable<E> $i)
	{
		clear();
		
		for (E e : $i)
		{
			add(e);
		}
		
		return (T) this;
	}
	
	/**
	 * Если элемент присутствует, то удалить его
	 * <br> Если отсутствует, то добавить
	 */
	public default <T extends IExtendedList<Element_Type>> T toggle(Element_Type elem)
	{
		if (contains(elem))
			remove(elem);
		else
			add(elem);
		
		return (T) this;
	}
	
	/** Аналог {@link #forEach(java.util.function.Consumer)} */
	public default void each(IFunction1NoR<Element_Type> fun)
	{
		forEach(fun.asJavaConsumer());
	}
	
	public default <T extends IExtendedList<Element_Type>> T sortBy(IFunction2<Element_Type, Element_Type, Integer> comparatorFun)
	{
		sort(comparatorFun.asJavaComparator());
		return (T) this;
	}
	
	/** Копировать лист */
	public default <T extends IExtendedList<Element_Type>> T copy()
	{
		ExtendedListWrapper<Element_Type> ret = new ExtendedListWrapper<Element_Type>();
		ret.addAll(this);
		return (T) ret;
	}
	
	/** Перемешать элементы */
	@Alias("shuffle")
	public default <T extends IExtendedList<Element_Type>> T randomize()
	{
		return shuffle();
	}
	
	/** Перемешать элементы */
	@Alias("shuffle")
	public default <T extends IExtendedList<Element_Type>> T randomize(long randomSeed)
	{
		return shuffle(randomSeed);
	}
	
	/** Перемешать элементы */
	public default <T extends IExtendedList<Element_Type>> T shuffle()
	{
		Collections.shuffle(this);
		return (T) this;
	}
	
	/** Перемешать элементы */
	public default <T extends IExtendedList<Element_Type>> T shuffle(long randomSeed)
	{
		Collections.shuffle(this, new Random(randomSeed));
		return (T) this;
	}

	/**
	 * Копировать объект 
	 * @param start Позиция, с которой копируются элементы
	 * @param lenght Позиция, до которой копируются элементы
	 */
	public default <T extends IExtendedList<Element_Type>> T copy(int start, int lenght)
	{
		ExtendedListWrapper<Element_Type> ret = new ExtendedListWrapper<Element_Type>();
		
		for (int i1 = start; i1 < lenght; i1 ++)
			ret.add(get(i1));
		
		return (T) ret;
	}
	
	/** Добавить все элементы */
	public default <T extends IExtendedList<Element_Type>> T addAll(Element_Type... elements)
	{
		for (Element_Type element : elements)
			add(element);
		
		return (T) this;
	}

	/** Добавить все элементы */
	public default boolean containsAll(Element_Type... elements)
	{
		for (Element_Type element : elements)
			if (!contains(element))
				return false;
		
		return true;
	}

	/** Добавить элемент, если эквивалетный не содежрится */
	public default <T extends IExtendedList<Element_Type>> T addExclusive(Element_Type type)
	{
		if (!contains(type))
			add(type);
		
		return (T) this;
	}
	
	/** Добавить элемент, если эквивалетный не содежрится */
	public default <T extends IExtendedList<Element_Type>> T addExclusive(int pos, Element_Type type)
	{
		if (!contains(type))
			add(pos, type);
		
		return (T) this;
	}

	/** Сдвинуть элементы влево */
	public default <T extends IExtendedList<Element_Type>> T shiftLeft()
	{
		Element_Type element = this.get(0);
		
		int max = this.size() - 1;
		
		for (int i1 = 0; i1 < max; i1 ++)
		{
			this.set(i1, this.get(i1 + 1));
		}
		
		this.set(max, element);
		
		return (T) this;
	}
	
	/** Удалить последний элемент */
	public default <T extends IExtendedList<Element_Type>> T removeLastElement()
	{
		if (!isEmpty())
			remove(size() - 1);
		return (T) this;
	}
	
	/** Удалить первый элемент */
	public default <T extends IExtendedList<Element_Type>> T removeFirstElement()
	{
		if (!isEmpty())
			remove(0);
		return (T) this;
	}
	
	/** Получить {@link DataStream} с элементами листа */
	public default IDataStream<Element_Type> dataStream()
	{
		return new DataStream<>(this);
	}
	
	/** Получить парраллельный {@link DataStream} с элементами листа */
	public default IDataStream<Element_Type> parrallelDataStream()
	{
		return new DataStream<>(this).setParrallel(true);
	}
	
	public default <T extends Element_Type> T getLastElement()
	{
		return isEmpty() ? null : (T) get(size() - 1);
	}
	
	public default <T extends Element_Type> T getFirstElement()
	{
		return isEmpty() ? null : (T) get(0);
	}

	/** Вывести лист через логгер */
	public default <T extends IExtendedList<Element_Type>> T printList(ILogger logger, Object... text)
	{
		if (logger == null)
			return (T) this;
		
		StringBuilder builder = new StringBuilder();
		
		builder.append(StringUtils.concatWithSpaces(text));
		
		builder.append("\n");
		builder.append(getClass().getSimpleName());
		builder.append(": {");
		
		for (int i1 = 0; i1 < this.size(); i1 ++)
		{
			builder.append("\n");
			builder.append("       ");
			builder.append(get(i1)+"");
		}
		
		builder.append("\n");
		builder.append("}");
		
		LogInfo logInfo = LogInfo.of(
			ExceptionsUtils.getThreadStacktrace(1), 
			StringUtils.concatWithSpaces(text), 
			StandartLoggingLevels.LEVEL_INFO, 
			Thread.currentThread().getName(), 
			System.currentTimeMillis(),
			false,
			logger,
			false
		);
		
		logger.log(logInfo);
		
		return (T) this;
	}
	
	public default <T extends Element_Type> T[] toArray(Class<T> classOfArray)
	{
		T[] arr = CollectionsSWL.createArray(classOfArray, size());
		toArray(arr);
		return arr;
	}
	
}
