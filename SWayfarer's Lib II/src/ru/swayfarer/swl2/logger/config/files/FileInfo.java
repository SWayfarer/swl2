package ru.swayfarer.swl2.logger.config.files;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.config.files.archiver.ArchivingInfo;
import ru.swayfarer.swl2.logger.handlers.LogFileHandler;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.string.StringUtils;

/** Информация о файле, в который будет вестись запись логов */
@AllArgsConstructor(staticName = "of") @Data
public class FileInfo {
	
	/** Путь до файла */
	@InternalElement
	public String filePath;
	
	/** Информация о архивировании файла*/
	@InternalElement
	public ArchivingInfo archivingInfo;
	
	public FileInfo() {}
	
	public void apply(ILogger logger) 
	{
		if (StringUtils.isEmpty(filePath))
			return;
		
		FileSWL file = new FileSWL(filePath);
		file.createIfNotFoundSafe();
		
		logger.evtPostLogging().subscribe(LogFileHandler.of(file, (f) -> true));
		
		if (archivingInfo != null)
		{
			archivingInfo.fileInfo = this;
			archivingInfo.apply(logger);
		}
	}
}