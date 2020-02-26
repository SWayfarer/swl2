package ru.swayfarer.swl2.resource.streams;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;

import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;

/**
 * Расширенный поток данных.
 * @author swayfarer
 *
 */
public class CrossInputStream extends FilterInputStream {

	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/**
	 * Конструктор
	 * @param in Оборачиваемый поток
	 */
	public CrossInputStream(InputStream in)
	{
		super(in);
	}
	
	/**
	 * Статический метод-фабрика для {@link CrossInputStream}
	 * @param stream Оборачиваемый поток
	 */
	public static CrossInputStream of(InputStream stream)
	{
		ExceptionsUtils.IfNull(stream, IllegalArgumentException.class, "Wrapped stream can't be null!");
		
		return new CrossInputStream(stream);
	}
	
	/** Получить {@link DataInputStreamSWL} */
	public DataInputStreamSWL data()
	{
		return DataInputStreamSWL.of(this);
	}
	
	/** Получить {@link DataInputStreamSWL} */
	public DataInputStreamSWL data(String encoding)
	{
		return DataInputStreamSWL.of(this, encoding);
	}
	
	/** Преобразовать в {@link GZIPInputStream} */
	public GZIPInputStream gz()
	{
		try
		{
			return new GZIPInputStream(this);
		}
		catch (IOException e)
		{
			logger.error(e, "Error while greating GZInputStream from", this);
		}
		
		return null;
	}
	
	/** Преобразовать в {@link ZipInputStream} */
	public ZipInputStream zip()
	{
		return new ZipInputStream(this);
	}
	
	/** Преобразовать в {@link ObjectInputStream} */
	public ObjectInputStream objSerialize()
	{
		try
		{
			return new ObjectInputStream(this);
		}
		catch (IOException e)
		{
			logger.error(e, "Error while greating ObjectInputStream from", this);
		}
		
		return null;
	}
}
