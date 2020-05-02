package ru.swayfarer.swl2.resource.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipFile;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.math.MathUtils;
import ru.swayfarer.swl2.resource.pathtransformers.PathTransforms;
import ru.swayfarer.swl2.resource.rlink.RLUtils;
import ru.swayfarer.swl2.resource.rlink.ResourceLink;
import ru.swayfarer.swl2.resource.streams.DataInputStreamSWL;
import ru.swayfarer.swl2.resource.streams.DataOutputStreamSWL;
import ru.swayfarer.swl2.resource.streams.StreamsUtils;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Расширенный файл
 * @author swayfarer
 */
@SuppressWarnings("serial")
public class FileSWL extends File implements IHasSubfiles {

	/** Локи по файлам ({@link FileSWL} можно залочить, чтоьы не случилось параллельной записи)*/
	public static ConcurrentHashMap<String, Lock> filesLocks = new ConcurrentHashMap<>();
	
	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Конструктор */
	public FileSWL()
	{
		this("%appDir%");
	}
	
	public URL toURL()
	{
		return toRlink().toURL();
	}
	
	public boolean hasSubFile(String name)
	{
		FileSWL subFile = subFile(name);
		return subFile == null ? false : subFile.isExistingFile();
	}
	
	public boolean hasSubDir(String name)
	{
		FileSWL subFile = subFile(name);
		return subFile == null ? false : subFile.isExistingDir();
	}
	
	/** Получить все байты файла */
	public byte[] getData()
	{
		DataInputStreamSWL dis = toInputStream();
		
		return dis == null ? null : dis.readAllSafe();
	}
	
	public FileSWL withPostfix(@ConcattedString Object... text)
	{
		String postfix = StringUtils.concat(text);
		return new FileSWL(getAbsolutePath() + text);
	}
	
	/** Лежит ли файл в указанной директории? */
	public boolean isIn(FileSWL file)
	{
		if (!file.isDirectory())
			return false;
		
		String targetDir = file.getAbsolutePath();
		String fileDir = getAbsolutePath();
		
		targetDir = PathTransforms.fixSlashes(targetDir);
		fileDir = PathTransforms.fixSlashes(fileDir);
		
		if (targetDir.endsWith("./"))
			targetDir = targetDir.substring(0, targetDir.length() - 2);
		
		if (fileDir.endsWith("./"))
			fileDir = targetDir.substring(0, fileDir.length() - 2);
		
		return getAbsolutePath().startsWith(targetDir);
	}
	
	public IExtendedList<FileSWL> getAllSubfiles()
	{
		return getAllSubfiles((f) -> true);
	}
	
	public IExtendedList<FileSWL> getAllSubfiles(IFunction1<FileSWL, Boolean> filter)
	{
		return getSubfilesTo(filter, null);
	}
	
	public IExtendedList<FileSWL> getSubfilesTo(IFunction1<FileSWL, Boolean> filter, IExtendedList<FileSWL> target)
	{
		if (target == null)
			target = CollectionsSWL.createExtendedList();
		
		if (filter.apply(this))
			target.add(this);
		
		if (isDirectory())
		{
			for (FileSWL subFile : getSubfiles())
			{
				subFile.getSubfilesTo(filter, target);
			}
		}
		
		return target.dataStream().filter(filter).toList();
	}
	
	/** Получить подфайл */
	public FileSWL subFile(@ConcattedString Object... filepath)
	{
		if (!isDirectory())
			return null;
		
		return new FileSWL(this, StringUtils.concat(filepath));
	}
	
	/** Получить файл со случайным доступом */
	public RandomAccessFile toRandomAccess(String mode)
	{
		try
		{
			return new RandomAccessFile(this, mode);
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while getting random access file from", this);
		}
		
		return null;
	}
	
	/** Получить {@link ResourceLink} на этот файл */
	public ResourceLink toRlink()
	{
		return RLUtils.file(this);
	}
	
	/** Получить расположение в формате {@link ResourceLink} */
	public String toRlinkString()
	{
		return toRlink().toRlinkString();
	}
	
	/** Удалить (если папка, то включая подпапки и подфайлы) */
	public boolean remove()
	{
		if (exists() && isDirectory())
			for (FileSWL file : getSubfiles())
				if (!file.remove())
					return false;
		
		return delete();
	}
	
	/** Получить абсолютный путь до этого файла, убрав из него путь до указанного */
	public String getAbsolutePathWithout(FileSWL file)
	{
		return getAbsolutePath().replace(file.getAbsolutePath(), "");
	}
	
	/** Это существующий файл */
	public boolean isExistingFile()
	{
		return exists() && isFile();
	}
	
	/** Это существующая папка */
	public boolean isExistingDir()
	{
		return exists() && isDirectory();
	}
	
	/** Конструктор */
	public FileSWL(File parent, @ConcattedString Object... childName)
	{
		super(parent, PathTransforms.transform(StringUtils.concat(childName)));
	}

	/** Эквивалетнен ли хэш файла указанному */
	public boolean hashEquals(String hash)
	{
		String fileHash = getHash();
		
		if (StringUtils.isEmpty(hash))
			return StringUtils.isEmpty(fileHash);
		
		return fileHash.equals(hash);
	}
	
	/** Получить sha-256 хэш файла */
	public String getHash()
	{
		return FilesUtils.getFileHash(this, MathUtils.HASH_SHA_256);
	}
	
	/** Получить хэш файла */
	public String getHash(String hashType)
	{
		return FilesUtils.getFileHash(this, hashType);
	}
	
	/** Получить манифест файла, если это jar */
	public Manifest getManifest()
	{
		try
		{
			JarFile jar = new JarFile(this);
			
			Manifest ret = jar.getManifest();
			
			jar.close();
			
			return ret;
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while getting manifest file for", this);
		}
		
		return null;
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
	
	public static FileSWL ofURL(URL url)
	{
		if (url == null)
			return null;
		
		return logger.safeReturn(() -> {
			return new FileSWL(url.toURI().getPath());
		}, null, "Error while getting file from", url);
	}
	
	/** Получить имя без расширения */
	public String getNameWithoutExtension()
	{
		String name = getName();
		String extension = getExtension();
		
		if (extension.isEmpty())
			return name;
		
		return name.substring(0, name.length() - extension.length() - 1);
	}
	
	/** Получить расширение файла */
	public String getExtension()
	{
		String name = getName();
		
		String ret = "";
		
		char[] chars = name.toCharArray();
		
		for (char ch : chars)
		{
			if (ch == '.')
			{
				ret = "";
			}
			else
			{
				ret += ch;
			}
		}
		
		if (ret.equalsIgnoreCase(name))
			return "";
		
		return ret;
	}
	
	public FileSWL getParentFile()
	{
		return FileSWL.of(super.getParentFile());
	}
	
	public FileSWL canonize()
	{
		return logger.safeReturn(() -> {
			return new FileSWL(getCanonicalPath());
		}, this, "Error while canonizing file", this);
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
		return getLocalPath(new FileSWL());
	}
	
	/** Получить путь до файла, по возможности относительно указанной директории */
	public String getLocalPath(FileSWL root)
	{
		String workDir = PathTransforms.fixSlashes(root.getAbsolutePath());
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
		String[] fileNames = list();
		
		if (CollectionsSWL.isNullOrEmpty(fileNames))
			return CollectionsSWL.createExtendedList();
		
		IExtendedList<FileSWL> ret = CollectionsSWL.createExtendedList(fileNames.length);
		
		for (String name : fileNames)
		{
			ret.add(new FileSWL(this, name));
		}
		
		return ret;
	}
	
	/** Получить список подфайлов, удовлетворяющих фильтру, если это директория */
	public IExtendedList<FileSWL> getSubFiles(IFunction1<FileSWL, Boolean> filter)
	{
		return getSubfiles().dataStream()
			.filtered(filter)
		.toList();
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
	
	/**
	 *  Получить файл, переименнованный из этого
	 *  <h1> Доступные актеры: </h1>
	 *  <br> %file% - имя файла с расширением
	 *  <br> %fileabs% - абсолютный путь до файла
	 *  <br> %filewext% - имя без расширения
	 *  <br> %fileext% - расширение файла 
	 */
	public FileSWL renamed(@ConcattedString Object... text)
	{
		return new FileSWL(getNameByThis(StringUtils.concat(text)));
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
	
	/**
	 *  Переименовать в файл
	 *  <h1> Доступные актеры: </h1>
	 *  <br> %file% - имя файла с расширением
	 *  <br> %fileabs% - абсолютный путь до файла
	 *  <br> %filewext% - имя без расширения
	 *  <br> %fileext% - расширение файла 
	 */
	public FileSWL renameTo(@ConcattedString Object... name)
	{
		String newName = StringUtils.concat(name);
		
		newName = getNameByThis(newName);
		
		if (!StringUtils.isEmpty(newName))
		{
			FileSWL file = new FileSWL(newName);
			renameTo(file);
			return file;
		}
		
		return null;
	}
	
	public String getNameByThis(String name)
	{
		return name
				.replace("%file%", getName())
				.replace("%fileabs%", getAbsolutePath())
				.replace("%filewext%", getNameWithoutExtension())
				.replace("%fileext%", getExtension());
	}
	
	/** Получить поток исходящих данных */
	public FileOutputStream toOutputFileStream(boolean isAppend)
	{
		try
		{
			createIfNotFound();
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
