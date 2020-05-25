package ru.swayfarer.swl2.classes.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.collections.streams.DataStream;
import ru.swayfarer.swl2.collections.streams.IDataStream;

/**
 * Поток полей
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class FieldsStream extends DataStream<Field> {

	/**
	 * Конструктор
	 * @param cl Класс, поля которого будут в потоке
	 */
	public FieldsStream(Class<?> cl)
	{
		this(CollectionsSWL.createExtendedList(ReflectionUtils.getAccessibleFields(cl).values()));
	}
	
	/**
	 * Конструктор
	 * @param elements Элементы потока
	 */
	public FieldsStream(IExtendedList<Field> elements)
	{
		super(elements);
	}
	
	/**
	 * Оставить поля, типы которых имплементируют указаные интерфейсы
	 * @param cl Интерфейсы
	 * @return Поток с примененными изменениями
	 */
	public <T extends FieldsStream> T implement(Class<?>... cl)
	{
		return filter((field) -> DataStream.of(cl).each((classOfInterface) -> ReflectionUtils.isImplements(field.getType(), classOfInterface)));
	}
	
	/**
	 * Оставить поля, типы которых наследуют указанный класс
	 * @param cl Класс-родитель
	 * @return Поток с примененными изменениями
	 */
	public <T extends FieldsStream> T extend(Class<?> cl)
	{
		return filter((field) -> ReflectionUtils.isImplements(field.getType(), cl));
	}
	
	/**
	 * Оставить поля, типы совпадают с указанным
	 * @param cl Класс, которому будут соотсветствовать типы искомых полей
	 * @return Поток с примененными изменениями 
	 */
	public <T extends FieldsStream> T type(Class<?> cl)
	{
		return filter((field) -> field.getType().equals(cl));
	}
	
	/**
	 * Оставить поля, имена которых начинаются на указанную строку
	 * @param str Строка, с которой будут начинаться имена оставшихся полей
	 * @return Поток с примененными изменениями
	 */
	public <T extends FieldsStream> T starts(String str)
	{
		return filter((field) -> field.getName().startsWith(str));
	}
	
	/**
	 * Оставить поля, имена которых заканчиваются на указанную строку
	 * @param str Строка, с которой будут заканчиваться имена оставшихся полей
	 * @return Поток с примененными изменениями
	 */
	public <T extends FieldsStream> T ends(String str)
	{
		return filter((field) -> field.getName().endsWith(str));
	}
	
	/**
	 * Оставить только final-поля
	 * @return Поток с примененными изменениями
	 */
	public <T extends FieldsStream> T finals()
	{
		return withModifiers(Modifier.FINAL);
	}
	
	/**
	 * Оставить только public-поля
	 * @return Поток с примененными изменениями
	 */
	public <T extends FieldsStream> T publics()
	{
		return withModifiers(Modifier.PUBLIC);
	}
	
	/**
	 * Оставить только protected-поля
	 * @return Поток с примененными изменениями
	 */
	public <T extends FieldsStream> T protecteds()
	{
		return withModifiers(Modifier.PROTECTED);
	}
	
	/**
	 * Оставить только private-поля
	 * @return Поток с примененными изменениями
	 */
	public <T extends FieldsStream> T privates()
	{
		return withModifiers(Modifier.PRIVATE);
	}
	
	/**
	 * Оставить только static-поля
	 * @return Поток с примененными изменениями
	 */
	public <T extends FieldsStream> T statics()
	{
		return withModifiers(Modifier.STATIC);
	}
	
	/**
	 * Оставить только поля с указанными модификаторами 
	 * @param modifiers Модификаторы, такие, как {@link Modifier#PUBLIC}
	 * @return Поток с примененными изменениями
	 */
	public <T extends FieldsStream> T withModifiers(int... modifiers)
	{
		return filter((field) -> {
			if (CollectionsSWL.isNullOrEmpty(modifiers))
				return true;
			
			int access = field.getModifiers();
			
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
	
	@Override
	public <E, T extends IDataStream<E>> T wrap(IExtendedList<E> list)
	{
		return (T) new FieldsStream(ReflectionUtils.<IExtendedList<Field>>forceCast(list)).setParrallel(true);
	}

}
