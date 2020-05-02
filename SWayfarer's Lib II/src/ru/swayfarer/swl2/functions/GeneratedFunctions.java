package ru.swayfarer.swl2.functions;

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Функции для удобства использования функционального подхода
 * <h1>Автоматически сгенерировано с помощью
 * ru.swayfarer.swl2.functions.generator.FunctionsGenerator</h1>
 * 
 * @author swayfarer
 */
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
		 * 
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

		/** Получить стандартный Java-{@link Supplier} */
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
		 * 
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
		 * 
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
		 * 
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
		 * 
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
		 * 
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
		 * 
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
		 * 
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
		 * 
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
		 * 
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
		 * 
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
		 * 
		 * @return Функция с кэшированными результатами
		 */
		public default IFunction11<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Ret_Type> cached(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10,
				Arg11 arg11)
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
		 * 
		 * @return Функция с кэшированными результатами
		 */
		public default IFunction12<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Ret_Type> cached(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10,
				Arg11 arg11, Arg12 arg12)
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
		public default IFunction13<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Ret_Type> andAfter(
				IFunction13<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13);
				return fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13);
			};
		}

		public default IFunction13<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Ret_Type> andThan(
				IFunction13<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Ret_Type> fun)
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
		public default IFunction13<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Ret_Type> andBefore(
				IFunction13<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13);
				return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13);
			};
		}

		/**
		 * Получить функцию с кэшированным результатом
		 * 
		 * @return Функция с кэшированными результатами
		 */
		public default IFunction13<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Ret_Type> cached(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9,
				Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13)
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
		public default IFunction14<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Ret_Type> andAfter(
				IFunction14<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14);
				return fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14);
			};
		}

		public default IFunction14<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Ret_Type> andThan(
				IFunction14<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Ret_Type> fun)
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
		public default IFunction14<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Ret_Type> andBefore(
				IFunction14<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14);
				return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14);
			};
		}

		/**
		 * Получить функцию с кэшированным результатом
		 * 
		 * @return Функция с кэшированными результатами
		 */
		public default IFunction14<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Ret_Type> cached(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9,
				Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14)
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
	public static interface IFunction14NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14>
			extends IFunction14<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Void> {

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
		public default IFunction14NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14> andAfter(
				IFunction14NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14);
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14);
			};
		}

		public default IFunction14NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14> andThan(
				IFunction14NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14> fun)
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
		public default IFunction14NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14> andBefore(
				IFunction14NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14> fun)
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
		public default IFunction15<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Ret_Type> andAfter(
				IFunction15<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15);
				return fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15);
			};
		}

		public default IFunction15<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Ret_Type> andThan(
				IFunction15<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Ret_Type> fun)
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
		public default IFunction15<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Ret_Type> andBefore(
				IFunction15<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15);
				return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15);
			};
		}

		/**
		 * Получить функцию с кэшированным результатом
		 * 
		 * @return Функция с кэшированными результатами
		 */
		public default IFunction15<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Ret_Type> cached(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8,
				Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15)
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
	public static interface IFunction15NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15>
			extends IFunction15<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Void> {

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
		public default IFunction15NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15> andAfter(
				IFunction15NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15);
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15);
			};
		}

		public default IFunction15NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15> andThan(
				IFunction15NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15> fun)
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
		public default IFunction15NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15> andBefore(
				IFunction15NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15> fun)
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
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16) -> fun
					.apply(apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16));
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
		public default IFunction16<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Ret_Type> andAfter(
				IFunction16<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
				return fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
			};
		}

		public default IFunction16<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Ret_Type> andThan(
				IFunction16<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Ret_Type> fun)
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
		public default IFunction16<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Ret_Type> andBefore(
				IFunction16<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
				return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
			};
		}

		/**
		 * Получить функцию с кэшированным результатом
		 * 
		 * @return Функция с кэшированными результатами
		 */
		public default IFunction16<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Ret_Type> cached(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7,
				Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16)
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
	public static interface IFunction16NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16>
			extends IFunction16<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Void> {

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
		public default IFunction16NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16> andAfter(
				IFunction16NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
			};
		}

		public default IFunction16NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16> andThan(
				IFunction16NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16> fun)
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
		public default IFunction16NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16> andBefore(
				IFunction16NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16);
			};
		}

	}

	@FunctionalInterface
	public static interface IFunction17<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Ret_Type> {

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type __(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17);
		}

		public default IFunction17<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Ret_Type> andApply(IFunction1<Ret_Type, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17) -> fun
					.apply(apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17));
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type $(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17);
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16,
				Arg17 arg17)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17);
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17);

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction17<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Ret_Type> andAfter(
				IFunction17<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17);
				return fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17);
			};
		}

		public default IFunction17<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Ret_Type> andThan(
				IFunction17<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Ret_Type> fun)
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
		public default IFunction17<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Ret_Type> andBefore(
				IFunction17<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17);
				return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17);
			};
		}

		/**
		 * Получить функцию с кэшированным результатом
		 * 
		 * @return Функция с кэшированными результатами
		 */
		public default IFunction17<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Ret_Type> cached(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7,
				Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17)
		{
			Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17);
			return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17) -> ret;
		}

		/**
		 * Получить функцию без возвращаемого типа
		 * 
		 * @return Новую фунцию
		 */
		public default IFunction17NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17> asNoReturn()
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17) -> apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15,
					arg16, arg17);
		}

	}

	/**
	 * Версия функции IFunction17 без возвращаемого типа (NoR = No return)
	 * 
	 * @author swayfarer
	 */
	@FunctionalInterface
	public static interface IFunction17NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17>
			extends IFunction17<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Void> {

		@Override
		public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17)
		{
			applyNoR(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17);
			return null;
		}

		public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17);

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction17NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17> andAfter(
				IFunction17NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17);
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17);
			};
		}

		public default IFunction17NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17> andThan(
				IFunction17NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17> fun)
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
		public default IFunction17NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17> andBefore(
				IFunction17NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17);
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17);
			};
		}

	}

	@FunctionalInterface
	public static interface IFunction18<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Ret_Type> {

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type __(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18);
		}

		public default IFunction18<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Ret_Type> andApply(IFunction1<Ret_Type, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18) -> fun
					.apply(apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18));
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type $(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18);
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16,
				Arg17 arg17, Arg18 arg18)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18);
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18);

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction18<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Ret_Type> andAfter(
				IFunction18<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18);
				return fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18);
			};
		}

		public default IFunction18<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Ret_Type> andThan(
				IFunction18<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Ret_Type> fun)
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
		public default IFunction18<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Ret_Type> andBefore(
				IFunction18<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18);
				return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18);
			};
		}

		/**
		 * Получить функцию с кэшированным результатом
		 * 
		 * @return Функция с кэшированными результатами
		 */
		public default IFunction18<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Ret_Type> cached(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6,
				Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17, Arg18 arg18)
		{
			Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18);
			return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18) -> ret;
		}

		/**
		 * Получить функцию без возвращаемого типа
		 * 
		 * @return Новую фунцию
		 */
		public default IFunction18NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18> asNoReturn()
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18) -> apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14,
					arg15, arg16, arg17, arg18);
		}

	}

	/**
	 * Версия функции IFunction18 без возвращаемого типа (NoR = No return)
	 * 
	 * @author swayfarer
	 */
	@FunctionalInterface
	public static interface IFunction18NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18>
			extends IFunction18<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Void> {

		@Override
		public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18)
		{
			applyNoR(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18);
			return null;
		}

		public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18);

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction18NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18> andAfter(
				IFunction18NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18);
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18);
			};
		}

		public default IFunction18NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18> andThan(
				IFunction18NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18> fun)
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
		public default IFunction18NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18> andBefore(
				IFunction18NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18);
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18);
			};
		}

	}

	@FunctionalInterface
	public static interface IFunction19<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Ret_Type> {

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type __(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19);
		}

		public default IFunction19<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Ret_Type> andApply(IFunction1<Ret_Type, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19) -> fun
					.apply(apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19));
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type $(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19);
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16,
				Arg17 arg17, Arg18 arg18, Arg19 arg19)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19);
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19);

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction19<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Ret_Type> andAfter(
				IFunction19<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19);
				return fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19);
			};
		}

		public default IFunction19<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Ret_Type> andThan(
				IFunction19<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Ret_Type> fun)
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
		public default IFunction19<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Ret_Type> andBefore(
				IFunction19<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19);
				return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19);
			};
		}

		/**
		 * Получить функцию с кэшированным результатом
		 * 
		 * @return Функция с кэшированными результатами
		 */
		public default IFunction19<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Ret_Type> cached(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5,
				Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17, Arg18 arg18, Arg19 arg19)
		{
			Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19);
			return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19) -> ret;
		}

		/**
		 * Получить функцию без возвращаемого типа
		 * 
		 * @return Новую фунцию
		 */
		public default IFunction19NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19> asNoReturn()
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19) -> apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13,
					arg14, arg15, arg16, arg17, arg18, arg19);
		}

	}

	/**
	 * Версия функции IFunction19 без возвращаемого типа (NoR = No return)
	 * 
	 * @author swayfarer
	 */
	@FunctionalInterface
	public static interface IFunction19NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19>
			extends IFunction19<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Void> {

		@Override
		public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19)
		{
			applyNoR(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19);
			return null;
		}

		public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19);

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction19NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19> andAfter(
				IFunction19NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19);
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19);
			};
		}

		public default IFunction19NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19> andThan(
				IFunction19NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19> fun)
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
		public default IFunction19NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19> andBefore(
				IFunction19NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19);
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19);
			};
		}

	}

	@FunctionalInterface
	public static interface IFunction20<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Ret_Type> {

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type __(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20);
		}

		public default IFunction20<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Ret_Type> andApply(IFunction1<Ret_Type, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20) -> fun
					.apply(apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20));
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type $(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20);
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16,
				Arg17 arg17, Arg18 arg18, Arg19 arg19, Arg20 arg20)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20);
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20);

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction20<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Ret_Type> andAfter(
				IFunction20<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20);
				return fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20);
			};
		}

		public default IFunction20<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Ret_Type> andThan(
				IFunction20<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Ret_Type> fun)
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
		public default IFunction20<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Ret_Type> andBefore(
				IFunction20<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20);
				return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20);
			};
		}

		/**
		 * Получить функцию с кэшированным результатом
		 * 
		 * @return Функция с кэшированными результатами
		 */
		public default IFunction20<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Ret_Type> cached(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5,
				Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17, Arg18 arg18, Arg19 arg19, Arg20 arg20)
		{
			Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20);
			return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20) -> ret;
		}

		/**
		 * Получить функцию без возвращаемого типа
		 * 
		 * @return Новую фунцию
		 */
		public default IFunction20NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20> asNoReturn()
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20) -> apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12,
					arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20);
		}

	}

	/**
	 * Версия функции IFunction20 без возвращаемого типа (NoR = No return)
	 * 
	 * @author swayfarer
	 */
	@FunctionalInterface
	public static interface IFunction20NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20>
			extends IFunction20<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Void> {

		@Override
		public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20)
		{
			applyNoR(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20);
			return null;
		}

		public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20);

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction20NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20> andAfter(
				IFunction20NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20);
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20);
			};
		}

		public default IFunction20NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20> andThan(
				IFunction20NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20> fun)
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
		public default IFunction20NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20> andBefore(
				IFunction20NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20);
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20);
			};
		}

	}

	@FunctionalInterface
	public static interface IFunction21<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Ret_Type> {

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type __(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21);
		}

		public default IFunction21<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Ret_Type> andApply(IFunction1<Ret_Type, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21) -> fun
					.apply(apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21));
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type $(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21);
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16,
				Arg17 arg17, Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21);
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21);

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction21<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Ret_Type> andAfter(
				IFunction21<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21);
				return fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21);
			};
		}

		public default IFunction21<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Ret_Type> andThan(
				IFunction21<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Ret_Type> fun)
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
		public default IFunction21<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Ret_Type> andBefore(
				IFunction21<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21);
				return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21);
			};
		}

		/**
		 * Получить функцию с кэшированным результатом
		 * 
		 * @return Функция с кэшированными результатами
		 */
		public default IFunction21<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Ret_Type> cached(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4,
				Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17, Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21)
		{
			Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21);
			return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21) -> ret;
		}

		/**
		 * Получить функцию без возвращаемого типа
		 * 
		 * @return Новую фунцию
		 */
		public default IFunction21NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21> asNoReturn()
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21) -> apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11,
					arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21);
		}

	}

	/**
	 * Версия функции IFunction21 без возвращаемого типа (NoR = No return)
	 * 
	 * @author swayfarer
	 */
	@FunctionalInterface
	public static interface IFunction21NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21>
			extends IFunction21<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Void> {

		@Override
		public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21)
		{
			applyNoR(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21);
			return null;
		}

		public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21);

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction21NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21> andAfter(
				IFunction21NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21);
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21);
			};
		}

		public default IFunction21NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21> andThan(
				IFunction21NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21> fun)
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
		public default IFunction21NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21> andBefore(
				IFunction21NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21);
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21);
			};
		}

	}

	@FunctionalInterface
	public static interface IFunction22<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Ret_Type> {

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type __(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22);
		}

		public default IFunction22<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Ret_Type> andApply(IFunction1<Ret_Type, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22) -> fun
					.apply(apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22));
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type $(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22);
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16,
				Arg17 arg17, Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22);
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22);

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction22<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Ret_Type> andAfter(
				IFunction22<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22);
				return fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22);
			};
		}

		public default IFunction22<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Ret_Type> andThan(
				IFunction22<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Ret_Type> fun)
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
		public default IFunction22<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Ret_Type> andBefore(
				IFunction22<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22);
				return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22);
			};
		}

		/**
		 * Получить функцию с кэшированным результатом
		 * 
		 * @return Функция с кэшированными результатами
		 */
		public default IFunction22<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Ret_Type> cached(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4,
				Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17, Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22)
		{
			Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22);
			return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22) -> ret;
		}

		/**
		 * Получить функцию без возвращаемого типа
		 * 
		 * @return Новую фунцию
		 */
		public default IFunction22NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22> asNoReturn()
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22) -> apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10,
					arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22);
		}

	}

	/**
	 * Версия функции IFunction22 без возвращаемого типа (NoR = No return)
	 * 
	 * @author swayfarer
	 */
	@FunctionalInterface
	public static interface IFunction22NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22>
			extends IFunction22<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Void> {

		@Override
		public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22)
		{
			applyNoR(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22);
			return null;
		}

		public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22);

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction22NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22> andAfter(
				IFunction22NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22);
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22);
			};
		}

		public default IFunction22NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22> andThan(
				IFunction22NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22> fun)
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
		public default IFunction22NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22> andBefore(
				IFunction22NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22);
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22);
			};
		}

	}

	@FunctionalInterface
	public static interface IFunction23<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Ret_Type> {

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type __(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23);
		}

		public default IFunction23<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Ret_Type> andApply(
				IFunction1<Ret_Type, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23) -> fun
					.apply(apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23));
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type $(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23);
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16,
				Arg17 arg17, Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23);
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23);

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction23<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Ret_Type> andAfter(
				IFunction23<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23);
				return fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23);
			};
		}

		public default IFunction23<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Ret_Type> andThan(
				IFunction23<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Ret_Type> fun)
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
		public default IFunction23<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Ret_Type> andBefore(
				IFunction23<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23);
				return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23);
			};
		}

		/**
		 * Получить функцию с кэшированным результатом
		 * 
		 * @return Функция с кэшированными результатами
		 */
		public default IFunction23<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Ret_Type> cached(Arg1 arg1, Arg2 arg2, Arg3 arg3,
				Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17, Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21,
				Arg22 arg22, Arg23 arg23)
		{
			Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23);
			return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23) -> ret;
		}

		/**
		 * Получить функцию без возвращаемого типа
		 * 
		 * @return Новую фунцию
		 */
		public default IFunction23NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23> asNoReturn()
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23) -> apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9,
					arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23);
		}

	}

	/**
	 * Версия функции IFunction23 без возвращаемого типа (NoR = No return)
	 * 
	 * @author swayfarer
	 */
	@FunctionalInterface
	public static interface IFunction23NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23>
			extends IFunction23<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Void> {

		@Override
		public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23)
		{
			applyNoR(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23);
			return null;
		}

		public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23);

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction23NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23> andAfter(
				IFunction23NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23);
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23);
			};
		}

		public default IFunction23NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23> andThan(
				IFunction23NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23> fun)
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
		public default IFunction23NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23> andBefore(
				IFunction23NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23);
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23);
			};
		}

	}

	@FunctionalInterface
	public static interface IFunction24<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Ret_Type> {

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type __(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24);
		}

		public default IFunction24<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Ret_Type> andApply(
				IFunction1<Ret_Type, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24) -> fun
					.apply(apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24));
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type $(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24);
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16,
				Arg17 arg17, Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24);
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24);

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction24<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Ret_Type> andAfter(
				IFunction24<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24);
				return fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24);
			};
		}

		public default IFunction24<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Ret_Type> andThan(
				IFunction24<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Ret_Type> fun)
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
		public default IFunction24<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Ret_Type> andBefore(
				IFunction24<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24);
				return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24);
			};
		}

		/**
		 * Получить функцию с кэшированным результатом
		 * 
		 * @return Функция с кэшированными результатами
		 */
		public default IFunction24<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Ret_Type> cached(Arg1 arg1, Arg2 arg2,
				Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17, Arg18 arg18, Arg19 arg19, Arg20 arg20,
				Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24)
		{
			Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24);
			return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24) -> ret;
		}

		/**
		 * Получить функцию без возвращаемого типа
		 * 
		 * @return Новую фунцию
		 */
		public default IFunction24NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24> asNoReturn()
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24) -> apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8,
					arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24);
		}

	}

	/**
	 * Версия функции IFunction24 без возвращаемого типа (NoR = No return)
	 * 
	 * @author swayfarer
	 */
	@FunctionalInterface
	public static interface IFunction24NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24>
			extends IFunction24<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Void> {

		@Override
		public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24)
		{
			applyNoR(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24);
			return null;
		}

		public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24);

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction24NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24> andAfter(
				IFunction24NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24);
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24);
			};
		}

		public default IFunction24NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24> andThan(
				IFunction24NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24> fun)
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
		public default IFunction24NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24> andBefore(
				IFunction24NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24);
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24);
			};
		}

	}

	@FunctionalInterface
	public static interface IFunction25<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Ret_Type> {

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type __(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25);
		}

		public default IFunction25<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Ret_Type> andApply(
				IFunction1<Ret_Type, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25) -> fun
					.apply(apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25));
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type $(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25);
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16,
				Arg17 arg17, Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25);
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25);

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction25<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Ret_Type> andAfter(
				IFunction25<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25);
				return fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25);
			};
		}

		public default IFunction25<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Ret_Type> andThan(
				IFunction25<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Ret_Type> fun)
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
		public default IFunction25<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Ret_Type> andBefore(
				IFunction25<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25);
				return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25);
			};
		}

		/**
		 * Получить функцию с кэшированным результатом
		 * 
		 * @return Функция с кэшированными результатами
		 */
		public default IFunction25<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Ret_Type> cached(Arg1 arg1, Arg2 arg2,
				Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17, Arg18 arg18, Arg19 arg19, Arg20 arg20,
				Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25)
		{
			Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25);
			return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25) -> ret;
		}

		/**
		 * Получить функцию без возвращаемого типа
		 * 
		 * @return Новую фунцию
		 */
		public default IFunction25NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25> asNoReturn()
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25) -> apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7,
					arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25);
		}

	}

	/**
	 * Версия функции IFunction25 без возвращаемого типа (NoR = No return)
	 * 
	 * @author swayfarer
	 */
	@FunctionalInterface
	public static interface IFunction25NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25>
			extends IFunction25<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Void> {

		@Override
		public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25)
		{
			applyNoR(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25);
			return null;
		}

		public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25);

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction25NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25> andAfter(
				IFunction25NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25);
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25);
			};
		}

		public default IFunction25NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25> andThan(
				IFunction25NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25> fun)
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
		public default IFunction25NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25> andBefore(
				IFunction25NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25);
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25);
			};
		}

	}

	@FunctionalInterface
	public static interface IFunction26<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Ret_Type> {

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type __(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26);
		}

		public default IFunction26<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Ret_Type> andApply(
				IFunction1<Ret_Type, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26) -> fun
					.apply(apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26));
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type $(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26);
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16,
				Arg17 arg17, Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26);
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26);

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction26<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Ret_Type> andAfter(
				IFunction26<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26);
				return fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26);
			};
		}

		public default IFunction26<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Ret_Type> andThan(
				IFunction26<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Ret_Type> fun)
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
		public default IFunction26<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Ret_Type> andBefore(
				IFunction26<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26);
				return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26);
			};
		}

		/**
		 * Получить функцию с кэшированным результатом
		 * 
		 * @return Функция с кэшированными результатами
		 */
		public default IFunction26<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Ret_Type> cached(Arg1 arg1,
				Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17, Arg18 arg18, Arg19 arg19,
				Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26)
		{
			Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26);
			return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26) -> ret;
		}

		/**
		 * Получить функцию без возвращаемого типа
		 * 
		 * @return Новую фунцию
		 */
		public default IFunction26NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26> asNoReturn()
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26) -> apply(arg1, arg2, arg3, arg4, arg5, arg6,
					arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26);
		}

	}

	/**
	 * Версия функции IFunction26 без возвращаемого типа (NoR = No return)
	 * 
	 * @author swayfarer
	 */
	@FunctionalInterface
	public static interface IFunction26NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26>
			extends IFunction26<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Void> {

		@Override
		public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26)
		{
			applyNoR(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26);
			return null;
		}

		public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26);

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction26NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26> andAfter(
				IFunction26NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26);
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26);
			};
		}

		public default IFunction26NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26> andThan(
				IFunction26NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26> fun)
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
		public default IFunction26NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26> andBefore(
				IFunction26NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26);
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26);
			};
		}

	}

	@FunctionalInterface
	public static interface IFunction27<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Ret_Type> {

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type __(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27);
		}

		public default IFunction27<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Ret_Type> andApply(
				IFunction1<Ret_Type, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27) -> fun
					.apply(apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27));
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type $(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27);
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16,
				Arg17 arg17, Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27);
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27);

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction27<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Ret_Type> andAfter(
				IFunction27<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27);
				return fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27);
			};
		}

		public default IFunction27<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Ret_Type> andThan(
				IFunction27<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Ret_Type> fun)
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
		public default IFunction27<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Ret_Type> andBefore(
				IFunction27<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27);
				return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27);
			};
		}

		/**
		 * Получить функцию с кэшированным результатом
		 * 
		 * @return Функция с кэшированными результатами
		 */
		public default IFunction27<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Ret_Type> cached(
				Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17, Arg18 arg18,
				Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27)
		{
			Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27);
			return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26, a27) -> ret;
		}

		/**
		 * Получить функцию без возвращаемого типа
		 * 
		 * @return Новую фунцию
		 */
		public default IFunction27NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27> asNoReturn()
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27) -> apply(arg1, arg2, arg3, arg4, arg5,
					arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27);
		}

	}

	/**
	 * Версия функции IFunction27 без возвращаемого типа (NoR = No return)
	 * 
	 * @author swayfarer
	 */
	@FunctionalInterface
	public static interface IFunction27NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27>
			extends IFunction27<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Void> {

		@Override
		public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27)
		{
			applyNoR(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27);
			return null;
		}

		public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27);

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction27NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27> andAfter(
				IFunction27NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27);
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27);
			};
		}

		public default IFunction27NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27> andThan(
				IFunction27NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27> fun)
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
		public default IFunction27NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27> andBefore(
				IFunction27NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27);
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27);
			};
		}

	}

	@FunctionalInterface
	public static interface IFunction28<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Ret_Type> {

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type __(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28);
		}

		public default IFunction28<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Ret_Type> andApply(
				IFunction1<Ret_Type, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28) -> fun
					.apply(apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28));
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type $(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28);
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16,
				Arg17 arg17, Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28);
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28);

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction28<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Ret_Type> andAfter(
				IFunction28<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28);
				return fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28);
			};
		}

		public default IFunction28<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Ret_Type> andThan(
				IFunction28<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Ret_Type> fun)
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
		public default IFunction28<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Ret_Type> andBefore(
				IFunction28<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28);
				return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28);
			};
		}

		/**
		 * Получить функцию с кэшированным результатом
		 * 
		 * @return Функция с кэшированными результатами
		 */
		public default IFunction28<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Ret_Type> cached(
				Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17, Arg18 arg18,
				Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28)
		{
			Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28);
			return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26, a27, a28) -> ret;
		}

		/**
		 * Получить функцию без возвращаемого типа
		 * 
		 * @return Новую фунцию
		 */
		public default IFunction28NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28> asNoReturn()
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28) -> apply(arg1, arg2, arg3, arg4,
					arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28);
		}

	}

	/**
	 * Версия функции IFunction28 без возвращаемого типа (NoR = No return)
	 * 
	 * @author swayfarer
	 */
	@FunctionalInterface
	public static interface IFunction28NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28>
			extends IFunction28<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Void> {

		@Override
		public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28)
		{
			applyNoR(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28);
			return null;
		}

		public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28);

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction28NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28> andAfter(
				IFunction28NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28);
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28);
			};
		}

		public default IFunction28NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28> andThan(
				IFunction28NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28> fun)
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
		public default IFunction28NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28> andBefore(
				IFunction28NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28);
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28);
			};
		}

	}

	@FunctionalInterface
	public static interface IFunction29<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Ret_Type> {

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type __(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28, Arg29 arg29)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29);
		}

		public default IFunction29<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Ret_Type> andApply(
				IFunction1<Ret_Type, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29) -> fun
					.apply(apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29));
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type $(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28, Arg29 arg29)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29);
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16,
				Arg17 arg17, Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28, Arg29 arg29)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29);
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28, Arg29 arg29);

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction29<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Ret_Type> andAfter(
				IFunction29<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29);
				return fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29);
			};
		}

		public default IFunction29<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Ret_Type> andThan(
				IFunction29<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Ret_Type> fun)
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
		public default IFunction29<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Ret_Type> andBefore(
				IFunction29<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29);
				return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29);
			};
		}

		/**
		 * Получить функцию с кэшированным результатом
		 * 
		 * @return Функция с кэшированными результатами
		 */
		public default IFunction29<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Ret_Type> cached(
				Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17, Arg18 arg18,
				Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28, Arg29 arg29)
		{
			Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29);
			return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26, a27, a28, a29) -> ret;
		}

		/**
		 * Получить функцию без возвращаемого типа
		 * 
		 * @return Новую фунцию
		 */
		public default IFunction29NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29> asNoReturn()
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29) -> apply(arg1, arg2, arg3,
					arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29);
		}

	}

	/**
	 * Версия функции IFunction29 без возвращаемого типа (NoR = No return)
	 * 
	 * @author swayfarer
	 */
	@FunctionalInterface
	public static interface IFunction29NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29>
			extends IFunction29<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Void> {

		@Override
		public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28, Arg29 arg29)
		{
			applyNoR(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29);
			return null;
		}

		public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28, Arg29 arg29);

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction29NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29> andAfter(
				IFunction29NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29);
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29);
			};
		}

		public default IFunction29NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29> andThan(
				IFunction29NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29> fun)
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
		public default IFunction29NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29> andBefore(
				IFunction29NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29);
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29);
			};
		}

	}

	@FunctionalInterface
	public static interface IFunction30<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Ret_Type> {

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type __(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28, Arg29 arg29, Arg30 arg30)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30);
		}

		public default IFunction30<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Ret_Type> andApply(
				IFunction1<Ret_Type, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30) -> fun
					.apply(apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30));
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type $(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28, Arg29 arg29, Arg30 arg30)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30);
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16,
				Arg17 arg17, Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28, Arg29 arg29, Arg30 arg30)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30);
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28, Arg29 arg29, Arg30 arg30);

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction30<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Ret_Type> andAfter(
				IFunction30<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30);
				return fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30);
			};
		}

		public default IFunction30<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Ret_Type> andThan(
				IFunction30<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Ret_Type> fun)
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
		public default IFunction30<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Ret_Type> andBefore(
				IFunction30<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30);
				return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30);
			};
		}

		/**
		 * Получить функцию с кэшированным результатом
		 * 
		 * @return Функция с кэшированными результатами
		 */
		public default IFunction30<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Ret_Type> cached(
				Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17, Arg18 arg18,
				Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28, Arg29 arg29, Arg30 arg30)
		{
			Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30);
			return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26, a27, a28, a29, a30) -> ret;
		}

		/**
		 * Получить функцию без возвращаемого типа
		 * 
		 * @return Новую фунцию
		 */
		public default IFunction30NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30> asNoReturn()
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30) -> apply(arg1,
					arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30);
		}

	}

	/**
	 * Версия функции IFunction30 без возвращаемого типа (NoR = No return)
	 * 
	 * @author swayfarer
	 */
	@FunctionalInterface
	public static interface IFunction30NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30>
			extends IFunction30<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Void> {

		@Override
		public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28, Arg29 arg29, Arg30 arg30)
		{
			applyNoR(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30);
			return null;
		}

		public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28, Arg29 arg29, Arg30 arg30);

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction30NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30> andAfter(
				IFunction30NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30);
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30);
			};
		}

		public default IFunction30NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30> andThan(
				IFunction30NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30> fun)
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
		public default IFunction30NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30> andBefore(
				IFunction30NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30);
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30);
			};
		}

	}

	@FunctionalInterface
	public static interface IFunction31<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31, Ret_Type> {

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type __(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28, Arg29 arg29, Arg30 arg30, Arg31 arg31)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31);
		}

		public default IFunction31<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31, Ret_Type> andApply(
				IFunction1<Ret_Type, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31) -> fun
					.apply(apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30,
							arg31));
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type $(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28, Arg29 arg29, Arg30 arg30, Arg31 arg31)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31);
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16,
				Arg17 arg17, Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28, Arg29 arg29, Arg30 arg30, Arg31 arg31)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31);
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28, Arg29 arg29, Arg30 arg30, Arg31 arg31);

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction31<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31, Ret_Type> andAfter(
				IFunction31<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31);
				return fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30,
						arg31);
			};
		}

		public default IFunction31<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31, Ret_Type> andThan(
				IFunction31<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31, Ret_Type> fun)
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
		public default IFunction31<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31, Ret_Type> andBefore(
				IFunction31<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31);
				return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31);
			};
		}

		/**
		 * Получить функцию с кэшированным результатом
		 * 
		 * @return Функция с кэшированными результатами
		 */
		public default IFunction31<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31, Ret_Type> cached(
				Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17, Arg18 arg18,
				Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28, Arg29 arg29, Arg30 arg30, Arg31 arg31)
		{
			Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30,
					arg31);
			return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26, a27, a28, a29, a30, a31) -> ret;
		}

		/**
		 * Получить функцию без возвращаемого типа
		 * 
		 * @return Новую фунцию
		 */
		public default IFunction31NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31> asNoReturn()
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31) -> apply(
					arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31);
		}

	}

	/**
	 * Версия функции IFunction31 без возвращаемого типа (NoR = No return)
	 * 
	 * @author swayfarer
	 */
	@FunctionalInterface
	public static interface IFunction31NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31>
			extends
			IFunction31<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31, Void> {

		@Override
		public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28, Arg29 arg29, Arg30 arg30, Arg31 arg31)
		{
			applyNoR(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31);
			return null;
		}

		public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28, Arg29 arg29, Arg30 arg30, Arg31 arg31);

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction31NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31> andAfter(
				IFunction31NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31);
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31);
			};
		}

		public default IFunction31NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31> andThan(
				IFunction31NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31> fun)
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
		public default IFunction31NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31> andBefore(
				IFunction31NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31);
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31);
			};
		}

	}

	@FunctionalInterface
	public static interface IFunction32<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31, Arg32, Ret_Type> {

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type __(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28, Arg29 arg29, Arg30 arg30, Arg31 arg31, Arg32 arg32)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31, arg32);
		}

		public default IFunction32<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31, Arg32, Ret_Type> andApply(
				IFunction1<Ret_Type, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31,
					arg32) -> fun.apply(apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29,
							arg30, arg31, arg32));
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type $(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28, Arg29 arg29, Arg30 arg30, Arg31 arg31, Arg32 arg32)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31, arg32);
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16,
				Arg17 arg17, Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28, Arg29 arg29, Arg30 arg30, Arg31 arg31, Arg32 arg32)
		{
			return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31, arg32);
		}

		/**
		 * Применить функцию
		 * 
		 * @return Возвращаемое значение функции
		 */
		public Ret_Type apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28, Arg29 arg29, Arg30 arg30, Arg31 arg31, Arg32 arg32);

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction32<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31, Arg32, Ret_Type> andAfter(
				IFunction32<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31, Arg32, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31, arg32) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31, arg32);
				return fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31,
						arg32);
			};
		}

		public default IFunction32<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31, Arg32, Ret_Type> andThan(
				IFunction32<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31, Arg32, Ret_Type> fun)
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
		public default IFunction32<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31, Arg32, Ret_Type> andBefore(
				IFunction32<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31, Arg32, Ret_Type> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31, arg32) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31,
						arg32);
				return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31,
						arg32);
			};
		}

		/**
		 * Получить функцию с кэшированным результатом
		 * 
		 * @return Функция с кэшированными результатами
		 */
		public default IFunction32<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31, Arg32, Ret_Type> cached(
				Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17, Arg18 arg18,
				Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28, Arg29 arg29, Arg30 arg30, Arg31 arg31, Arg32 arg32)
		{
			Ret_Type ret = apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31,
					arg32);
			return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26, a27, a28, a29, a30, a31, a32) -> ret;
		}

		/**
		 * Получить функцию без возвращаемого типа
		 * 
		 * @return Новую фунцию
		 */
		public default IFunction32NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31, Arg32> asNoReturn()
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31,
					arg32) -> apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30,
							arg31, arg32);
		}

	}

	/**
	 * Версия функции IFunction32 без возвращаемого типа (NoR = No return)
	 * 
	 * @author swayfarer
	 */
	@FunctionalInterface
	public static interface IFunction32NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31, Arg32>
			extends
			IFunction32<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31, Arg32, Void> {

		@Override
		public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28, Arg29 arg29, Arg30 arg30, Arg31 arg31, Arg32 arg32)
		{
			applyNoR(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31, arg32);
			return null;
		}

		public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8, Arg9 arg9, Arg10 arg10, Arg11 arg11, Arg12 arg12, Arg13 arg13, Arg14 arg14, Arg15 arg15, Arg16 arg16, Arg17 arg17,
				Arg18 arg18, Arg19 arg19, Arg20 arg20, Arg21 arg21, Arg22 arg22, Arg23 arg23, Arg24 arg24, Arg25 arg25, Arg26 arg26, Arg27 arg27, Arg28 arg28, Arg29 arg29, Arg30 arg30, Arg31 arg31, Arg32 arg32);

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction32NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31, Arg32> andAfter(
				IFunction32NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31, Arg32> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31, arg32) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31, arg32);
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31,
						arg32);
			};
		}

		public default IFunction32NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31, Arg32> andThan(
				IFunction32NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31, Arg32> fun)
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
		public default IFunction32NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31, Arg32> andBefore(
				IFunction32NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Arg9, Arg10, Arg11, Arg12, Arg13, Arg14, Arg15, Arg16, Arg17, Arg18, Arg19, Arg20, Arg21, Arg22, Arg23, Arg24, Arg25, Arg26, Arg27, Arg28, Arg29, Arg30, Arg31, Arg32> fun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31, arg32) -> {
				fun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31,
						arg32);
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24, arg25, arg26, arg27, arg28, arg29, arg30, arg31, arg32);
			};
		}

	}

}