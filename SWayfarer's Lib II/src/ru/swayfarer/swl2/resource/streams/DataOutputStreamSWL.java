package ru.swayfarer.swl2.resource.streams;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;

import ru.swayfarer.swl2.crypto.Cryptor;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.Alias;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Поток выходных данных
 * @author swayfarer
 */
@SuppressWarnings("unchecked")
public class DataOutputStreamSWL extends DataOutputStream{

	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Кодировка для работы со строками */
	@InternalElement
	public String encoding = "UTF-8";
	
	/** Разделитель строк, для автоматической подстановки */
	@InternalElement
	public String lineSplitter = StringUtils.LF;
	
	/** Конструктор */
	public DataOutputStreamSWL(OutputStream out)
	{
		super(out);
	}
	
	/** Записать инты */
	public void writeInts(int... ints) throws IOException
	{
		for (int i : ints)
		{
			writeInt(i);
		}
	}
	
	/** Записать инт безопасно */
	public boolean writeIntSafe(int i)
	{
		try
		{
			writeInt(i);
			return true;
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while writing int", i, "to stream", this);
		}
		
		return false;
	}

	/** Записать double безопасно */
	public boolean writeDoubleSafe(double i)
	{
		try
		{
			writeDouble(i);
			return true;
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while writing double", i, "to stream", this);
		}
		
		return false;
	}
	
	/** Записать float безопасно */
	public boolean writeFloatSafe(float i)
	{
		try
		{
			writeFloat(i);
			return true;
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while writing float", i, "to stream", this);
		}
		
		return false;
	}
	
	/** Записать строку БЕЗ инта (размером в байтах) в начале с переходом на новую строку */
	public boolean writeStringLn(@ConcattedString Object... text)
	{
		boolean writed = writeStringWithEncoding(encoding, false, text);
		
		if (writed)
			writed = writeString(lineSplitter);
		
		return writed;
	}
	
	/** Записать строку БЕЗ инта (размером в байтах) в начале */
	public boolean writeString(@ConcattedString Object... text)
	{
		return writeStringWithEncoding(encoding, false, text);
	}
	
	/** Добавить текст и перейти на новую строку */
	@Alias("writeStringLn")
	public boolean writeLn(@ConcattedString Object... text)
	{
		return writeStringLn(text);
	}
	
	/** Добавить текст и перейти на новую строку */
	@Alias("writeStringLn")
	public boolean writeLn()
	{
		return writeStringLn("");
	}
	
	/** Записать строку с интом(размером в байтах) в начале */
	public boolean writeStringL(@ConcattedString Object... text)
	{
		return writeStringWithEncoding(encoding, true, text);
	}
	
	/** Записать строку */
	public boolean writeStringWithEncoding(String encoding, boolean putSize, @ConcattedString Object... text)
	{
		String s = StringUtils.concat(text);
		
		try
		{
			byte[] bytes = s.getBytes(encoding);
			
			if (putSize)
				write(bytes.length);
			
			write(bytes);
			
			return true;
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while writing string '", s, "' to stream", this);
		}
		
		return false;
	}
	
	/** Записать boolean безопасно */
	public boolean writeBooleanSafe(boolean i)
	{
		try
		{
			writeBoolean(i);
			return true;
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while writing boolean", i, "to stream", this);
		}
		
		return false;
	}
	
	/** Записать UTF-строку безопасно */
	public boolean writeUTFSafe(String i)
	{
		try
		{
			writeUTF(i);
			return true;
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while writing utf string", i, "to stream", this);
		}
		
		return false;
	}
	
	/** Записать массив байтов безопасно */
	public DataOutputStreamSWL writeSafe(byte[] bytes)
	{
		try
		{
			write(bytes);
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while writing bytes array", bytes, "to stream", this);
		}
		
		return this;
	}
	
	/** Закрыть поток безопасно */
	public DataOutputStreamSWL closeSafe()
	{
		try
		{
			close();
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while closing stream", this);
		}
		
		return this;
	}
	
	/** Задать {@link #lineSplitter} */
	public <T extends DataOutputStreamSWL> T setLineSplitter(String splitter)
	{
		if (StringUtils.isEmpty(splitter))
			return (T) this;
		
		this.lineSplitter = splitter;
		
		return (T) this;
	}
	
	/** Задать {@link #encoding} */
	@Alias("setEncoding")
	public <T extends DataOutputStreamSWL> T encoding(String encoding)
	{
		return setEncoding(encoding);
	}
	
	/** Задать {@link #encoding} */
	public <T extends DataOutputStreamSWL> T setEncoding(String encoding)
	{
		if (StringUtils.isEmpty(encoding))
			return (T) this;
		
		this.encoding = encoding;
		
		return (T) this;
	}
	
	/** Обернуть поток в аналогичный этому, сохраняя все харрактеристики, такие, как {@link #encoding} */
	public DataOutputStreamSWL wrap(OutputStream is)
	{
		return new DataOutputStreamSWL(is).setEncoding(encoding).setLineSplitter(lineSplitter);
	}
	
	/** Запаковывать все данные в GZ */
	public DataOutputStreamSWL gz()
	{
		try
		{
			return wrap(new GZIPOutputStream(this));
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while creating GZipOutputStrem for", this);
		}
		
		return null;
	}
	
	/** Закриптовать поток */
	public DataOutputStreamSWL crypted(Cryptor cryptor)
	{
		return wrap(new CipherOutputStream(this, cryptor.getEncryptCipher()));
	}
	
	/** Закриптовать поток */
	public DataOutputStreamSWL crypted(Cipher cipher)
	{
		return wrap(new CipherOutputStream(this, cipher));
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
	
	public static DataOutputStreamSWL of(RandomAccessFile randomAccessFile)
	{
		if (randomAccessFile == null)
			return null;
		
		return new DataOutputStreamSWL(new RandomAccessFileOutputStream(randomAccessFile));
	}
	
	public static DataOutputStreamSWL of(OutputStream stream)
	{
		if (stream == null)
			return null;
		
		return new DataOutputStreamSWL(stream);
	}
}
