package ru.swayfarer.swl2.binary.buffers;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import ru.swayfarer.swl2.binary.BinaryUtils;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.resource.streams.BytesInputStreamSWL;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * 
 * 
 * Саморасширяющийса ByteBuffer. Обертка. 
 * 
 * <h1>Основано на: https://gist.github.com/DudeMartin/5273469</h1>
 * @author Martin Tuskevicius
 * @author swayfarer
 * @see ByteBuffer
 */
@SuppressWarnings("unchecked")
public class DynamicByteBuffer implements Comparable<ByteBuffer> {

	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Использовать HeapByteBuffer */
	public static final int STRATEGY_HEAP = 0;
	
	/** Использовать DirectByteBuffer */
	public static final int STRATEGY_DIRECT = 1;
	
	/** Стандартная стратегия аллокации буфферов */
	public static int defaultGrowStrategy = STRATEGY_DIRECT;
	
	/** Стандартный стартовый размер буффера в байтах */
	public static int defaultCapacity = 128;
	
	/** Стандартный rоэффицент роста буффера */
	public static float defaultExpandFactor = 2f;
	
	/** Стратерия аллокации буффера */
	@InternalElement
	public int growStrategyId = defaultGrowStrategy;
	
	/** Кодировка, используемая для строк в этом буффере */
	@InternalElement
	public String encoding;
	
	/** Обернутый буффер */
	@InternalElement
	protected ByteBuffer byteBuffer;
	
	/** Коэффицент роста буффера */
	@InternalElement
	protected float expandFactor;
	
	/** Кол-во использованных байт */
	@InternalElement
	public int lenght = 0;

	/**
	 * Конструктор 
	 */
	public DynamicByteBuffer()
	{
		this(defaultCapacity);
	}
	
	/** 
	 * Конструктор
	 * @param initialCapacity Начальный размер буффера в байтах 
	 */
	public DynamicByteBuffer(int initialCapacity)
	{
		this(initialCapacity, defaultExpandFactor);
	}
	
	/** 
	 * Конструктор
	 * @param initialCapacity Начальный размер буффера в байтах 
	 * @param expandFactor Коэффицент роста буффера 
	 */
	public DynamicByteBuffer(int initialCapacity, float expandFactor)
	{
		this(initialCapacity, expandFactor, defaultGrowStrategy);
	}
	
	/** 
	 * Конструктор
	 * @param initialCapacity Начальный размер буффера в байтах 
	 * @param expandFactor Коэффицент роста буффера 
	 * @param growStrategy Стратегия расширения буффера
	 */
	public DynamicByteBuffer(int initialCapacity, float expandFactor, int growStrategy)
	{
		if (expandFactor <= 1)
		{
			throw new IllegalArgumentException("The expand factor must be greater than 1!");
		}
		
		setGrowStrategy(growStrategy);
		this.byteBuffer = allocate(initialCapacity);
		this.expandFactor = expandFactor;
	}
	
	public DynamicByteBuffer removeBytesFromStart(int n)
	{
		int index = 0;
		
		for (int i = n; i < this.position(); i++)
		{
			this.put(index++, this.get(i));
			this.put(i, (byte) 0);
		}
		this.position(index);
		lenght -= n;
	    
	    return this;
	}
	
	/**
	 * Установить стратегию аллокации буфферов (см {@link #STRATEGY_HEAP}, {@link #STRATEGY_DIRECT})
	 */
	public DynamicByteBuffer setGrowStrategy(int strategyId)
	{
		this.growStrategyId = strategyId;
		
		return this;
	}
	
	/**
	 * Получить байты буффера как {@link BytesInputStreamSWL}
	 */
	public BytesInputStreamSWL toInputStream()
	{
		return BytesInputStreamSWL.createStream(getOnlyUsedBytes());
	}
	
	/**
	 * Получить стратегию аллокации буффера
	 */
	public int getGrowStrategy()
	{
		return growStrategyId;
	}
	
	/** Выделить новый буффер */
	@InternalElement
	public ByteBuffer allocate(int capacity)
	{
		switch (growStrategyId)
		{
			case STRATEGY_HEAP:
				return ByteBuffer.allocate(capacity);
			case STRATEGY_DIRECT:
				return ByteBuffer.allocateDirect(capacity);
			default:
				return ByteBuffer.allocate(capacity);
		}
	}
	
	/** См {@link ByteBuffer#capacity()} */
	public int capacity()
	{
		return byteBuffer.capacity();
	}

	/** См {@link ByteBuffer#clear()} */
	public Buffer clear()
	{
		lenght = 0;
		return ByteBufferCompatBridge.clear(byteBuffer);
	}

	/** См {@link ByteBuffer#flip()} */
	public Buffer flip()
	{
		return ByteBufferCompatBridge.flip(byteBuffer);
	}

	/** См {@link ByteBuffer#hasRemaining()} */
	public boolean hasRemaining()
	{
		return byteBuffer.hasRemaining();
	}

	/** См {@link ByteBuffer#isReadOnly()} */
	public boolean isReadOnly()
	{
		return byteBuffer.isReadOnly();
	}

	/** См {@link ByteBuffer#limit()} */
	public int limit()
	{
		return byteBuffer.limit();
	}

	/** См {@link ByteBuffer#limit(int)} */
	public Buffer limit(int newLimit)
	{
		return ByteBufferCompatBridge.limit(byteBuffer, newLimit);// byteBuffer.limit(newLimit);
	}

	/** См {@link ByteBuffer#mark()} */
	public Buffer mark()
	{
		return ByteBufferCompatBridge.mark(byteBuffer); //byteBuffer.mark();
	}

	/** См {@link ByteBuffer#position()} */
	public int position()
	{
		return byteBuffer.position();
	}

	/** См {@link ByteBuffer#position(int)} */
	public Buffer position(int newPosition)
	{
		return ByteBufferCompatBridge.position(byteBuffer, newPosition); //byteBuffer.position(newPosition);
	}

	/** См {@link ByteBuffer#remaining()} */
	public int remaining()
	{
		return byteBuffer.remaining();
	}

	/** См {@link ByteBuffer#reset()} */
	public Buffer reset()
	{
		return ByteBufferCompatBridge.reset(byteBuffer); //byteBuffer.reset();
	}

	/** См {@link ByteBuffer#rewind()} */
	public Buffer rewind()
	{
		return ByteBufferCompatBridge.reset(byteBuffer); //byteBuffer.rewind();
	}

	/** См {@link ByteBuffer#array()} */
	public byte[] array()
	{
		if (byteBuffer.hasArray())
			return byteBuffer.array();
		
		byte[] bytes = new byte[lenght];
		
		int pos = position();
		position(0);
		get(bytes);
		position(pos);
		
		return bytes;
	}
	
	/** Получить следующую строку из буффера */
	public String getString()
	{
		return getString(getInt());
	}

	/** Получить следующую строку из буффера с указанием длины */
	public String getString(int lenght)
	{
		return getString(null, lenght);
	}
	
	/** Получить следующую строку из буффера с указанием длины и кодировки */
	public String getString(String encoding, int lenght)
	{
		byte[] bytes = new byte[lenght];
		get(bytes);
		
		if (encoding == null)
			encoding = this.encoding;
		
		try
		{
			return encoding == null ? new String(bytes) : new String(bytes, encoding);
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while reading string");
		}
		
		return null;
	}
	
	public void putString(@ConcattedString Object... text)
	{
		putString((String)null, text);
	}
	
	public void putString(@ConcattedString String encoding, Object... text)
	{
		
		if (encoding == null)
			encoding = this.encoding;
		
		String s = StringUtils.concatWithSpaces(text);
		
		if (StringUtils.isEmpty(s))
			return;
		
		try
		{
			byte[] bytes = encoding == null ? s.getBytes() : s.getBytes(encoding);
			putInt(bytes.length);
			put(bytes);
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while writing string");
		}
	}

	/** См {@link ByteBuffer#arrayOffset()} */
	public int arrayOffset()
	{
		return byteBuffer.arrayOffset();
	}

	/** См {@link ByteBuffer#compact()} */
	public ByteBuffer compact()
	{
		return byteBuffer.compact();
	}

	/** См {@link ByteBuffer#compareTo(ByteBuffer) } */
	public int compareTo(ByteBuffer that)
	{
		return byteBuffer.compareTo(that);
	}

	/** См {@link ByteBuffer#duplicate() } */
	public ByteBuffer duplicate()
	{
		return byteBuffer.duplicate();
	}

	/** См {@link ByteBuffer#equals(Object) } */
	public boolean equals(Object ob)
	{
		return byteBuffer.equals(ob);
	}

	/** См {@link ByteBuffer#get() } */
	public byte get()
	{
		return byteBuffer.get();
	}

	public ByteBuffer get(byte[] dst)
	{
		return byteBuffer.get(dst);
	}

	public ByteBuffer get(byte[] dst, int offset, int length)
	{
		return byteBuffer.get(dst, offset, length);
	}

	public byte get(int index)
	{
		return byteBuffer.get(index);
	}

	public char getChar()
	{
		return byteBuffer.getChar();
	}

	public char getChar(int index)
	{
		return byteBuffer.getChar(index);
	}
	
	public IntBuffer asFilledIntBuffer()
	{
		int pos = position();
		position(0);
		IntBuffer ret = getByteBuffer().asIntBuffer();
		position(pos);
		return ret;
	}
	
	public CharBuffer asFilledCharBuffer()
	{
		int pos = position();
		position(0);
		CharBuffer ret = getByteBuffer().asCharBuffer();
		position(pos);
		return ret;
	}
	
	public FloatBuffer asFilledFloatBuffer()
	{
		int pos = position();
		position(0);
		FloatBuffer ret = getByteBuffer().asFloatBuffer();
		position(pos);
		return ret;
	}
	
	public DoubleBuffer asFilledDoubleBuffer()
	{
		int pos = position();
		position(0);
		DoubleBuffer ret = getByteBuffer().asDoubleBuffer();
		position(pos);
		return ret;
	}

	public double getDouble()
	{
		return byteBuffer.getDouble();
	}

	public double getDouble(int index)
	{
		return byteBuffer.getDouble(index);
	}

	public float getFloat()
	{
		return byteBuffer.getFloat();
	}

	public float getFloat(int index)
	{
		return byteBuffer.getFloat(index);
	}

	public int getInt()
	{
		return byteBuffer.getInt();
	}

	public int getInt(int index)
	{
		return byteBuffer.getInt(index);
	}

	public long getLong()
	{
		return byteBuffer.getLong();
	}

	public long getLong(int index)
	{
		return byteBuffer.getLong(index);
	}

	public short getShort()
	{
		return byteBuffer.getShort();
	}

	public short getShort(int index)
	{
		return byteBuffer.getShort(index);
	}

	public boolean hasArray()
	{
		return byteBuffer.hasArray();
	}

	public boolean isDirect()
	{
		return byteBuffer.isDirect();
	}

	public ByteOrder order()
	{
		return byteBuffer.order();
	}
	
	public static DynamicByteBuffer wrap(byte[] bytes)
	{
		if (bytes == null)
			return null;
		
		return new DynamicByteBuffer(bytes.length, defaultExpandFactor).put(bytes);
	}
	
	/**
	 * Получить массив только использованных байт
	 * @return
	 */
	public byte[] getOnlyUsedBytes()
	{
		byte[] bytes = new byte[lenght];
		System.arraycopy(array(), 0, bytes, 0, lenght);
		
		return bytes;
	}

	public DynamicByteBuffer order(ByteOrder bo)
	{
		byteBuffer.order(bo);
		return this;
	}

	public DynamicByteBuffer put(byte b)
	{
		ensureSpace(1);
		byteBuffer.put(b);
		return this;
	}

	public DynamicByteBuffer put(DynamicByteBuffer buf)
	{
		put(buf.getOnlyUsedBytes());
		return this;
	}
	
	public DynamicByteBuffer put(byte[] src)
	{
		ensureSpace(src.length);
		byteBuffer.put(src);
		return this;
	}

	public DynamicByteBuffer put(byte[] src, int offset, int length)
	{
		ensureSpace(length);
		byteBuffer.put(src, offset, length);
		return this;
	}

	public DynamicByteBuffer putUnsigned(int i)
	{
		return put(BinaryUtils.toSignedByte(i));
	}
	
	public DynamicByteBuffer put(ByteBuffer src)
	{
		ensureSpace(src.remaining());
		byteBuffer.put(src);
		return this;
	}

	public DynamicByteBuffer put(int index, byte b)
	{
		ensureSpace(1);
		byteBuffer.put(index, b);
		return this;
	}

	public DynamicByteBuffer putChar(char value)
	{
		ensureSpace(2);
		byteBuffer.putChar(value); return this;
	}

	public DynamicByteBuffer putChar(int index, char value)
	{
		ensureSpace(2);
		byteBuffer.putChar(index, value); return this;
	}

	public DynamicByteBuffer putDouble(double value)
	{
		ensureSpace(8);
		byteBuffer.putDouble(value); return this;
	}

	public DynamicByteBuffer putDouble(int index, double value)
	{
		ensureSpace(8);
		byteBuffer.putDouble(index, value); return this;
	}

	public DynamicByteBuffer putFloat(float value)
	{
		ensureSpace(4);
		byteBuffer.putFloat(value); return this;
	}

	public DynamicByteBuffer putFloat(int index, float value)
	{
		ensureSpace(4);
		byteBuffer.putFloat(index, value); return this;
	}

	public DynamicByteBuffer putInt(int value)
	{
		ensureSpace(4);
		byteBuffer.putInt(value); return this;
	}

	public DynamicByteBuffer putInt(int index, int value)
	{
		ensureSpace(4);
		byteBuffer.putInt(index, value); return this;
	}

	public DynamicByteBuffer putLong(int index, long value)
	{
		ensureSpace(8);
		byteBuffer.putLong(index, value); return this;
	}

	public DynamicByteBuffer putLong(long value)
	{
		ensureSpace(8);
		byteBuffer.putLong(value); return this;
	}

	public DynamicByteBuffer putShort(int index, short value)
	{
		ensureSpace(2);
		byteBuffer.putShort(index, value); return this;
	}

	public DynamicByteBuffer putShort(short value)
	{
		ensureSpace(2);
		byteBuffer.putShort(value); return this;
	}

	public ByteBuffer slice()
	{
		return byteBuffer.slice();
	}

	@Override
	public int hashCode()
	{
		return byteBuffer.hashCode();
	}

	@Override
	public String toString()
	{
		return byteBuffer.toString();
	}

	public ByteBuffer getByteBuffer()
	{
		return byteBuffer;
	}

	public void setExpandFactor(float expandFactor)
	{
		if (expandFactor < 1)
		{
			throw new IllegalArgumentException("The expand factor must be greater or equal to 1!");
		}
		this.expandFactor = expandFactor;
	}

	public float getExpandFactor()
	{
		return expandFactor;
	}

	protected void ensureSpace(int needed)
	{
		if (remaining() < needed)
		{
			int newCapacity = (int) (byteBuffer.capacity() * expandFactor);
			
			while (newCapacity < (byteBuffer.capacity() + needed))
			{
				newCapacity *= expandFactor;
			}
			
			ByteBuffer expanded = allocate(newCapacity);

			expanded.order(byteBuffer.order());
			
			ByteBufferCompatBridge.position(byteBuffer, 0);
			//byteBuffer.position(0);
			
			expanded.put(getOnlyUsedBytes());
			
			byteBuffer = expanded;
		}
		
		int i1 = position() + needed - lenght;
		
		if (i1 > 0)
			lenght += i1;
	}
	
	public static <T extends DynamicByteBuffer> T allocateHeap()
	{
		return (T) new DynamicByteBuffer(defaultCapacity, defaultExpandFactor, STRATEGY_HEAP);
	}
	
	public static <T extends DynamicByteBuffer> T allocateDirect()
	{
		return (T) new DynamicByteBuffer(defaultCapacity, defaultExpandFactor, STRATEGY_DIRECT);
	}
	
	public static <T extends DynamicByteBuffer> T allocateDirect(int capacity)
	{
		return (T) new DynamicByteBuffer(capacity, defaultExpandFactor, STRATEGY_DIRECT);
	}
}