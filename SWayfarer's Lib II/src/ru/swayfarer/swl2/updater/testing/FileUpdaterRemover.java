package ru.swayfarer.swl2.updater.testing;

import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1NoR;
import ru.swayfarer.swl2.resource.file.FileSWL;

public class FileUpdaterRemover implements IFunction1NoR<String> {

	public FileSWL remoteRootFile;
	
	public FileUpdaterRemover(FileSWL remoteRootFile)
	{
		super();
		this.remoteRootFile = remoteRootFile;
	}

	@Override
	public void applyNoR(String filePathFromRoot)
	{
		FileSWL file = remoteRootFile.subFile(filePathFromRoot);
		file.removeIfExists();
	}
}
