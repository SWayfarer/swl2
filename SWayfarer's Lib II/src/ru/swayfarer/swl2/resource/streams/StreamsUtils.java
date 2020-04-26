package ru.swayfarer.swl2.resource.streams;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.observable.property.ObservableProperty;

/**
 * Утилиты для работы с потоками данных 
 * @author swayfarer
 *
 */
public class StreamsUtils {

	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Размер буффреа, используемого для операций с потоками */
	public static final int BUFFER_SIZE = 1 << 12;

	/** Прочитать безопасно поток полностью и закрыть его. */
	public static byte[] readAllAndClose(InputStream is)
	{
		if (is == null)
			return null;

		try
		{
			byte[] bytes = readAll(is);
			is.close();
			return bytes;
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Прочитать поток как строку
	 * 
	 * @param encoding  Кодировка строки
	 * @param stream Читаемый поток
	 * @return Полученная строка
	 */
	public static String readAll(String encoding, InputStream stream)
	{
		if (stream == null)
			return null;

		try
		{
			InputStreamReader reader = encoding == null ? new InputStreamReader(stream) : new InputStreamReader(stream, encoding);

			char[] buffer = new char[1];

			StringBuilder builder = new StringBuilder();

			while (reader.read(buffer) != -1)
			{
				builder.append(buffer);
			}

			reader.close();

			return builder.toString();
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while reading stream " + stream);
		}

		return null;
	}

	/** Прочитать поток полностью */
	public static byte[] readAll(InputStream stream)
	{
		try
		{
			byte[] buffer = new byte[BUFFER_SIZE];

			int read;
			int totalLength = 0;
			while ((read = stream.read(buffer, totalLength, buffer.length - totalLength)) != -1)
			{
				totalLength += read;

				// Extend our buffer
				if (totalLength >= buffer.length - 1)
				{
					byte[] newBuffer = new byte[buffer.length + BUFFER_SIZE];
					System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
					buffer = newBuffer;
				}
			}

			final byte[] result = new byte[totalLength];
			System.arraycopy(buffer, 0, result, 0, totalLength);
			return result;
		}
		catch (Throwable t)
		{
			// LogWrapper.log(Level.WARN, t, "Problem loading class");
			return new byte[0];
		}
	}

	/** Копировать содержимое одного потока в другой с закрытием обоих */
	public static int copyStreamSafe(InputStream istream, OutputStream ostream)
	{
		return copyStreamSafe(istream, ostream, true, true);
	}

	/** Копировать содержимое одного потока в другой */
	public static int copyStreamSafe(InputStream istream, OutputStream ostream, boolean isCloseIn, boolean isCloseOut)
	{
		try
		{
			return copyStream(istream, ostream, isCloseIn, isCloseOut);
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while copyng stream", istream, "to", ostream);
		}

		return -1;
	}

	/** Копировать содержимое одного потока в другой */
	public static int copyStream(InputStream istream, OutputStream ostream, boolean isCloseIn, boolean isCloseOut) throws IOException
	{
		return copyStream(istream, ostream, null, isCloseIn, isCloseOut);
	}
	
	/** Копировать содержимое одного потока в другой */
	public static int copyStream(InputStream istream, OutputStream ostream, ObservableProperty<Integer> progressListener, boolean isCloseIn, boolean isCloseOut) throws IOException
	{
		ExceptionsUtils.IfNull(istream, IllegalArgumentException.class, "The Input stream can't be null!");
		ExceptionsUtils.IfNull(ostream, IllegalArgumentException.class, "The Out stream can't be null!");
		
		DataOutputStream out = new DataOutputStream(ostream);
		DataInputStreamSWL in = new DataInputStreamSWL(istream);

		byte[] buffer = new byte[BUFFER_SIZE];

		int totalLen = 0;
		
		int len = in.read(buffer);

		while (len != -1)
		{
			out.write(buffer, 0, len);
			totalLen += len;
			
			if (progressListener != null) {
				progressListener.setValue(totalLen);
			}
			
			len = in.read(buffer);
		}

		if (isCloseOut)
			out.close();

		if (isCloseIn)
			in.close();
		
		return totalLen;
	}

}
