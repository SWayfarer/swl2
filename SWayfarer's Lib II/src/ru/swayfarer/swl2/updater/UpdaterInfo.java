package ru.swayfarer.swl2.updater;

import java.net.URL;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.math.MathUtils;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.resource.file.FilesUtils;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.swconf.config.AutoSerializableConfig;

/**
 * Информация об обновлении, на основании которой происходит обновление
 * @author swayfarer
 *
 */
public class UpdaterInfo extends AutoSerializableConfig implements IUpdaterInfo {

	/** Маски, имена соответствующие которым будут игнорироваться */
	@InternalElement
	public IExtendedList<String> exlusions = CollectionsSWL.createExtendedList();
	
	/** Контент обновления */
	@InternalElement
	public UpdateContent content;
	
	@Override
	public boolean isAcceptsFile(FileSWL root, FileSWL file)
	{
		String localPath = file.getLocalPath(root);
		return exlusions == null ? true : !exlusions.dataStream().contains((mask) -> StringUtils.isMatchesByMask(mask, localPath));
	}

	@Override
	public UpdateContent getUpdateContent()
	{
		return content;
	}
	
	/** 
	 * Получить информацию об обновлении
	 * @param root Корневая директория, с которой будет снято обновление
	 * @return Полученная информация об обновлении 
	 */
	public static UpdateContent of(FileSWL root)
	{
		return of(root, MathUtils.HASH_MD5);
	}
	
	/** 
	 * Получить информацию об обновлении
	 * @param root Корневая директория, с которой будет снято обновление
	 * @param hashingType Тип хэширования, по которому будут браться хэши файлов
	 * @return Полученная информация об обновлении 
	 */
	public static UpdateContent of(FileSWL root, String hashingType)
	{
		return of(root, hashingType, null);
	}
	
	/** 
	 * Получить информацию об обновлении
	 * @param root Корневая директория, с которой будет снято обновление
	 * @param hashingType Тип хэширования, по которому будут браться хэши файлов
	 * @param uploader Функция-загрузчик файлов на удаленное хранилище 
	 * @return Полученная информация об обновлении 
	 */
	public static UpdateContent of(FileSWL root, String hashingType, IFunction2<String, FileSWL, URL> uploader)
	{
		UpdateContent ret = new UpdateContent();
		ret.hashingType = hashingType;
		
		FilesUtils.forEachFile((f) -> true, (f) -> {
			ret.add(root, f, hashingType, uploader, false);
		}, root, false);
		
		return ret;
	}

}
