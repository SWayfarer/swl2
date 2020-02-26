package ru.swayfarer.swl2.resource.streams;

import java.io.IOException;

import ru.swayfarer.swl2.binary.buffers.DynamicByteBuffer;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Читалка строк из потоков
 * <br> Можно задать любую последовательность символов как разделитель строк
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class StringLinesReadHelper {

	/** Логгер*/
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Разделитель строк  */
	@InternalElement
	public String lineSplitter = StringUtils.LF;
	
	@InternalElement
	public String encoding;
	
	/** Буфер прочитанных байтов */
	@InternalElement
	public DynamicByteBuffer buffer = new DynamicByteBuffer();
	
	/** В этот буффер попадают символы, которые подохреваются на совпадение с {@link #lineSplitter}. После отмены подохрений забрасываются в прочитанную строку. */
	@InternalElement
	public DynamicByteBuffer waiting = new DynamicByteBuffer(64);

	/** Задать разделитель строк */
	public <T extends StringLinesReadHelper> T setLineSplitter(String splitter)
	{
		this.lineSplitter = splitter;
		return (T) this;
	}
	
	/** Прочитать строку безопасно */
	public String readLineSafe(DataInputStreamSWL is)
	{
		try
		{
			return readLine(is);
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while reading line from", is, "\nEncoding:", encoding, "\nLine splitter:", lineSplitter);
		}
		
		return null;
	}
	
	/** Прочитать строку */
	public synchronized String readLine(DataInputStreamSWL in) throws IOException
	{
		if (in.isClosed())
			return null;
		
		if (StringUtils.isEmpty(encoding))
			encoding = "UTF-8";
		
		waiting.clear();
		buffer.clear();
		
		int unsignedByte;
		int splitterCompareIndex = 0;
		char ch;
		
		while ((unsignedByte = in.read()) > 0)
		{
			ch = (char) unsignedByte;
			
			if (ch == lineSplitter.charAt(splitterCompareIndex))
			{
				
				splitterCompareIndex ++;
				waiting.putUnsigned(unsignedByte);
			}
			else 
			{
				if (splitterCompareIndex > 0)
				{
					buffer.put(waiting);
					waiting.clear();
					splitterCompareIndex = 0;
				}
				
				buffer.putUnsigned(unsignedByte);
			}
			
			if (splitterCompareIndex == lineSplitter.length())
			{
				break;
			}
		}
		
		return new String(buffer.getOnlyUsedBytes(), encoding);
	}

}
