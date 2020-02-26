package ru.swayfarer.swl2.classes;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.streams.DataStream;
import ru.swayfarer.swl2.equals.EqualsUtils;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction3NoR;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;

@SuppressWarnings( { "unchecked", "rawtypes" } )
public class ReflectionUtils {

	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();

	/** Функция для получения доступа к значению поля */
	public static IFunction2<Field, Object, Object> fieldGetAccessor = (field, instance) -> {
		
		try
		{
			field.setAccessible(true);
			return field.get(instance);
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while getting field", field, "value from", instance);
		}
		
		return null;
	};
	
	/** Функция для задания значения поля*/
	public static IFunction3NoR<Field, Object, Object> fieldSetAccessor = (field, instance, value) -> {
		
		try
		{
			field.setAccessible(true);
			field.set(instance, value);
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while setting field", field, "value from", instance);
		}
	};
	
	/** Очистить от массива в классе, если присутствует */
	public static Class<?> cleanFromArray(Class<?> cl)
	{
		if (cl == null)
			return null;
		
		if (cl.isArray())
			return cl.getComponentType();
		
		return cl;
	}
	
	public static <T> T newInstanceOf(Class<?> cl, Object... args)
	{
		try
		{
			ExceptionsUtils.IfNull(cl, IllegalArgumentException.class, "Class for creating instance can't be null!");
			Constructor constructor = findConstructor(cl, args);
			
			if (constructor == null)
			{
				logger.warning("Can't find constructor for args", Arrays.asList(args));
			}
			
		}
		catch (Throwable e)
		{
			logger.error(e, "");
		}
		
		return null;
	}
	
	public static <T> T newInstanceOf(Class<?> cl)
	{
		try
		{
			ExceptionsUtils.IfNull(cl, IllegalArgumentException.class, "Class for creating instance can't be null!");
			return (T) cl.newInstance();
		}
		catch (Throwable e)
		{
			logger.error(e, "");
		}
		
		return null;
	}
	
	/**
	 * Найти метод в классе
	 * @param cl Класс, в котором ищем
	 * @param parameterTypes Массив типов аргументов
	 * @param methodNames Массив имен методов
	 * @return Найденный метод. Null, если метод не найден или произошла ошибка
	 */
	public static Method findMethod(Class<?> cl, Class<?>[] parameterTypes, String... methodNames)
    {
        for (String methodName : methodNames)
        {
            try
            {
                Method m = cl.getDeclaredMethod(methodName, parameterTypes);
                m.setAccessible(true);
                return m;
            }
            catch (SecurityException e)
            {
            	logger.error(e, "Error while getting access for", methodName);
            }
            catch (Throwable e) 
            {
				
			}
        }
        
        return null;
    }
	
	/** 
	 * Найти метод, соответствующий фильтру
	 * @param cl Класс, в котором ищем метод
	 * @param filter Фильтр, по которому ищем метод
	 * @return Найдейнный метод или null
	 */
	public static Method findMethod(Class<?> cl, IFunction1<Method, Boolean> filter)
	{
		return DataStream.of(cl.getDeclaredMethods()).find(filter);
	}
	
	/**
	 * Найти метод в классе
	 * @param cl Класс, в котором ищем
	 * @param methodNames Возможные имена методов (для обфускации)
	 * @return Найденный метод. Null, если метод не был найден или произошла ошибка
	 */
	public static Method findMethod(Class<?> cl, String... methodNames)
    {
        for (String methodName : methodNames)
        {
            try
            {
                Method m = cl.getDeclaredMethod(methodName, (Class<?>[])null);
                m.setAccessible(true);
                return m;
            }
            catch (SecurityException e)
            {
            	logger.error(e, "Can't get access to", methodName);
            }
            catch (Exception e)
            {
            	//Console.printThrowable("Error while setting method "+methodName+" to accessible", e);
            }
        }
        
        for (String methodName : methodNames)
        {
            try
            {
               for (Method method : cl.getDeclaredMethods())
               {
            	   if (method.getName().equals(methodName))
            	   {
            		   method.setAccessible(true);
            		   return method;
            	   }
               }
            }
            catch (SecurityException e)
            {
            	logger.error(e, "Can't get access to", methodName);
            }
            catch (Exception e)
            {
            	//Console.printThrowable("Error while setting method "+methodName+" to accessible", e);
            }
        }
        
        return null;
    }
	
	/**
	 * Найти подходящий под параметры метод
	 * @param cl Класс, конструктор которого ищем
	 * @param args Параметры конструктора
	 * @return Найденный метод. Null, если не найдено ничего
	 */
	public static Method findMethod(Class<?> cl, String name, Object... args)
	{
		Method[] methods = null;
		
		while (cl != null)
		{
			methods = cl.getDeclaredMethods();
			
			for (Method method : methods)
			{
				if (method.getName().equals(name) && isParamsAccepted(method.getParameters(), true, args))
					return method;
			}
			
			for (Method method : methods)
			{
				if (method.getName().equals("launch"))
				{
					System.out.println(isParamsAccepted(method.getParameters(), false, args));
				}
				
				if (method.getName().equals(name) && isParamsAccepted(method.getParameters(), false, args))
					return method;
			}
			
			cl = cl.getSuperclass();
		}
		
		return null;
	}
	
	/**
	 * Найти подходящий под параметры метод
	 * @param cl Класс, конструктор которого ищем
	 * @param args Параметры конструктора
	 * @return Найденный метод. Null, если не найдено ничего
	 */
	public static Constructor findConstructor(Class<?> cl, Object... args)
	{
		for (Constructor constructor : cl.getDeclaredConstructors())
		{
			if (isParamsAccepted(constructor.getParameters(), true, args))
				return constructor;
		}
		
		return null;
	}
	
	/** Принудительный автокаст. Для тех случаев, где с ними начинает твориться непотребщина */
	public static <T> T forceCast(Object obj)
	{
		return (T) obj;
	}
	
	/** Есть ли у класса аннотация? */
	public static boolean hasAnnotation(Class<?> classOfAnnotation, Class<?> cl)
	{
		return cl.getAnnotation(forceCast(classOfAnnotation)) != null;
	}
	
	/** Есть ли у поля аннотация? */
	public static boolean hasAnnotation(Class<?> classOfAnnotation, Field field)
	{
		return field.getAnnotation(forceCast(classOfAnnotation)) != null;
	}
	
	/**
	 * Вызвать приватный метод объекта
	 * @param instance Объект, для которого вызывается метод
	 * @param ptypes Массив типов аргументов 
	 * @param args Массив аргументов
	 * @param names Возможные имена поля (для обфускации)
	 * @throws Exception Если во время выполнения метода что-то пошло не так
	 */
	public static <T> T invokeMethod(Object instance, Class<?>[] ptypes, Object[] args, String... names) throws Exception
	{
		Method method = null;
		
		if (ptypes == null)
			method = findMethod(instance.getClass(), names);
		else
			method = findMethod(instance.getClass(), ptypes, names);
		
		if (method == null)
			return null;
		
		method.setAccessible(true);
		
		return (T) method.invoke(instance, args);
	}
	
	/**
	 * Вызвать приватный метод объекта
	 * @param instance Объект, для которого вызывается метод
	 * @param names Возможные имена поля (для обфускации)
	 * @throws Exception Если во время выполнения метода что-то пошло не так
	 */
	public static <T> T invokeMethod(Object instance, String... names) throws Exception
	{
		return (T) invokeMethod(instance, null, null, names);
	}
	
	/**
	 * Вызвать метод 
	 * @param instance Экземпляр, в котором будет запускаться метод
	 * @param name Имя метода 
	 * @param args Аргумента 
	 */
	public static <T> T invokeMethod(Object instance, String name, Object... args)
	{
		ExceptionsUtils.IfNull(instance, IllegalArgumentException.class, "Object for method invoke can't be null!");
		return invokeMethod(instance.getClass(), instance, name, args);
	}
	
	/**
	 * Вызвать метод 
	 * @param cl Класс, метод которого запускается
	 * @param instance Экземпляр, в котором будет запускаться метод
	 * @param name Имя метода 
	 * @param args Аргумента 
	 */
	public static <T> T invokeMethod(Class<?> cl, Object instance, String name, Object... args)
	{
		try
		{
			Method method = findMethod(cl, name, args);
			
			if (method != null)
				return (T) method.invoke(instance, args);
			else
				logger.warning("Can't find method", name, "with args", CollectionsSWL.createExtendedList(args), "at", cl);
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while invoking method", name, "of", cl, "with args", Arrays.asList(args));
		}
		
		return null;
	}
	
	/**
	 * Являются ли типы параметров эквивалентными?
	 * @param cl1
	 * @param cl2
	 * @return
	 */
	public static boolean isClassesEquals(Class<?> cl1, Class<?> cl2)
	{
		if (cl1 == cl2)
			return true;
		
		if (cl1 == int.class)
			return cl2 == Integer.class;
		
		if (cl1 == byte.class)
			return cl2 == Byte.class;
		
		if (cl1 == short.class)
			return cl2 == Short.class;
		
		if (cl1 == long.class)
			return cl2 == Long.class;
		
		if (cl1 == char.class)
			return cl2 == Character.class;
		
		if (cl1 == double.class)
			return cl2 == Double.class;
		
		if (cl1 == float.class)
			return cl2 == Float.class;
		
		if (cl1 == boolean.class)
			return cl2 == Boolean.class;
		
		if (cl1.isAssignableFrom(cl2))
			return true;
		else
			logger.info(cl1, "is not assignable from", cl2);
		
		return false;
	}
	
	/**
	 * Считать ли параметры подходящими 
	 * @param params Массив параметров
	 * @param isFully Считать ли null-аргументы совпадающими
	 * @param args Массив аргументов
	 * @return Подходят ли?
	 */
	public static boolean isParamsAccepted(Parameter[] params, boolean isFully, Object... args)
	{
		if (params.length != args.length)
			return false;
		
		for (int i1 = 0; i1 < params.length; i1 ++)
		{
			if (args[i1] == null && !isPrimitive(params[i1].getType()))
			{
				if (isFully)
				{
					return false;
				}
			}
			else if (!isClassesEquals(params[i1].getType(), args[i1].getClass()))
			{
				return false;
			}
		}
		
		return true;
	}
	 
	/** Является ли класс примитивом (например, int.class) */
	public static boolean isPrimitive(Class<?> cl)
	{
		return EqualsUtils.objectEqualsSome(cl, byte.class, short.class, int.class, long.class, boolean.class, double.class, float.class, char.class);
	}
	
	/** Задать все поля как доступные */
	public static boolean setAccessible(AccessibleObject... accessibleObjects)
	{
		try
		{
			AccessibleObject.setAccessible(accessibleObjects, true);
			return true;
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while setting");
		}
		
		return false;
	}
}
