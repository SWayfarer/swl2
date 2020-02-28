package ru.swayfarer.swl2.resource.streams;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import ru.swayfarer.swl2.binary.BinaryUtils;
import ru.swayfarer.swl2.binary.buffers.DynamicByteBuffer;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;

/**
 * {@link InputStream}, который можно откатить назад, пока позволяет максимальный размер кэша 
 * @author swayfarer
 *
 */
public class BackableInputStream extends FilterInputStream {

	/** Максимальный размер кэша */
	public int maxBufferSize; 
	
	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Буффер, в котором хранятся кэшированные байты */
	@InternalElement
	public DynamicByteBuffer buffer = DynamicByteBuffer.allocateDirect();
	
	/** Кол-во "разчитанных" байтов */
	public int unreadBytesCount = 0;
	
	/** Конструктор, который укажет максимальный размер буффера как размер самого потока */
	public BackableInputStream(InputStream in)
	{
		super(in);
		ExceptionsUtils.safe(() -> maxBufferSize = in.available());
	}
	
	/** Конструктор */
	public BackableInputStream(InputStream in, int bufferSize)
	{
		super(in);
		maxBufferSize = bufferSize;
	}

	@Override
	public int available() throws IOException
	{
		return unreadBytesCount + super.available();
	}
	
	/** Отмотать несколько байтов назад */
	public void back(int bytesCount)
	{
		ExceptionsUtils.If(bytesCount + unreadBytesCount > maxBufferSize, ArrayIndexOutOfBoundsException.class, "Can't back bigger than cache size (", maxBufferSize, ")!");
		unreadBytesCount += bytesCount;
	}
	
	@Override
	public int read() throws IOException
	{
		if (unreadBytesCount > 0)
		{
			int i = buffer.lenght - unreadBytesCount --;
			return BinaryUtils.toUnsignedByte(buffer.get(i));
		}

		int ret = super.read();
		
		if (ret > -1)
		{
			checkBufferSize(1);
			buffer.putUnsigned(ret);
		}
		
		return ret;
	}
	
	@Override
	public int read(byte[] b) throws IOException
	{
		return read(b, 0, b.length);
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException
	{
		if (unreadBytesCount > 0)
		{
			if (unreadBytesCount >= len - off)
			{
				int readedSize = Math.min(len - off, buffer.lenght - buffer.position());
				buffer.get(b, off, len);
				unreadBytesCount -= readedSize;
				return readedSize;
			}
			else
			{
				return super.read(b, off + unreadBytesCount, len - read(b, off, unreadBytesCount));
			}
		}
		else
		{
			int ret = super.read(b, off, len);
			
			byte[] bytes = new byte[len - off];
			System.arraycopy(b, off, bytes, 0, len);
			checkBufferSize(len);
			buffer.put(bytes);
			
			return ret;
		}
	}
	
	/** Проверить размер буффера и обновить его, если что */
	public void checkBufferSize(int size)
	{
		int futureSize = size + buffer.lenght;
		
		if (futureSize > maxBufferSize)
		{
			buffer.removeBytesFromStart(futureSize - maxBufferSize);
		}
	}
}
