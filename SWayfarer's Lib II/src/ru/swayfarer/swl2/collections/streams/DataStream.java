package ru.swayfarer.swl2.collections.streams;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.TreeSet;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.ExtendedListWrapper;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.system.SystemUtils;
import ru.swayfarer.swl2.tasks.factories.ThreadPoolTaskFactory;

/**
 * Простая реализация {@link IDataStream}
 * @author swayfarer
 */
@SuppressWarnings("unchecked")
public class DataStream<Element_Type> implements IDataStream<Element_Type>{

	/** Екзекьютор, который выполняет парраллельные опрерации */
	@InternalElement
	public static ThreadPoolTaskFactory executor = new ThreadPoolTaskFactory(1000, SystemUtils.getCpuCoresCount()).setName("DataStreamsPool");
	
	/** Парраллельный ли поток? */
	@InternalElement
	public boolean isParallel = false;
	
	/** Элементы потока */
	@InternalElement
	public IExtendedList<Element_Type> elements;
	
	/** Создает ли новый поток при каждой операции? */
	@InternalElement
	public boolean isCreatingNewOnWrap = true;
	
	/** Конструктор */
	public DataStream(IExtendedList<Element_Type> elements)
	{
		super();
		this.elements = elements;
	}
	
	/** Задать парраллельность */
	public <T extends DataStream<Element_Type>> T setParrallel(boolean isParrallel)
	{
		this.isParallel = isParrallel;
		
		return (T) this;
	}
	
	/** Удалить дублирующиеся элементы, сравнивая их через компаратор */
	@Override
	public <T extends IDataStream<Element_Type>> T distinct(IFunction2<? super Element_Type, ? super Element_Type, Integer> comparator)
	{
		TreeSet<Element_Type> set = new TreeSet<Element_Type>(comparator.asJavaComparator());
		set.addAll(elements);
		IExtendedList<Element_Type> list = new ExtendedListWrapper<>();
		list.addAll(set);
		return wrap(list);
	}
	
	/** Удалить дублирующиеся элементы */
	@Override
	public <T extends IDataStream<Element_Type>> T distinct()
	{
		IExtendedList<Element_Type> list = new ExtendedListWrapper<>(new ArrayList<>(new LinkedHashSet<>(elements)));
		return wrap(list);
	}
	
	/** Создать поток для листа */
	@InternalElement
	public <E, T extends IDataStream<E>> T wrap(IExtendedList<E> list)
	{
		return (T) new DataStream<E>(list);
	}

	/** Парраллельный ли поток? */
	@Override
	public boolean isParallel()
	{
		return isParallel;
	}

	/** Получить элемент под указанным id */
	@Override
	public Element_Type get(int id)
	{
		return elements.get(id);
	}

	/** Кол-во элементов в потоке */
	@Override
	public int size()
	{
		return elements.size();
	}

	/** Содержит ли элемент? */
	@Override
	public boolean contains(Element_Type element)
	{
		return elements.contains(element);
	}

	/** Пустой ли поток? */
	@Override
	public boolean isEmpty()
	{
		return elements.isEmpty();
	}

	/** Вставить элемент на укзанную позицию */
	@Override
	public <T extends IDataStream<Element_Type>> T insert(int pos, Element_Type element)
	{
		elements.add(pos, element);
		return (T) this;
	}

	/** Получить первый индекс элемента */
	@Override
	public int indexOf(Element_Type element)
	{
		return elements.indexOf(element);
	}

	/** Получить первый индекс элемента, проходящего фильтр */
	@Override
	public int indexOf(IFunction1<? super Element_Type, Boolean> filter)
	{
		int nextId = 0;
		
		for (Element_Type element : elements)
		{
			if (filter.apply(element))
				return nextId;
			
			nextId ++;
		}
		
		return -1;
	}

	/** Найти первый элемент, проходящий фильтр */
	@Override
	public Element_Type find(IFunction1<? super Element_Type, Boolean> filter)
	{
		for (Element_Type element : elements)
		{
			if (filter.apply(element))
				return element;
		}
		
		return null;
	}

	/** Оставить только элементы, проходящие фильтр */
	@Override
	public <T extends IDataStream<Element_Type>> T filtered(IFunction1<? super Element_Type, Boolean> condition)
	{
		IExtendedList<Element_Type> list = CollectionsSWL.createExtendedList();
		
		for (Element_Type element : elements)
			if (condition.apply(element))
				list.add(element);
		
		return wrap(list);
	}

	/** Отремапить поток*/
	@Override
	public <NE, T extends IDataStream<NE>> T mapped(IFunction1<? super Element_Type, ? extends NE> mapper)
	{
		IExtendedList<NE> list = CollectionsSWL.createExtendedList();
		
		for (Element_Type element : elements)
			list.add(mapper.apply(element));
		
		return wrap(list);
	}

	/** Подпоток элементов от и до указанных позиций */
	@Override
	public <T extends IDataStream<Element_Type>> T sub(int start, int end)
	{
		return wrap(CollectionsSWL.createExtendedList(elements.subList(start, end)));
	}

	/** Хотя бы один элемент удовлетворяет условию */
	@Override
	public boolean someMatches(IFunction1<? super Element_Type, Boolean> predicate)
	{
		for (Element_Type element : elements)
			if (predicate.apply(element))
				return true;
		
		return false;
	}

	/** Все элементы удовлетворяют условию */
	@Override
	public boolean matches(IFunction1<? super Element_Type, Boolean> predicate)
	{
		for (Element_Type element : elements)
			if (!predicate.apply(element))
				return false;
		
		return true;
	}

	/** Выполнить действие для каждого элемента потока */
	@Override
	public <T extends IDataStream<Element_Type>> T each(IFunction2NoR<Integer, ? super Element_Type> fun)
	{
		int next = 0;
		
		if (isParallel())
		{
			for (Element_Type element : elements)
			{
				final int id = next ++;
				executor.execute(() -> fun.apply(id, element));
			}
		}
		else
		{
			for (Element_Type element : elements)
			{
				fun.apply(next ++, element);
			}
		}
		
		return (T) this;
	}

	/** Отсортировать поток, сравнивая элементы по указанному компаратору */
	@Override
	public <T extends IDataStream<Element_Type>> T sorted(IFunction2<? super Element_Type, ? super Element_Type, Integer> comparator)
	{
		IExtendedList<Element_Type> list = elements.copy();
		list.sort(comparator.asJavaComparator());
		return wrap(list);
	}

	/** Сортированный поток */
	@Override
	public <T extends IDataStream<Element_Type>> T sorted()
	{
		IExtendedList<Element_Type> list = elements.copy();
		CollectionsSWL.sort(list);
		return wrap(list);
	}

	/** Получить лист элементов потока */
	@Override
	public IExtendedList<Element_Type> toList()
	{
		return elements.copy();
	}

	/** Перевернуть поток */
	@Override
	public <T extends IDataStream<Element_Type>> T invert()
	{
		IExtendedList<Element_Type> list = elements.copy();
		CollectionsSWL.reverse(list);
		return wrap(list);
	}

	/** Пропустить указанное кол-во элементов */
	@Override
	public <T extends IDataStream<Element_Type>> T skip(IFunction1<? super Element_Type, Boolean> predicate)
	{
		IExtendedList<Element_Type> list = CollectionsSWL.createExtendedList();
		
		boolean isFound = false;
		int index = 0;
		
		for (Element_Type element : elements)
		{
			if (predicate.apply(element))
			{
				isFound = true;
				break;
			}
			
			index ++;
		}
		
		if (isFound)
			list.addAll(elements.subList(index, elements.size()));
		
		return wrap(list);
	}

	/** Перемешать элементы потока */
	@Override
	public <T extends IDataStream<Element_Type>> T randomize(long seed)
	{
		IExtendedList<Element_Type> list = CollectionsSWL.createExtendedList();
		
		for (Element_Type element : CollectionsSWL.<Element_Type>randomize(elements))
		{
			list.add(element);
		}
		
		return wrap(list);
	}

	/** Удалить все элементы */
	@Override
	public <T extends IDataStream<Element_Type>> T removeAll(Element_Type element)
	{
		while (elements.contains(element))
			elements.remove(element);
		
		return (T) this;
	}
	
	public static <Element_Type> DataStream<Element_Type> of(Collection<Element_Type> elements)
	{
		return new DataStream<>(CollectionsSWL.createExtendedList(elements));
	}
	
	public static <Element_Type> DataStream<Element_Type> of(Element_Type... elements)
	{
		return new DataStream<>(CollectionsSWL.createExtendedList(elements));
	}
}
