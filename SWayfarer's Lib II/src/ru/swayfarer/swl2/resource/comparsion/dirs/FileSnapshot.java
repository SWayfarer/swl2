package ru.swayfarer.swl2.resource.comparsion.dirs;

import lombok.Data;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.resource.pathtransformers.PathTransforms;

/**
 * Снимок файла 
 * @author swayfarer
 *
 */
@Data
public class FileSnapshot {
	
	/** Хэш файла */
	@InternalElement
	public String hash;
	
	/** Имя файла */
	@InternalElement
	public String name;
	
	/** Получить снимок указанного файла */
	public static FileSnapshot of(FileSWL file, FileSWL rootDir, String hashingType)
	{
		FileSnapshot snapshot = new FileSnapshot();
		snapshot.hash = file.getHash(hashingType);
		snapshot.name = PathTransforms.fixSlashes(file.getAbsolutePathWithout(rootDir));
		
		return snapshot;
	}
	
}