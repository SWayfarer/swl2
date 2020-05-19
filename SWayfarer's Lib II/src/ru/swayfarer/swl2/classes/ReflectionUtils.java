package ru.swayfarer.swl2.classes;

import java.io.File;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.collections.streams.DataStream;
import ru.swayfarer.swl2.equals.EqualsUtils;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction3NoR;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.resource.rlink.ResourceLink;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.string.property.SystemProperty;

/**
 * Утилиты для работы с рефлексией
 * @author swayfarer
 *
 */
@SuppressWarnings( { "unchecked", "rawtypes" } )
public class ReflectionUtils {
	
	public static IExtendedList<IFunction2<String, URL, String>> registeredSourcesFun = CollectionsSWL.createExtendedList();
	
	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Кэшированные элменты */
	@InternalElement
	public static Map<Class<?>, Map<String, Field>> cachedAccessibleFields = CollectionsSWL.createIdentityMap();

	/** Функция для получения доступа к значению поля */
	@InternalElement
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
	
	public static String getClassSource(String className)
	{
		String name = className.replace(".", "/") + ".class";
		
		URL url = ReflectionUtils.class.getClassLoader().getResource(name);
		
		return getResourceSource(className, url);
	}
	
	public static IExtendedList<String> getClasspath()
	{
		String cp = new SystemProperty("java.class.path").getValue();
		return CollectionsSWL.createExtendedList(cp.split(File.pathSeparator));
	}
	
	public static String getClassSource(Class<?> cl)
	{
		if (cl == null)
			return null;
		
		String name = cl.getName().replace(".", "/") + ".class";
		
		ClassLoader classloader = cl.getClassLoader();
		
		if (classloader == null)
			return "<jvm>";
		
		URL url = classloader.getResource(name);
		
		return getResourceSource(cl.getName(), url);
	}
	
	public static void registerDefaultSourceFuns()
	{
		registeredSourcesFun.add((cl, url) -> {
			if (url.getProtocol().equals("file"))
			{
				String pkg = ExceptionsUtils.getClassPackage(cl);
				int parentsCount = StringUtils.countMatches(pkg, ".") + 2;
				FileSWL file = FileSWL.ofURL(url);
				
				if (file != null)
				{
					for (int i1 = 0; i1 < parentsCount && file != null; i1 ++)
					{
						file = file.getParentFile();
					}
					
					if (file != null)
						return file.getName() + "/";
				}
			}
			return null;
		});
		
		registeredSourcesFun.add((cl, url) -> {
			if (url.getProtocol().equals("jar"))
			{
				//jar:file:/run/media/swayfarer/PC%20II%20-%20%d0%9e%d1%81%d0%bd%d0%be%d0%b2%d0%bd%d0%be%d0%b5/Soft/Okta/maven/repo/log4j/log4j/1.2.17/log4j-1.2.17.jar!/org/apache/log4j/Appender.class
				String externalForm = url.toExternalForm();
				externalForm = externalForm.substring(0, externalForm.lastIndexOf("!"));
				return ResourceLink.getSimpleName(externalForm);
			}
			
			return null;
		});
	}
	
	public static String getResourceSource(String className, URL url)
	{
		if (url == null)
			return "<unknown>";
		
		for (IFunction2<String, URL, String> fun : registeredSourcesFun)
		{
			String ret = fun.apply(className, url);
			
			if (!StringUtils.isEmpty(ret))
				return ret;
		}
		
		return "<unknown>";
	}
	
	public static void invokeMethods(IFunction1<Method, Boolean> filter, Class<?> classOfObj, Object obj, Object... args)
	{
		int argsCount = args == null ? 0 : args.length;
		
		getAccessibleMethods(classOfObj)
			.dataStream()
			.filter((m) -> m.getParameterCount() == argsCount)
			.filter((m) -> isParamsAccepted(m.getParameters(), true, args))
			.filter(filter)
			.each((m) -> {
				logger.safe(() -> {
					m.invoke(obj, args);
				}, "Error while invoking method of \nclass", classOfObj, "\ninstace", obj, "\nmethod", m, "\nwith args", args);
			});
	}
	
	/** Есть ли у поля аннотация? */
	public static boolean hasAnnotation(Field field, Class<?> annotationClass)
	{
		return field.getAnnotation(forceCast(annotationClass)) != null;
	}
	
	/** Получить все доступные поля объекта */
	public static Map<String, Field> getAccessibleFields(Object obj)
	{
		return getAccessibleFields(obj.getClass());
	}
	
	/** Получить значение статического поля класса */
	public static <T> T getFieldValue(Class<?> cl, Object... names)
	{
		return getFieldValue(null, cl, names);
	}
	
	public static Field findField(Class<?> cl, Object instance, String... names)
	{
		Map<String, Field> fields = getAccessibleFields(cl);
		
		for (Object obj : names)
		{
			String name = String.valueOf(obj);
			
			if (StringUtils.isEmpty(name))
				continue;
			
			Field field = fields.get(name);
			
			try
			{
				if (field != null)
				{
					if (instance == null && !Modifier.isStatic(field.getModifiers()))
						continue;
					
					return field;
				}
			}
			catch (Throwable e)
			{
				logger.error(e, "Error while getting field value");
			}
		}
		
		return null;
	}
	
	/** Получить значение поля объекта */
	public static <T> T getFieldValue(Object instance, Object... names)
	{
		return getFieldValue(instance, instance.getClass(), names);
	}
	
	/** Получить значение поля */
	public static <T> T getFieldValue(Object instance, Class<?> cl, Object... names)
	{
		Map<String, Field> fields = getAccessibleFields(cl);
		
		for (Object obj : names)
		{
			String name = String.valueOf(obj);
			
			if (StringUtils.isEmpty(name))
				continue;
			
			Field field = fields.get(name);
			
			try
			{
				if (field != null)
				{
					if (instance == null && !Modifier.isStatic(field.getModifiers()))
						continue;
					
					return (T) field.get(instance);
				}
			}
			catch (Throwable e)
			{
				logger.error(e, "Error while getting field value");
			}
		}
		
		return null;
	}
	
	/** Является ли класс числом? */
	public static boolean isNumber(Class<?> cl)
	{
		return EqualsUtils.objectEqualsSome(cl, Number.class, byte.class, Byte.class, short.class, Short.class, int.class, Integer.class, long.class, Long.class, float.class, Float.class, double.class, Double.class);
	}
	
	/** Получить доступные поля */
	public static Map<String, Field> getAccessibleFields(Class<?> cl)
	{
		Map<String, Field> ret = cachedAccessibleFields.get(cl);
		
		if (ret == null)
		{
			ret = CollectionsSWL.createHashMap();
			
			while (cl != null && cl != Object.class)
			{
				for (Field field : cl.getDeclaredFields())
				{
					if (setAccessible(field))
						ret.put(field.getName(), field);
				}
				
				cl = cl.getSuperclass();
			}
		}
		
		return ret;
	}
	
	public static IExtendedList<Method> getAccessibleMethods(Object obj)
	{
		return getAccessibleMethods(obj.getClass());
	}
	
	public static IExtendedList<Method> getAccessibleMethods(Class<?> cl)
	{
		IExtendedList<Method> ret = CollectionsSWL.createExtendedList();
		
		for (Method method : cl.getDeclaredMethods())
		{
			if (setAccessible(method))
				ret.add(method);
		}
		
		return ret;
	}
	
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
	
	/** Очистить от отбертки класс, если присутствует */
	public static Class<?> cleanFromWrapper(Class<?> cl)
	{
		if (cl == Byte.class)
			return byte.class;
		else if (cl == Short.class)
			return short.class;
		else if (cl == Integer.class)
			return int.class;
		else if (cl == Long.class)
			return long.class;
		else if (cl == Float.class)
			return float.class;
		else if (cl == Double.class)
			return double.class;
		else if (cl == Byte[].class)
			return byte[].class;
		else if (cl == Short[].class)
			return short[].class;
		else if (cl == Integer[].class)
			return int[].class;
		else if (cl == Long[].class)
			return long[].class;
		else if (cl == Float[].class)
			return float[].class;
		else if (cl == Double[].class)
			return double[].class;
		
		return cl;
	}
	
	/** Очистить от массива в классе, если присутствует */
	public static Class<?> cleanFromArray(Class<?> cl)
	{
		if (cl == null)
			return null;
		
		if (cl.isArray())
			return cl.getComponentType();
		
		return cl;
	}
	
	/** Создать новый объект указанного класса, с наиболее подходящим для переданных аррументов конструктором */
	public static <T> T newInstanceOf(Class<?> cl, Object... args)
	{
		try
		{
			ExceptionsUtils.IfNull(cl, IllegalArgumentException.class, "Class for creating instance can't be null!");
			Constructor constructor = findConstructor(cl, args);
			
			if (constructor == null)
			{
				logger.warning("Can't find constructor for args", args);
			}
			
			return (T) constructor.newInstance(args);
			
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while creating new instance of", cl);
		}
		
		return null;
	}
	
	/** Является ли объект массивом? */
	public static boolean isArray(Object obj)
	{
		if (obj == null)
			return false;
		
		return obj.getClass().isArray();
	}
	
	/** Создать новый объект через пустой конструктор */
	public static <T> T newInstanceOf(Class<?> cl)
	{
		try
		{
			ExceptionsUtils.IfNull(cl, IllegalArgumentException.class, "Class for creating instance can't be null!");
			return (T) cl.getConstructor().newInstance();
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
				if (method.getName().equals(name) && isParamsAccepted(method.getParameters(), false, args))
					return method;
			}
			
			cl = cl.getSuperclass();
		}
		
		return null;
	}
	
	public static <T> Class<T> findClass(String name)
	{
		return (Class<T>) logger.safeReturn(() -> Class.forName(name), null, "Error while finding class", name);
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
	public static MethodInvokationResult invokeMethod(Object instance, Class<?>[] ptypes, Object[] args, String... names)
	{
		Method method = null;
		
		MethodInvokationResult ret = new MethodInvokationResult();
		
		if (ptypes == null)
			method = findMethod(instance.getClass(), names);
		else
			method = findMethod(instance.getClass(), ptypes, names);
		
		if (method == null)
			return null;
		
		method.setAccessible(true);
		
		Method finalMethod = method;
		
		try
		{
			ret.returnValue = finalMethod.invoke(instance, args);
		}
		catch (Throwable e)
		{
			ret.setInvokationThrowable(e);
			logger.error(e, "Error while invoking method", method, "of", instance, "with args:", args);
		}
		
		return ret;
	}
	
	/**
	 * Вызвать приватный метод объекта
	 * @param instance Объект, для которого вызывается метод
	 * @param names Возможные имена поля (для обфускации)
	 * @throws Exception Если во время выполнения метода что-то пошло не так
	 */
	public static MethodInvokationResult invokeMethod(Object instance, String... names)
	{
		return invokeMethod(instance, null, null, names);
	}
	
	/**
	 * Вызвать метод 
	 * @param instance Экземпляр, в котором будет запускаться метод
	 * @param name Имя метода 
	 * @param args Аргумента 
	 */
	public static MethodInvokationResult invokeMethod(String name, Object instance , Object... args)
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
	public static MethodInvokationResult invokeMethod(Class<?> cl, Object instance, String name, Object... args)
	{
		MethodInvokationResult ret = new MethodInvokationResult();
		
		try
		{
			Method method = findMethod(cl, name, args);
			
			if (method != null)
				ret.returnValue = method.invoke(instance, args);
			else
				logger.warning("Can't find method", name, "with args", CollectionsSWL.createExtendedList(args), "at", cl);
		}
		catch (Throwable e)
		{
			ret.setInvokationThrowable(e);
			logger.error(e, "Error while invoking method", name, "of", cl, "with args", Arrays.asList(args));
		}
		
		return ret;
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
	
	/** Статическое ли поле? */
	public static boolean isStatic(Field field)
	{
		return Modifier.isStatic(field.getModifiers());
	}
	
	/** Статическое ли поле? */
	public static boolean isStatic(Method method)
	{
		return Modifier.isStatic(method.getModifiers());
	}
	
	/** 
	 * Выполнить функцию для каждого метода указанного класса
	 * <h1> Функция принимает: </h1>
	 * Метод, инстанс класса
	 * <br> Каждая из функций выполняется в отдельном try-catch
	 */
	public static void forEachMethod(Object instance, IFunction2NoR<Method, Object> fun)
	{
		forEachMethod(instance.getClass(), instance, fun);
	}
	
	/** Задать значение статического поля */
	public static void setFieldValue(Class<?> cl, Object value, String... names)
	{
		setFieldValue(cl, null, value, names);
	}
	
	/** Задать значение поля */
	public static void setFieldValue(Object obj, Object value, String... names)
	{
		setFieldValue(obj.getClass(), obj, value, names);
	}
	
	/** Задать значение поля */
	public static void setFieldValue(Class<?> cl, Object obj, Object value, String... names)
	{
		Field field = findField(cl, obj, names);
		
		if (field != null)
			setFieldValue(obj, field, value);
	}
	
	/** Задать значение поля безопасно */
	public static void setFieldValue(Object obj, Field field, Object value)
	{
		logger.safe(() -> {
			field.setAccessible(true);
			field.set(obj, value);
		}, "Error while setting field", field, "of object", obj, "to value", value);
	}
	
	/** 
	 * Выполнить функцию для каждого метода указанного класса
	 * <h1> Функция принимает: </h1>
	 * Метод, инстанс класса
	 * <br> Каждая из функций выполняется в отдельном try-catch
	 */
	public static void forEachMethod(Class<?> cl, Object instance, IFunction2NoR<Method, Object> fun)
	{
		Method[] methods = cl.getDeclaredMethods();
		
		setAccessible(methods);
		
		for (Method method : methods)
		{
			logger.safe(() -> {
				
				if (instance != null || isStatic(method))
					fun.apply(method, instance);
			
			}, "Error while processing fun", fun, "for method", method, "of class", cl, "at object", instance);
		}
	}
	
	/** 
	 * Выполнить функцию для каждого поля указанного класса
	 * <h1> Функция принимает: </h1>
	 * Поле, инстанс класса, значение
	 * <br> Каждая из функций выполняется в отдельном try-catch
	 */
	public static void forEachField(Object instance, IFunction3NoR<Field, Object, Object> fun)
	{
		forEachField(instance.getClass(), instance, fun);
	}
	
	/** 
	 * Выполнить функцию для каждого поля указанного класса
	 * <h1> Функция принимает: </h1>
	 * Поле, инстанс класса, значение
	 * <br> Каждая из функций выполняется в отдельном try-catch
	 */
	public static void forEachField(Class<?> cl, Object instance, IFunction3NoR<Field, Object, Object> fun)
	{
		Field[] fields = cl.getDeclaredFields();
		
		setAccessible(fields);
		
		for (Field field : fields)
		{
			logger.safe(() -> {
				
				if (instance != null || isStatic(field))
					fun.apply(field, instance, field.get(instance));
			
			}, "Error while processing fun", fun, "for field", field, "of class", cl, "at object", instance);
		}
	}
	
	@Data @AllArgsConstructor(staticName = "of")
	public static class MethodInvokationResult {
		public Object returnValue;
		public Throwable invokationThrowable;
		
		public MethodInvokationResult() {}
		
		public <T> T getResult()
		{
			return (T) returnValue;
		}
	}
	
	static
	{
		registerDefaultSourceFuns();
	}
}
