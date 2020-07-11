package ru.swayfarer.swl2.functions;

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Функции для удобства использования функционального подхода 
 * <h1> Автоматически сгенерировано с помощью ru.swayfarer.swl2.functions.generator.FunctionsGenerator </h1>
 * @author swayfarer
 * */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class GeneratedFunctions {
@FunctionalInterface
public static interface IFunction0<Ret_Type> {
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type __()
	{
		return apply();
	}
	
	public default IFunction0<Ret_Type> andApply(IFunction1<Ret_Type, Ret_Type> fun)
	{
		return () -> fun.apply(apply());
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type $()
	{
		return apply();
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type process()
	{
		return apply();
	}
	
	public default IFunction0<Boolean> and(IFunction0<Boolean> fun)
	{
		IFunction0<Boolean> thisFun = (IFunction0<Boolean>)((Object) this);
		return () -> thisFun.apply() && fun.apply();
	}
	
	public default IFunction0<Boolean> or(IFunction0<Boolean> fun)
	{
		IFunction0<Boolean> thisFun = (IFunction0<Boolean>)((Object) this);
		return () -> thisFun.apply() || fun.apply();
	}
	
	/**
	 * Выполнить функцию, запомнить результат и возвращать всегда его
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction0<Ret_Type> memorize()
	{
		Ret_Type ret = apply();
	
		return () -> ret;
	}
	
	/**
	 * При первом выполнении функция запомнит результат и будет всегда его возвращать
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction0<Ret_Type> memorized()
	{
		IFunction0<Ret_Type> fun = this;
	
		return new IFunction0<Ret_Type> () 
		{
			public AtomicBoolean isProcessed = new AtomicBoolean();
			public volatile Ret_Type retValue;
		
			public Ret_Type apply()
			{
				if (!isProcessed.get())
				{
					retValue = fun.apply();
					isProcessed.set(true);
				}
				
				return retValue;
			}
		};
	}
	
	/**
	 * При первом выполнении функция запомнит результат и будет всегда его возвращать
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction0<Ret_Type> threadLocal()
	{
		IFunction0<Ret_Type> fun = this;
	
		return new IFunction0<Ret_Type> () 
		{
			public volatile ThreadLocal<Boolean> isPassed = new ThreadLocal<Boolean>();
			public volatile ThreadLocal<Ret_Type> threadLocal = new ThreadLocal<Ret_Type>();
		
			public boolean isPassed()
			{
				return isPassed.get() == Boolean.TRUE;
			}
			
			public Ret_Type apply()
			{
				if (isPassed())
				{
					return threadLocal.get();
				}
				
				Ret_Type ret = fun.apply();
				threadLocal.set(ret);
				isPassed.set(true);
				return ret;
			}
		};
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public Ret_Type apply();
	
	/**
	 * Добавить 'post-функцию' функцию
	 * 
	 * @param nextFun
	 *            Функция, которая будет выполнена после этой. Цепочка
	 *            вернет результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction0<Ret_Type> andAfter(IFunction0<Ret_Type> fun)
	{
		return () -> {
			apply();
			return fun.apply();
		};
	}
	
	public default IFunction0<Ret_Type> andThan(IFunction0<Ret_Type> fun)
	{
		return andAfter(fun);
	}
	
	/**
	 * Добавить 'pre-функцию' функцию
	 * 
	 * @param fun
	 *            Функция, которая будет выполнена до этой. Цепочка вернет
	 *            результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction0<Ret_Type> andBefore(IFunction0<Ret_Type> fun)
	{
		return () -> {
			fun.apply();
			return apply();
		};
	}
	
	/**
	 * Получить функцию с кэшированным результатом
	 * @return Функция с кэшированными результатами
	 */
	public default IFunction0<Ret_Type> cached()
	{
		Ret_Type ret = apply();
		return () -> ret;
	}
	
	/**
	 * Получить функцию без возвращаемого типа
	 * 
	 * @return Новую фунцию
	 */
	public default IFunction0NoR asNoReturn()
	{
		return () -> apply();
	}
	
	
	
	
	/** Получить стандартный Java-{@link Supplier}*/
	public default Supplier<Ret_Type> asJavaSupplier()
	{
		return () -> apply();
	}
	
}

/**
 * Версия функции IFunction0 без возвращаемого типа (NoR = No return)
 * 
 * @author swayfarer
 */
 @FunctionalInterface
public static interface IFunction0NoR extends IFunction0<Void> {
	
	@Override
	public default Void apply()
	{
		applyNoR();
		return null;
	}
	
	public void applyNoR();
	
	/**
	 * Добавить 'post-функцию' функцию
	 * 
	 * @param nextFun
	 *            Функция, которая будет выполнена после этой. Цепочка
	 *            вернет результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction0NoR andAfter(IFunction0NoR fun)
	{
		return () -> {
			apply();
			fun.apply();
		};
	}
	
	public default IFunction0NoR andThan(IFunction0NoR fun)
	{
		return andAfter(fun);
	}
	
	/**
	 * Добавить 'pre-функцию' функцию
	 * 
	 * @param fun
	 *            Функция, которая будет выполнена до этой. Цепочка вернет
	 *            результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction0NoR andBefore(IFunction0NoR fun)
	{
		return () -> {
			fun.apply();
			apply();
		};
	}
	
		
	/**
	 * Получить старндартный Java-{@link Runnable}
	 * 
	 * @return Новый {@link Runnable}
	 */
	public default Runnable asJavaRunnable()
	{
		return () -> apply();
	}
		
}


@FunctionalInterface
public static interface IFunction1<Arg1, Ret_Type> {
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type __(Arg1 arg1)
	{
		return apply(arg1);
	}
	
	public default IFunction1<Arg1, Ret_Type> andApply(IFunction1<Ret_Type, Ret_Type> fun)
	{
		return (arg1) -> fun.apply(apply(arg1));
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type $(Arg1 arg1)
	{
		return apply(arg1);
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type process(Arg1 arg1)
	{
		return apply(arg1);
	}
	
	public default IFunction1<Arg1, Boolean> and(IFunction1<Arg1, Boolean> fun)
	{
		IFunction1<Arg1, Boolean> thisFun = (IFunction1<Arg1, Boolean>)((Object) this);
		return (a1) -> thisFun.apply(a1) && fun.apply(a1);
	}
	
	public default IFunction1<Arg1, Boolean> or(IFunction1<Arg1, Boolean> fun)
	{
		IFunction1<Arg1, Boolean> thisFun = (IFunction1<Arg1, Boolean>)((Object) this);
		return (a1) -> thisFun.apply(a1) || fun.apply(a1);
	}
	
	/**
	 * Выполнить функцию, запомнить результат и возвращать всегда его
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction1<Arg1, Ret_Type> memorize(Arg1 arg1)
	{
		Ret_Type ret = apply(arg1);
	
		return (a1) -> ret;
	}
	
	/**
	 * При первом выполнении функция запомнит результат и будет всегда его возвращать
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction1<Arg1, Ret_Type> memorized()
	{
		IFunction1<Arg1, Ret_Type> fun = this;
	
		return new IFunction1<Arg1, Ret_Type> () 
		{
			public AtomicBoolean isProcessed = new AtomicBoolean();
			public volatile Ret_Type retValue;
		
			public Ret_Type apply(Arg1 arg1)
			{
				if (!isProcessed.get())
				{
					retValue = fun.apply(arg1);
					isProcessed.set(true);
				}
				
				return retValue;
			}
		};
	}
	
	/**
	 * При первом выполнении функция запомнит результат и будет всегда его возвращать
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction1<Arg1, Ret_Type> threadLocal()
	{
		IFunction1<Arg1, Ret_Type> fun = this;
	
		return new IFunction1<Arg1, Ret_Type> () 
		{
			public volatile ThreadLocal<Boolean> isPassed = new ThreadLocal<Boolean>();
			public volatile ThreadLocal<Ret_Type> threadLocal = new ThreadLocal<Ret_Type>();
		
			public boolean isPassed()
			{
				return isPassed.get() == Boolean.TRUE;
			}
			
			public Ret_Type apply(Arg1 arg1)
			{
				if (isPassed())
				{
					return threadLocal.get();
				}
				
				Ret_Type ret = fun.apply(arg1);
				threadLocal.set(ret);
				isPassed.set(true);
				return ret;
			}
		};
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public Ret_Type apply(Arg1 arg1);
	
	/**
	 * Добавить 'post-функцию' функцию
	 * 
	 * @param nextFun
	 *            Функция, которая будет выполнена после этой. Цепочка
	 *            вернет результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction1<Arg1, Ret_Type> andAfter(IFunction1<Arg1, Ret_Type> fun)
	{
		return (arg1) -> {
			apply(arg1);
			return fun.apply(arg1);
		};
	}
	
	public default IFunction1<Arg1, Ret_Type> andThan(IFunction1<Arg1, Ret_Type> fun)
	{
		return andAfter(fun);
	}
	
	/**
	 * Добавить 'pre-функцию' функцию
	 * 
	 * @param fun
	 *            Функция, которая будет выполнена до этой. Цепочка вернет
	 *            результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction1<Arg1, Ret_Type> andBefore(IFunction1<Arg1, Ret_Type> fun)
	{
		return (arg1) -> {
			fun.apply(arg1);
			return apply(arg1);
		};
	}
	
	/**
	 * Получить функцию с кэшированным результатом
	 * @return Функция с кэшированными результатами
	 */
	public default IFunction1<Arg1, Ret_Type> cached(Arg1 arg1)
	{
		Ret_Type ret = apply(arg1);
		return (a1) -> ret;
	}
	
	/**
	 * Получить функцию без возвращаемого типа
	 * 
	 * @return Новую фунцию
	 */
	public default IFunction1NoR<Arg1> asNoReturn()
	{
		return (arg1) -> apply(arg1);
	}
	
	
	
	/**
	 * Получить старнадрный Java-{@link Predicate}
	 * 
	 * @return Возвращает новый Java-{@link Predicate}
	 */
	public default Predicate<Arg1> asJavaPredicate()
	{
		return new Predicate<Arg1>()
		{
			@Override
			public boolean test(Arg1 t)
			{
				Object ret = apply(t);

				if (ret == null)
					throw new IllegalArgumentException("The method asJavaPredicate() is available only for functions that return a no-null value!");

				if (ret == null || !(ret instanceof Boolean))
					throw new IllegalArgumentException("The method asJavaPredicate() is available only for functions that return a boolean value!");

				return (Boolean) ret;
			}
		};
	}
	
	/**
	 * Получить стандартную Java-{@link Function}
	 * 
	 * @return Возвращает новую {@link Function}
	 */
	public default Function<Arg1, Ret_Type> asJavaFunction()
	{
		return (arg1) -> apply(arg1);
	}
	
	/**
	 * Получить стандартный Java-{@link Consumer}
	 * 
	 * @return Возвращает новую функцию
	 */
	public default Consumer<Arg1> asJavaConsumer()
	{
		return (arg1) -> apply(arg1);
	}
	
	
}

/**
 * Версия функции IFunction1 без возвращаемого типа (NoR = No return)
 * 
 * @author swayfarer
 */
 @FunctionalInterface
public static interface IFunction1NoR<Arg1> extends IFunction1<Arg1, Void> {
	
	@Override
	public default Void apply(Arg1 arg1)
	{
		applyNoR(arg1);
		return null;
	}
	
	public void applyNoR(Arg1 arg1);
	
	/**
	 * Добавить 'post-функцию' функцию
	 * 
	 * @param nextFun
	 *            Функция, которая будет выполнена после этой. Цепочка
	 *            вернет результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction1NoR<Arg1> andAfter(IFunction1NoR<Arg1> fun)
	{
		return (arg1) -> {
			apply(arg1);
			fun.apply(arg1);
		};
	}
	
	public default IFunction1NoR<Arg1> andThan(IFunction1NoR<Arg1> fun)
	{
		return andAfter(fun);
	}
	
	/**
	 * Добавить 'pre-функцию' функцию
	 * 
	 * @param fun
	 *            Функция, которая будет выполнена до этой. Цепочка вернет
	 *            результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction1NoR<Arg1> andBefore(IFunction1NoR<Arg1> fun)
	{
		return (arg1) -> {
			fun.apply(arg1);
			apply(arg1);
		};
	}
	
}


@FunctionalInterface
public static interface IFunction2<Arg1, Arg2, Ret_Type> {
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type __(Arg1 arg1, Arg2 arg2)
	{
		return apply(arg1, arg2);
	}
	
	public default IFunction2<Arg1, Arg2, Ret_Type> andApply(IFunction1<Ret_Type, Ret_Type> fun)
	{
		return (arg1, arg2) -> fun.apply(apply(arg1, arg2));
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type $(Arg1 arg1, Arg2 arg2)
	{
		return apply(arg1, arg2);
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type process(Arg1 arg1, Arg2 arg2)
	{
		return apply(arg1, arg2);
	}
	
	public default IFunction2<Arg1, Arg2, Boolean> and(IFunction2<Arg1, Arg2, Boolean> fun)
	{
		IFunction2<Arg1, Arg2, Boolean> thisFun = (IFunction2<Arg1, Arg2, Boolean>)((Object) this);
		return (a1, a2) -> thisFun.apply(a1, a2) && fun.apply(a1, a2);
	}
	
	public default IFunction2<Arg1, Arg2, Boolean> or(IFunction2<Arg1, Arg2, Boolean> fun)
	{
		IFunction2<Arg1, Arg2, Boolean> thisFun = (IFunction2<Arg1, Arg2, Boolean>)((Object) this);
		return (a1, a2) -> thisFun.apply(a1, a2) || fun.apply(a1, a2);
	}
	
	/**
	 * Выполнить функцию, запомнить результат и возвращать всегда его
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction2<Arg1, Arg2, Ret_Type> memorize(Arg1 arg1, Arg2 arg2)
	{
		Ret_Type ret = apply(arg1, arg2);
	
		return (a1, a2) -> ret;
	}
	
	/**
	 * При первом выполнении функция запомнит результат и будет всегда его возвращать
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction2<Arg1, Arg2, Ret_Type> memorized()
	{
		IFunction2<Arg1, Arg2, Ret_Type> fun = this;
	
		return new IFunction2<Arg1, Arg2, Ret_Type> () 
		{
			public AtomicBoolean isProcessed = new AtomicBoolean();
			public volatile Ret_Type retValue;
		
			public Ret_Type apply(Arg1 arg1, Arg2 arg2)
			{
				if (!isProcessed.get())
				{
					retValue = fun.apply(arg1, arg2);
					isProcessed.set(true);
				}
				
				return retValue;
			}
		};
	}
	
	/**
	 * При первом выполнении функция запомнит результат и будет всегда его возвращать
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction2<Arg1, Arg2, Ret_Type> threadLocal()
	{
		IFunction2<Arg1, Arg2, Ret_Type> fun = this;
	
		return new IFunction2<Arg1, Arg2, Ret_Type> () 
		{
			public volatile ThreadLocal<Boolean> isPassed = new ThreadLocal<Boolean>();
			public volatile ThreadLocal<Ret_Type> threadLocal = new ThreadLocal<Ret_Type>();
		
			public boolean isPassed()
			{
				return isPassed.get() == Boolean.TRUE;
			}
			
			public Ret_Type apply(Arg1 arg1, Arg2 arg2)
			{
				if (isPassed())
				{
					return threadLocal.get();
				}
				
				Ret_Type ret = fun.apply(arg1, arg2);
				threadLocal.set(ret);
				isPassed.set(true);
				return ret;
			}
		};
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public Ret_Type apply(Arg1 arg1, Arg2 arg2);
	
	/**
	 * Добавить 'post-функцию' функцию
	 * 
	 * @param nextFun
	 *            Функция, которая будет выполнена после этой. Цепочка
	 *            вернет результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction2<Arg1, Arg2, Ret_Type> andAfter(IFunction2<Arg1, Arg2, Ret_Type> fun)
	{
		return (arg1, arg2) -> {
			apply(arg1, arg2);
			return fun.apply(arg1, arg2);
		};
	}
	
	public default IFunction2<Arg1, Arg2, Ret_Type> andThan(IFunction2<Arg1, Arg2, Ret_Type> fun)
	{
		return andAfter(fun);
	}
	
	/**
	 * Добавить 'pre-функцию' функцию
	 * 
	 * @param fun
	 *            Функция, которая будет выполнена до этой. Цепочка вернет
	 *            результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction2<Arg1, Arg2, Ret_Type> andBefore(IFunction2<Arg1, Arg2, Ret_Type> fun)
	{
		return (arg1, arg2) -> {
			fun.apply(arg1, arg2);
			return apply(arg1, arg2);
		};
	}
	
	/**
	 * Получить функцию с кэшированным результатом
	 * @return Функция с кэшированными результатами
	 */
	public default IFunction2<Arg1, Arg2, Ret_Type> cached(Arg1 arg1, Arg2 arg2)
	{
		Ret_Type ret = apply(arg1, arg2);
		return (a1, a2) -> ret;
	}
	
	/**
	 * Получить функцию без возвращаемого типа
	 * 
	 * @return Новую фунцию
	 */
	public default IFunction2NoR<Arg1, Arg2> asNoReturn()
	{
		return (arg1, arg2) -> apply(arg1, arg2);
	}
	
	
	/**
	 * Получить стандартный Java-{@link Comparator}
	 * 
	 * @return Возвращает новую функцию
	 */
	public default Comparator asJavaComparator()
	{

		return new Comparator()
		{
			@Override
			public int compare(Object arg1, Object arg2)
			{
				return (Integer) apply((Arg1) arg1, (Arg2) arg2);
			}
		};
	}
	
	
	
}

/**
 * Версия функции IFunction2 без возвращаемого типа (NoR = No return)
 * 
 * @author swayfarer
 */
 @FunctionalInterface
public static interface IFunction2NoR<Arg1, Arg2> extends IFunction2<Arg1, Arg2, Void> {
	
	@Override
	public default Void apply(Arg1 arg1, Arg2 arg2)
	{
		applyNoR(arg1, arg2);
		return null;
	}
	
	public void applyNoR(Arg1 arg1, Arg2 arg2);
	
	/**
	 * Добавить 'post-функцию' функцию
	 * 
	 * @param nextFun
	 *            Функция, которая будет выполнена после этой. Цепочка
	 *            вернет результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction2NoR<Arg1, Arg2> andAfter(IFunction2NoR<Arg1, Arg2> fun)
	{
		return (arg1, arg2) -> {
			apply(arg1, arg2);
			fun.apply(arg1, arg2);
		};
	}
	
	public default IFunction2NoR<Arg1, Arg2> andThan(IFunction2NoR<Arg1, Arg2> fun)
	{
		return andAfter(fun);
	}
	
	/**
	 * Добавить 'pre-функцию' функцию
	 * 
	 * @param fun
	 *            Функция, которая будет выполнена до этой. Цепочка вернет
	 *            результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction2NoR<Arg1, Arg2> andBefore(IFunction2NoR<Arg1, Arg2> fun)
	{
		return (arg1, arg2) -> {
			fun.apply(arg1, arg2);
			apply(arg1, arg2);
		};
	}
	
}


@FunctionalInterface
public static interface IFunction3<Arg1, Arg2, Arg3, Ret_Type> {
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type __(Arg1 arg1, Arg2 arg2, Arg3 arg3)
	{
		return apply(arg1, arg2, arg3);
	}
	
	public default IFunction3<Arg1, Arg2, Arg3, Ret_Type> andApply(IFunction1<Ret_Type, Ret_Type> fun)
	{
		return (arg1, arg2, arg3) -> fun.apply(apply(arg1, arg2, arg3));
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type $(Arg1 arg1, Arg2 arg2, Arg3 arg3)
	{
		return apply(arg1, arg2, arg3);
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3)
	{
		return apply(arg1, arg2, arg3);
	}
	
	public default IFunction3<Arg1, Arg2, Arg3, Boolean> and(IFunction3<Arg1, Arg2, Arg3, Boolean> fun)
	{
		IFunction3<Arg1, Arg2, Arg3, Boolean> thisFun = (IFunction3<Arg1, Arg2, Arg3, Boolean>)((Object) this);
		return (a1, a2, a3) -> thisFun.apply(a1, a2, a3) && fun.apply(a1, a2, a3);
	}
	
	public default IFunction3<Arg1, Arg2, Arg3, Boolean> or(IFunction3<Arg1, Arg2, Arg3, Boolean> fun)
	{
		IFunction3<Arg1, Arg2, Arg3, Boolean> thisFun = (IFunction3<Arg1, Arg2, Arg3, Boolean>)((Object) this);
		return (a1, a2, a3) -> thisFun.apply(a1, a2, a3) || fun.apply(a1, a2, a3);
	}
	
	/**
	 * Выполнить функцию, запомнить результат и возвращать всегда его
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction3<Arg1, Arg2, Arg3, Ret_Type> memorize(Arg1 arg1, Arg2 arg2, Arg3 arg3)
	{
		Ret_Type ret = apply(arg1, arg2, arg3);
	
		return (a1, a2, a3) -> ret;
	}
	
	/**
	 * При первом выполнении функция запомнит результат и будет всегда его возвращать
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction3<Arg1, Arg2, Arg3, Ret_Type> memorized()
	{
		IFunction3<Arg1, Arg2, Arg3, Ret_Type> fun = this;
	
		return new IFunction3<Arg1, Arg2, Arg3, Ret_Type> () 
		{
			public AtomicBoolean isProcessed = new AtomicBoolean();
			public volatile Ret_Type retValue;
		
			public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3)
			{
				if (!isProcessed.get())
				{
					retValue = fun.apply(arg1, arg2, arg3);
					isProcessed.set(true);
				}
				
				return retValue;
			}
		};
	}
	
	/**
	 * При первом выполнении функция запомнит результат и будет всегда его возвращать
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction3<Arg1, Arg2, Arg3, Ret_Type> threadLocal()
	{
		IFunction3<Arg1, Arg2, Arg3, Ret_Type> fun = this;
	
		return new IFunction3<Arg1, Arg2, Arg3, Ret_Type> () 
		{
			public volatile ThreadLocal<Boolean> isPassed = new ThreadLocal<Boolean>();
			public volatile ThreadLocal<Ret_Type> threadLocal = new ThreadLocal<Ret_Type>();
		
			public boolean isPassed()
			{
				return isPassed.get() == Boolean.TRUE;
			}
			
			public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3)
			{
				if (isPassed())
				{
					return threadLocal.get();
				}
				
				Ret_Type ret = fun.apply(arg1, arg2, arg3);
				threadLocal.set(ret);
				isPassed.set(true);
				return ret;
			}
		};
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3);
	
	/**
	 * Добавить 'post-функцию' функцию
	 * 
	 * @param nextFun
	 *            Функция, которая будет выполнена после этой. Цепочка
	 *            вернет результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction3<Arg1, Arg2, Arg3, Ret_Type> andAfter(IFunction3<Arg1, Arg2, Arg3, Ret_Type> fun)
	{
		return (arg1, arg2, arg3) -> {
			apply(arg1, arg2, arg3);
			return fun.apply(arg1, arg2, arg3);
		};
	}
	
	public default IFunction3<Arg1, Arg2, Arg3, Ret_Type> andThan(IFunction3<Arg1, Arg2, Arg3, Ret_Type> fun)
	{
		return andAfter(fun);
	}
	
	/**
	 * Добавить 'pre-функцию' функцию
	 * 
	 * @param fun
	 *            Функция, которая будет выполнена до этой. Цепочка вернет
	 *            результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction3<Arg1, Arg2, Arg3, Ret_Type> andBefore(IFunction3<Arg1, Arg2, Arg3, Ret_Type> fun)
	{
		return (arg1, arg2, arg3) -> {
			fun.apply(arg1, arg2, arg3);
			return apply(arg1, arg2, arg3);
		};
	}
	
	/**
	 * Получить функцию с кэшированным результатом
	 * @return Функция с кэшированными результатами
	 */
	public default IFunction3<Arg1, Arg2, Arg3, Ret_Type> cached(Arg1 arg1, Arg2 arg2, Arg3 arg3)
	{
		Ret_Type ret = apply(arg1, arg2, arg3);
		return (a1, a2, a3) -> ret;
	}
	
	/**
	 * Получить функцию без возвращаемого типа
	 * 
	 * @return Новую фунцию
	 */
	public default IFunction3NoR<Arg1, Arg2, Arg3> asNoReturn()
	{
		return (arg1, arg2, arg3) -> apply(arg1, arg2, arg3);
	}
	
	
	
}

/**
 * Версия функции IFunction3 без возвращаемого типа (NoR = No return)
 * 
 * @author swayfarer
 */
 @FunctionalInterface
public static interface IFunction3NoR<Arg1, Arg2, Arg3> extends IFunction3<Arg1, Arg2, Arg3, Void> {
	
	@Override
	public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3)
	{
		applyNoR(arg1, arg2, arg3);
		return null;
	}
	
	public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3);
	
	/**
	 * Добавить 'post-функцию' функцию
	 * 
	 * @param nextFun
	 *            Функция, которая будет выполнена после этой. Цепочка
	 *            вернет результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction3NoR<Arg1, Arg2, Arg3> andAfter(IFunction3NoR<Arg1, Arg2, Arg3> fun)
	{
		return (arg1, arg2, arg3) -> {
			apply(arg1, arg2, arg3);
			fun.apply(arg1, arg2, arg3);
		};
	}
	
	public default IFunction3NoR<Arg1, Arg2, Arg3> andThan(IFunction3NoR<Arg1, Arg2, Arg3> fun)
	{
		return andAfter(fun);
	}
	
	/**
	 * Добавить 'pre-функцию' функцию
	 * 
	 * @param fun
	 *            Функция, которая будет выполнена до этой. Цепочка вернет
	 *            результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction3NoR<Arg1, Arg2, Arg3> andBefore(IFunction3NoR<Arg1, Arg2, Arg3> fun)
	{
		return (arg1, arg2, arg3) -> {
			fun.apply(arg1, arg2, arg3);
			apply(arg1, arg2, arg3);
		};
	}
	
}


@FunctionalInterface
public static interface IFunction4<Arg1, Arg2, Arg3, Arg4, Ret_Type> {
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type __(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4)
	{
		return apply(arg1, arg2, arg3, arg4);
	}
	
	public default IFunction4<Arg1, Arg2, Arg3, Arg4, Ret_Type> andApply(IFunction1<Ret_Type, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4) -> fun.apply(apply(arg1, arg2, arg3, arg4));
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type $(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4)
	{
		return apply(arg1, arg2, arg3, arg4);
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4)
	{
		return apply(arg1, arg2, arg3, arg4);
	}
	
	public default IFunction4<Arg1, Arg2, Arg3, Arg4, Boolean> and(IFunction4<Arg1, Arg2, Arg3, Arg4, Boolean> fun)
	{
		IFunction4<Arg1, Arg2, Arg3, Arg4, Boolean> thisFun = (IFunction4<Arg1, Arg2, Arg3, Arg4, Boolean>)((Object) this);
		return (a1, a2, a3, a4) -> thisFun.apply(a1, a2, a3, a4) && fun.apply(a1, a2, a3, a4);
	}
	
	public default IFunction4<Arg1, Arg2, Arg3, Arg4, Boolean> or(IFunction4<Arg1, Arg2, Arg3, Arg4, Boolean> fun)
	{
		IFunction4<Arg1, Arg2, Arg3, Arg4, Boolean> thisFun = (IFunction4<Arg1, Arg2, Arg3, Arg4, Boolean>)((Object) this);
		return (a1, a2, a3, a4) -> thisFun.apply(a1, a2, a3, a4) || fun.apply(a1, a2, a3, a4);
	}
	
	/**
	 * Выполнить функцию, запомнить результат и возвращать всегда его
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction4<Arg1, Arg2, Arg3, Arg4, Ret_Type> memorize(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4)
	{
		Ret_Type ret = apply(arg1, arg2, arg3, arg4);
	
		return (a1, a2, a3, a4) -> ret;
	}
	
	/**
	 * При первом выполнении функция запомнит результат и будет всегда его возвращать
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction4<Arg1, Arg2, Arg3, Arg4, Ret_Type> memorized()
	{
		IFunction4<Arg1, Arg2, Arg3, Arg4, Ret_Type> fun = this;
	
		return new IFunction4<Arg1, Arg2, Arg3, Arg4, Ret_Type> () 
		{
			public AtomicBoolean isProcessed = new AtomicBoolean();
			public volatile Ret_Type retValue;
		
			public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4)
			{
				if (!isProcessed.get())
				{
					retValue = fun.apply(arg1, arg2, arg3, arg4);
					isProcessed.set(true);
				}
				
				return retValue;
			}
		};
	}
	
	/**
	 * При первом выполнении функция запомнит результат и будет всегда его возвращать
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction4<Arg1, Arg2, Arg3, Arg4, Ret_Type> threadLocal()
	{
		IFunction4<Arg1, Arg2, Arg3, Arg4, Ret_Type> fun = this;
	
		return new IFunction4<Arg1, Arg2, Arg3, Arg4, Ret_Type> () 
		{
			public volatile ThreadLocal<Boolean> isPassed = new ThreadLocal<Boolean>();
			public volatile ThreadLocal<Ret_Type> threadLocal = new ThreadLocal<Ret_Type>();
		
			public boolean isPassed()
			{
				return isPassed.get() == Boolean.TRUE;
			}
			
			public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4)
			{
				if (isPassed())
				{
					return threadLocal.get();
				}
				
				Ret_Type ret = fun.apply(arg1, arg2, arg3, arg4);
				threadLocal.set(ret);
				isPassed.set(true);
				return ret;
			}
		};
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4);
	
	/**
	 * Добавить 'post-функцию' функцию
	 * 
	 * @param nextFun
	 *            Функция, которая будет выполнена после этой. Цепочка
	 *            вернет результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction4<Arg1, Arg2, Arg3, Arg4, Ret_Type> andAfter(IFunction4<Arg1, Arg2, Arg3, Arg4, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4) -> {
			apply(arg1, arg2, arg3, arg4);
			return fun.apply(arg1, arg2, arg3, arg4);
		};
	}
	
	public default IFunction4<Arg1, Arg2, Arg3, Arg4, Ret_Type> andThan(IFunction4<Arg1, Arg2, Arg3, Arg4, Ret_Type> fun)
	{
		return andAfter(fun);
	}
	
	/**
	 * Добавить 'pre-функцию' функцию
	 * 
	 * @param fun
	 *            Функция, которая будет выполнена до этой. Цепочка вернет
	 *            результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction4<Arg1, Arg2, Arg3, Arg4, Ret_Type> andBefore(IFunction4<Arg1, Arg2, Arg3, Arg4, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4) -> {
			fun.apply(arg1, arg2, arg3, arg4);
			return apply(arg1, arg2, arg3, arg4);
		};
	}
	
	/**
	 * Получить функцию с кэшированным результатом
	 * @return Функция с кэшированными результатами
	 */
	public default IFunction4<Arg1, Arg2, Arg3, Arg4, Ret_Type> cached(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4)
	{
		Ret_Type ret = apply(arg1, arg2, arg3, arg4);
		return (a1, a2, a3, a4) -> ret;
	}
	
	/**
	 * Получить функцию без возвращаемого типа
	 * 
	 * @return Новую фунцию
	 */
	public default IFunction4NoR<Arg1, Arg2, Arg3, Arg4> asNoReturn()
	{
		return (arg1, arg2, arg3, arg4) -> apply(arg1, arg2, arg3, arg4);
	}
	
	
	
}

/**
 * Версия функции IFunction4 без возвращаемого типа (NoR = No return)
 * 
 * @author swayfarer
 */
 @FunctionalInterface
public static interface IFunction4NoR<Arg1, Arg2, Arg3, Arg4> extends IFunction4<Arg1, Arg2, Arg3, Arg4, Void> {
	
	@Override
	public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4)
	{
		applyNoR(arg1, arg2, arg3, arg4);
		return null;
	}
	
	public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4);
	
	/**
	 * Добавить 'post-функцию' функцию
	 * 
	 * @param nextFun
	 *            Функция, которая будет выполнена после этой. Цепочка
	 *            вернет результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction4NoR<Arg1, Arg2, Arg3, Arg4> andAfter(IFunction4NoR<Arg1, Arg2, Arg3, Arg4> fun)
	{
		return (arg1, arg2, arg3, arg4) -> {
			apply(arg1, arg2, arg3, arg4);
			fun.apply(arg1, arg2, arg3, arg4);
		};
	}
	
	public default IFunction4NoR<Arg1, Arg2, Arg3, Arg4> andThan(IFunction4NoR<Arg1, Arg2, Arg3, Arg4> fun)
	{
		return andAfter(fun);
	}
	
	/**
	 * Добавить 'pre-функцию' функцию
	 * 
	 * @param fun
	 *            Функция, которая будет выполнена до этой. Цепочка вернет
	 *            результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction4NoR<Arg1, Arg2, Arg3, Arg4> andBefore(IFunction4NoR<Arg1, Arg2, Arg3, Arg4> fun)
	{
		return (arg1, arg2, arg3, arg4) -> {
			fun.apply(arg1, arg2, arg3, arg4);
			apply(arg1, arg2, arg3, arg4);
		};
	}
	
}


@FunctionalInterface
public static interface IFunction5<Arg1, Arg2, Arg3, Arg4, Arg5, Ret_Type> {
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type __(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5)
	{
		return apply(arg1, arg2, arg3, arg4, arg5);
	}
	
	public default IFunction5<Arg1, Arg2, Arg3, Arg4, Arg5, Ret_Type> andApply(IFunction1<Ret_Type, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5) -> fun.apply(apply(arg1, arg2, arg3, arg4, arg5));
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type $(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5)
	{
		return apply(arg1, arg2, arg3, arg4, arg5);
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5)
	{
		return apply(arg1, arg2, arg3, arg4, arg5);
	}
	
	public default IFunction5<Arg1, Arg2, Arg3, Arg4, Arg5, Boolean> and(IFunction5<Arg1, Arg2, Arg3, Arg4, Arg5, Boolean> fun)
	{
		IFunction5<Arg1, Arg2, Arg3, Arg4, Arg5, Boolean> thisFun = (IFunction5<Arg1, Arg2, Arg3, Arg4, Arg5, Boolean>)((Object) this);
		return (a1, a2, a3, a4, a5) -> thisFun.apply(a1, a2, a3, a4, a5) && fun.apply(a1, a2, a3, a4, a5);
	}
	
	public default IFunction5<Arg1, Arg2, Arg3, Arg4, Arg5, Boolean> or(IFunction5<Arg1, Arg2, Arg3, Arg4, Arg5, Boolean> fun)
	{
		IFunction5<Arg1, Arg2, Arg3, Arg4, Arg5, Boolean> thisFun = (IFunction5<Arg1, Arg2, Arg3, Arg4, Arg5, Boolean>)((Object) this);
		return (a1, a2, a3, a4, a5) -> thisFun.apply(a1, a2, a3, a4, a5) || fun.apply(a1, a2, a3, a4, a5);
	}
	
	/**
	 * Выполнить функцию, запомнить результат и возвращать всегда его
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction5<Arg1, Arg2, Arg3, Arg4, Arg5, Ret_Type> memorize(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5)
	{
		Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5);
	
		return (a1, a2, a3, a4, a5) -> ret;
	}
	
	/**
	 * При первом выполнении функция запомнит результат и будет всегда его возвращать
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction5<Arg1, Arg2, Arg3, Arg4, Arg5, Ret_Type> memorized()
	{
		IFunction5<Arg1, Arg2, Arg3, Arg4, Arg5, Ret_Type> fun = this;
	
		return new IFunction5<Arg1, Arg2, Arg3, Arg4, Arg5, Ret_Type> () 
		{
			public AtomicBoolean isProcessed = new AtomicBoolean();
			public volatile Ret_Type retValue;
		
			public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5)
			{
				if (!isProcessed.get())
				{
					retValue = fun.apply(arg1, arg2, arg3, arg4, arg5);
					isProcessed.set(true);
				}
				
				return retValue;
			}
		};
	}
	
	/**
	 * При первом выполнении функция запомнит результат и будет всегда его возвращать
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction5<Arg1, Arg2, Arg3, Arg4, Arg5, Ret_Type> threadLocal()
	{
		IFunction5<Arg1, Arg2, Arg3, Arg4, Arg5, Ret_Type> fun = this;
	
		return new IFunction5<Arg1, Arg2, Arg3, Arg4, Arg5, Ret_Type> () 
		{
			public volatile ThreadLocal<Boolean> isPassed = new ThreadLocal<Boolean>();
			public volatile ThreadLocal<Ret_Type> threadLocal = new ThreadLocal<Ret_Type>();
		
			public boolean isPassed()
			{
				return isPassed.get() == Boolean.TRUE;
			}
			
			public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5)
			{
				if (isPassed())
				{
					return threadLocal.get();
				}
				
				Ret_Type ret = fun.apply(arg1, arg2, arg3, arg4, arg5);
				threadLocal.set(ret);
				isPassed.set(true);
				return ret;
			}
		};
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5);
	
	/**
	 * Добавить 'post-функцию' функцию
	 * 
	 * @param nextFun
	 *            Функция, которая будет выполнена после этой. Цепочка
	 *            вернет результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction5<Arg1, Arg2, Arg3, Arg4, Arg5, Ret_Type> andAfter(IFunction5<Arg1, Arg2, Arg3, Arg4, Arg5, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5) -> {
			apply(arg1, arg2, arg3, arg4, arg5);
			return fun.apply(arg1, arg2, arg3, arg4, arg5);
		};
	}
	
	public default IFunction5<Arg1, Arg2, Arg3, Arg4, Arg5, Ret_Type> andThan(IFunction5<Arg1, Arg2, Arg3, Arg4, Arg5, Ret_Type> fun)
	{
		return andAfter(fun);
	}
	
	/**
	 * Добавить 'pre-функцию' функцию
	 * 
	 * @param fun
	 *            Функция, которая будет выполнена до этой. Цепочка вернет
	 *            результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction5<Arg1, Arg2, Arg3, Arg4, Arg5, Ret_Type> andBefore(IFunction5<Arg1, Arg2, Arg3, Arg4, Arg5, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5) -> {
			fun.apply(arg1, arg2, arg3, arg4, arg5);
			return apply(arg1, arg2, arg3, arg4, arg5);
		};
	}
	
	/**
	 * Получить функцию с кэшированным результатом
	 * @return Функция с кэшированными результатами
	 */
	public default IFunction5<Arg1, Arg2, Arg3, Arg4, Arg5, Ret_Type> cached(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5)
	{
		Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5);
		return (a1, a2, a3, a4, a5) -> ret;
	}
	
	/**
	 * Получить функцию без возвращаемого типа
	 * 
	 * @return Новую фунцию
	 */
	public default IFunction5NoR<Arg1, Arg2, Arg3, Arg4, Arg5> asNoReturn()
	{
		return (arg1, arg2, arg3, arg4, arg5) -> apply(arg1, arg2, arg3, arg4, arg5);
	}
	
	
	
}

/**
 * Версия функции IFunction5 без возвращаемого типа (NoR = No return)
 * 
 * @author swayfarer
 */
 @FunctionalInterface
public static interface IFunction5NoR<Arg1, Arg2, Arg3, Arg4, Arg5> extends IFunction5<Arg1, Arg2, Arg3, Arg4, Arg5, Void> {
	
	@Override
	public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5)
	{
		applyNoR(arg1, arg2, arg3, arg4, arg5);
		return null;
	}
	
	public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5);
	
	/**
	 * Добавить 'post-функцию' функцию
	 * 
	 * @param nextFun
	 *            Функция, которая будет выполнена после этой. Цепочка
	 *            вернет результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction5NoR<Arg1, Arg2, Arg3, Arg4, Arg5> andAfter(IFunction5NoR<Arg1, Arg2, Arg3, Arg4, Arg5> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5) -> {
			apply(arg1, arg2, arg3, arg4, arg5);
			fun.apply(arg1, arg2, arg3, arg4, arg5);
		};
	}
	
	public default IFunction5NoR<Arg1, Arg2, Arg3, Arg4, Arg5> andThan(IFunction5NoR<Arg1, Arg2, Arg3, Arg4, Arg5> fun)
	{
		return andAfter(fun);
	}
	
	/**
	 * Добавить 'pre-функцию' функцию
	 * 
	 * @param fun
	 *            Функция, которая будет выполнена до этой. Цепочка вернет
	 *            результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction5NoR<Arg1, Arg2, Arg3, Arg4, Arg5> andBefore(IFunction5NoR<Arg1, Arg2, Arg3, Arg4, Arg5> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5) -> {
			fun.apply(arg1, arg2, arg3, arg4, arg5);
			apply(arg1, arg2, arg3, arg4, arg5);
		};
	}
	
}


@FunctionalInterface
public static interface IFunction6<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Ret_Type> {
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type __(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6)
	{
		return apply(arg1, arg2, arg3, arg4, arg5, arg6);
	}
	
	public default IFunction6<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Ret_Type> andApply(IFunction1<Ret_Type, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6) -> fun.apply(apply(arg1, arg2, arg3, arg4, arg5, arg6));
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type $(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6)
	{
		return apply(arg1, arg2, arg3, arg4, arg5, arg6);
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6)
	{
		return apply(arg1, arg2, arg3, arg4, arg5, arg6);
	}
	
	public default IFunction6<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Boolean> and(IFunction6<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Boolean> fun)
	{
		IFunction6<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Boolean> thisFun = (IFunction6<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Boolean>)((Object) this);
		return (a1, a2, a3, a4, a5, a6) -> thisFun.apply(a1, a2, a3, a4, a5, a6) && fun.apply(a1, a2, a3, a4, a5, a6);
	}
	
	public default IFunction6<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Boolean> or(IFunction6<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Boolean> fun)
	{
		IFunction6<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Boolean> thisFun = (IFunction6<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Boolean>)((Object) this);
		return (a1, a2, a3, a4, a5, a6) -> thisFun.apply(a1, a2, a3, a4, a5, a6) || fun.apply(a1, a2, a3, a4, a5, a6);
	}
	
	/**
	 * Выполнить функцию, запомнить результат и возвращать всегда его
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction6<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Ret_Type> memorize(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6)
	{
		Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6);
	
		return (a1, a2, a3, a4, a5, a6) -> ret;
	}
	
	/**
	 * При первом выполнении функция запомнит результат и будет всегда его возвращать
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction6<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Ret_Type> memorized()
	{
		IFunction6<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Ret_Type> fun = this;
	
		return new IFunction6<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Ret_Type> () 
		{
			public AtomicBoolean isProcessed = new AtomicBoolean();
			public volatile Ret_Type retValue;
		
			public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6)
			{
				if (!isProcessed.get())
				{
					retValue = fun.apply(arg1, arg2, arg3, arg4, arg5, arg6);
					isProcessed.set(true);
				}
				
				return retValue;
			}
		};
	}
	
	/**
	 * При первом выполнении функция запомнит результат и будет всегда его возвращать
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction6<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Ret_Type> threadLocal()
	{
		IFunction6<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Ret_Type> fun = this;
	
		return new IFunction6<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Ret_Type> () 
		{
			public volatile ThreadLocal<Boolean> isPassed = new ThreadLocal<Boolean>();
			public volatile ThreadLocal<Ret_Type> threadLocal = new ThreadLocal<Ret_Type>();
		
			public boolean isPassed()
			{
				return isPassed.get() == Boolean.TRUE;
			}
			
			public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6)
			{
				if (isPassed())
				{
					return threadLocal.get();
				}
				
				Ret_Type ret = fun.apply(arg1, arg2, arg3, arg4, arg5, arg6);
				threadLocal.set(ret);
				isPassed.set(true);
				return ret;
			}
		};
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6);
	
	/**
	 * Добавить 'post-функцию' функцию
	 * 
	 * @param nextFun
	 *            Функция, которая будет выполнена после этой. Цепочка
	 *            вернет результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction6<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Ret_Type> andAfter(IFunction6<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6) -> {
			apply(arg1, arg2, arg3, arg4, arg5, arg6);
			return fun.apply(arg1, arg2, arg3, arg4, arg5, arg6);
		};
	}
	
	public default IFunction6<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Ret_Type> andThan(IFunction6<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Ret_Type> fun)
	{
		return andAfter(fun);
	}
	
	/**
	 * Добавить 'pre-функцию' функцию
	 * 
	 * @param fun
	 *            Функция, которая будет выполнена до этой. Цепочка вернет
	 *            результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction6<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Ret_Type> andBefore(IFunction6<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6) -> {
			fun.apply(arg1, arg2, arg3, arg4, arg5, arg6);
			return apply(arg1, arg2, arg3, arg4, arg5, arg6);
		};
	}
	
	/**
	 * Получить функцию с кэшированным результатом
	 * @return Функция с кэшированными результатами
	 */
	public default IFunction6<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Ret_Type> cached(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6)
	{
		Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6);
		return (a1, a2, a3, a4, a5, a6) -> ret;
	}
	
	/**
	 * Получить функцию без возвращаемого типа
	 * 
	 * @return Новую фунцию
	 */
	public default IFunction6NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6> asNoReturn()
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6) -> apply(arg1, arg2, arg3, arg4, arg5, arg6);
	}
	
	
	
}

/**
 * Версия функции IFunction6 без возвращаемого типа (NoR = No return)
 * 
 * @author swayfarer
 */
 @FunctionalInterface
public static interface IFunction6NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6> extends IFunction6<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Void> {
	
	@Override
	public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6)
	{
		applyNoR(arg1, arg2, arg3, arg4, arg5, arg6);
		return null;
	}
	
	public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6);
	
	/**
	 * Добавить 'post-функцию' функцию
	 * 
	 * @param nextFun
	 *            Функция, которая будет выполнена после этой. Цепочка
	 *            вернет результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction6NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6> andAfter(IFunction6NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6) -> {
			apply(arg1, arg2, arg3, arg4, arg5, arg6);
			fun.apply(arg1, arg2, arg3, arg4, arg5, arg6);
		};
	}
	
	public default IFunction6NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6> andThan(IFunction6NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6> fun)
	{
		return andAfter(fun);
	}
	
	/**
	 * Добавить 'pre-функцию' функцию
	 * 
	 * @param fun
	 *            Функция, которая будет выполнена до этой. Цепочка вернет
	 *            результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction6NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6> andBefore(IFunction6NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6) -> {
			fun.apply(arg1, arg2, arg3, arg4, arg5, arg6);
			apply(arg1, arg2, arg3, arg4, arg5, arg6);
		};
	}
	
}


@FunctionalInterface
public static interface IFunction7<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Ret_Type> {
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type __(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7)
	{
		return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	}
	
	public default IFunction7<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Ret_Type> andApply(IFunction1<Ret_Type, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7) -> fun.apply(apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7));
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type $(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7)
	{
		return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7)
	{
		return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	}
	
	public default IFunction7<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Boolean> and(IFunction7<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Boolean> fun)
	{
		IFunction7<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Boolean> thisFun = (IFunction7<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Boolean>)((Object) this);
		return (a1, a2, a3, a4, a5, a6, a7) -> thisFun.apply(a1, a2, a3, a4, a5, a6, a7) && fun.apply(a1, a2, a3, a4, a5, a6, a7);
	}
	
	public default IFunction7<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Boolean> or(IFunction7<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Boolean> fun)
	{
		IFunction7<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Boolean> thisFun = (IFunction7<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Boolean>)((Object) this);
		return (a1, a2, a3, a4, a5, a6, a7) -> thisFun.apply(a1, a2, a3, a4, a5, a6, a7) || fun.apply(a1, a2, a3, a4, a5, a6, a7);
	}
	
	/**
	 * Выполнить функцию, запомнить результат и возвращать всегда его
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction7<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Ret_Type> memorize(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7)
	{
		Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	
		return (a1, a2, a3, a4, a5, a6, a7) -> ret;
	}
	
	/**
	 * При первом выполнении функция запомнит результат и будет всегда его возвращать
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction7<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Ret_Type> memorized()
	{
		IFunction7<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Ret_Type> fun = this;
	
		return new IFunction7<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Ret_Type> () 
		{
			public AtomicBoolean isProcessed = new AtomicBoolean();
			public volatile Ret_Type retValue;
		
			public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7)
			{
				if (!isProcessed.get())
				{
					retValue = fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
					isProcessed.set(true);
				}
				
				return retValue;
			}
		};
	}
	
	/**
	 * При первом выполнении функция запомнит результат и будет всегда его возвращать
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction7<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Ret_Type> threadLocal()
	{
		IFunction7<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Ret_Type> fun = this;
	
		return new IFunction7<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Ret_Type> () 
		{
			public volatile ThreadLocal<Boolean> isPassed = new ThreadLocal<Boolean>();
			public volatile ThreadLocal<Ret_Type> threadLocal = new ThreadLocal<Ret_Type>();
		
			public boolean isPassed()
			{
				return isPassed.get() == Boolean.TRUE;
			}
			
			public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7)
			{
				if (isPassed())
				{
					return threadLocal.get();
				}
				
				Ret_Type ret = fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
				threadLocal.set(ret);
				isPassed.set(true);
				return ret;
			}
		};
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7);
	
	/**
	 * Добавить 'post-функцию' функцию
	 * 
	 * @param nextFun
	 *            Функция, которая будет выполнена после этой. Цепочка
	 *            вернет результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction7<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Ret_Type> andAfter(IFunction7<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7) -> {
			apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
			return fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		};
	}
	
	public default IFunction7<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Ret_Type> andThan(IFunction7<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Ret_Type> fun)
	{
		return andAfter(fun);
	}
	
	/**
	 * Добавить 'pre-функцию' функцию
	 * 
	 * @param fun
	 *            Функция, которая будет выполнена до этой. Цепочка вернет
	 *            результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction7<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Ret_Type> andBefore(IFunction7<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7) -> {
			fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		};
	}
	
	/**
	 * Получить функцию с кэшированным результатом
	 * @return Функция с кэшированными результатами
	 */
	public default IFunction7<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Ret_Type> cached(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7)
	{
		Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return (a1, a2, a3, a4, a5, a6, a7) -> ret;
	}
	
	/**
	 * Получить функцию без возвращаемого типа
	 * 
	 * @return Новую фунцию
	 */
	public default IFunction7NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7> asNoReturn()
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7) -> apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	}
	
	
	
}

/**
 * Версия функции IFunction7 без возвращаемого типа (NoR = No return)
 * 
 * @author swayfarer
 */
 @FunctionalInterface
public static interface IFunction7NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7> extends IFunction7<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Void> {
	
	@Override
	public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7)
	{
		applyNoR(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		return null;
	}
	
	public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7);
	
	/**
	 * Добавить 'post-функцию' функцию
	 * 
	 * @param nextFun
	 *            Функция, которая будет выполнена после этой. Цепочка
	 *            вернет результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction7NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7> andAfter(IFunction7NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7) -> {
			apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
			fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		};
	}
	
	public default IFunction7NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7> andThan(IFunction7NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7> fun)
	{
		return andAfter(fun);
	}
	
	/**
	 * Добавить 'pre-функцию' функцию
	 * 
	 * @param fun
	 *            Функция, которая будет выполнена до этой. Цепочка вернет
	 *            результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction7NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7> andBefore(IFunction7NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7) -> {
			fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
			apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		};
	}
	
}


@FunctionalInterface
public static interface IFunction8<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Ret_Type> {
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type __(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8)
	{
		return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	}
	
	public default IFunction8<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Ret_Type> andApply(IFunction1<Ret_Type, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) -> fun.apply(apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8));
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type $(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8)
	{
		return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8)
	{
		return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	}
	
	public default IFunction8<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Boolean> and(IFunction8<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Boolean> fun)
	{
		IFunction8<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Boolean> thisFun = (IFunction8<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Boolean>)((Object) this);
		return (a1, a2, a3, a4, a5, a6, a7, a8) -> thisFun.apply(a1, a2, a3, a4, a5, a6, a7, a8) && fun.apply(a1, a2, a3, a4, a5, a6, a7, a8);
	}
	
	public default IFunction8<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Boolean> or(IFunction8<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Boolean> fun)
	{
		IFunction8<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Boolean> thisFun = (IFunction8<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Boolean>)((Object) this);
		return (a1, a2, a3, a4, a5, a6, a7, a8) -> thisFun.apply(a1, a2, a3, a4, a5, a6, a7, a8) || fun.apply(a1, a2, a3, a4, a5, a6, a7, a8);
	}
	
	/**
	 * Выполнить функцию, запомнить результат и возвращать всегда его
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction8<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Ret_Type> memorize(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8)
	{
		Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	
		return (a1, a2, a3, a4, a5, a6, a7, a8) -> ret;
	}
	
	/**
	 * При первом выполнении функция запомнит результат и будет всегда его возвращать
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction8<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Ret_Type> memorized()
	{
		IFunction8<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Ret_Type> fun = this;
	
		return new IFunction8<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Ret_Type> () 
		{
			public AtomicBoolean isProcessed = new AtomicBoolean();
			public volatile Ret_Type retValue;
		
			public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8)
			{
				if (!isProcessed.get())
				{
					retValue = fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
					isProcessed.set(true);
				}
				
				return retValue;
			}
		};
	}
	
	/**
	 * При первом выполнении функция запомнит результат и будет всегда его возвращать
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction8<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Ret_Type> threadLocal()
	{
		IFunction8<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Ret_Type> fun = this;
	
		return new IFunction8<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Ret_Type> () 
		{
			public volatile ThreadLocal<Boolean> isPassed = new ThreadLocal<Boolean>();
			public volatile ThreadLocal<Ret_Type> threadLocal = new ThreadLocal<Ret_Type>();
		
			public boolean isPassed()
			{
				return isPassed.get() == Boolean.TRUE;
			}
			
			public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8)
			{
				if (isPassed())
				{
					return threadLocal.get();
				}
				
				Ret_Type ret = fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
				threadLocal.set(ret);
				isPassed.set(true);
				return ret;
			}
		};
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8);
	
	/**
	 * Добавить 'post-функцию' функцию
	 * 
	 * @param nextFun
	 *            Функция, которая будет выполнена после этой. Цепочка
	 *            вернет результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction8<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Ret_Type> andAfter(IFunction8<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) -> {
			apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
			return fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		};
	}
	
	public default IFunction8<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Ret_Type> andThan(IFunction8<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Ret_Type> fun)
	{
		return andAfter(fun);
	}
	
	/**
	 * Добавить 'pre-функцию' функцию
	 * 
	 * @param fun
	 *            Функция, которая будет выполнена до этой. Цепочка вернет
	 *            результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction8<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Ret_Type> andBefore(IFunction8<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) -> {
			fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		};
	}
	
	/**
	 * Получить функцию с кэшированным результатом
	 * @return Функция с кэшированными результатами
	 */
	public default IFunction8<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Ret_Type> cached(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8)
	{
		Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return (a1, a2, a3, a4, a5, a6, a7, a8) -> ret;
	}
	
	/**
	 * Получить функцию без возвращаемого типа
	 * 
	 * @return Новую фунцию
	 */
	public default IFunction8NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8> asNoReturn()
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) -> apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	}
	
	
	
}

/**
 * Версия функции IFunction8 без возвращаемого типа (NoR = No return)
 * 
 * @author swayfarer
 */
 @FunctionalInterface
public static interface IFunction8NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8> extends IFunction8<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Void> {
	
	@Override
	public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8)
	{
		applyNoR(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		return null;
	}
	
	public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8);
	
	/**
	 * Добавить 'post-функцию' функцию
	 * 
	 * @param nextFun
	 *            Функция, которая будет выполнена после этой. Цепочка
	 *            вернет результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction8NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8> andAfter(IFunction8NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) -> {
			apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
			fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		};
	}
	
	public default IFunction8NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8> andThan(IFunction8NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8> fun)
	{
		return andAfter(fun);
	}
	
	/**
	 * Добавить 'pre-функцию' функцию
	 * 
	 * @param fun
	 *            Функция, которая будет выполнена до этой. Цепочка вернет
	 *            результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction8NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8> andBefore(IFunction8NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) -> {
			fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
			apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
		};
	}
	
}


@FunctionalInterface
public static interface IFunction9<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Ret_Type> {
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type __(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9)
	{
		return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
	}
	
	public default IFunction9<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Ret_Type> andApply(IFunction1<Ret_Type, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9) -> fun.apply(apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9));
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type $(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9)
	{
		return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9)
	{
		return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
	}
	
	public default IFunction9<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Boolean> and(IFunction9<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Boolean> fun)
	{
		IFunction9<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Boolean> thisFun = (IFunction9<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Boolean>)((Object) this);
		return (a1, a2, a3, a4, a5, a6, a7, a8, a9) -> thisFun.apply(a1, a2, a3, a4, a5, a6, a7, a8, a9) && fun.apply(a1, a2, a3, a4, a5, a6, a7, a8, a9);
	}
	
	public default IFunction9<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Boolean> or(IFunction9<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Boolean> fun)
	{
		IFunction9<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Boolean> thisFun = (IFunction9<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Boolean>)((Object) this);
		return (a1, a2, a3, a4, a5, a6, a7, a8, a9) -> thisFun.apply(a1, a2, a3, a4, a5, a6, a7, a8, a9) || fun.apply(a1, a2, a3, a4, a5, a6, a7, a8, a9);
	}
	
	/**
	 * Выполнить функцию, запомнить результат и возвращать всегда его
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction9<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Ret_Type> memorize(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9)
	{
		Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
	
		return (a1, a2, a3, a4, a5, a6, a7, a8, a9) -> ret;
	}
	
	/**
	 * При первом выполнении функция запомнит результат и будет всегда его возвращать
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction9<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Ret_Type> memorized()
	{
		IFunction9<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Ret_Type> fun = this;
	
		return new IFunction9<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Ret_Type> () 
		{
			public AtomicBoolean isProcessed = new AtomicBoolean();
			public volatile Ret_Type retValue;
		
			public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9)
			{
				if (!isProcessed.get())
				{
					retValue = fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
					isProcessed.set(true);
				}
				
				return retValue;
			}
		};
	}
	
	/**
	 * При первом выполнении функция запомнит результат и будет всегда его возвращать
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction9<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Ret_Type> threadLocal()
	{
		IFunction9<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Ret_Type> fun = this;
	
		return new IFunction9<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Ret_Type> () 
		{
			public volatile ThreadLocal<Boolean> isPassed = new ThreadLocal<Boolean>();
			public volatile ThreadLocal<Ret_Type> threadLocal = new ThreadLocal<Ret_Type>();
		
			public boolean isPassed()
			{
				return isPassed.get() == Boolean.TRUE;
			}
			
			public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9)
			{
				if (isPassed())
				{
					return threadLocal.get();
				}
				
				Ret_Type ret = fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
				threadLocal.set(ret);
				isPassed.set(true);
				return ret;
			}
		};
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9);
	
	/**
	 * Добавить 'post-функцию' функцию
	 * 
	 * @param nextFun
	 *            Функция, которая будет выполнена после этой. Цепочка
	 *            вернет результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction9<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Ret_Type> andAfter(IFunction9<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9) -> {
			apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
			return fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		};
	}
	
	public default IFunction9<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Ret_Type> andThan(IFunction9<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Ret_Type> fun)
	{
		return andAfter(fun);
	}
	
	/**
	 * Добавить 'pre-функцию' функцию
	 * 
	 * @param fun
	 *            Функция, которая будет выполнена до этой. Цепочка вернет
	 *            результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction9<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Ret_Type> andBefore(IFunction9<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9) -> {
			fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		};
	}
	
	/**
	 * Получить функцию с кэшированным результатом
	 * @return Функция с кэшированными результатами
	 */
	public default IFunction9<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Ret_Type> cached(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9)
	{
		Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		return (a1, a2, a3, a4, a5, a6, a7, a8, a9) -> ret;
	}
	
	/**
	 * Получить функцию без возвращаемого типа
	 * 
	 * @return Новую фунцию
	 */
	public default IFunction9NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9> asNoReturn()
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9) -> apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
	}
	
	
	
}

/**
 * Версия функции IFunction9 без возвращаемого типа (NoR = No return)
 * 
 * @author swayfarer
 */
 @FunctionalInterface
public static interface IFunction9NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9> extends IFunction9<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Void> {
	
	@Override
	public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9)
	{
		applyNoR(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		return null;
	}
	
	public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9);
	
	/**
	 * Добавить 'post-функцию' функцию
	 * 
	 * @param nextFun
	 *            Функция, которая будет выполнена после этой. Цепочка
	 *            вернет результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction9NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9> andAfter(IFunction9NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9) -> {
			apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
			fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		};
	}
	
	public default IFunction9NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9> andThan(IFunction9NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9> fun)
	{
		return andAfter(fun);
	}
	
	/**
	 * Добавить 'pre-функцию' функцию
	 * 
	 * @param fun
	 *            Функция, которая будет выполнена до этой. Цепочка вернет
	 *            результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction9NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9> andBefore(IFunction9NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9) -> {
			fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
			apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
		};
	}
	
}


@FunctionalInterface
public static interface IFunction10<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Ret_Type> {
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type __(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10)
	{
		return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
	}
	
	public default IFunction10<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Ret_Type> andApply(IFunction1<Ret_Type, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10) -> fun.apply(apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10));
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type $(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10)
	{
		return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10)
	{
		return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
	}
	
	public default IFunction10<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Boolean> and(IFunction10<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Boolean> fun)
	{
		IFunction10<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Boolean> thisFun = (IFunction10<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Boolean>)((Object) this);
		return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10) -> thisFun.apply(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10) && fun.apply(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10);
	}
	
	public default IFunction10<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Boolean> or(IFunction10<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Boolean> fun)
	{
		IFunction10<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Boolean> thisFun = (IFunction10<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Boolean>)((Object) this);
		return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10) -> thisFun.apply(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10) || fun.apply(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10);
	}
	
	/**
	 * Выполнить функцию, запомнить результат и возвращать всегда его
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction10<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Ret_Type> memorize(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10)
	{
		Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
	
		return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10) -> ret;
	}
	
	/**
	 * При первом выполнении функция запомнит результат и будет всегда его возвращать
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction10<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Ret_Type> memorized()
	{
		IFunction10<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Ret_Type> fun = this;
	
		return new IFunction10<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Ret_Type> () 
		{
			public AtomicBoolean isProcessed = new AtomicBoolean();
			public volatile Ret_Type retValue;
		
			public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10)
			{
				if (!isProcessed.get())
				{
					retValue = fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
					isProcessed.set(true);
				}
				
				return retValue;
			}
		};
	}
	
	/**
	 * При первом выполнении функция запомнит результат и будет всегда его возвращать
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction10<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Ret_Type> threadLocal()
	{
		IFunction10<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Ret_Type> fun = this;
	
		return new IFunction10<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Ret_Type> () 
		{
			public volatile ThreadLocal<Boolean> isPassed = new ThreadLocal<Boolean>();
			public volatile ThreadLocal<Ret_Type> threadLocal = new ThreadLocal<Ret_Type>();
		
			public boolean isPassed()
			{
				return isPassed.get() == Boolean.TRUE;
			}
			
			public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10)
			{
				if (isPassed())
				{
					return threadLocal.get();
				}
				
				Ret_Type ret = fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
				threadLocal.set(ret);
				isPassed.set(true);
				return ret;
			}
		};
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10);
	
	/**
	 * Добавить 'post-функцию' функцию
	 * 
	 * @param nextFun
	 *            Функция, которая будет выполнена после этой. Цепочка
	 *            вернет результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction10<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Ret_Type> andAfter(IFunction10<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10) -> {
			apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
			return fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		};
	}
	
	public default IFunction10<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Ret_Type> andThan(IFunction10<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Ret_Type> fun)
	{
		return andAfter(fun);
	}
	
	/**
	 * Добавить 'pre-функцию' функцию
	 * 
	 * @param fun
	 *            Функция, которая будет выполнена до этой. Цепочка вернет
	 *            результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction10<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Ret_Type> andBefore(IFunction10<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10) -> {
			fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		};
	}
	
	/**
	 * Получить функцию с кэшированным результатом
	 * @return Функция с кэшированными результатами
	 */
	public default IFunction10<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Ret_Type> cached(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10)
	{
		Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10) -> ret;
	}
	
	/**
	 * Получить функцию без возвращаемого типа
	 * 
	 * @return Новую фунцию
	 */
	public default IFunction10NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10> asNoReturn()
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10) -> apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
	}
	
	
	
}

/**
 * Версия функции IFunction10 без возвращаемого типа (NoR = No return)
 * 
 * @author swayfarer
 */
 @FunctionalInterface
public static interface IFunction10NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10> extends IFunction10<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Void> {
	
	@Override
	public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10)
	{
		applyNoR(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		return null;
	}
	
	public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10);
	
	/**
	 * Добавить 'post-функцию' функцию
	 * 
	 * @param nextFun
	 *            Функция, которая будет выполнена после этой. Цепочка
	 *            вернет результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction10NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10> andAfter(IFunction10NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10) -> {
			apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
			fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		};
	}
	
	public default IFunction10NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10> andThan(IFunction10NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10> fun)
	{
		return andAfter(fun);
	}
	
	/**
	 * Добавить 'pre-функцию' функцию
	 * 
	 * @param fun
	 *            Функция, которая будет выполнена до этой. Цепочка вернет
	 *            результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction10NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10> andBefore(IFunction10NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10) -> {
			fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
			apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
		};
	}
	
}


@FunctionalInterface
public static interface IFunction11<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Ret_Type> {
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type __(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11)
	{
		return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
	}
	
	public default IFunction11<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Ret_Type> andApply(IFunction1<Ret_Type, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11) -> fun.apply(apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11));
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type $(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11)
	{
		return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11)
	{
		return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
	}
	
	public default IFunction11<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Boolean> and(IFunction11<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Boolean> fun)
	{
		IFunction11<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Boolean> thisFun = (IFunction11<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Boolean>)((Object) this);
		return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11) -> thisFun.apply(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11) && fun.apply(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11);
	}
	
	public default IFunction11<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Boolean> or(IFunction11<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Boolean> fun)
	{
		IFunction11<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Boolean> thisFun = (IFunction11<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Boolean>)((Object) this);
		return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11) -> thisFun.apply(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11) || fun.apply(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11);
	}
	
	/**
	 * Выполнить функцию, запомнить результат и возвращать всегда его
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction11<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Ret_Type> memorize(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11)
	{
		Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
	
		return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11) -> ret;
	}
	
	/**
	 * При первом выполнении функция запомнит результат и будет всегда его возвращать
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction11<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Ret_Type> memorized()
	{
		IFunction11<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Ret_Type> fun = this;
	
		return new IFunction11<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Ret_Type> () 
		{
			public AtomicBoolean isProcessed = new AtomicBoolean();
			public volatile Ret_Type retValue;
		
			public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11)
			{
				if (!isProcessed.get())
				{
					retValue = fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
					isProcessed.set(true);
				}
				
				return retValue;
			}
		};
	}
	
	/**
	 * При первом выполнении функция запомнит результат и будет всегда его возвращать
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction11<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Ret_Type> threadLocal()
	{
		IFunction11<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Ret_Type> fun = this;
	
		return new IFunction11<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Ret_Type> () 
		{
			public volatile ThreadLocal<Boolean> isPassed = new ThreadLocal<Boolean>();
			public volatile ThreadLocal<Ret_Type> threadLocal = new ThreadLocal<Ret_Type>();
		
			public boolean isPassed()
			{
				return isPassed.get() == Boolean.TRUE;
			}
			
			public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11)
			{
				if (isPassed())
				{
					return threadLocal.get();
				}
				
				Ret_Type ret = fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
				threadLocal.set(ret);
				isPassed.set(true);
				return ret;
			}
		};
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11);
	
	/**
	 * Добавить 'post-функцию' функцию
	 * 
	 * @param nextFun
	 *            Функция, которая будет выполнена после этой. Цепочка
	 *            вернет результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction11<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Ret_Type> andAfter(IFunction11<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11) -> {
			apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
			return fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
		};
	}
	
	public default IFunction11<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Ret_Type> andThan(IFunction11<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Ret_Type> fun)
	{
		return andAfter(fun);
	}
	
	/**
	 * Добавить 'pre-функцию' функцию
	 * 
	 * @param fun
	 *            Функция, которая будет выполнена до этой. Цепочка вернет
	 *            результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction11<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Ret_Type> andBefore(IFunction11<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11) -> {
			fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
		};
	}
	
	/**
	 * Получить функцию с кэшированным результатом
	 * @return Функция с кэшированными результатами
	 */
	public default IFunction11<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Ret_Type> cached(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11)
	{
		Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
		return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11) -> ret;
	}
	
	/**
	 * Получить функцию без возвращаемого типа
	 * 
	 * @return Новую фунцию
	 */
	public default IFunction11NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11> asNoReturn()
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11) -> apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
	}
	
	
	
}

/**
 * Версия функции IFunction11 без возвращаемого типа (NoR = No return)
 * 
 * @author swayfarer
 */
 @FunctionalInterface
public static interface IFunction11NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11> extends IFunction11<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Void> {
	
	@Override
	public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11)
	{
		applyNoR(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
		return null;
	}
	
	public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11);
	
	/**
	 * Добавить 'post-функцию' функцию
	 * 
	 * @param nextFun
	 *            Функция, которая будет выполнена после этой. Цепочка
	 *            вернет результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction11NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11> andAfter(IFunction11NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11) -> {
			apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
			fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
		};
	}
	
	public default IFunction11NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11> andThan(IFunction11NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11> fun)
	{
		return andAfter(fun);
	}
	
	/**
	 * Добавить 'pre-функцию' функцию
	 * 
	 * @param fun
	 *            Функция, которая будет выполнена до этой. Цепочка вернет
	 *            результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction11NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11> andBefore(IFunction11NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11) -> {
			fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
			apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
		};
	}
	
}


@FunctionalInterface
public static interface IFunction12<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Ret_Type> {
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type __(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12)
	{
		return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
	}
	
	public default IFunction12<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Ret_Type> andApply(IFunction1<Ret_Type, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12) -> fun.apply(apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12));
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type $(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12)
	{
		return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12)
	{
		return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
	}
	
	public default IFunction12<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Boolean> and(IFunction12<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Boolean> fun)
	{
		IFunction12<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Boolean> thisFun = (IFunction12<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Boolean>)((Object) this);
		return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12) -> thisFun.apply(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12) && fun.apply(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12);
	}
	
	public default IFunction12<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Boolean> or(IFunction12<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Boolean> fun)
	{
		IFunction12<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Boolean> thisFun = (IFunction12<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Boolean>)((Object) this);
		return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12) -> thisFun.apply(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12) || fun.apply(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12);
	}
	
	/**
	 * Выполнить функцию, запомнить результат и возвращать всегда его
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction12<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Ret_Type> memorize(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12)
	{
		Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
	
		return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12) -> ret;
	}
	
	/**
	 * При первом выполнении функция запомнит результат и будет всегда его возвращать
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction12<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Ret_Type> memorized()
	{
		IFunction12<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Ret_Type> fun = this;
	
		return new IFunction12<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Ret_Type> () 
		{
			public AtomicBoolean isProcessed = new AtomicBoolean();
			public volatile Ret_Type retValue;
		
			public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12)
			{
				if (!isProcessed.get())
				{
					retValue = fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
					isProcessed.set(true);
				}
				
				return retValue;
			}
		};
	}
	
	/**
	 * При первом выполнении функция запомнит результат и будет всегда его возвращать
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction12<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Ret_Type> threadLocal()
	{
		IFunction12<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Ret_Type> fun = this;
	
		return new IFunction12<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Ret_Type> () 
		{
			public volatile ThreadLocal<Boolean> isPassed = new ThreadLocal<Boolean>();
			public volatile ThreadLocal<Ret_Type> threadLocal = new ThreadLocal<Ret_Type>();
		
			public boolean isPassed()
			{
				return isPassed.get() == Boolean.TRUE;
			}
			
			public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12)
			{
				if (isPassed())
				{
					return threadLocal.get();
				}
				
				Ret_Type ret = fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
				threadLocal.set(ret);
				isPassed.set(true);
				return ret;
			}
		};
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12);
	
	/**
	 * Добавить 'post-функцию' функцию
	 * 
	 * @param nextFun
	 *            Функция, которая будет выполнена после этой. Цепочка
	 *            вернет результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction12<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Ret_Type> andAfter(IFunction12<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12) -> {
			apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
			return fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
		};
	}
	
	public default IFunction12<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Ret_Type> andThan(IFunction12<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Ret_Type> fun)
	{
		return andAfter(fun);
	}
	
	/**
	 * Добавить 'pre-функцию' функцию
	 * 
	 * @param fun
	 *            Функция, которая будет выполнена до этой. Цепочка вернет
	 *            результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction12<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Ret_Type> andBefore(IFunction12<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12) -> {
			fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
		};
	}
	
	/**
	 * Получить функцию с кэшированным результатом
	 * @return Функция с кэшированными результатами
	 */
	public default IFunction12<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Ret_Type> cached(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12)
	{
		Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
		return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12) -> ret;
	}
	
	/**
	 * Получить функцию без возвращаемого типа
	 * 
	 * @return Новую фунцию
	 */
	public default IFunction12NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12> asNoReturn()
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12) -> apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
	}
	
	
	
}

/**
 * Версия функции IFunction12 без возвращаемого типа (NoR = No return)
 * 
 * @author swayfarer
 */
 @FunctionalInterface
public static interface IFunction12NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12> extends IFunction12<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Void> {
	
	@Override
	public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12)
	{
		applyNoR(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
		return null;
	}
	
	public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12);
	
	/**
	 * Добавить 'post-функцию' функцию
	 * 
	 * @param nextFun
	 *            Функция, которая будет выполнена после этой. Цепочка
	 *            вернет результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction12NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12> andAfter(IFunction12NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12) -> {
			apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
			fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
		};
	}
	
	public default IFunction12NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12> andThan(IFunction12NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12> fun)
	{
		return andAfter(fun);
	}
	
	/**
	 * Добавить 'pre-функцию' функцию
	 * 
	 * @param fun
	 *            Функция, которая будет выполнена до этой. Цепочка вернет
	 *            результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction12NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12> andBefore(IFunction12NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12) -> {
			fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
			apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
		};
	}
	
}


@FunctionalInterface
public static interface IFunction13<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Ret_Type> {
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type __(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13)
	{
		return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13);
	}
	
	public default IFunction13<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Ret_Type> andApply(IFunction1<Ret_Type, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13) -> fun.apply(apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13));
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type $(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13)
	{
		return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13);
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13)
	{
		return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13);
	}
	
	public default IFunction13<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Boolean> and(IFunction13<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Boolean> fun)
	{
		IFunction13<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Boolean> thisFun = (IFunction13<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Boolean>)((Object) this);
		return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13) -> thisFun.apply(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13) && fun.apply(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13);
	}
	
	public default IFunction13<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Boolean> or(IFunction13<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Boolean> fun)
	{
		IFunction13<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Boolean> thisFun = (IFunction13<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Boolean>)((Object) this);
		return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13) -> thisFun.apply(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13) || fun.apply(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13);
	}
	
	/**
	 * Выполнить функцию, запомнить результат и возвращать всегда его
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction13<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Ret_Type> memorize(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13)
	{
		Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13);
	
		return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13) -> ret;
	}
	
	/**
	 * При первом выполнении функция запомнит результат и будет всегда его возвращать
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction13<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Ret_Type> memorized()
	{
		IFunction13<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Ret_Type> fun = this;
	
		return new IFunction13<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Ret_Type> () 
		{
			public AtomicBoolean isProcessed = new AtomicBoolean();
			public volatile Ret_Type retValue;
		
			public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13)
			{
				if (!isProcessed.get())
				{
					retValue = fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13);
					isProcessed.set(true);
				}
				
				return retValue;
			}
		};
	}
	
	/**
	 * При первом выполнении функция запомнит результат и будет всегда его возвращать
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction13<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Ret_Type> threadLocal()
	{
		IFunction13<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Ret_Type> fun = this;
	
		return new IFunction13<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Ret_Type> () 
		{
			public volatile ThreadLocal<Boolean> isPassed = new ThreadLocal<Boolean>();
			public volatile ThreadLocal<Ret_Type> threadLocal = new ThreadLocal<Ret_Type>();
		
			public boolean isPassed()
			{
				return isPassed.get() == Boolean.TRUE;
			}
			
			public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13)
			{
				if (isPassed())
				{
					return threadLocal.get();
				}
				
				Ret_Type ret = fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13);
				threadLocal.set(ret);
				isPassed.set(true);
				return ret;
			}
		};
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13);
	
	/**
	 * Добавить 'post-функцию' функцию
	 * 
	 * @param nextFun
	 *            Функция, которая будет выполнена после этой. Цепочка
	 *            вернет результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction13<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Ret_Type> andAfter(IFunction13<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13) -> {
			apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13);
			return fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13);
		};
	}
	
	public default IFunction13<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Ret_Type> andThan(IFunction13<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Ret_Type> fun)
	{
		return andAfter(fun);
	}
	
	/**
	 * Добавить 'pre-функцию' функцию
	 * 
	 * @param fun
	 *            Функция, которая будет выполнена до этой. Цепочка вернет
	 *            результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction13<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Ret_Type> andBefore(IFunction13<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13) -> {
			fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13);
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13);
		};
	}
	
	/**
	 * Получить функцию с кэшированным результатом
	 * @return Функция с кэшированными результатами
	 */
	public default IFunction13<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Ret_Type> cached(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13)
	{
		Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13);
		return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13) -> ret;
	}
	
	/**
	 * Получить функцию без возвращаемого типа
	 * 
	 * @return Новую фунцию
	 */
	public default IFunction13NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13> asNoReturn()
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13) -> apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13);
	}
	
	
	
}

/**
 * Версия функции IFunction13 без возвращаемого типа (NoR = No return)
 * 
 * @author swayfarer
 */
 @FunctionalInterface
public static interface IFunction13NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13> extends IFunction13<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Void> {
	
	@Override
	public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13)
	{
		applyNoR(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13);
		return null;
	}
	
	public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13);
	
	/**
	 * Добавить 'post-функцию' функцию
	 * 
	 * @param nextFun
	 *            Функция, которая будет выполнена после этой. Цепочка
	 *            вернет результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction13NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13> andAfter(IFunction13NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13) -> {
			apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13);
			fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13);
		};
	}
	
	public default IFunction13NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13> andThan(IFunction13NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13> fun)
	{
		return andAfter(fun);
	}
	
	/**
	 * Добавить 'pre-функцию' функцию
	 * 
	 * @param fun
	 *            Функция, которая будет выполнена до этой. Цепочка вернет
	 *            результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction13NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13> andBefore(IFunction13NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13) -> {
			fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13);
			apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13);
		};
	}
	
}


@FunctionalInterface
public static interface IFunction14<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Ret_Type> {
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type __(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14)
	{
		return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14);
	}
	
	public default IFunction14<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Ret_Type> andApply(IFunction1<Ret_Type, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14) -> fun.apply(apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14));
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type $(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14)
	{
		return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14);
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14)
	{
		return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14);
	}
	
	public default IFunction14<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Boolean> and(IFunction14<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Boolean> fun)
	{
		IFunction14<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Boolean> thisFun = (IFunction14<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Boolean>)((Object) this);
		return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14) -> thisFun.apply(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14) && fun.apply(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14);
	}
	
	public default IFunction14<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Boolean> or(IFunction14<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Boolean> fun)
	{
		IFunction14<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Boolean> thisFun = (IFunction14<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Boolean>)((Object) this);
		return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14) -> thisFun.apply(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14) || fun.apply(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14);
	}
	
	/**
	 * Выполнить функцию, запомнить результат и возвращать всегда его
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction14<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Ret_Type> memorize(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14)
	{
		Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14);
	
		return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14) -> ret;
	}
	
	/**
	 * При первом выполнении функция запомнит результат и будет всегда его возвращать
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction14<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Ret_Type> memorized()
	{
		IFunction14<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Ret_Type> fun = this;
	
		return new IFunction14<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Ret_Type> () 
		{
			public AtomicBoolean isProcessed = new AtomicBoolean();
			public volatile Ret_Type retValue;
		
			public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14)
			{
				if (!isProcessed.get())
				{
					retValue = fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14);
					isProcessed.set(true);
				}
				
				return retValue;
			}
		};
	}
	
	/**
	 * При первом выполнении функция запомнит результат и будет всегда его возвращать
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction14<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Ret_Type> threadLocal()
	{
		IFunction14<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Ret_Type> fun = this;
	
		return new IFunction14<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Ret_Type> () 
		{
			public volatile ThreadLocal<Boolean> isPassed = new ThreadLocal<Boolean>();
			public volatile ThreadLocal<Ret_Type> threadLocal = new ThreadLocal<Ret_Type>();
		
			public boolean isPassed()
			{
				return isPassed.get() == Boolean.TRUE;
			}
			
			public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14)
			{
				if (isPassed())
				{
					return threadLocal.get();
				}
				
				Ret_Type ret = fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14);
				threadLocal.set(ret);
				isPassed.set(true);
				return ret;
			}
		};
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14);
	
	/**
	 * Добавить 'post-функцию' функцию
	 * 
	 * @param nextFun
	 *            Функция, которая будет выполнена после этой. Цепочка
	 *            вернет результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction14<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Ret_Type> andAfter(IFunction14<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14) -> {
			apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14);
			return fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14);
		};
	}
	
	public default IFunction14<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Ret_Type> andThan(IFunction14<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Ret_Type> fun)
	{
		return andAfter(fun);
	}
	
	/**
	 * Добавить 'pre-функцию' функцию
	 * 
	 * @param fun
	 *            Функция, которая будет выполнена до этой. Цепочка вернет
	 *            результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction14<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Ret_Type> andBefore(IFunction14<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14) -> {
			fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14);
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14);
		};
	}
	
	/**
	 * Получить функцию с кэшированным результатом
	 * @return Функция с кэшированными результатами
	 */
	public default IFunction14<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Ret_Type> cached(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14)
	{
		Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14);
		return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14) -> ret;
	}
	
	/**
	 * Получить функцию без возвращаемого типа
	 * 
	 * @return Новую фунцию
	 */
	public default IFunction14NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14> asNoReturn()
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14) -> apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14);
	}
	
	
	
}

/**
 * Версия функции IFunction14 без возвращаемого типа (NoR = No return)
 * 
 * @author swayfarer
 */
 @FunctionalInterface
public static interface IFunction14NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14> extends IFunction14<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Void> {
	
	@Override
	public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14)
	{
		applyNoR(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14);
		return null;
	}
	
	public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14);
	
	/**
	 * Добавить 'post-функцию' функцию
	 * 
	 * @param nextFun
	 *            Функция, которая будет выполнена после этой. Цепочка
	 *            вернет результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction14NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14> andAfter(IFunction14NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14) -> {
			apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14);
			fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14);
		};
	}
	
	public default IFunction14NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14> andThan(IFunction14NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14> fun)
	{
		return andAfter(fun);
	}
	
	/**
	 * Добавить 'pre-функцию' функцию
	 * 
	 * @param fun
	 *            Функция, которая будет выполнена до этой. Цепочка вернет
	 *            результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction14NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14> andBefore(IFunction14NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14) -> {
			fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14);
			apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14);
		};
	}
	
}


@FunctionalInterface
public static interface IFunction15<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Ret_Type> {
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type __(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15)
	{
		return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15);
	}
	
	public default IFunction15<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Ret_Type> andApply(IFunction1<Ret_Type, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15) -> fun.apply(apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15));
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type $(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15)
	{
		return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15);
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15)
	{
		return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15);
	}
	
	public default IFunction15<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Boolean> and(IFunction15<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Boolean> fun)
	{
		IFunction15<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Boolean> thisFun = (IFunction15<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Boolean>)((Object) this);
		return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15) -> thisFun.apply(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15) && fun.apply(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15);
	}
	
	public default IFunction15<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Boolean> or(IFunction15<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Boolean> fun)
	{
		IFunction15<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Boolean> thisFun = (IFunction15<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Boolean>)((Object) this);
		return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15) -> thisFun.apply(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15) || fun.apply(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15);
	}
	
	/**
	 * Выполнить функцию, запомнить результат и возвращать всегда его
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction15<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Ret_Type> memorize(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15)
	{
		Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15);
	
		return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15) -> ret;
	}
	
	/**
	 * При первом выполнении функция запомнит результат и будет всегда его возвращать
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction15<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Ret_Type> memorized()
	{
		IFunction15<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Ret_Type> fun = this;
	
		return new IFunction15<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Ret_Type> () 
		{
			public AtomicBoolean isProcessed = new AtomicBoolean();
			public volatile Ret_Type retValue;
		
			public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15)
			{
				if (!isProcessed.get())
				{
					retValue = fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15);
					isProcessed.set(true);
				}
				
				return retValue;
			}
		};
	}
	
	/**
	 * При первом выполнении функция запомнит результат и будет всегда его возвращать
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction15<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Ret_Type> threadLocal()
	{
		IFunction15<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Ret_Type> fun = this;
	
		return new IFunction15<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Ret_Type> () 
		{
			public volatile ThreadLocal<Boolean> isPassed = new ThreadLocal<Boolean>();
			public volatile ThreadLocal<Ret_Type> threadLocal = new ThreadLocal<Ret_Type>();
		
			public boolean isPassed()
			{
				return isPassed.get() == Boolean.TRUE;
			}
			
			public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15)
			{
				if (isPassed())
				{
					return threadLocal.get();
				}
				
				Ret_Type ret = fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15);
				threadLocal.set(ret);
				isPassed.set(true);
				return ret;
			}
		};
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15);
	
	/**
	 * Добавить 'post-функцию' функцию
	 * 
	 * @param nextFun
	 *            Функция, которая будет выполнена после этой. Цепочка
	 *            вернет результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction15<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Ret_Type> andAfter(IFunction15<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15) -> {
			apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15);
			return fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15);
		};
	}
	
	public default IFunction15<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Ret_Type> andThan(IFunction15<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Ret_Type> fun)
	{
		return andAfter(fun);
	}
	
	/**
	 * Добавить 'pre-функцию' функцию
	 * 
	 * @param fun
	 *            Функция, которая будет выполнена до этой. Цепочка вернет
	 *            результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction15<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Ret_Type> andBefore(IFunction15<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15) -> {
			fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15);
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15);
		};
	}
	
	/**
	 * Получить функцию с кэшированным результатом
	 * @return Функция с кэшированными результатами
	 */
	public default IFunction15<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Ret_Type> cached(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15)
	{
		Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15);
		return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15) -> ret;
	}
	
	/**
	 * Получить функцию без возвращаемого типа
	 * 
	 * @return Новую фунцию
	 */
	public default IFunction15NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15> asNoReturn()
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15) -> apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15);
	}
	
	
	
}

/**
 * Версия функции IFunction15 без возвращаемого типа (NoR = No return)
 * 
 * @author swayfarer
 */
 @FunctionalInterface
public static interface IFunction15NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15> extends IFunction15<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Void> {
	
	@Override
	public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15)
	{
		applyNoR(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15);
		return null;
	}
	
	public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15);
	
	/**
	 * Добавить 'post-функцию' функцию
	 * 
	 * @param nextFun
	 *            Функция, которая будет выполнена после этой. Цепочка
	 *            вернет результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction15NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15> andAfter(IFunction15NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15) -> {
			apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15);
			fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15);
		};
	}
	
	public default IFunction15NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15> andThan(IFunction15NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15> fun)
	{
		return andAfter(fun);
	}
	
	/**
	 * Добавить 'pre-функцию' функцию
	 * 
	 * @param fun
	 *            Функция, которая будет выполнена до этой. Цепочка вернет
	 *            результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction15NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15> andBefore(IFunction15NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15) -> {
			fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15);
			apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15);
		};
	}
	
}


@FunctionalInterface
public static interface IFunction16<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Ret_Type> {
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type __(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16)
	{
		return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
	}
	
	public default IFunction16<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Ret_Type> andApply(IFunction1<Ret_Type, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16) -> fun.apply(apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16));
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type $(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16)
	{
		return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16)
	{
		return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
	}
	
	public default IFunction16<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Boolean> and(IFunction16<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Boolean> fun)
	{
		IFunction16<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Boolean> thisFun = (IFunction16<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Boolean>)((Object) this);
		return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16) -> thisFun.apply(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16) && fun.apply(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16);
	}
	
	public default IFunction16<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Boolean> or(IFunction16<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Boolean> fun)
	{
		IFunction16<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Boolean> thisFun = (IFunction16<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Boolean>)((Object) this);
		return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16) -> thisFun.apply(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16) || fun.apply(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16);
	}
	
	/**
	 * Выполнить функцию, запомнить результат и возвращать всегда его
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction16<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Ret_Type> memorize(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16)
	{
		Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
	
		return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16) -> ret;
	}
	
	/**
	 * При первом выполнении функция запомнит результат и будет всегда его возвращать
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction16<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Ret_Type> memorized()
	{
		IFunction16<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Ret_Type> fun = this;
	
		return new IFunction16<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Ret_Type> () 
		{
			public AtomicBoolean isProcessed = new AtomicBoolean();
			public volatile Ret_Type retValue;
		
			public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16)
			{
				if (!isProcessed.get())
				{
					retValue = fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
					isProcessed.set(true);
				}
				
				return retValue;
			}
		};
	}
	
	/**
	 * При первом выполнении функция запомнит результат и будет всегда его возвращать
	 * 
	 * @return Возвращаемое значение функции
	 */
	public default IFunction16<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Ret_Type> threadLocal()
	{
		IFunction16<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Ret_Type> fun = this;
	
		return new IFunction16<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Ret_Type> () 
		{
			public volatile ThreadLocal<Boolean> isPassed = new ThreadLocal<Boolean>();
			public volatile ThreadLocal<Ret_Type> threadLocal = new ThreadLocal<Ret_Type>();
		
			public boolean isPassed()
			{
				return isPassed.get() == Boolean.TRUE;
			}
			
			public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16)
			{
				if (isPassed())
				{
					return threadLocal.get();
				}
				
				Ret_Type ret = fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
				threadLocal.set(ret);
				isPassed.set(true);
				return ret;
			}
		};
	}
	
	/**
	 * Применить функцию
	 * 
	 * @return Возвращаемое значение функции
	 */
	public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16);
	
	/**
	 * Добавить 'post-функцию' функцию
	 * 
	 * @param nextFun
	 *            Функция, которая будет выполнена после этой. Цепочка
	 *            вернет результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction16<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Ret_Type> andAfter(IFunction16<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16) -> {
			apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
			return fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
		};
	}
	
	public default IFunction16<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Ret_Type> andThan(IFunction16<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Ret_Type> fun)
	{
		return andAfter(fun);
	}
	
	/**
	 * Добавить 'pre-функцию' функцию
	 * 
	 * @param fun
	 *            Функция, которая будет выполнена до этой. Цепочка вернет
	 *            результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction16<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Ret_Type> andBefore(IFunction16<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Ret_Type> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16) -> {
			fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
		};
	}
	
	/**
	 * Получить функцию с кэшированным результатом
	 * @return Функция с кэшированными результатами
	 */
	public default IFunction16<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Ret_Type> cached(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16)
	{
		Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
		return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16) -> ret;
	}
	
	/**
	 * Получить функцию без возвращаемого типа
	 * 
	 * @return Новую фунцию
	 */
	public default IFunction16NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16> asNoReturn()
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16) -> apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
	}
	
	
	
}

/**
 * Версия функции IFunction16 без возвращаемого типа (NoR = No return)
 * 
 * @author swayfarer
 */
 @FunctionalInterface
public static interface IFunction16NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16> extends IFunction16<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Void> {
	
	@Override
	public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16)
	{
		applyNoR(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
		return null;
	}
	
	public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16);
	
	/**
	 * Добавить 'post-функцию' функцию
	 * 
	 * @param nextFun
	 *            Функция, которая будет выполнена после этой. Цепочка
	 *            вернет результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction16NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16> andAfter(IFunction16NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16) -> {
			apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
			fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
		};
	}
	
	public default IFunction16NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16> andThan(IFunction16NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16> fun)
	{
		return andAfter(fun);
	}
	
	/**
	 * Добавить 'pre-функцию' функцию
	 * 
	 * @param fun
	 *            Функция, которая будет выполнена до этой. Цепочка вернет
	 *            результат последней функции
	 * @return Новую функцию, свянанную цепочкой
	 */
	public default IFunction16NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16> andBefore(IFunction16NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16> fun)
	{
		return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16) -> {
			fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
			apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
		};
	}
	
}

}