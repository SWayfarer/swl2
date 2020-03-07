package ru.swayfarer.swl2.exceptions;

import java.lang.reflect.Constructor;

import lombok.SneakyThrows;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.collections.streams.DataStream;
import ru.swayfarer.swl2.markers.Alias;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.StringUtils;

public class ExceptionsUtils {
	
	public static String LAMBDA_GENERATED_METHOD_REGEX = StringUtils.regex()
			.text("lambda$")
			.some()
			.num()
	.build();
	
	public static String LAMBDA_GENERATED_METHOD_REGEX_2 = StringUtils.regex()
			.something()
			.text("$$Lambda$")
			.something()
			.text(".")
			.some()
			.num()
			.text(".")
			.something()
	.build();
	
	/** Если метод вызван не из одного из указанных классов, то будет {@link Exception} */
	@SneakyThrows
	public static void IfCallerNot(Class<?>... callers)
	{
		String callerClass = getStacktraceClassAt(2);
		
		if (!DataStream.of(callers).someMatches((caller) -> caller != null && caller.getName().equals(callerClass)))
		{
			throw createThrowable(IllegalAccessException.class, StringUtils.concatWithSpaces("Class", callerClass, "can't invoke this method! Permission denied!"), 2);
		}
	}
	
	/** Если метод вызван не из своего класса, то будет {@link Exception} */
	@SneakyThrows
	public static void IfCallerNotSelf()
	{
		String caller = getStacktraceClassAt(2);
		
		if (!caller.equals(getStacktraceClassAt(1)))
		{
			throw createThrowable(IllegalAccessException.class, StringUtils.concatWithSpaces("Class", caller, "can't invoke this method! Permission denied!"), 2);
		}
	}
	
	/**
	 * Создать {@link Throwable} с указанным сообщением
	 * @param classOfThrowable Класс исключения, которое будет создано
	 * @param message Сообщение
	 * @param stacktraceOffset Смещение начала стактрейса
	 * @return Созданный {@link Throwable}, Null, если не выйдет создать
	 */
	@InternalElement
	public static <Throwable_Type extends Throwable> Throwable_Type createThrowable(Class<Throwable_Type> classOfThrowable, String message, int stacktraceOffset)
	{
		try
		{
			Constructor<Throwable_Type> constructor = classOfThrowable.getConstructor(String.class);
			Throwable_Type ret =  constructor.newInstance(message);
		
			// Удаляем лишние элементы стактрейса
			
			StackTraceElement[] stacktrace = ret.getStackTrace();
			
			int offset = 0;
			
			// Ищем в стактрейсе этот метод, игнорируя всю рефлексию
			for (StackTraceElement st : stacktrace)
			{
				if (st.getClassName().equals(ExceptionsUtils.class.getName()) && st.getMethodName().equals("createThrowable"))
				{
					break;
				}
				offset ++;
			}
			
			ret.setStackTrace(shiftStacktrace(stacktrace, offset + stacktraceOffset).toArray(StackTraceElement.class));
			
			return ret;
		}
		catch (NoSuchMethodException e)
		{
			System.err.println("[ExceptionsUtils]: Can't create exception of type " + classOfThrowable.getCanonicalName() + ". Returning null...");
			e.printStackTrace();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Удалить несколько первыъ элементов стакстрейса 
	 * @param stacktrace Исходный стактрейс
	 * @param stacktraceOffset Смещение стактрейса
	 * @return Смещенный стакрейс
	 */
	public static  IExtendedList<StackTraceElement> shiftStacktrace(StackTraceElement[] stacktrace, int stacktraceOffset)
	{
		StackTraceElement[] newStackTrace = new StackTraceElement[stacktrace.length - stacktraceOffset];
		System.arraycopy(stacktrace, stacktrace.length - newStackTrace.length, newStackTrace, 0, newStackTrace.length);
		
		return CollectionsSWL.createExtendedList(newStackTrace);
	}
	
	/**
	 * Получить актуальный стактрейс
	 * @param stacktraceStartOffset - Смещение начала стактейса. (Столько элементов будет из него удалено)
	 * @return Актуальный стактейс с указанным смещением
	 */
	public static IExtendedList<StackTraceElement> getThreadStacktrace(int stacktraceStartOffset)
	{
		return shiftStacktrace(new Throwable().getStackTrace(), 1 + stacktraceStartOffset);
	}
	
	/**
	 * Получить имя класса в текущем стактрейсе под указанным номером
	 * @param classIndex номер класса
	 * @return Имя класса
	 */
	public static String getStacktraceClassAt(int classIndex)
	{
		IExtendedList<StackTraceElement> stackTraceElements = getThreadStacktrace(StacktraceOffsets.OFFSET_CALLER);
		
		return stackTraceElements.get(classIndex).getClassName();
	}
	
	/** Стактрейс источника вызова */
	@Alias("getCallerStacktrace")
	public static StackTraceElement caller()
	{
		return getThreadStacktrace(StacktraceOffsets.OFFSET_CALLER + 1).getFirstElement();
	}
	
	/** Стактрейс источника вызова */
	public static StackTraceElement getCallerStacktrace()
	{
		return getThreadStacktrace(StacktraceOffsets.OFFSET_CALLER + 1).getFirstElement();
	}
	
	/**
	 * Получить имя (без пакта) класса в текущем стактрейсе под указанным номером
	 * @param classIndex номер класса
	 * @return Имя класса
	 */
	public static String getSimpleClassAt(int classIndex)
	{
		String name = getStacktraceClassAt(classIndex + 1);
		name = getClassSimpleName(name);
		return name;
	}
	
	/**
	 * Получить имя (без пакта) класса в текущем стактрейсе под указанным номером
	 * @param classIndex номер класса
	 * @return Имя класса
	 */
	public static String getPackageAt(int classIndex)
	{
		String name = getStacktraceClassAt(classIndex + 1);
		name = getClassPackage(name);
		return name;
	}
	
	/** Очистить от лямбд в генерированных классах */
	public static IExtendedList<StackTraceElement> cleanFromGeneratedLambdas(IExtendedList<StackTraceElement> stackTraceElements)
	{
		return stackTraceElements.dataStream()
				.filter((st) -> !st.getClassName().contains("$$Lambda$"))
				.toList();
	}
	
	/** Получить простое имя класса из каноничного */
	public static String getClassSimpleName(String canonicalName)
	{
		if (canonicalName.contains(".") && !canonicalName.endsWith("."))
		{
			canonicalName = canonicalName.substring(canonicalName.lastIndexOf(".") + 1);
		}
		
		return canonicalName;
	}
	
	/** Получить простое имя класса из каноничного */
	public static String getClassPackage(String canonicalName)
	{
		if (canonicalName.contains(".") && !canonicalName.endsWith("."))
		{
			canonicalName = canonicalName.substring(0, canonicalName.lastIndexOf("."));
		}
		
		return canonicalName;
	}
	
	/**
	 * Бросить исключение, если выполняется условие
	 * @param condition Условие
	 * @param classOfThrowable Класс создаваемого исключения
 	 * @param message Сообщение исключения
	 */
	@SneakyThrows
	public static void If(boolean condition, Class<? extends Throwable> classOfThrowable, @ConcattedString Object... message)
	{
		if (condition)
			throw createThrowable(classOfThrowable, StringUtils.concatWithSpaces(message), 2);
	}
	
	/**
	 * Бросить исключение, если проверяемый объект null
	 * @param object Проверяемый объект
	 * @param classOfThrowable Класс создаваемого исключения
 	 * @param message Сообщение исключения
	 */
	@SneakyThrows
	public static void IfNull(Object object, Class<? extends Throwable> classOfThrowable, @ConcattedString Object... message)
	{
		if (object == null)
			throw createThrowable(classOfThrowable, StringUtils.concatWithSpaces(message), 2);
	}
	
	/**
	 * Бросить исключение, если проверяемый объект null
	 * @param object Проверяемый объект
 	 * @param message Сообщение исключения
	 */
	@SneakyThrows
	public static void IfNullArg(Object object, @ConcattedString Object... message)
	{
		if (object == null)
			throw createThrowable(IllegalArgumentException.class, StringUtils.concatWithSpaces(message), 2);
	}
	
	/**
	 * Бросить исключение, если проверяемая строка пуста
	 * @param str Проверяемая строка
	 * @param classOfThrowable Класс создаваемого исключения
 	 * @param message Сообщение исключения
	 */
	@SneakyThrows
	public static void IfEmpty(@ConcattedString Object[] str, Class<? extends Throwable> classOfThrowable, @ConcattedString Object... message)
	{
		if (StringUtils.isEmpty(str))
			throw createThrowable(classOfThrowable, StringUtils.concatWithSpaces(message), 2);
	}
	
	/**
	 * Бросить исключение, если проверяемая строка пуста
	 * @param str Проверяемая строка
	 * @param classOfThrowable Класс создаваемого исключения
 	 * @param message Сообщение исключения
	 */
	@SneakyThrows
	public static void IfEmpty(String str, Class<? extends Throwable> classOfThrowable, @ConcattedString Object... message)
	{
		if (StringUtils.isEmpty(str))
			throw createThrowable(classOfThrowable, StringUtils.concatWithSpaces(message), 2);
	}
	
	/**
	 * Бросить исключение, если проверяемый объект не null
	 * @param object Проверяемый объект
	 * @param classOfThrowable Класс создаваемого исключения
 	 * @param message Сообщение исключения
	 */
	@SneakyThrows
	public static void IfNotNull(Object object, Class<? extends Throwable> classOfThrowable, @ConcattedString Object... message)
	{
		if (object != null)
			throw createThrowable(classOfThrowable, StringUtils.concatWithSpaces(message), 2);
	}
	
	/**
	 * Бросить исключение, если не выполняется условие
	 * @param condition Условие
	 * @param classOfThrowable Класс создаваемого исключения
 	 * @param message Сообщение исключения
	 */
	@SneakyThrows
	public static void IfNot(boolean condition, Class<? extends Throwable> classOfThrowable, @ConcattedString Object... message)
	{
		if (!condition)
			throw createThrowable(classOfThrowable, StringUtils.concatWithSpaces(message), 2);
	}
	
	/**
	 * Безопасно выполнить действие
	 * @return Исключение, если появится 
	 */
	public static Throwable safe(IUnsafeRunnable r)
	{
		try
		{
			r.run();
		}
		catch (Throwable e)
		{
			return e;
		}
		
		return null;
	}
	
	public static interface StacktraceOffsets {

		/** Используйте этот оффсет, чтобы получить стакстрейс текущего метода*/
		public static final int OFFSET_CURRENT = 0;
		
		/** Используйте этот оффсет, чтобы получить стакстрейс предыдущего (вызывающего) метода */
		public static final int OFFSET_CALLER = 1;
	}
	
}
