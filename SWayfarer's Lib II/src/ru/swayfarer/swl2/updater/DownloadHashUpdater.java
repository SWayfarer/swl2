package ru.swayfarer.swl2.updater;

import java.net.URL;
import java.util.concurrent.locks.ReentrantLock;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.collections.streams.DataStream;
import ru.swayfarer.swl2.collections.streams.IDataStream;
import ru.swayfarer.swl2.equals.EqualsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1NoR;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.Observables;
import ru.swayfarer.swl2.observable.property.ObservableProperty;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.resource.rlink.ResourceLink;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.swconf2.mapper.annotations.IgnoreSwconf;
import ru.swayfarer.swl2.updater.UpdateContent.FileInfo;
import ru.swayfarer.swl2.updater.progress.IProgressLoading;
import ru.swayfarer.swl2.updater.progress.ProgressDownloading;

/**
 * Загрузчик обновлений, сравнивающий файлы по хэшам
 * <br> Загрузчики требуют информацию об обновлении, которая указывает, как именно его производить {@link #updaterInfo}
 * @author swayfarer
 *
 */
public class DownloadHashUpdater {

	/** Информация об обновлении */
	@InternalElement
	public UpdaterInfo updaterInfo;
	
	/** Логгер */
	@InternalElement @IgnoreSwconf
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Событие начала обновления файла */ @IgnoreSwconf
	public IObservable<FileUpdateEvent> eventStartUpdating = Observables.createObservable();
	
	/** Событие ошибки обновления файла */ @IgnoreSwconf
	public IObservable<FileUpdateEvent> eventFail = Observables.createObservable();
	
	/**
	 * Обновить текущую информацию об обновлении с Корневой директории и залить изменения на удаленное хранилище
	 * @param root Корневой файл, от которого пойдет обновление
	 * @param remoteRemover Функция, удаляющая файл с удаленного хоста 
	 * @param uploader Функция, которая выгружает файл на удаленный хост
	 */
	public void refresh(FileSWL root, IFunction1NoR<String> remoteRemover, IFunction2<String, FileSWL, URL> uploader)
	{
		updaterInfo.initImports();
		UpdateContent remoteUpdateContent = updaterInfo.getUpdateContent();
		
		ReentrantLock lock = new ReentrantLock();
		
		// Удаляем лишнее из удаленного хранилища 
		for (FileInfo remoteFileInfo : CollectionsSWL.createExtendedList(remoteUpdateContent.files.values()))
		{
			FileSWL localFile = root.subFile(remoteFileInfo.path);
			
			if (!updaterInfo.isAcceptsFile(remoteFileInfo.path))
				continue;
			
			boolean isMustRemove = false;
			
			if (!localFile.exists())
			{
				isMustRemove = true;
			}
			else if (remoteFileInfo.isDirectory != localFile.isDirectory())
			{
				isMustRemove = true;
			}
			
			if (isMustRemove)
			{
				lock.lock();
				remoteUpdateContent.files.remove(remoteFileInfo.path);
				lock.unlock();
				
				remoteRemover.apply(remoteFileInfo.path);
			}
		}
		
//		logger.info(root.getAllSubfiles().size());
		
		root.getAllSubfiles().parrallelDataStream().each(localFile -> {
			String localPath = localFile.getLocalPath(root);
			FileInfo remoteFileInfo = remoteUpdateContent.files.get(localPath);
			
			if (!updaterInfo.isAcceptsFile(localPath))
				return;
			
			boolean isMustUpload = false;
			
			if (remoteFileInfo == null)
			{
				isMustUpload = true;
			}
			else if (localFile.isFile())
			{
				if (!remoteFileInfo.hash.equals(localFile.getHash(remoteUpdateContent.hashingType)))
				{
					isMustUpload = true;
				}
			}
			
			if (isMustUpload)
			{
				if (!StringUtils.isEmpty(localPath))
				{
					UpdateContent.FileInfo fileInfo = FileInfo.of(localFile, localPath, remoteUpdateContent.hashingType, uploader);
					
					lock.lock();
					remoteUpdateContent.files.put(localPath, fileInfo);
					lock.unlock();
				}
			}
		});
	}
	
	/**
	 * Обновить текущую информацию об обновлении с Корневой директории и залить изменения на удаленное хранилище
	 * @param root Корневой файл, от которого пойдет обновление
	 * @param remoteRemover Функция, удаляющая файл с удаленного хоста 
	 * @param uploader Функция, которая выгружает файл на удаленный хост
	 */
	public void refreshNew(FileSWL root, IFunction1NoR<String> remoteRemover, IFunction2<String, FileSWL, URL> uploader)
	{
		UpdateContent updateContent = updaterInfo.getUpdateContent();
		
		UpdateContent actualInfo = UpdaterInfo.of(root, updateContent.hashingType, null);
		
		UpdateContent remoteFiles = updateContent.copy();
		
		IExtendedList<String> removePrefixes = CollectionsSWL.createExtendedList();
		
		// Собираем папки, ставшие файлами для контента сервера 
		remoteFiles.files.dataStream().each((e) -> {
			
			if (!this.updaterInfo.isAcceptsFile(e.getKey()))
				return;
			
			FileInfo remoteFileInfo = e.getValue();;
			FileInfo actualFileInfo = actualInfo.files.get(e.getKey());
			
			if (actualFileInfo != null)
			{
				if (remoteFileInfo.isDirectory && !actualFileInfo.isDirectory)
				{
					removePrefixes.add(remoteFileInfo.path);
					remoteFiles.files.remove(e.getKey());
				}
				else if (!remoteFileInfo.isDirectory && actualFileInfo.isDirectory)
				{
					remoteRemover.apply(remoteFileInfo.path);
					remoteFiles.files.remove(e.getKey());
				}
			}
			else
			{
				remoteFiles.files.remove(e.getKey());
			}
		});
		
		// Собираем папки, ставшие файлами для контента клиента 
		actualInfo.files.dataStream().each((e) -> {
			
			if (!this.updaterInfo.isAcceptsFile(e.getKey()))
				return;
			
			FileInfo remoteFileInfo = remoteFiles.files.get(e.getKey());
			FileInfo actualFileInfo = e.getValue();
			
			if (remoteFileInfo != null && actualFileInfo != null)
			{
				if (remoteFileInfo.isDirectory && !actualFileInfo.isDirectory)
				{
					// Если на сервере лежит директория, а актуальнен файл, то заносим директорию в список на удаление и стираем саму директорию из кэша
					removePrefixes.add(remoteFileInfo.path);
					remoteFiles.files.remove(e.getKey());
					
					remoteFiles.add(root, root.subFile(actualFileInfo.path), actualFileInfo.hashType, uploader, false);
				}
				else if (!remoteFileInfo.isDirectory && actualFileInfo.isDirectory)
				{
					// Если на сервере лежит файл, а актуальна папка, то выгружаем папку на сервер и удаляем файл
					remoteRemover.apply(remoteFileInfo.path);
					remoteFiles.add(root, new FileSWL(root, actualFileInfo.path), actualFileInfo.hashType, uploader, actualFileInfo.isDirectory);
				}
			}
			
			// Если на сервере вообще ничего нет по такому пути, то добавляем 
			else if (remoteFileInfo == null && actualFileInfo != null)
			{
				remoteFiles.add(root, new FileSWL(root, actualFileInfo.path), actualFileInfo.hashType, uploader, actualFileInfo.isDirectory);
			}
		});
		
		// Удаляем содержимое папок, ставших файлами
		IDataStream<String> prefixes = removePrefixes.dataStream();
		
		actualInfo.files.dataStream().each((e) -> {
			
			if (!this.updaterInfo.isAcceptsFile(e.getKey()))
				return;
			
			FileInfo remoteFileInfo = remoteFiles.files.get(e.getKey());
			
			if (remoteFileInfo != null && prefixes.contains((p) -> remoteFileInfo.path.startsWith(p)))
			{
				remoteRemover.apply(remoteFileInfo.path);
			}
		});
		
		// Удаляем папки, ставшие файлами 
		removePrefixes.each((e) -> {
			
			if (!this.updaterInfo.isAcceptsFile(e))
				return;
			
			remoteRemover.apply(e);
			FileInfo actualFileInfo = updateContent.files.get(e);
			actualFileInfo.url = uploader.apply(e, root.subFile(e)).toExternalForm();
		});
		
		// Работаем с оставшимися файлами 
		actualInfo.files.each((e) -> {
			
			if (!this.updaterInfo.isAcceptsFile(e.getKey()))
				return;
			
			FileInfo remoteFileInfo = remoteFiles.files.get(e.getKey());
			FileInfo actualFileInfo = e.getValue();
			
			if (!EqualsUtils.objectEquals(remoteFileInfo.hash, false, actualFileInfo.hash))
			{
				actualFileInfo.url = uploader.apply(actualFileInfo.path, root.subFile(remoteFileInfo.path)).toExternalForm();
			}
		});
		
		this.updaterInfo.content = remoteFiles.copy();
	}
	
	
	/**
	 * Обновить директорию до актуального состояния
	 * @param root Корневая директория
	 * @param remover Функция, которая будет удалять файлы
	 */
	public void update(FileSWL root, IFunction2NoR<FileSWL, FileSWL> remover)
	{
		updaterInfo.initImports();
		UpdateContent updateContent = updaterInfo.getUpdateContent();
		
		root.getAllSubfiles().parrallelDataStream()
		.each((f) -> {
			String localPath = f.getLocalPath(root);
			
			FileInfo remoteFileInfo = updateContent.files.get(localPath);
			
			if (StringUtils.isEmpty(localPath))
				return;
			
			if (!this.updaterInfo.isAcceptsFile(localPath))
				return;
			
			if (remoteFileInfo == null)
			{
				logger.info("Removing non-existing at remove file:", localPath);
				f.remove();
			}
			else
			{
				if (remoteFileInfo.isDirectory && f.isFile())
				{
					logger.info("Removing not a directory file:", f);
					f.remove();
					f.createIfNotFoundDir();
				}
				else if (!remoteFileInfo.isDirectory && f.isDirectory())
				{
					logger.info("Removing not a file directory:", f);
					f.remove();
					f.createIfNotFoundSafe();
				}
			}
		});
		
		DataStream.of(updateContent.files.values()).setParrallel(true).each(fileInfo -> {
			FileSWL localFile = root.subFile(fileInfo.path);
			
			if (!this.updaterInfo.isAcceptsFile(fileInfo.path))
			{
				eventStartUpdating.next(new FileUpdateEvent(localFile, fileInfo, null));
				return;
			}
				
			
			if (fileInfo.isDirectory)
				localFile.createIfNotFoundDir();
			else
				localFile.createIfNotFoundSafe();
			
			if (!fileInfo.isDirectory)
			{
				boolean isMustUpdate = false;
				
				if (!localFile.exists())
				{
					logger.warning("File", fileInfo.path, "is not exists at", localFile.getAbsolutePath(), localFile.exists());
					isMustUpdate = true;
				}
				else if (localFile.exists() && !EqualsUtils.objectEqualsSome(localFile.getHash(fileInfo.hashType), fileInfo.hash))
				{
					logger.warning("File", fileInfo.path, "has not equal hash", fileInfo.hash, localFile.getHash(updateContent.hashingType));
					isMustUpdate = true;
				}
				
				if (isMustUpdate)
				{
					logger.safeOperation(() -> {
						IProgressLoading loading = new ProgressDownloading(new URL(fileInfo.url));
						ObservableProperty<Integer> progress = loading.getProgress();
						eventStartUpdating.next(new FileUpdateEvent(localFile, fileInfo, progress));

						logger.info("Downloading to", localFile.getAbsolutePath());
						if (!loading.start(localFile))
						{
							eventFail.next(new FileUpdateEvent(localFile, fileInfo, progress));
							logger.error("Update failed for file", localFile.getAbsolutePath());
						}
						
					}, "Updating file", fileInfo.path);
				}
				else
				{
					eventStartUpdating.next(new FileUpdateEvent(localFile, fileInfo, null));
				}
			}
		});
	}
	
	public static DownloadHashUpdater fromSwconfInfo(ResourceLink rlink)
	{
		UpdaterInfo updaterInfo = new UpdaterInfo();
		updaterInfo.setResourceLink(rlink);
		updaterInfo.init();
		DownloadHashUpdater updater = new DownloadHashUpdater();
		updater.updaterInfo = updaterInfo;
		return updater;
	}
	
	public static class FileUpdateEvent {
		
		public FileSWL file;
		public FileInfo fileInfo;
		public ObservableProperty<Integer> progress = Observables.createProperty();
		
		public FileUpdateEvent(FileSWL file, FileInfo fileInfo, ObservableProperty<Integer> progress)
		{
			super();
			this.file = file;
			this.fileInfo = fileInfo;
			this.progress = progress;
		}
		
		public boolean isSkiped()
		{
			return progress == null;
		}
		
		public boolean isExistsOnRepo()
		{
			return fileInfo != null;
		}
	}
	
}
