package ru.swayfarer.swl2.resource.streams;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.Stack;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;

import ru.swayfarer.swl2.binary.BinaryUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.crypto.Cryptor;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.Alias;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Поток для работы с данными 
 * @author swayfarer
 */
@SuppressWarnings("unchecked")
public class DataInputStreamSWL extends DataInputStream {
	
	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Разделитель строк */
	@InternalElement
	public String lineSplitter = StringUtils.LF;
	
	/** Читалка строк */
	@InternalElement
	public StringLinesReadHelper linesReadHelper;
	
	/** Кодировка для работы со строками в потоке */
	@InternalElement
	public String encoding;

	/** Закрыт ли поток? */
	@InternalElement
	public boolean isClosed;
	
	/** Поддерживается ли операция {@link #back(int)} */
	@InternalElement
	public BackableInputStream backStream;
	
	/** Запомненные позиции для отката */
	@InternalElement
	public Stack<Integer> lastPositions = new Stack<>();
		
	/**
	 * Конструктор
	 * @param in Оборачиваемый поток
	 */
	public DataInputStreamSWL(InputStream in)
	{
		this("UTF-8", in);
	}
	
	/**
	 * Конструктор
	 * @param in Оборачиваемый поток
	 * @param encoding Кодировка для работы со строками 
	 */
	public DataInputStreamSWL(String encoding, InputStream in)
	{
		super(in);
		this.encoding = encoding;
	}
	
	/**
	 * Прочитать все строки
	 */
	public IExtendedList<String> readAllLines()
	{
		IExtendedList<String> ret = CollectionsSWL.createExtendedList();
		
		while (hasNextByte())
		{
			ret.add(readStringLnSafe());
		}
		
		closeSafe();
		
		return ret;
	}
	
	/** Пропустить байты */
	public <T extends DataInputStreamSWL> T forwardSafe(long bytesCount)
	{
		ExceptionsUtils.safe(() -> forward(bytesCount));
		
		return (T) this;
	}
	
	/** Пропустить байты */
	public <T extends DataInputStreamSWL> T forward(long bytesCount) throws IOException
	{
		skip(bytesCount);
		
		return (T) this;
	}
	
	public DataInputStreamSWL crypted(Cryptor cryptor)
	{
		return crypted(cryptor.getDecryptCypher());
	}
	
	public DataInputStreamSWL crypted(Cipher cipher)
	{
		return wrap(new CipherInputStream(this, cipher));
	}
	
	public boolean isClosed()
	{
		return isClosed;
	}
	
	/**
	 * Безопасно прочитать все байты потока.
	 * @return Массив байтов потока, если не произошло ошибки, иначе null.
	 */
	public byte[] readAllSafe()
	{
		try
		{
			return readAll();
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while safe reading of all stream", this);
		}
		
		return null;
	}
	
	/** Прочитать весь поток как UTF-8 строку */
	public String readAllAsUtf8()
	{
		return readAllAsString("UTF-8");
	}
	
	/** Прочитать весь поток как строку с указанием кодировки */
	public String readAllAsString()
	{
		return readAllAsString(encoding);
	}
	
	/** Прочитать весь поток как строку с указанием кодировки */
	public String readAllAsString(String encoding)
	{
		if (encoding == null)
			encoding = "UTF-8";
		
		try
		{
			byte[] bytes = readAllSafe();
			
			if (bytes == null)
				return null;
			
			String s = new String(bytes, encoding);
			
			close();
			
			return s;
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while reading stream", this, "as string");
		}
		
		closeSafe();
		
		return null;
	}
	
	/**
	 * Прочитать все байты потока.
	 */
	public byte[] readAll() throws IOException
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		StreamsUtils.copyStream(this, stream, true, true);
		return stream.toByteArray();
	}
	
	/**
	 * Есть ли байт, который можно прочитать
	 */
	public boolean hasNextByte()
	{
		return !atEnd();
	}
	
	/**
	 * Прочитан ли поток полностью?
	 */
	public boolean atEnd()
	{
		if (isClosed())
			return true;
		
		try
		{
			return available() <= 0;
		}
		catch (Throwable e)
		{
			// Nope!
		}
		
		return true;
	}
	
	@Override
	public void close() throws IOException
	{
		isClosed = true;
		super.close();
	}
	
	/**
	 * Получить {@link BufferedReader} для потока
	 * @return
	 */
	@Alias("getReader")
	public BufferedReader toReader()
	{
		return getReader();
	}
	
	/** Прочитать текстовую строку безопасно */
	public String readStringLnSafe()
	{
		try
		{
			return readStringLn();
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while reading string line from", this);
		}
		
		return null;
	}
	
	/** Прочитать текстовую строку */
	public String readStringLn() throws IOException
	{
		return getInternalLinesReader().readLine(this);
	}
	
	/**
	 * Получить {@link BufferedReader} для потока
	 * @return
	 */
	public BufferedReader getReader()
	{
		return new BufferedReader(new InputStreamReader(this));
	}
	
	/** Получить {@link #linesReadHelper} */
	@InternalElement
	public StringLinesReadHelper getInternalLinesReader()
	{
		if (linesReadHelper == null)
			linesReadHelper = new StringLinesReadHelper().setLineSplitter(lineSplitter);
		
		return linesReadHelper;
	}
	
	/**
	 * Безопасно закрыть поток
	 * @return Удалось ли закрыть?
	 */
	public boolean closeSafe()
	{
		try
		{
			close();
			return true;
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while closing stream", this);
		}
		
		return false;
	}
	
	public int availableSafe()
	{
		try
		{
			return available();
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while getting avaliable bytes count from", this);
		}
		
		return -1;
	}
	
	/** Сохранить точку для отката */
	public DataInputStreamSWL saveBackpoint()
	{
		lastPositions.push(availableSafe());
		return this;
	}
	
	/** Откатиться к последней сохраненной через {@link #saveBackpoint()} позиции */
	public DataInputStreamSWL back()
	{
		if (lastPositions.isEmpty())
			return this;
		
		int current = availableSafe();
		
		if (current == -1)
			return this;
		
		int last = lastPositions.peek();
		
		if (last > current)
			back(last - current);
		
		return this;
	}
	
	/**
	 * Откатить байты
	 * @param bytesBack Кол-во байтов, которые будут откатнуты 
	 */
	public DataInputStreamSWL back(int bytesBack)
	{
		ExceptionsUtils.IfNull(backStream, IllegalStateException.class, "Trying to unread bytes brom non-backable stream!");
		backStream.back(bytesBack);
		return this;
	}
	
	/** Откатить указанное количество интов */
	public DataInputStreamSWL backInt(int count)
	{
		return back(count * BinaryUtils.INT_BYTES_SIZE);
	}
	
	/** Откатить указанное количество short'ов */
	public DataInputStreamSWL backShort(int count)
	{
		return back(count * BinaryUtils.SHORT_BYTES_SIZE);
	}
	
	/** Откатить указанное количество long'ов */
	public DataInputStreamSWL backLong(int count)
	{
		return back(count * BinaryUtils.LONG_BYTES_SIZE);
	}
	
	/** Откатить указанное количество double'ов */
	public DataInputStreamSWL backDouble(int count)
	{
		return back(count * BinaryUtils.DOUBLE_BYTES_SIZE);
	}
	
	/** Откатить указанное количество float'ов */
	public DataInputStreamSWL backFloat(int count)
	{
		return back(count * BinaryUtils.FLOAT_BYTES_SIZE);
	}
	
	/**
	 *  Включить возможность откатиться назад при чтении 
	 *  <h1> Обратное чтение не сохраняется при оборачивании потока через {@link #gz()} и т.п.! </h1>
	 */
	public DataInputStreamSWL backable()
	{
		in = backStream = new BackableInputStream(in);
		return this;
	}
	
	/** 
	 * Включить возможность откатиться назад при чтении с указанием количества запоминаемых байт для отката 
	 * <h1> Обратное чтение не сохраняется при оборачивании потока через {@link #gz()} и т.п.! </h1>
	 */
	public DataInputStreamSWL backable(int backBufferSize)
	{
		in = backStream = new BackableInputStream(in, backBufferSize);
		return this;
	}
	
	/** Запаковывать все данные через gz */
	public DataInputStreamSWL gz()
	{
		try
		{
			return wrap(new GZIPInputStream(this));
		}
		catch (IOException e)
		{
			logger.error(e, "Error while creating GZIPInputStream for", this);
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
	
	/** Обернуть поток данных в эквивалентный этому, с сохранением всех значений (кодировок и пр) */
	@InternalElement
	public DataInputStreamSWL wrap(InputStream is)
	{
		return new DataInputStreamSWL(encoding, is);
	}
	
	/** Задать {@link #encoding} */
	@Alias("setEncoding")
	public <T extends DataInputStreamSWL> T encoding(String encoding)
	{
		return setEncoding(encoding);
	}
	
	/** Задать {@link #encoding} */
	public <T extends DataInputStreamSWL> T setEncoding(String encoding)
	{
		this.encoding = encoding;
		
		if (linesReadHelper != null)
			linesReadHelper.encoding = encoding;
		
		return (T) this;
	}
	
	/** Задать {@link #lineSplitter} */
	public <T extends DataInputStreamSWL> T setLineSplitter(String splitter)
	{
		this.lineSplitter = splitter;
		
		if (linesReadHelper != null)
			linesReadHelper.setLineSplitter(lineSplitter);
		
		return (T) this;
	}

	/** Статический метод-фабрика */
	public static DataInputStreamSWL of(InputStream stream)
	{
		if (stream == null)
			return null;
		
		return new DataInputStreamSWL(stream);
	}
	
	/** Статический метод-фабрика */
	public static DataInputStreamSWL of(InputStream stream, String encoding)
	{
		if (stream == null)
			return null;
		
		return new DataInputStreamSWL(encoding, stream);
	}
}
