package ru.swayfarer.swl2.updater;

import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.collections.extended.IExtendedMap;
import ru.swayfarer.swl2.config.AutoSerializableConfig;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.math.MathUtils;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.resource.file.FilesUtils;
import ru.swayfarer.swl2.resource.rlink.RLUtils;
import ru.swayfarer.swl2.resource.rlink.ResourceLink;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.swconf2.helper.SwconfIO;
import ru.swayfarer.swl2.swconf2.mapper.annotations.CommentedSwconf;
import ru.swayfarer.swl2.updater.UpdateContent.FileInfo;

/**
 * Информация об обновлении, на основании которой происходит обновление
 * @author swayfarer
 *
 */
public class UpdaterInfo extends AutoSerializableConfig implements IUpdaterInfo {

	public SwconfIO swconfIO = new SwconfIO();
	
	/** Маски, имена соответствующие которым будут игнорироваться */
	@InternalElement
	@CommentedSwconf("If file local path starts with exlusion it will be not touched by updating")
	public IExtendedList<String> exlusions = CollectionsSWL.createExtendedList();
	
	@InternalElement
	@CommentedSwconf("If not empty, update will be tounch only files which local names starts with inclusions")
	public IExtendedList<String> inclusions = CollectionsSWL.createExtendedList();
	
	/** Контент обновления */
	@InternalElement
	@CommentedSwconf("Update content")
	public UpdateContent content;
	
	@CommentedSwconf("Parent imports of update. This configuration will override imports if it's needed.")
	public IExtendedList<String> imports = CollectionsSWL.createExtendedList();
	
	public AtomicBoolean isImportsLoaded = new AtomicBoolean(false);
	
	@Override
	public boolean isAcceptsFile(FileSWL root, FileSWL file)
	{
		String localPath = file.getLocalPath(root);
		return exlusions == null ? true : !exlusions.dataStream().contains((mask) -> StringUtils.isMatchesByMask(mask, localPath));
	}
	
	public void initImports()
	{
		if (isImportsLoaded.get())
			return;
		
		isImportsLoaded.set(true);
		
		IExtendedMap<String,FileInfo> files = this.content.files;
		
		for (String importInfo : imports)
		{
			ResourceLink rlink = RLUtils.createLink(importInfo);
			
			UpdaterInfo info = swconfIO.deserialize(UpdaterInfo.class, rlink); 
			
			if (info != null)
				importInfo(info);
		}
		
		this.content.files.putAll(files);
	}
	
	public void importInfo(UpdaterInfo info)
	{
		if (info == null)
			return;
		
		info.initImports();
		
		if (!content.hashingType.equals(info.content.hashingType))
		{
			logger.warning("Skiping import", info, "for", this, "because content hashing types are not equal: ", info.content.hashingType, content.hashingType);
			return;
		}
		
		int oldInclusionsSize = inclusions.size();
		int oldExlusionsSize = exlusions.size();
		int oldContentSize = content.files.size();
		
		logger.info("Importing info", info, "inclusions count is", info.inclusions.size(), "exlusions", info.exlusions.size(), "content size is", info.content.files.size(), "and hashing type", info.content.hashingType);
		
		this.inclusions.addAll(info.inclusions);
		this.exlusions.addAll(info.exlusions);
		this.content.files.putAll(info.content.files);
		
		logger.info("Imported info sucsessfull! Added", inclusions.size() - oldInclusionsSize, "inclusions", exlusions.size() - oldExlusionsSize, "exclusions", content.files.size() - oldContentSize, "files!");
	}
	
	public boolean isAcceptsFile(String localPath)
	{
		if (!CollectionsSWL.isNullOrEmpty(inclusions))
		{
			if (!inclusions.dataStream().contains((s) -> localPath.startsWith(s)))
			{
				return false;
			}
		}
		
		return !isExclusion(localPath);
	}
	
	public boolean isExclusion(String localPath)
	{
		if (!CollectionsSWL.isNullOrEmpty(exlusions))
		{
			return exlusions.dataStream().contains((s) -> localPath.startsWith(s));
		}
		
		return false;
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
