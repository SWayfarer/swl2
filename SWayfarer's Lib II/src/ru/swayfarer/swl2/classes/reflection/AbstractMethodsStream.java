package ru.swayfarer.swl2.classes.reflection;

import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.collections.streams.DataStream;

/**
 * Абстрактный поток методов <br>
 * Для того, чтобы не писать одни и те же методы для конструкторов и методов
 * @author swayfarer
 *
 * @param <Element_Type> Тип элемента
 */
public abstract class AbstractMethodsStream<Element_Type> extends DataStream<Element_Type> {

	public AbstractMethodsStream(IExtendedList<Element_Type> elements)
	{
		super(elements);
	}

	/**
	 * Оставить только методы, имена которых начинаются на указанную строку
	 * @param str Строка, с которой будут начинаться оставшиеся методы
	 * @return Поток с примененными изменениями
	 */
	public <T extends AbstractMethodsStream<Element_Type>> T starts(String str)
	{
		return filter((method) -> getName(method).startsWith(str));
	}
	
	/**
	 * Оставить только методы, имена которых заканчиваются на указанную строку
	 * @param str Строка, с которой будут заканчиваться оставшиеся методы
	 * @return Поток с примененными изменениями
	 */
	public <T extends AbstractMethodsStream<Element_Type>> T ends(String str)
	{
		return filter((method) -> getName(method).endsWith(str));
	}
	
	/**
	 * Оставить только методы, аргументы которых совпадают по типу с указанными
	 * @param args Классы параметров метода соответственно их позиции. <br> Например, someMethod(Object, String) = {@link Object}, {@link String} 
	 * @return Поток с примененными изменениями
	 */
	public <T extends AbstractMethodsStream<Element_Type>> T args(Class<?>... args)
	{
		return filter((method) -> {
			Class<?>[] classes = getParameterTypes(method);
			return (CollectionsSWL.isNullOrEmpty(classes) && CollectionsSWL.isNullOrEmpty(args)) || classes.equals(args);
		});
	}
	
	/**
	 * Оставить только методы, подходящие для указанных аргументов
	 * @param args Аргументы, методы, подходящие для которых, будут оставлены
	 * @return Поток с примененными изменениями
	 */
	public <T extends AbstractMethodsStream<Element_Type>> T forArgs(Object... args)
	{
		return filter((method) -> ReflectionUtils.isParamsAccepted(getParameters(method), false, args));
	}
	
	/**
	 * Оставить только методы, количество аргументов которых равно указанному
	 * @param count Кол-во аргументов
	 * @return Поток с примененными изменениями
	 */
	public <T extends AbstractMethodsStream<Element_Type>> T argsCount(int count)
	{
		return filter((method) -> getParametersCount(method) == count);
	}
	
	/**
	 * Оставить только методы, соответствующие указанным модификаторам 
	 * @param modifiers Модификаторы, например, {@link Modifier#PUBLIC}
	 * @return Поток с примененными изменениями
	 */
	public <T extends AbstractMethodsStream<Element_Type>> T withModifiers(int... modifiers)
	{
		return filter((method) -> {
			if (CollectionsSWL.isNullOrEmpty(modifiers))
				return true;
			
			int access = getModifiers(method);
			
			for (int modifier : modifiers)
			{
				if ((access & modifier) == 0)
				{
					return false;
				}
			}
			
			return true;
		});
	}
	
	/**
	 * Оставить только final-методы
	 * @return Поток с примененными изменениями
	 */
	public <T extends AbstractMethodsStream<Element_Type>> T finals()
	{
		return withModifiers(Modifier.FINAL);
	}
	
	/**
	 * Оставить только public-методы
	 * @return Поток с примененными изменениями
	 */
	public <T extends AbstractMethodsStream<Element_Type>> T publics()
	{
		return withModifiers(Modifier.PUBLIC);
	}
	
	/**
	 * Оставить только protected-методы
	 * @return Поток с примененными изменениями
	 */
	public <T extends AbstractMethodsStream<Element_Type>> T protecteds()
	{
		return withModifiers(Modifier.PROTECTED);
	}
	
	/**
	 * Оставить только private-методы
	 * @return Поток с примененными изменениями
	 */
	public <T extends AbstractMethodsStream<Element_Type>> T privates()
	{
		return withModifiers(Modifier.PRIVATE);
	}
	
	/**
	 * Оставить только static-методы
	 * @return Поток с примененными изменениями
	 */
	public <T extends AbstractMethodsStream<Element_Type>> T statics()
	{
		return withModifiers(Modifier.STATIC);
	}
	
	/**
	 * Получить масив типов параметров для метода
	 * @param element Метод, параметры которого получаем
	 * @return Массив типов ({@link Class})
	 */
	public abstract Class<?>[] getParameterTypes(Object element);
	
	/**
	 * Получить массив параметров для метода
	 * @param element Метод, параметры котором получаем
	 * @return Массив параметров ({@link Parameter})
	 */
	public abstract Parameter[] getParameters(Object element);
	
	/**
	 * Получить имя метода 
	 * @param element Метод, имя которого получаем
	 * @return Имя метода
	 */
	public abstract String getName(Object element);
	
	/**
	 * Получить модификаторы метода 
	 * @param element Метод, модификаторы которого получаем
	 * @return Модификаторы метода в виде инта, из которого можно доставать конкретные через битовые операции
	 */
	public abstract int getModifiers(Object element);
	
	/**
	 * Получить кол-во параметров метода
	 * @param element Метод, параметры кол-во параметров которых получаем
	 * @return Кол-во параметров методов
	 */
	public abstract int getParametersCount(Object element);
}
