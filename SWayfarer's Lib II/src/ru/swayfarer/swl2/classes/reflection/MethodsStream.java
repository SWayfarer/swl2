package ru.swayfarer.swl2.classes.reflection;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.collections.streams.DataStream;
import ru.swayfarer.swl2.collections.streams.IDataStream;

/**
 * Поток методов
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class MethodsStream extends AbstractMethodsStream<Method> {

	/**
	 * Конструктор
	 * @param cl Класс, методы которого будут помещены в поток 
	 */
	public MethodsStream(Class<?> cl)
	{
		this(ReflectionUtils.getAccessibleMethods(cl));
	}
	
	/**
	 * Конструктор
	 * @param elements Элементы потока
	 */
	public MethodsStream(IExtendedList<Method> elements)
	{
		super(elements);
	}
	
	/**
	 * Оставить только методы, возвращаемый тип которых равен указанному
	 * @param cl Класс, объекты которого будут возвращать оставшиеся методы 
	 * @return Поток с примененными изменениями
	 */
	public <T extends MethodsStream> T returnType(Class<?> cl)
	{
		return filter((method) -> method.getReturnType() == cl);
	}

	/**
	 * Оставить только методы, возвращаемый тип которых имплементирует указанный
	 * @param cl Класс интерфейса, который будет имплементироваться оставшимися объектами 
	 * @return Поток с примененными изменениями
	 */
	public <T extends MethodsStream> T returnImplement(Class<?> cl)
	{
		return filter((method) -> DataStream.of(method.getReturnType().getInterfaces()).contains(cl));
	}
	
	/**
	 * Оставить только методы, возвращаемый тип которых наследует указанный
	 * @param cl Класс, который будет наследоваться возвращаемыми типами оставщихся методов
	 * @return Поток с примененными изменениями
	 */
	public <T extends MethodsStream> T returnExtends(Class<?> cl)
	{
		return filter((method) -> method.getReturnType().getSuperclass().equals(cl));
	}

	@Override
	public Class<?>[] getParameterTypes(Object element)
	{
		return ((Method)element).getParameterTypes();
	}

	@Override
	public Parameter[] getParameters(Object element)
	{
		return ((Method)element).getParameters();
	}

	@Override
	public String getName(Object element)
	{
		return ((Method)element).getName();
	}

	@Override
	public int getModifiers(Object element)
	{
		return ((Method)element).getModifiers();
	}

	@Override
	public int getParametersCount(Object element)
	{
		return ((Method)element).getParameterCount();
	}
	
	@Override
	public <E, T extends IDataStream<E>> T wrap(IExtendedList<E> list)
	{
		return (T) new MethodsStream(ReflectionUtils.<IExtendedList<Method>>forceCast(list)).setParrallel(true);
	}
}
