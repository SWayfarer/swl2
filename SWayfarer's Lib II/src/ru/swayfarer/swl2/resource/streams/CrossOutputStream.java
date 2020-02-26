package ru.swayfarer.swl2.resource.streams;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipOutputStream;

import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;

/**
 * Расширенный поток, позволяющий выбрать обертку над собой
 * @author swayfarer
 *
 */
public class CrossOutputStream extends FilterOutputStream {

	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Конструктор */
	public CrossOutputStream(OutputStream out)
	{
		super(out);
	}
	
	
	
	/** Получить {@link GZIPOutputStream} */
	public GZIPOutputStream gz()
	{
		try
		{
			return new GZIPOutputStream(out);
		}
		catch (IOException e)
		{
			logger.error(e, "Error while creating GZIPOutputStream for", this);
		}
		
		return null;
	}
	
	/** Получить {@link ZipOutputStream} */
	public ZipOutputStream zip()
	{
		return new ZipOutputStream(out);
	}
	
	/** Получить {@link ObjectOutputStream} */
	public ObjectOutputStream objSerialize()
	{
		try
		{
			return new ObjectOutputStream(out);
		}
		catch (IOException e)
		{
			logger.error(e, "Error while creating ObjectOutputStream for", this);
		}
		
		return null;
	}
	
	/** Получить {@link DataOutputStreamSWL} */
	public DataOutputStreamSWL data()
	{
		return DataOutputStreamSWL.of(this);
	}
	
	/**
	 * Статический метод-фабрика для {@link CrossOutputStream}
	 * @param stream Оборачиваемый поток
	 */
	public static CrossOutputStream of(OutputStream stream)
	{
		ExceptionsUtils.IfNull(stream, IllegalArgumentException.class, "Wrapped stream can't be null!");
		
		return new CrossOutputStream(stream);
	}

}
