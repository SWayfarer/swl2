package ru.swayfarer.swl2.updater;

import java.net.URL;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedMap;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.resource.file.FilesUtils;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.swconf2.mapper.annotations.CommentedSwconf;

/**
 * Контент обновления
 * <br> Из контента обновления берется информация о том, в какой вид нужно привети обновляемую папку
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class UpdateContent {

	/** Тип хэширования, по которому сравниваются файлы */
	@InternalElement
	@CommentedSwconf("Type of hashing that will be used for update refreshing")
	public String hashingType;
	
	/** Информация о файлах и папках обновления */
	@InternalElement
	@CommentedSwconf("Files of update with additional information about them")
	public IExtendedMap<String, FileInfo> files = CollectionsSWL.createExtendedMap();
	
	/**
	 * Есть ли в обновлении информация о файле?
	 * @param path Путь до файла относительно Корневой директории
	 * @return Есть ли информация?
	 */
	public boolean hasFile(String path)
	{
		return files.containsKey(path);
	}
	
	/**
	 * Добавить информацию о файле
	 * @param root Корневая директория, относительно которой файл в последствии будет обновлен
	 * @param file Добавляемый файл
	 * @param hashingType Тип хэширования, по которому будет взят хэш добавляемого файла
	 * @param uploader Функция-загрузчик файла в удаленное хранилище
	 * @param isRecursive Добавлять ли дочерние файлы и папки? 
	 * @return Этот аптейтер
	 */
	public <T extends UpdateContent> T add(FileSWL root, FileSWL file, String hashingType, IFunction2<String, FileSWL, URL> uploader, boolean isRecursive) 
	{
		if (isRecursive)
		{
			FilesUtils.forEachFile((f) -> f != file, (f) -> {
				add(root, f, hashingType, uploader, isRecursive);
			}, file, false);
		}
		
		String localPath = file.getLocalPath(root);
		
		if (!StringUtils.isEmpty(localPath))
		{
			UpdateContent.FileInfo fileInfo = FileInfo.of(file, localPath, hashingType, uploader);
			files.put(localPath, fileInfo);
		}
		
		return (T) this;
	}
	
	/**
	 * Получить копию информации о контенте 
	 * @return Этот аптейтер
	 */
	public UpdateContent copy()
	{
		UpdateContent content = new UpdateContent();
		content.files = files.copy();
		content.hashingType = hashingType;
		
		return content;
	}
	
	/**
	 * Информация о файле
	 * <br> Содержит информацию о файле внутри контента обновления ({@link UpdateContent})
	 * @author swayfarer
	 *
	 */
	@InternalElement
	public static class FileInfo {
		
		/** Является ли файл папкой? */
		public boolean isDirectory;
		
		/** Хэш файла */
		public String hash;
		
		/** Тип хэширования, по которому был взят {@link #hash} */
		public String hashType;
		
		/** Путь до файла относително Корневой директории */
		public String path;
		
		/** {@link URL} в формате {@link URL#toExternalForm()}, по которому можно взять актуальную версию файла */
		public String url;
		
		/**
		 * Получить информацию о файле
		 * @param file Файл, информацию о котором получаем
		 * @param localPath Путь, по которому файл лежит относительно Корневой директории обноаления
		 * @param hashingType Тип хэширования, по которому будет взят хэш файла 
		 * @param uploader Функция-загрузчик файла на удаленное хранилище
		 * @return Полученная информация о файле 
		 */
		public static FileInfo of(FileSWL file, String localPath, String hashingType, IFunction2<String, FileSWL, URL> uploader)
		{
			FileInfo ret = new FileInfo();
			
			if (uploader != null)
			{
				URL url = uploader.apply(localPath, file);
				
				if (url != null)
					ret.url = url.toExternalForm();
				else
					ret.url = "<no>";
			}
			
			ret.hashType = hashingType;
			ret.hash = file.isDirectory() ? file.getSubfilesCount() + "" : file.getHash(hashingType);
			ret.path = localPath;
			ret.isDirectory = file.isDirectory();
			
			return ret;
		}
		
	}
}
