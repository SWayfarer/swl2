package ru.swayfarer.swl2.logger.printers;

import java.io.PrintStream;
import java.util.Locale;

import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.SimpleObservable;
import ru.swayfarer.swl2.resource.streams.BytesOutputStreamSWL;

/**
 * Обертка на {@link PrintStream}, позволяющая навешать на его {@link IObservable}
 * @author swayfarer
 *
 */
public class PrintStreamWrapper extends PrintStream {

	/** Событие принта {@link PrintStream#print(boolean)} */
	public IObservable<PrintEvent> eventPrint = new SimpleObservable<>();
	
	/** Событие принта с переходом на новую строку {@link PrintStream#println(boolean)} */
	public IObservable<PrintEvent> eventPrintLn = new SimpleObservable<>();
	
	/** Обернутый поток */
	@InternalElement
	public PrintStream wrappedStream;
	
	/** Конструктор, оборачивающий {@link System#out} */
	public PrintStreamWrapper()
	{
		this(System.out);
	}
	
	/** Конструктор */
	public PrintStreamWrapper(PrintStream wrappedStream)
	{
		super(BytesOutputStreamSWL.createStream());
		ExceptionsUtils.IfNull(wrappedStream, IllegalArgumentException.class, "Wrapped stream can't be null!");
		this.wrappedStream = wrappedStream;
	}
	
	/** Перехватывалка всех println'ов */
	public void printlnObj(Object obj)
	{
		PrintEvent event = PrintEvent.of(this, String.valueOf(obj));
		eventPrintLn.next(event);
		
		if (event.isCanceled())
			return;
		
		wrappedStream.println(obj);
	}
	
	/** Перехватывалка всех print'ов */
	public void printObj(Object obj)
	{
		PrintEvent event = PrintEvent.of(this, String.valueOf(obj));
		eventPrint.next(event);
		
		if (event.isCanceled())
			return;
		
		wrappedStream.print(obj);
	}
	
	/*
	 * Биндинги
	 */
	
	
	@Override
	public PrintStream printf(Locale l, String format, Object... args)
	{
		String text = String.format(l, format, args);
		printObj(text);
		
		return this;
	}
	
	@Override
	public PrintStream printf(String format, Object... args)
	{
		String text = String.format(format, args);
		printObj(text);
		
		return this;
	}
	
	public static PrintStreamWrapper of(PrintStream printStream)
	{
		if (printStream == null)
			return null;
		
		if (printStream instanceof PrintStreamWrapper)
			return (PrintStreamWrapper) printStream;
		
		return new PrintStreamWrapper(printStream);
	}
	
	@Override
	public void println()
	{
		printlnObj("");
	}
	
	@Override
	public void println(boolean x)
	{
		printlnObj(x);
	}
	
	@Override
	public void println(char x)
	{
		printlnObj(x);
	}
	
	@Override
	public void println(char[] x)
	{
		printlnObj(x);
	}
	
	@Override
	public void println(double x)
	{
		printlnObj(x);
	}
	
	@Override
	public void println(float x)
	{
		printlnObj(x);
	}
	
	@Override
	public void println(int x)
	{
		printlnObj(x);
	}
	
	@Override
	public void println(long x)
	{
		printlnObj(x);
	}
	
	@Override
	public void println(Object x)
	{
		printlnObj(x);
	}
	
	@Override
	public void println(String x)
	{
		printlnObj(x);
	}
	
	@Override
	public void print(boolean b)
	{
		printObj(b);
	}
	
	@Override
	public void print(char c)
	{
		printObj(c);
	}
	
	@Override
	public void print(char[] s)
	{
		printObj(s);
	}
	
	@Override
	public void print(double d)
	{
		printObj(d);
	}
	
	@Override
	public void print(float f)
	{
		printObj(f);
	}
	
	@Override
	public void print(int i)
	{
		printObj(i);
	}
	
	@Override
	public void print(long l)
	{
		printObj(l);
	}
	
	@Override
	public void print(Object obj)
	{
		printObj(obj);
	}
	
	@Override
	public void print(String s)
	{
		printObj(s);
	}
}
