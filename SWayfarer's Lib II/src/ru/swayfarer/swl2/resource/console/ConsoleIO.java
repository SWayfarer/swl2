package ru.swayfarer.swl2.resource.console;

import java.io.PrintStream;
import java.util.Scanner;

import ru.swayfarer.swl2.ansi.AnsiFormatter;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.logger.SimpleLogLevel;
import ru.swayfarer.swl2.logger.SimpleLoggerSWL;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.resource.streams.DataInputStreamSWL;
import ru.swayfarer.swl2.string.StringUtils;

public class ConsoleIO {

	public static AnsiFormatter colorFormatter = AnsiFormatter.getInstance();
	
	public static DataInputStreamSWL dis = DataInputStreamSWL.of(System.in);
	public static Scanner scan = new Scanner(System.in);
	
	public static void hideColors()
	{
		colorFormatter = new SimpleLoggerSWL.ClearAnsiFormatter();
	}
	
	public static void info(@ConcattedString Object... text)
	{
		log(System.out, SimpleLogLevel.INFO_LOG_COLOR_PREFIX + StringUtils.concatWithSpaces(text));
	}
	
	public static void log(PrintStream stream, String str)
	{
		stream.println(colorFormatter.format(str));
	}

	public static void error(@ConcattedString Object... text)
	{
		log(System.err, SimpleLogLevel.ERROR_LOG_COLOR_PREFIX + StringUtils.concatWithSpaces(text));
	}
	
	public static Boolean readBoolean(String message, boolean isReTry)
	{
		String ln = readFilteredString(message, isReTry, StringUtils::isBoolean);
		return StringUtils.isEmpty(ln) ? null : Boolean.valueOf(ln);
	}
	
	public static Integer readInt(String message, boolean isReTry)
	{
		String ln = readFilteredString(message, isReTry, StringUtils::isInteger);
		return StringUtils.isEmpty(ln) ? null : Integer.valueOf(ln);
	}
	
	public static Long readLong(String message, boolean isReTry)
	{
		String ln = readFilteredString(message, isReTry, StringUtils::isLong);
		return StringUtils.isEmpty(ln) ? null : Long.valueOf(ln);
	}
	
	public static Float readFloat(String message, boolean isReTry)
	{
		String ln = readFilteredString(message, isReTry, StringUtils::isFloat);
		return StringUtils.isEmpty(ln) ? null : Float.valueOf(ln);
	}
	
	public static Double readDouble(String message, boolean isReTry)
	{
		String ln = readFilteredString(message, isReTry, StringUtils::isDouble);
		return StringUtils.isEmpty(ln) ? null : Double.valueOf(ln);
	}
	
	public static String readFilteredString(String message, boolean isReTry, IFunction1<String, Boolean> filter)
	{
		boolean isFirst = true;
		
		while (isFirst || isReTry)
		{
			if (!StringUtils.isBlank(message))
			{
				info(message);
			}
			
			String ln = dis.readStringLnSafe();
			
			if (ln == null)
				return null;
			
			if (filter == null || filter.apply(ln))
				return ln;
			
			isFirst = false;
		}
		
		return null;
	}
	
	public static String readString()
	{
		while (true)
		{
			try
			{
				if (scan.hasNextLine())
					return scan.nextLine();
			}
			catch (Throwable e)
			{
				scan = new Scanner(System.in);
				scan.nextByte();
			}
		}
	}
	
}
