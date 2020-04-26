package ru.swayfarer.swl2.resource.file;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.resource.pathtransformers.PathTransforms;
import ru.swayfarer.swl2.string.StringUtils;

public class ListedSubfilesContainer implements IHasSubfiles {

	public IExtendedList<FileSWL> files = CollectionsSWL.createExtendedList();
	
	public ListedSubfilesContainer()
	{
		super();
	}

	public ListedSubfilesContainer(IExtendedList<FileSWL> files)
	{
		super();
		this.files = files;
	}

	@Override
	public IExtendedList<FileSWL> getSubFiles(IFunction1<FileSWL, Boolean> filter)
	{
		return files.dataStream().filter(filter).toList();
	}

	@Override
	public FileSWL subFile(@ConcattedString Object... filepath)
	{
		String path = StringUtils.concat(filepath);
		
		path = PathTransforms.fixSlashes(path);
		path = PathTransforms.transform(path);
		
		String fileName = path;
		
		int indexOfSlash = fileName.indexOf('/');
		
		if (indexOfSlash > 0)
		{
			fileName = fileName.substring(0, indexOfSlash);
		}
		
		final String fFileName = fileName;
		
		FileSWL file = files.dataStream().find((e) -> e.getName().equals(fFileName));
		
		if (file != null)
		{
			if (indexOfSlash > 0)
			{
				return file.subFile(path.substring(indexOfSlash) + 1);
			}
			else
			{
				return file;
			}
		}
		
		return null;
		
	}

}
