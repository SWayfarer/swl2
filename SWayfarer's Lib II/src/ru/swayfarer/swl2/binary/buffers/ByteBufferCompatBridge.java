package ru.swayfarer.swl2.binary.buffers;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * Мост для совместимости Java 8 и 8+
 * <br> В новых JVM методы {@link ByteBuffer} несколько изменились, из-за чего на разных java компилируются разные дескрипторы для них
 * <br> Этот мост создан, чтобы избежать {@link NoSuchMethodException}
 * @author swayfarer
 *
 */
public class ByteBufferCompatBridge {

	public static ByteBuffer position(ByteBuffer byteBuffer, int pos)
	{
		return (ByteBuffer) ((Buffer)byteBuffer).position(pos);
	}
	
	public static ByteBuffer limit(ByteBuffer byteBuffer, int limit)
	{
		return (ByteBuffer) ((Buffer)byteBuffer).limit(limit);
	}
	
	public static ByteBuffer mark(ByteBuffer byteBuffer)
	{
		return (ByteBuffer) ((Buffer)byteBuffer).mark();
	}
	
	public static ByteBuffer reset(ByteBuffer byteBuffer)
	{
		return (ByteBuffer) ((Buffer)byteBuffer).reset();
	}
	
	public static ByteBuffer clear(ByteBuffer byteBuffer)
	{
		return (ByteBuffer) ((Buffer)byteBuffer).clear();
	}
	
	public static ByteBuffer flip(ByteBuffer byteBuffer)
	{
		return (ByteBuffer) ((Buffer)byteBuffer).flip();
	}
	
	public static ByteBuffer rewind(ByteBuffer byteBuffer)
	{
		return (ByteBuffer) ((Buffer)byteBuffer).rewind();
	}	
}
