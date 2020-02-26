package ru.swayfarer.swl2.resource.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.zip.ZipFile;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.collections.streams.DataStream;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.resource.pathtransformers.PathTransforms;
import ru.swayfarer.swl2.resource.streams.DataInputStreamSWL;
import ru.swayfarer.swl2.resource.streams.DataOutputStreamSWL;
import ru.swayfarer.swl2.resource.streams.StreamsUtils;

/**
 * Расширенный файл
 * @author swayfarer
 */
@SuppressWarnings("serial")
public class FileSWL extends File {

	public static ConcurrentHashMap<String, Lock> filesLocks = new ConcurrentHashMap<>();
	
	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Конструктор */
	public FileSWL()
	{
		this("%appDir%");
	}
	
	/** Конструктор */
	public FileSWL(File parent, String child)
	{
		super(parent, PathTransforms.transform(child));
	}

	/** Конструктор */
	public FileSWL(String parent, String child)
	{
		super(PathTransforms.transform(parent), PathTransforms.transform(child));
	}

	/** Конструктор */
	public FileSWL(String pathname)
	{
		super(PathTransforms.transform(pathname));
	}

	/** Конструктор */
	public FileSWL(URI uri)
	{
		super(uri);
	}
	
	public FileSWL getParentFile()
	{
		return FileSWL.of(super.getParentFile());
	}
	
	/** Получить путь до родителя, по возможности относительно рабочей директории */
	public String getParentLocalPath()
	{
		FileSWL parent = FileSWL.of(getParentFile());
		
		if (parent != null)
			return parent.getLocalPath();
		
		return null;
	}
	
	/** Получить путь до файла, по возможности относительно рабочей директории */
	public String getLocalPath()
	{
		String workDir = PathTransforms.fixSlashes(new FileSWL().getAbsolutePath());
		String currentDir = PathTransforms.fixSlashes(getAbsolutePath());
		
		if (workDir.endsWith("./"))
			workDir = workDir.substring(0, workDir.length() - 2);
		
		return currentDir.replace(workDir, "");
	}
	
	@Override
	public String getAbsolutePath()
	{
		String path = super.getAbsolutePath();
		
		if (path.endsWith("/."))
			path = path.substring(0, path.length() - 2);
		
		if (isDirectory() && !path.endsWith("/"))
			path += "/";
		
		return path;
	}
	
	/** Создать, если не сущесвует, эту директорию */
	public FileSWL createIfNotFoundDir()
	{
		mkdirs();
		return this;
	}
	
	/** Создать, если не сущесвует, этот файл безопасно */
	public FileSWL createIfNotFoundSafe()
	{
		try
		{
			return createIfNotFound();
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while safe creating file", this);
		}
		
		return null;
	}
	
	/** Создать, если не сущесвует, этот файл */
	public FileSWL createIfNotFound() throws IOException
	{
		if (!this.exists())
		{
			if (this.isDirectory())
			{
				this.mkdirs();
			}
			else
			{
				File parent = this.getParentFile();
				
				if (parent != null)
					parent.mkdirs();
				
				this.createNewFile();
			}
		}
		
		return this;
	}
	
	/** Получить список подфайлов, если это директория */
	public IExtendedList<FileSWL> getSubfiles()
	{
		return getSubFiles((f) -> true);
	}
	
	/** Получить список подфайлов, удовлетворяющих фильтру, если это директория */
	public IExtendedList<FileSWL> getSubFiles(IFunction1<FileSWL, Boolean> filter)
	{
		IExtendedList<FileSWL> ret = DataStream.of(listFiles())
				.mapped((f) -> FileSWL.of(f)) 
				.filtered(filter)
		.toList();
		
		return ret;
	}
	
	/** Скопировать в (Если копируется директория, то будут скопированы все ее файлы) */
	public FileSWL copyTo(FileSWL file)
	{
		if (isDirectory())
		{
			file.createIfNotFoundDir();
			getSubfiles().parrallelDataStream().each((subFile) -> subFile.copyTo(new FileSWL(file, subFile.getName())));
		}
		else
		{
			file.createIfNotFoundSafe();
			file.setDataFrom(this);
		}
		
		return file;
	}
	
	/** Задать содержимое файла из другого файла */
	public FileSWL setDataFrom(FileSWL file)
	{
		if (file == null || !file.exists())
			return null;
		
		if (createIfNotFoundSafe() == null)
			return null;
		
		InputStream fis = file.toInputFileStream();
		OutputStream fos = toOutputFileStream(false);
		
		StreamsUtils.copyStreamSafe(fis, fos);
		
		return this;
	}
	
	/** Задать содержимое файла */
	public FileSWL setData(byte[] bytes)
	{
		if (CollectionsSWL.isNullOrEmpty(bytes))
			return null;
		
		createIfNotFoundSafe();
		DataOutputStreamSWL stream = toOutputStream();
		stream.writeSafe(bytes);
		stream.closeSafe();
		return this;
	}
	
	
	public Lock getLock()
	{
		try
		{
			String key = getCanonicalPath();
			
			Lock ret = filesLocks.get(key);
			
			if (ret == null)
			{
				ret = new ReentrantLock();
				filesLocks.put(key, ret);
			}
			
			return ret;
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while getting lock for file", this);
		}
		
		return null;
	}
	
	/** Заблокировать файл в потоке, чтобы не было одновременной записи в файл */
	public void lock()
	{
		getLock().lock();
	}
	
	/** Разблокировать файл в потоке */
	public void unlock()
	{
		getLock().unlock();
	}
	
	public DataOutputStreamSWL toAppendStream()
	{
		return new DataOutputStreamSWL(toOutputFileStream(true));
	}
	
	/** Получить поток исходящих данных */
	public DataOutputStreamSWL toOutputStream()
	{
		return DataOutputStreamSWL.of(toOutputFileStream(false));
	}
	
	/** Получить поток исходящих данных */
	public FileOutputStream toOutputFileStream(boolean isAppend)
	{
		try
		{
			return new FileOutputStream(this, isAppend);
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while creating FileOutputStream for", this);
		}
		
		return null;
	}
	
	public ZipFile toZipFile()
	{
		try
		{
			return new ZipFile(this);
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while creting zip-file for", this);
		}
		
		return null;
	}

	/** Получить поток входящих данных */
	public DataInputStreamSWL toInputStream()
	{
		return DataInputStreamSWL.of(toInputFileStream());
	}
	
	/** Получить поток входящих данных */
	public FileInputStream toInputFileStream()
	{
		try
		{
			FileInputStream stream = new FileInputStream(this);
			
			return stream;
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while creating FileInputStream for", this);
		}
		
		return null;
	}
	
	/** Статический метод-фабрика */
	public static FileSWL of(File file)
	{
		if (file == null)
			return null;
		
		if (file instanceof FileSWL)
			return (FileSWL) file;
		
		return new FileSWL(file.getAbsolutePath());
	}

}
