package ru.swayfarer.swl2.binary;

import java.nio.ByteOrder;

import lombok.var;
import ru.swayfarer.swl2.binary.buffers.DynamicByteBuffer;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedMap;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.string.reader.StringReaderSWL;
import ru.swayfarer.swl2.threads.ThreadsUtils;

/**
 * Утилиты для работы с бинарными операциями 
 * @author swayfarer
 *
 */
public class BinaryUtils {

	public static IExtendedMap<String, Long> registeredSizeUnits = CollectionsSWL.createExtendedMap(
			"kb", 1024l,
			"mb", 1024l * 1024,
			"gb", 1024l * 1024 * 1024
	);
	
	public static int SHORT_BYTES_SIZE = 2;
	public static int INT_BYTES_SIZE = 4;
	public static int FLOAT_BYTES_SIZE = 4;
	public static int DOUBLE_BYTES_SIZE = 8;
	public static int LONG_BYTES_SIZE = 8;
	
	public static ByteOrder DEFAULT_BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;
	
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
	
	/** Получить байты числа в {@link #DEFAULT_BYTE_ORDER} */
	public static byte[] toBytes(short i)
	{
		DynamicByteBuffer buffer = ThreadsUtils.getThreadLocal(DynamicByteBuffer::allocateDirect);
		buffer.putShort(i);
		byte[] ret = buffer.getOnlyUsedBytes();
		buffer.clear();
		return ret;
	}
	
	/** Получить байты числа в {@link #DEFAULT_BYTE_ORDER} */
	public static byte[] toBytes(int i)
	{
		DynamicByteBuffer buffer = ThreadsUtils.getThreadLocal(DynamicByteBuffer::allocateDirect);
		buffer.putInt(i);
		byte[] ret = buffer.getOnlyUsedBytes();
		buffer.clear();
		return ret;
	}
	
	/** Получить байты числа в {@link #DEFAULT_BYTE_ORDER} */
	public static byte[] toBytes(long i)
	{
		DynamicByteBuffer buffer = ThreadsUtils.getThreadLocal(DynamicByteBuffer::allocateDirect);
		buffer.putLong(i);
		byte[] ret = buffer.getOnlyUsedBytes();
		buffer.clear();
		return ret;
	}
	
	/** Получить байты числа в {@link #DEFAULT_BYTE_ORDER} */
	public static byte[] toBytes(float i)
	{
		DynamicByteBuffer buffer = ThreadsUtils.getThreadLocal(DynamicByteBuffer::allocateDirect);
		buffer.putFloat(i);
		byte[] ret = buffer.getOnlyUsedBytes();
		buffer.clear();
		return ret;
	}
	
	/** Получить байты числа в {@link #DEFAULT_BYTE_ORDER} */
	public static byte[] toBytes(double i)
	{
		DynamicByteBuffer buffer = ThreadsUtils.getThreadLocal(DynamicByteBuffer::allocateDirect);
		buffer.putDouble(i);
		byte[] ret = buffer.getOnlyUsedBytes();
		buffer.clear();
		return ret;
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
	
	public static long getBytes(String size)
	{
		var reader = new StringReaderSWL(size);
		
		int index = 0;
		
		while (reader.hasNextElement())
		{
			if (reader.skipSome(" ") || reader.skipSome(StringUtils.TAB)) 
			{
				index ++;
				// Nope!
			}
			else if (!StringUtils.isDouble(reader.next()))
				break;
			else
				index ++;
		}
		
		if (index < size.length())
		{
			var value = size.substring(0, index);
			var unit = size.substring(index);
			unit = StringUtils.removeLastWhitespaces(unit);
			
			Long unitModifier = registeredSizeUnits.get(unit);
			
			ExceptionsUtils.IfNull(unitModifier, IllegalArgumentException.class, "Size unit '" + unit + "' is not registered! Please, register it or use another! Available units:", registeredSizeUnits.keySet());
			
			reader.close();
			return Long.valueOf(value.trim()) * unitModifier;
		}
		
		reader.close();
		return Long.valueOf(size);
	}
	
}
