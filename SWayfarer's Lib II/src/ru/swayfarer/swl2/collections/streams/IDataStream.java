package ru.swayfarer.swl2.collections.streams;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;
import java.util.stream.Stream;

import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1NoR;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.markers.Alias;
import ru.swayfarer.swl2.reference.IReference;

/**
 * Поток данных. Мой аналог Java-{@link Stream}
 * @author swayfarer
 */
@SuppressWarnings("unchecked")
public interface IDataStream<Element_Type> {
	
	/** Выполняются ли операции над данными парраллельно?*/
	public boolean isParallel();

	/** 
	 * Получить элемент по позиции. Дубль для {@link #get(int)}
	 * @param id Позиция элемента
	 * @return Полученный элемент
	 */
	@Alias("get")
	public default Element_Type at(int id)
	{
		return get(id);
	}
	
	/**
	 * Удалить элемент
	 * @param element Удаляемый элемент
	 * @return Поток после применения изменений.
	 */
	public <T extends IDataStream<Element_Type>> T removeAll(Element_Type element);
	
	/** 
	 * Получить элемент по позиции
	 * @param id Позиция элемента
	 * @return Полученный элемент
	 */
	public Element_Type get(int id);
	
	/** Получить последний элемент */
	public default Element_Type last()
	{
		int size = size();
		
		return size <= 0 ? null : get(size - 1);
	}
	
	/** Получить первый элемент */
	public default Element_Type first()
	{
		return isEmpty() ? null : get(0);
	}
	
	/**
	 * Получить размер потока данных (кол-во элементов)
	 * @return Количество элементов в потоке данных
	 */
	public int size();
	
	/** 
	 * Содержит ли поток данных элемент?
	 * @param element Элемент, наличие которого проверяется
	 * @return True, если содержится, иначе False
	 */
	public boolean contains(Element_Type element);
	
	/**
	 * Пустой ли поток данных?
	 * @return True, если пустой, иначе False
	 */
	public boolean isEmpty();
	
	/**
	 * Вставить элемент в поток данных
	 * @param pos Позиция, на которую будет вставлен элемент
	 * @param element Элемент, который будет вставлен
	 * @return Поток данных с проведенными изменениями
	 */
	public <T extends IDataStream<Element_Type>> T insert(int pos, Element_Type element);
	
	/**
	 * Получить позицию первого элемента в потоке данных
	 * @param element Элемент, позицию которого получаем
	 * @return Полученная позиция. -1, если элемент не был найден
	 */
	public int indexOf(Element_Type element);
	
	/**
	 * Получить позицию первого элемента, подходящего под фильтр, в потоке данных
	 * @param filter Фильтр, через который проверяются элементы
	 * @return Полученная позиция. -1, если элемент не был найден
	 */
	public int indexOf(IFunction1<? super Element_Type, Boolean> filter);
	
	/**
	 * Найти первый элемент, соответствующий фильтру
	 * @param filter Фильтр, по которому проверяем на соответствие
	 * @return Найденный элемент
	 */
	public Element_Type find(IFunction1<? super Element_Type, Boolean> filter);
	
	/**
	 * Найти первый элемент, соответствующий фильтру
	 * @param filter Фильтр, по которому проверяем на соответствие
	 * @param ref Ссылка, в которую будет помещен найденный элемент
	 * @return Поток после проделанных операций
	 */
	public default <T extends IDataStream<Element_Type>> T find(IReference ref, IFunction1<? super Element_Type, Boolean> filter)
	{
		if (ref == null)
			return (T) this;
		
		Element_Type element = find(filter);
		
		ref.setValue(element);
		
		return (T) this;
	}
	
	/** 
	 * Удалить все элементы, соотвествующие фильтру
	 * @param condition Фильтр, по которому проверяем на соответствие
	 * @return Поток данных с проведенными изменениями
	 */
	public default <T extends IDataStream<Element_Type>> T removeAll(IFunction1<? super Element_Type, Boolean> condition) 
	{
		return filtered((x) -> !condition.apply(x));
	}
	
	/**
	 * Добавить элементы в начало потока данных
	 * @param elements Добавляемые элементы
	 * @return Поток данных с проведенными изменениями
	 */
	public default <T extends IDataStream<Element_Type>> T prepend(Element_Type... elements)
	{
		int pos = 0;
		
		for (Element_Type element : elements)
		{
			insert(pos ++, element);;
		}
		
		return (T) this;
	}
	
	/**
	 * Добавить элементы в конец потока данных
	 * @param elements Добавляемые элементы
	 * @returnПоток данных с проведенными изменениями
	 */
	public default <T extends IDataStream<Element_Type>> T append(Element_Type... elements)
	{
		int size = size();
		
		for (Element_Type element : elements)
		{
			insert(size++, element);;
		}
		
		return (T) this;
	}
	
	/**
	 * Отфильтровать поток данных
	 * @param condition Филтр, которому должны соответсвовать элементы
	 * @return Поток данных с проведенными изменениями
	 */
	@Alias("filtered")
	public default <T extends IDataStream<Element_Type>> T filter(IFunction1<? super Element_Type, Boolean> condition)
	{
		return filtered(condition);
	}
	
	/**
	 * Отфильтровать поток данных
	 * @param condition Филтр, которому должны соответсвовать элементы
	 * @return Поток данных с проведенными изменениями
	 */
	public <T extends IDataStream<Element_Type>> T filtered(IFunction1<? super Element_Type, Boolean> condition);
	
	/**
	 *  Аналог {@link #filtered(IFunction1)}
	 *  @see {@link #filtered(IFunction1)}
	 *  */
	@Alias("filtered")
	public default <T extends IDataStream<Element_Type>> T is(IFunction1<? super Element_Type, Boolean> condition)
	{
		return filtered(condition);
	}
	
	/**
	 *  Аналог {@link #filtered(IFunction1)}, с инверсией условия (т.е. для прохождения фильтра условие НЕ долэно выполнятья)
	 *  @see {@link #filtered(IFunction1)}
	 *  */
	public default <T extends IDataStream<Element_Type>> T not(IFunction1<? super Element_Type, Boolean> condition)
	{
		return filtered((x) -> !condition.apply(x));
	}
	
	/**
	 * Удалить все дублирующиеся элементы
	 * @param comparator Компаратор, по которому определяется соответствие элементов
	 * @return Поток данных с проведенными изменениями
	 */
	public default <T extends IDataStream<Element_Type>> T distinct(IFunction2<? super Element_Type, ? super Element_Type, Integer> comparator)
	{
		return filtered(new TreeSet<Element_Type>(comparator.asJavaComparator())::add);
	}
	
	
	/**
	 * Удалить все дублирующиеся элементы
	 * @return Поток данных с проведенными изменениями
	 */
	public default <T extends IDataStream<Element_Type>> T distinct()
	{
		return filtered(new HashSet<Element_Type>()::add);
	}
	
	/**
	 * Отсортировать поток данных
	 * @param comparator Компаратор, по которому будут сортироваться элементы
	 * @return Поток данных с проведенными изменениями
	 */
	public <T extends IDataStream<Element_Type>> T sorted(IFunction2<? super Element_Type, ? super Element_Type, Integer> comparator);
	
	/**
	 * Отсортировать поток данных
	 * @return Поток данных с проведенными изменениями
	 */
	public <T extends IDataStream<Element_Type>> T sorted();
	
	/**
	 * Разместить элементы в обратном порядке
	 * @return Поток данных с проведенными изменениями
	 */
	public <T extends IDataStream<Element_Type>> T invert();
	
	/**
	 * Трансформировать элементы через ремаппер
	 * @param mapper Ремаппер. Функция, которая принимает объект и возвращает его трансформированную версию
	 * @return Поток данных с проведенными изменениями
	 */
	public <New_Element_Type, T extends IDataStream<New_Element_Type>> T mapped(IFunction1<? super Element_Type, ? extends New_Element_Type> mapper);
	
	/**
	 * Оставить ограниченное число элементов
	 * @param max Максимум элементов
	 * @return Поток данных с проведенными изменениями
	 */
	public default <T extends IDataStream<Element_Type>> T limit(int max)
	{
		return sub(0, max);
	}
	
	/** Превратить в карту, где ключ и значение будут генерироваться двумя функциями для каждого элемента */
	public default <Key_Type, Value_Type> Map<Key_Type, Value_Type> toMap(IFunction1<Element_Type, ? extends Key_Type> keyFactory, IFunction1<Element_Type, ? extends Value_Type> valueFactory)
	{
		Map<Key_Type, Value_Type> map = new HashMap<>();
		each((e) -> map.put(keyFactory.apply(e), valueFactory.apply(e)));
		return map;
	}
	
	/**
	 * Создать подпоток элементов, начинающимися с указанного индекса
	 * @param start Элемент, с которого будет заполняться новый поток
	 * @return Поток данных с проведенными изменениями
	 */
	public default <T extends IDataStream<Element_Type>> T sub(int start)
	{
		return sub(start, size());
	}
	
	/**
	 * Создать подпоток элементов, от и до указанных индексов
	 * @param start Элемент, с которого будет заполняться новый поток
	 * @param end Элемент, которым будет заканчиваться новый поток
	 * @return Поток данных с проведенными изменениями
	 */
	public <T extends IDataStream<Element_Type>> T sub(int start, int end);
	
	/**
	 * Хотя бы один элемент удовлетворяет условию
	 * @param predicate Условие, на которое проверяются элементы
	 * @return True, если хотя бы один элемент удовлетворяет условию
	 */
	public boolean someMatches(IFunction1<? super Element_Type, Boolean> predicate);
	
	/**
	 * Все элементы удовлетворяют условию
	 * @param predicate Условие, на которое проверяются элементы
	 * @return True, если все элементы удовлетворяют условию
	 */
	public boolean matches(IFunction1<? super Element_Type, Boolean> predicate);
	
	/**
	 * Ни один элемент не удовлетворяет условию
	 * @param predicate Условие, на которое проверяются элементы
	 * @return True, если ни один элемент не удовлетворяет условию
	 */
	public default boolean notMatches(IFunction1<? super Element_Type, Boolean> predicate)
	{
		return !matches(predicate);
	}
	
	/**
	 * Разместить элементы потока в случайном порядке
	 * @return Поток данных после проведенных действий
	 */
	public default <T extends IDataStream<Element_Type>> T randomize()
	{
		return randomize(System.currentTimeMillis());
	}

	/**
	 * Разместить элементы потока в случайном порядке
	 * @param seed Сид генератора {@link Random}
	 * @return Поток данных после проведенных действий
	 */
	public <T extends IDataStream<Element_Type>> T randomize(long seed);
	
	/**
	 * Выполнить функцию для всех элементов потока
	 * @param fun Функция, которая будет выполнена для всех элементов потока
	 * @return Поток данных после проведенных действий
	 */
	public default <T extends IDataStream<Element_Type>> T each(IFunction1NoR<? super Element_Type> fun)
	{
		return each((a, b) -> fun.apply(b));
	}
	
	public <T extends IDataStream<Element_Type>> T each(IFunction2NoR<Integer, ? super Element_Type> fun);
	
	public default <T extends IDataStream<Element_Type>> T each(boolean condition, IFunction1NoR<? super Element_Type> ifTrueFun, IFunction1NoR<? super Element_Type> ifFalseFun)
	{
		return each(condition ? ifTrueFun : ifFalseFun);
	}
	
	/**
	 * Пропускать элементы до тех пор, пока один из них не будет соответсвовать фильтру
	 * @param predicate Условие, на которое проверяются элементы
	 * @return Поток данных после проведенных действий
	 */
	public <T extends IDataStream<Element_Type>> T skip(IFunction1<? super Element_Type, Boolean> predicate);
	
	/**
	 * Получить лист элементов потока
	 * @return Лист элементов потока
	 */
	public IExtendedList<Element_Type> toList();
	
	/**
	 * Получить Java-{@link Stream} с элементами этого потока
	 * @return Новый {@link Stream}
	 */
	public default Stream<Element_Type> toJavaStream()
	{
		return toList().stream();
	}
	
	/**
	 * Получить парраллельный Java-{@link Stream} с элементами этого потока
	 * @return Новый {@link Stream}
	 */
	public default Stream<Element_Type> toJavaParrallelStream()
	{
		return toList().parallelStream();
	}
}
