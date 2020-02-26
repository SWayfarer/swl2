package ru.swayfarer.swl2.binary;

import ru.swayfarer.swl2.exceptions.ExceptionsUtils;

/**
 * Утилиты для работы с бинарными операциями 
 * @author swayfarer
 *
 */
public class BinaryUtils {

	public static int SHORT_BYTES_SIZE = 2;
	public static int INT_BYTES_SIZE = 4;
	public static int FLOAT_BYTES_SIZE = 4;
	public static int DOUBLE_BYTES_SIZE = 8;
	public static int LONG_BYTES_SIZE = 8;
	
	/** Записать unsigned byte в int */
	public static int toUnsignedByte(byte b)
	{
		if (b >= 0)
			return b;
		
		return ((int) b) - ((int) Byte.MIN_VALUE) * 2;
	}
	
	/** Записать unsigned int в long */
	public static long toUnsignedInt(int b)
	{
		if (b >= 0)
			return b;
		
		return ((long) b) - ((long) Integer.MIN_VALUE) * 2;
	}
	
	/** Записать unsigned float в double */
	public static double toUnsignedFloat(float b)
	{
		if (b >= 0)
			return b;
		
		return ((double) b) - ((double) Float.MIN_VALUE) * 2;
	}
	
	/** Преобразовать float, записанный double, в signed */
	public static float toSignedFloat(double i)
	{
		if (i > Float.MAX_VALUE)
			i =  i + Float.MIN_VALUE * 2;
		
		ExceptionsUtils.If(i > Float.MAX_VALUE, IllegalArgumentException.class, "Bigger double!");
		ExceptionsUtils.If(i < Float.MIN_VALUE, IllegalArgumentException.class, "Smaller double!");
		
		return (float) i;
	}
	
	/** Преобразовать int, записанный long, в signed */
	public static int toSignedInt(long i)
	{
		if (i > Integer.MAX_VALUE)
			i =  i + Integer.MIN_VALUE * 2;
		
		ExceptionsUtils.If(i > Float.MAX_VALUE, IllegalArgumentException.class, "Bigger double!");
		ExceptionsUtils.If(i < Float.MIN_VALUE, IllegalArgumentException.class, "Smaller double!");
		
		return (int) i;
	}
	
	/** Преобразовать byte, записанный int, в signed */
	public static byte toSignedByte(int i)
	{
		if (i > Byte.MAX_VALUE)
			i =  i + Byte.MIN_VALUE * 2;
		
		ExceptionsUtils.If(i > Byte.MAX_VALUE, IllegalArgumentException.class, "Bigger integer!");
		ExceptionsUtils.If(i < Byte.MIN_VALUE, IllegalArgumentException.class, "Smaller integer!");
		
		return (byte) i;
	}
	
}
