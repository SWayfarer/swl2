package ru.swayfarer.swl2.classes.reflection;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.collections.streams.DataStream;
import ru.swayfarer.swl2.collections.streams.IDataStream;
import ru.swayfarer.swl2.equals.EqualsUtils;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;

/**
 * Поток методов
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class MethodsStream extends AbstractMethodsStream<MethodsStream, Method> {

	public static ILogger logger = LoggingManager.getLogger();
	
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
	 * @param classes Класс, объекты которого будут возвращать оставшиеся методы 
	 * @return Поток с примененными изменениями
	 */
	public <T extends MethodsStream> T returnType(Class<?>... classes)
	{
		return filter((method) -> EqualsUtils.objectEqualsSome(method.getReturnType(), (Object[]) classes));
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
	
	/**
	 * Оставить не-статические методы
	 * @return Поток с примененными изменениями
	 */
	public <T extends MethodsStream> T nonStatics()
	{
		return filter((method) -> !Modifier.isStatic(method.getModifiers()));
	}

	/**
	 * Вызов всех методов потока
	 * @param obj Объект, для которого будут вызваны методы
	 * @param args Аргументы, с которыми будет вызван метод
	 * @return Поток, после вызова всех методов
	 */
	public <T extends MethodsStream> T invoke(Object obj, Object... args)
	{
		return each((method) -> {
			logger.safe(() -> {
				method.invoke(obj, args);
			}, "Error while invoking method", method, "from", obj, "with args", args);
		});
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

	@Override
	public <T> T getFirstAnnotation(Object element, Class<T> classOfAnnotation)
	{
		return ((Method)element).getAnnotation(ReflectionUtils.forceCast(classOfAnnotation));
	}
}
