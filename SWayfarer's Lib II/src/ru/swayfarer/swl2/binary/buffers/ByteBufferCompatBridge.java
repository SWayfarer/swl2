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

	/** См {@link ByteBuffer#position(int)} */
	public static ByteBuffer position(ByteBuffer byteBuffer, int pos)
	{
		return (ByteBuffer) ((Buffer)byteBuffer).position(pos);
	}
	
	/** См {@link ByteBuffer#limit(int)} */
	public static ByteBuffer limit(ByteBuffer byteBuffer, int limit)
	{
		return (ByteBuffer) ((Buffer)byteBuffer).limit(limit);
	}
	
	/** См {@link ByteBuffer#mark()} */
	public static ByteBuffer mark(ByteBuffer byteBuffer)
	{
		return (ByteBuffer) ((Buffer)byteBuffer).mark();
	}
	
	/** См {@link ByteBuffer#reset()} */
	public static ByteBuffer reset(ByteBuffer byteBuffer)
	{
		return (ByteBuffer) ((Buffer)byteBuffer).reset();
	}
	
	/** См {@link ByteBuffer#clear()} */
	public static ByteBuffer clear(ByteBuffer byteBuffer)
	{
		return (ByteBuffer) ((Buffer)byteBuffer).clear();
	}
	
	/** См {@link ByteBuffer#flip()} */
	public static ByteBuffer flip(ByteBuffer byteBuffer)
	{
		return (ByteBuffer) ((Buffer)byteBuffer).flip();
	}
	
	/** См {@link ByteBuffer#rewind()} */
	public static ByteBuffer rewind(ByteBuffer byteBuffer)
	{
		return (ByteBuffer) ((Buffer)byteBuffer).rewind();
	}	
}
