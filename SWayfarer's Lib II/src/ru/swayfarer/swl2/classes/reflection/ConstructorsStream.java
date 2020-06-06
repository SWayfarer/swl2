package ru.swayfarer.swl2.classes.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.collections.streams.IDataStream;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;

/**
 * Поток конструкторов
 * @author swayfarer
 *
 */
@SuppressWarnings( {"rawtypes", "unchecked"} )
public class ConstructorsStream extends AbstractMethodsStream<ConstructorsStream, Constructor> {

	public static ILogger logger = LoggingManager.getLogger();
	
	/**
	 * Конструктор
	 * @param cl Класс, конструкторы которого будут помещены в поток 
	 */
	public ConstructorsStream(Class<?> cl)
	{
		this(CollectionsSWL.createExtendedList(cl.getDeclaredConstructors()));
	}
	
	/**
	 * Конструктор
	 * @param elements Элементы потока
	 */
	public ConstructorsStream(IExtendedList<Constructor> elements)
	{
		super(elements);
	}

	@Override
	public Class<?>[] getParameterTypes(Object element)
	{
		return ((Constructor)element).getParameterTypes();
	}

	@Override
	public Parameter[] getParameters(Object element)
	{
		return ((Constructor)element).getParameters();
	}

	@Override
	public String getName(Object element)
	{
		return ((Constructor)element).getName();
	}

	@Override
	public int getModifiers(Object element)
	{
		return ((Constructor)element).getModifiers();
	}

	@Override
	public int getParametersCount(Object element)
	{
		return ((Constructor)element).getParameterCount();
	}
	
	@Override
	public <E, T extends IDataStream<E>> T wrap(IExtendedList<E> list)
	{
		return (T) new ConstructorsStream(ReflectionUtils.<IExtendedList<Constructor>>forceCast(list)).setParrallel(isParallel);
	}

	@Override
	public <T> T getFirstAnnotation(Object element, Class<T> classOfAnnotation)
	{
		return (T) ((Constructor)element).getAnnotation(ReflectionUtils.forceCast(classOfAnnotation));
	}
	
	public <T> IDataStream<T> invoke(Object... args)
	{
		return ReflectionUtils.forceCast(map((c) -> logger.safeReturn(() -> c.newInstance(args), null, "")));
	}
}
