package ru.swayfarer.swl2.functions;

/* 
 * Автоматически сгенерировано с помощью ru.swayfarer.swl.functions.gen.FunctionsGenerator
 */

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Function;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class GeneratedFunctions {

	@FunctionalInterface
	public static interface IFunction0<Ret_Type> {

		/**
		 * Получить ничего не возвращающую функцию
		 * 
		 * @param methodReference
		 *            Метод, с которого будет снята функция. Пример:
		 *            System.out::printLn
		 * @return Новую функцию
		 */
		static IFunction0NoR consumerOf(IFunction0NoR methodReference)
		{
			return () -> {
				methodReference.apply();
			};
		}

		/**
		 * Применить функцию. Дубль для {@link #apply(Object)}
		 * 
		 * @return Возвращаемое значение функции
		 */
		default Ret_Type process()
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
		 * Получить старндартный Java-{@link Runnable}
		 * 
		 * @return Новый {@link Runnable}
		 */
		public default Runnable asJavaRunnable()
		{
			return () -> apply();
		}

		/**
		 * Добавить 'pre-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена до этой. Цепочка вернет
		 *            результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction0<Ret_Type> andBefore(IFunction0<Ret_Type> nextFun)
		{
			return () -> {
				nextFun.apply();
				return apply();
			};
		}

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction0<Ret_Type> andThan(IFunction0<Ret_Type> nextFun)
		{
			return () -> {
				apply();
				return nextFun.apply();
			};
		}

		/**
		 * Получить функцию без возвращаемого типа
		 * 
		 * @return Новую фунцию
		 */
		public default IFunction0NoR asNoReturnFun()
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

		/**
		 * Получить ничего не возвращающую функцию
		 * 
		 * @param methodReference
		 *            Метод, с которого будет снята функция. Пример:
		 *            System.out::printLn
		 * @return Новую функцию
		 */
		static IFunction0NoR valueOf(IFunction0NoR methodReference)
		{
			return () -> {
				methodReference.apply();
			};
		}

		public default Void apply()
		{
			applyNoR();
			return null;
		}

		public void applyNoR();

		/**
		 * Добавить 'pre-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена до этой. Цепочка вернет
		 *            результат последней функции
		 */
		public default IFunction0NoR andBefore(IFunction0NoR nextFun)
		{
			return () -> {
				nextFun.apply();
				apply();
			};
		}

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 */
		public default IFunction0NoR andThan(IFunction0NoR nextFun)
		{
			return () -> {
				apply();
				nextFun.apply();
			};
		}

	}

	@FunctionalInterface
	public static interface IFunction1<Arg1, Ret_Type> {

		/**
		 * Получить ничего не возвращающую функцию
		 * 
		 * @param methodReference
		 *            Метод, с которого будет снята функция. Пример:
		 *            System.out::printLn
		 * @return Новую функцию
		 */
		static <Arg1> IFunction1NoR<Arg1> consumerOf(IFunction1NoR<Arg1> methodReference)
		{
			return (arg1) -> {
				methodReference.apply(arg1);
			};
		}

		/**
		 * Применить функцию. Дубль для {@link #apply(Object)}
		 * 
		 * @return Возвращаемое значение функции
		 */
		default Ret_Type process(Arg1 arg1)
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
		 * Получить стандартный Java-{@link Consumer}
		 * 
		 * @return Возвращает новую функцию
		 */
		public default Consumer<Arg1> asJavaConsumer()
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
		 * Добавить 'pre-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена до этой. Цепочка вернет
		 *            результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction1<Arg1, Ret_Type> andBefore(IFunction1<Arg1, Ret_Type> nextFun)
		{
			return (arg1) -> {
				nextFun.apply(arg1);
				return apply(arg1);
			};
		}

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction1<Arg1, Ret_Type> andThan(IFunction1<Arg1, Ret_Type> nextFun)
		{
			return (arg1) -> {
				apply(arg1);
				return nextFun.apply(arg1);
			};
		}

		/**
		 * Получить функцию без возвращаемого типа
		 * 
		 * @return Новую фунцию
		 */
		public default IFunction1NoR<Arg1> asNoReturnFun()
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

		/**
		 * Получить ничего не возвращающую функцию
		 * 
		 * @param methodReference
		 *            Метод, с которого будет снята функция. Пример:
		 *            System.out::printLn
		 * @return Новую функцию
		 */
		static <Arg1> IFunction1NoR<Arg1> valueOf(IFunction1NoR<Arg1> methodReference)
		{
			return (arg1) -> {
				methodReference.apply(arg1);
			};
		}

		public default Void apply(Arg1 arg1)
		{
			applyNoR(arg1);
			return null;
		}

		public void applyNoR(Arg1 arg1);

		/**
		 * Добавить 'pre-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена до этой. Цепочка вернет
		 *            результат последней функции
		 */
		public default IFunction1NoR<Arg1> andBefore(IFunction1NoR<Arg1> nextFun)
		{
			return (arg1) -> {
				nextFun.apply(arg1);
				apply(arg1);
			};
		}

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 */
		public default IFunction1NoR<Arg1> andThan(IFunction1NoR<Arg1> nextFun)
		{
			return (arg1) -> {
				apply(arg1);
				nextFun.apply(arg1);
			};
		}

	}

	@FunctionalInterface
	public static interface IFunction2<Arg1, Arg2, Ret_Type> {

		/**
		 * Получить ничего не возвращающую функцию
		 * 
		 * @param methodReference
		 *            Метод, с которого будет снята функция. Пример:
		 *            System.out::printLn
		 * @return Новую функцию
		 */
		static <Arg1, Arg2> IFunction2NoR<Arg1, Arg2> consumerOf(IFunction2NoR<Arg1, Arg2> methodReference)
		{
			return (arg1, arg2) -> {
				methodReference.apply(arg1, arg2);
			};
		}

		/**
		 * Применить функцию. Дубль для {@link #apply(Object)}
		 * 
		 * @return Возвращаемое значение функции
		 */
		default Ret_Type process(Arg1 arg1, Arg2 arg2)
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

		/**
		 * Добавить 'pre-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена до этой. Цепочка вернет
		 *            результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction2<Arg1, Arg2, Ret_Type> andBefore(IFunction2<Arg1, Arg2, Ret_Type> nextFun)
		{
			return (arg1, arg2) -> {
				nextFun.apply(arg1, arg2);
				return apply(arg1, arg2);
			};
		}

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction2<Arg1, Arg2, Ret_Type> andThan(IFunction2<Arg1, Arg2, Ret_Type> nextFun)
		{
			return (arg1, arg2) -> {
				apply(arg1, arg2);
				return nextFun.apply(arg1, arg2);
			};
		}

		/**
		 * Получить функцию без возвращаемого типа
		 * 
		 * @return Новую фунцию
		 */
		public default IFunction2NoR<Arg1, Arg2> asNoReturnFun()
		{
			return (arg1, arg2) -> apply(arg1, arg2);
		}
	}

	/**
	 * Версия функции IFunction2 без возвращаемого типа (NoR = No return)
	 * 
	 * @author swayfarer
	 */
	@FunctionalInterface
	public static interface IFunction2NoR<Arg1, Arg2> extends IFunction2<Arg1, Arg2, Void> {

		/**
		 * Получить ничего не возвращающую функцию
		 * 
		 * @param methodReference
		 *            Метод, с которого будет снята функция. Пример:
		 *            System.out::printLn
		 * @return Новую функцию
		 */
		static <Arg1, Arg2> IFunction2NoR<Arg1, Arg2> valueOf(IFunction2NoR<Arg1, Arg2> methodReference)
		{
			return (arg1, arg2) -> {
				methodReference.apply(arg1, arg2);
			};
		}

		public default Void apply(Arg1 arg1, Arg2 arg2)
		{
			applyNoR(arg1, arg2);
			return null;
		}

		public void applyNoR(Arg1 arg1, Arg2 arg2);

		/**
		 * Добавить 'pre-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена до этой. Цепочка вернет
		 *            результат последней функции
		 */
		public default IFunction2NoR<Arg1, Arg2> andBefore(IFunction2NoR<Arg1, Arg2> nextFun)
		{
			return (arg1, arg2) -> {
				nextFun.apply(arg1, arg2);
				apply(arg1, arg2);
			};
		}

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 */
		public default IFunction2NoR<Arg1, Arg2> andThan(IFunction2NoR<Arg1, Arg2> nextFun)
		{
			return (arg1, arg2) -> {
				apply(arg1, arg2);
				nextFun.apply(arg1, arg2);
			};
		}

	}

	@FunctionalInterface
	public static interface IFunction3<Arg1, Arg2, Arg3, Ret_Type> {

		/**
		 * Получить ничего не возвращающую функцию
		 * 
		 * @param methodReference
		 *            Метод, с которого будет снята функция. Пример:
		 *            System.out::printLn
		 * @return Новую функцию
		 */
		static <Arg1, Arg2, Arg3> IFunction3NoR<Arg1, Arg2, Arg3> consumerOf(IFunction3NoR<Arg1, Arg2, Arg3> methodReference)
		{
			return (arg1, arg2, arg3) -> {
				methodReference.apply(arg1, arg2, arg3);
			};
		}

		/**
		 * Применить функцию. Дубль для {@link #apply(Object)}
		 * 
		 * @return Возвращаемое значение функции
		 */
		default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3)
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
		 * Добавить 'pre-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена до этой. Цепочка вернет
		 *            результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction3<Arg1, Arg2, Arg3, Ret_Type> andBefore(IFunction3<Arg1, Arg2, Arg3, Ret_Type> nextFun)
		{
			return (arg1, arg2, arg3) -> {
				nextFun.apply(arg1, arg2, arg3);
				return apply(arg1, arg2, arg3);
			};
		}

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction3<Arg1, Arg2, Arg3, Ret_Type> andThan(IFunction3<Arg1, Arg2, Arg3, Ret_Type> nextFun)
		{
			return (arg1, arg2, arg3) -> {
				apply(arg1, arg2, arg3);
				return nextFun.apply(arg1, arg2, arg3);
			};
		}

		/**
		 * Получить функцию без возвращаемого типа
		 * 
		 * @return Новую фунцию
		 */
		public default IFunction3NoR<Arg1, Arg2, Arg3> asNoReturnFun()
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

		/**
		 * Получить ничего не возвращающую функцию
		 * 
		 * @param methodReference
		 *            Метод, с которого будет снята функция. Пример:
		 *            System.out::printLn
		 * @return Новую функцию
		 */
		static <Arg1, Arg2, Arg3> IFunction3NoR<Arg1, Arg2, Arg3> valueOf(IFunction3NoR<Arg1, Arg2, Arg3> methodReference)
		{
			return (arg1, arg2, arg3) -> {
				methodReference.apply(arg1, arg2, arg3);
			};
		}

		public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3)
		{
			applyNoR(arg1, arg2, arg3);
			return null;
		}

		public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3);

		/**
		 * Добавить 'pre-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена до этой. Цепочка вернет
		 *            результат последней функции
		 */
		public default IFunction3NoR<Arg1, Arg2, Arg3> andBefore(IFunction3NoR<Arg1, Arg2, Arg3> nextFun)
		{
			return (arg1, arg2, arg3) -> {
				nextFun.apply(arg1, arg2, arg3);
				apply(arg1, arg2, arg3);
			};
		}

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 */
		public default IFunction3NoR<Arg1, Arg2, Arg3> andThan(IFunction3NoR<Arg1, Arg2, Arg3> nextFun)
		{
			return (arg1, arg2, arg3) -> {
				apply(arg1, arg2, arg3);
				nextFun.apply(arg1, arg2, arg3);
			};
		}

	}

	@FunctionalInterface
	public static interface IFunction4<Arg1, Arg2, Arg3, Arg4, Ret_Type> {

		/**
		 * Получить ничего не возвращающую функцию
		 * 
		 * @param methodReference
		 *            Метод, с которого будет снята функция. Пример:
		 *            System.out::printLn
		 * @return Новую функцию
		 */
		static <Arg1, Arg2, Arg3, Arg4> IFunction4NoR<Arg1, Arg2, Arg3, Arg4> consumerOf(IFunction4NoR<Arg1, Arg2, Arg3, Arg4> methodReference)
		{
			return (arg1, arg2, arg3, arg4) -> {
				methodReference.apply(arg1, arg2, arg3, arg4);
			};
		}

		/**
		 * Применить функцию. Дубль для {@link #apply(Object)}
		 * 
		 * @return Возвращаемое значение функции
		 */
		default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4)
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
		 * Добавить 'pre-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена до этой. Цепочка вернет
		 *            результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction4<Arg1, Arg2, Arg3, Arg4, Ret_Type> andBefore(IFunction4<Arg1, Arg2, Arg3, Arg4, Ret_Type> nextFun)
		{
			return (arg1, arg2, arg3, arg4) -> {
				nextFun.apply(arg1, arg2, arg3, arg4);
				return apply(arg1, arg2, arg3, arg4);
			};
		}

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction4<Arg1, Arg2, Arg3, Arg4, Ret_Type> andThan(IFunction4<Arg1, Arg2, Arg3, Arg4, Ret_Type> nextFun)
		{
			return (arg1, arg2, arg3, arg4) -> {
				apply(arg1, arg2, arg3, arg4);
				return nextFun.apply(arg1, arg2, arg3, arg4);
			};
		}

		/**
		 * Получить функцию без возвращаемого типа
		 * 
		 * @return Новую фунцию
		 */
		public default IFunction4NoR<Arg1, Arg2, Arg3, Arg4> asNoReturnFun()
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

		/**
		 * Получить ничего не возвращающую функцию
		 * 
		 * @param methodReference
		 *            Метод, с которого будет снята функция. Пример:
		 *            System.out::printLn
		 * @return Новую функцию
		 */
		static <Arg1, Arg2, Arg3, Arg4> IFunction4NoR<Arg1, Arg2, Arg3, Arg4> valueOf(IFunction4NoR<Arg1, Arg2, Arg3, Arg4> methodReference)
		{
			return (arg1, arg2, arg3, arg4) -> {
				methodReference.apply(arg1, arg2, arg3, arg4);
			};
		}

		public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4)
		{
			applyNoR(arg1, arg2, arg3, arg4);
			return null;
		}

		public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4);

		/**
		 * Добавить 'pre-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена до этой. Цепочка вернет
		 *            результат последней функции
		 */
		public default IFunction4NoR<Arg1, Arg2, Arg3, Arg4> andBefore(IFunction4NoR<Arg1, Arg2, Arg3, Arg4> nextFun)
		{
			return (arg1, arg2, arg3, arg4) -> {
				nextFun.apply(arg1, arg2, arg3, arg4);
				apply(arg1, arg2, arg3, arg4);
			};
		}

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 */
		public default IFunction4NoR<Arg1, Arg2, Arg3, Arg4> andThan(IFunction4NoR<Arg1, Arg2, Arg3, Arg4> nextFun)
		{
			return (arg1, arg2, arg3, arg4) -> {
				apply(arg1, arg2, arg3, arg4);
				nextFun.apply(arg1, arg2, arg3, arg4);
			};
		}

	}

	@FunctionalInterface
	public static interface IFunction5<Arg1, Arg2, Arg3, Arg4, Arg5, Ret_Type> {

		/**
		 * Получить ничего не возвращающую функцию
		 * 
		 * @param methodReference
		 *            Метод, с которого будет снята функция. Пример:
		 *            System.out::printLn
		 * @return Новую функцию
		 */
		static <Arg1, Arg2, Arg3, Arg4, Arg5> IFunction5NoR<Arg1, Arg2, Arg3, Arg4, Arg5> consumerOf(IFunction5NoR<Arg1, Arg2, Arg3, Arg4, Arg5> methodReference)
		{
			return (arg1, arg2, arg3, arg4, arg5) -> {
				methodReference.apply(arg1, arg2, arg3, arg4, arg5);
			};
		}

		/**
		 * Применить функцию. Дубль для {@link #apply(Object)}
		 * 
		 * @return Возвращаемое значение функции
		 */
		default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5)
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
		 * Добавить 'pre-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена до этой. Цепочка вернет
		 *            результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction5<Arg1, Arg2, Arg3, Arg4, Arg5, Ret_Type> andBefore(IFunction5<Arg1, Arg2, Arg3, Arg4, Arg5, Ret_Type> nextFun)
		{
			return (arg1, arg2, arg3, arg4, arg5) -> {
				nextFun.apply(arg1, arg2, arg3, arg4, arg5);
				return apply(arg1, arg2, arg3, arg4, arg5);
			};
		}

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction5<Arg1, Arg2, Arg3, Arg4, Arg5, Ret_Type> andThan(IFunction5<Arg1, Arg2, Arg3, Arg4, Arg5, Ret_Type> nextFun)
		{
			return (arg1, arg2, arg3, arg4, arg5) -> {
				apply(arg1, arg2, arg3, arg4, arg5);
				return nextFun.apply(arg1, arg2, arg3, arg4, arg5);
			};
		}

		/**
		 * Получить функцию без возвращаемого типа
		 * 
		 * @return Новую фунцию
		 */
		public default IFunction5NoR<Arg1, Arg2, Arg3, Arg4, Arg5> asNoReturnFun()
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

		/**
		 * Получить ничего не возвращающую функцию
		 * 
		 * @param methodReference
		 *            Метод, с которого будет снята функция. Пример:
		 *            System.out::printLn
		 * @return Новую функцию
		 */
		static <Arg1, Arg2, Arg3, Arg4, Arg5> IFunction5NoR<Arg1, Arg2, Arg3, Arg4, Arg5> valueOf(IFunction5NoR<Arg1, Arg2, Arg3, Arg4, Arg5> methodReference)
		{
			return (arg1, arg2, arg3, arg4, arg5) -> {
				methodReference.apply(arg1, arg2, arg3, arg4, arg5);
			};
		}

		public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5)
		{
			applyNoR(arg1, arg2, arg3, arg4, arg5);
			return null;
		}

		public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5);

		/**
		 * Добавить 'pre-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена до этой. Цепочка вернет
		 *            результат последней функции
		 */
		public default IFunction5NoR<Arg1, Arg2, Arg3, Arg4, Arg5> andBefore(IFunction5NoR<Arg1, Arg2, Arg3, Arg4, Arg5> nextFun)
		{
			return (arg1, arg2, arg3, arg4, arg5) -> {
				nextFun.apply(arg1, arg2, arg3, arg4, arg5);
				apply(arg1, arg2, arg3, arg4, arg5);
			};
		}

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 */
		public default IFunction5NoR<Arg1, Arg2, Arg3, Arg4, Arg5> andThan(IFunction5NoR<Arg1, Arg2, Arg3, Arg4, Arg5> nextFun)
		{
			return (arg1, arg2, arg3, arg4, arg5) -> {
				apply(arg1, arg2, arg3, arg4, arg5);
				nextFun.apply(arg1, arg2, arg3, arg4, arg5);
			};
		}

	}

	@FunctionalInterface
	public static interface IFunction6<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Ret_Type> {

		/**
		 * Получить ничего не возвращающую функцию
		 * 
		 * @param methodReference
		 *            Метод, с которого будет снята функция. Пример:
		 *            System.out::printLn
		 * @return Новую функцию
		 */
		static <Arg1, Arg2, Arg3, Arg4, Arg5, Arg6> IFunction6NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6> consumerOf(IFunction6NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6> methodReference)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6) -> {
				methodReference.apply(arg1, arg2, arg3, arg4, arg5, arg6);
			};
		}

		/**
		 * Применить функцию. Дубль для {@link #apply(Object)}
		 * 
		 * @return Возвращаемое значение функции
		 */
		default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6)
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
		 * Добавить 'pre-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена до этой. Цепочка вернет
		 *            результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction6<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Ret_Type> andBefore(IFunction6<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Ret_Type> nextFun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6) -> {
				nextFun.apply(arg1, arg2, arg3, arg4, arg5, arg6);
				return apply(arg1, arg2, arg3, arg4, arg5, arg6);
			};
		}

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction6<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Ret_Type> andThan(IFunction6<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Ret_Type> nextFun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6);
				return nextFun.apply(arg1, arg2, arg3, arg4, arg5, arg6);
			};
		}

		/**
		 * Получить функцию без возвращаемого типа
		 * 
		 * @return Новую фунцию
		 */
		public default IFunction6NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6> asNoReturnFun()
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

		/**
		 * Получить ничего не возвращающую функцию
		 * 
		 * @param methodReference
		 *            Метод, с которого будет снята функция. Пример:
		 *            System.out::printLn
		 * @return Новую функцию
		 */
		static <Arg1, Arg2, Arg3, Arg4, Arg5, Arg6> IFunction6NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6> valueOf(IFunction6NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6> methodReference)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6) -> {
				methodReference.apply(arg1, arg2, arg3, arg4, arg5, arg6);
			};
		}

		public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6)
		{
			applyNoR(arg1, arg2, arg3, arg4, arg5, arg6);
			return null;
		}

		public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6);

		/**
		 * Добавить 'pre-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена до этой. Цепочка вернет
		 *            результат последней функции
		 */
		public default IFunction6NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6> andBefore(IFunction6NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6> nextFun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6) -> {
				nextFun.apply(arg1, arg2, arg3, arg4, arg5, arg6);
				apply(arg1, arg2, arg3, arg4, arg5, arg6);
			};
		}

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 */
		public default IFunction6NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6> andThan(IFunction6NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6> nextFun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6);
				nextFun.apply(arg1, arg2, arg3, arg4, arg5, arg6);
			};
		}

	}

	@FunctionalInterface
	public static interface IFunction7<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Ret_Type> {

		/**
		 * Получить ничего не возвращающую функцию
		 * 
		 * @param methodReference
		 *            Метод, с которого будет снята функция. Пример:
		 *            System.out::printLn
		 * @return Новую функцию
		 */
		static <Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7> IFunction7NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7> consumerOf(IFunction7NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7> methodReference)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7) -> {
				methodReference.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
			};
		}

		/**
		 * Применить функцию. Дубль для {@link #apply(Object)}
		 * 
		 * @return Возвращаемое значение функции
		 */
		default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7)
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
		 * Добавить 'pre-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена до этой. Цепочка вернет
		 *            результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction7<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Ret_Type> andBefore(IFunction7<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Ret_Type> nextFun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7) -> {
				nextFun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
				return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
			};
		}

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction7<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Ret_Type> andThan(IFunction7<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Ret_Type> nextFun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
				return nextFun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
			};
		}

		/**
		 * Получить функцию без возвращаемого типа
		 * 
		 * @return Новую фунцию
		 */
		public default IFunction7NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7> asNoReturnFun()
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

		/**
		 * Получить ничего не возвращающую функцию
		 * 
		 * @param methodReference
		 *            Метод, с которого будет снята функция. Пример:
		 *            System.out::printLn
		 * @return Новую функцию
		 */
		static <Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7> IFunction7NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7> valueOf(IFunction7NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7> methodReference)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7) -> {
				methodReference.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
			};
		}

		public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7)
		{
			applyNoR(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
			return null;
		}

		public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7);

		/**
		 * Добавить 'pre-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена до этой. Цепочка вернет
		 *            результат последней функции
		 */
		public default IFunction7NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7> andBefore(IFunction7NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7> nextFun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7) -> {
				nextFun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
			};
		}

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 */
		public default IFunction7NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7> andThan(IFunction7NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7> nextFun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
				nextFun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
			};
		}

	}

	@FunctionalInterface
	public static interface IFunction8<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Ret_Type> {

		/**
		 * Получить ничего не возвращающую функцию
		 * 
		 * @param methodReference
		 *            Метод, с которого будет снята функция. Пример:
		 *            System.out::printLn
		 * @return Новую функцию
		 */
		static <Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8> IFunction8NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8> consumerOf(IFunction8NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8> methodReference)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) -> {
				methodReference.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
			};
		}

		/**
		 * Применить функцию. Дубль для {@link #apply(Object)}
		 * 
		 * @return Возвращаемое значение функции
		 */
		default Ret_Type process(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8)
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
		 * Добавить 'pre-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена до этой. Цепочка вернет
		 *            результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction8<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Ret_Type> andBefore(IFunction8<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Ret_Type> nextFun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) -> {
				nextFun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
				return apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
			};
		}

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 * @return Новую функцию, свянанную цепочкой
		 */
		public default IFunction8<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Ret_Type> andThan(IFunction8<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8, Ret_Type> nextFun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
				return nextFun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
			};
		}

		/**
		 * Получить функцию без возвращаемого типа
		 * 
		 * @return Новую фунцию
		 */
		public default IFunction8NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8> asNoReturnFun()
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

		/**
		 * Получить ничего не возвращающую функцию
		 * 
		 * @param methodReference
		 *            Метод, с которого будет снята функция. Пример:
		 *            System.out::printLn
		 * @return Новую функцию
		 */
		static <Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8> IFunction8NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8> valueOf(IFunction8NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8> methodReference)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) -> {
				methodReference.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
			};
		}

		public default Void apply(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8)
		{
			applyNoR(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
			return null;
		}

		public void applyNoR(Arg1 arg1, Arg2 arg2, Arg3 arg3, Arg4 arg4, Arg5 arg5, Arg6 arg6, Arg7 arg7, Arg8 arg8);

		/**
		 * Добавить 'pre-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена до этой. Цепочка вернет
		 *            результат последней функции
		 */
		public default IFunction8NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8> andBefore(IFunction8NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8> nextFun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) -> {
				nextFun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
			};
		}

		/**
		 * Добавить 'post-функцию' функцию
		 * 
		 * @param nextFun
		 *            Функция, которая будет выполнена после этой. Цепочка
		 *            вернет результат последней функции
		 */
		public default IFunction8NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8> andThan(IFunction8NoR<Arg1, Arg2, Arg3, Arg4, Arg5, Arg6, Arg7, Arg8> nextFun)
		{
			return (arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) -> {
				apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
				nextFun.apply(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
			};
		}

	}

}
