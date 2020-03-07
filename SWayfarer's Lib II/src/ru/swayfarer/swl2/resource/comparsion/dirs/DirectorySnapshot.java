package ru.swayfarer.swl2.resource.comparsion.dirs;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.math.MathUtils;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.resource.file.FilesUtils;

/** Снимок директории */
@Data
public class DirectorySnapshot {
	
	/** Тип хэширования, применявшийся при создании снимка */
	@InternalElement
	public String hashingType = MathUtils.HASH_SHA_256;
	
	/** Имя директории */
	@InternalElement
	public String name;
	
	/** Карта, содержащая снимки всех файлов директирии */
	@InternalElement
	public Map<String, FileSnapshot> files = CollectionsSWL.createConcurrentHashMap();
	
	/** Добавить файл в снапшот */
	public synchronized void addFile(FileSnapshot snapshot)
	{
		files.put(snapshot.name, snapshot);
	}
	
	/** Получить {@link CompareResult} сравнения этого снапшота с указанным */
	public DirectorySnapshot.CompareResult compare(DirectorySnapshot compareSnapshot)
	{
		ExceptionsUtils.IfNot(compareSnapshot.hashingType.equals(hashingType), IllegalStateException.class, "Compare target using another hashing type. Another hashed shanphots are incompatible!");
			
		DirectorySnapshot.CompareResult ret = new CompareResult();
		
		Map<String, FileSnapshot> compareSnapshotFiles = new HashMap<>(compareSnapshot.files);
		
		files.values().forEach((e) -> {
			
			FileSnapshot compareSnaphotCurrentFile = compareSnapshotFiles.get(e.name);
			
			if (compareSnaphotCurrentFile == null)
			{
				ret.addedFiles.add(e);
			}
			else 
			{
				boolean isHashesEquals = compareSnaphotCurrentFile.hash.equals(e.hash);
				
				if (!isHashesEquals)
					ret.changedFiles.add(e);
			}
			
			compareSnapshotFiles.remove(e.name);
			System.out.println(compareSnapshotFiles.size());
			
		});
		
		compareSnapshotFiles.values().forEach((e) -> ret.removedFiles.add(e));
		
		return ret;
	}
	
	/** Получить снимок указанной директории */
	public static DirectorySnapshot valueOf(FileSWL dir, String hashingType)
	{
		if (dir == null || !dir.isExistingDir())
			return null;
		
		DirectorySnapshot snapshot = new DirectorySnapshot();
		snapshot.hashingType = hashingType;
		
		snapshot.name = dir.getName();
		
		FilesUtils.forEachFile(null, (f) -> {

			FileSnapshot fileSnapshot = FileSnapshot.of(f, dir, snapshot.hashingType);
			snapshot.addFile(fileSnapshot);
			
		}, dir);
		
		return snapshot;
	}
	
	/** Результат сравнения снапшотов */
	public static class CompareResult {
		
		public IExtendedList<FileSnapshot> changedFiles = CollectionsSWL.createExtendedList();
		public IExtendedList<FileSnapshot> removedFiles = CollectionsSWL.createExtendedList();
		public IExtendedList<FileSnapshot> addedFiles = CollectionsSWL.createExtendedList();
		
	}
}