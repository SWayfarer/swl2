package ru.swayfarer.swl2.updater.testing;

import java.net.URL;

import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2;
import ru.swayfarer.swl2.resource.file.FileSWL;

public class FileUpdaterUploader implements IFunction2<String, FileSWL, URL> {

	public FileSWL remoteRootFile;
	
	public FileUpdaterUploader(FileSWL remoteFile)
	{
		super();
		this.remoteRootFile = remoteFile;
	}
	
	@Override
	public URL apply(String filePathFromRoot, FileSWL fileToUpload)
	{
		FileSWL existingFile = remoteRootFile.subFile(filePathFromRoot).removeIfExists();
		fileToUpload.copyTo(existingFile);
		return existingFile.toRlink().toURL();
	}

}
