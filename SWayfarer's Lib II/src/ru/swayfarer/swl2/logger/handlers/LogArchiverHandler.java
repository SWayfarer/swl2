package ru.swayfarer.swl2.logger.handlers;

import java.util.Date;

import lombok.AllArgsConstructor;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction3NoR;
import ru.swayfarer.swl2.logger.event.LogEvent;
import ru.swayfarer.swl2.logger.formatter.DateFormatRule;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.observable.subscription.ISubscription;
import ru.swayfarer.swl2.resource.compress.CompressionUtils.Archivers;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.string.StringUtils;

@AllArgsConstructor
@SuppressWarnings("unchecked")
public class LogArchiverHandler implements IFunction2NoR <ISubscription<LogEvent>, LogEvent> {

	public String archiveTemplate;
	public FileSWL archivesDir;
	public FileSWL logsFile;
	
	public IFunction1<FileSWL, Boolean> isNeedsToArchiveFun = (file) -> false;
	
	public IFunction3NoR<FileSWL, FileSWL, String> archiveFun = Archivers.gzArchiver;
	
	public LogArchiverHandler(FileSWL archivesDir, FileSWL logsFile)
	{
		this.archivesDir = archivesDir;
		this.logsFile = logsFile;
	}
	 
	@Override
	public void applyNoR(ISubscription<LogEvent> sub, LogEvent logEvent)
	{
		logsFile.lock();
		
		if (StringUtils.isEmpty(archiveTemplate))
			archiveTemplate = "%file%_%date[dd.MM.YYYY_HH-mm-ss]%";
		
		if (isNeedsToArchiveFun != null && isNeedsToArchiveFun.apply(logsFile))
		{
			archivesDir.createIfNotFoundDir();
			logsFile.createIfNotFoundSafe();
			String archiveName = archiveTemplate;
			archiveName = archiveName.replace("%file%", logsFile.getName());
			archiveName = DateFormatRule.format(archiveName, new Date());
			
			archiveFun.apply(logsFile, archivesDir, archiveName);
			logsFile.delete();
		}
		
		logsFile.unlock();
	}

	public <T extends LogArchiverHandler> T setArchiveConditionFun(IFunction1<FileSWL, Boolean> conditionFun)
	{
		this.isNeedsToArchiveFun= conditionFun;
		return (T) this;
	}
	
	public <T extends LogArchiverHandler> T setFileNameTemplate(@ConcattedString Object... format)
	{
		this.archiveTemplate = StringUtils.concat(format);
		return (T) this;
	}
	
	public <T extends LogArchiverHandler> T setArchiveFun(IFunction3NoR<FileSWL, FileSWL, String> archiveFun)
	{
		this.archiveFun = archiveFun;
		return (T) this;
	}

}
