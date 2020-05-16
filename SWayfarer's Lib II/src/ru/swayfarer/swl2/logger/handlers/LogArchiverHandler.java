package ru.swayfarer.swl2.logger.handlers;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

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

	/** Шаблон для имен архиов */
	public String archiveTemplate;
	
	/** Директория, в которую будут сохраняться архивы */
	public FileSWL archivesDir;
	
	/** Файл логов */
	public FileSWL logsFile;
	
	/** Функция, говорящая, когда стоит архивировать файл */
	public IFunction1<FileSWL, Boolean> isNeedsToArchiveFun = (file) -> false;
	
	/** Функция, архивирующая файл */
	public IFunction3NoR<FileSWL, FileSWL, String> archiveFun = Archivers.gzArchiver;
	
	public AtomicBoolean isForceSave = new AtomicBoolean(true);
	
	/** Конструктор */
	public LogArchiverHandler(FileSWL archivesDir, FileSWL logsFile)
	{
		this.archivesDir = archivesDir;
		this.logsFile = logsFile;
	}
	 
	@Override
	public void applyNoR(ISubscription<LogEvent> sub, LogEvent logEvent)
	{
		if (isNeedsToArchive(logEvent))
		{
			save(true);
		}
	}
	
	public void save(boolean isReCreate)
	{
		if (!logsFile.exists())
			return;
		
		logsFile.lock();

		if (StringUtils.isEmpty(archiveTemplate))
			archiveTemplate = "%file%_%date[dd.MM.YYYY_HH-mm-ss]%";

		archivesDir.createIfNotFoundDir();
		logsFile.createIfNotFoundSafe();

		String archiveName = archiveTemplate;
		archiveName = archiveName.replace("%file%", logsFile.getName());
		archiveName = DateFormatRule.format(archiveName, new Date());

		archiveFun.apply(logsFile, archivesDir, archiveName);

		if (isReCreate)
		{
			logsFile.removeIfExists();
			logsFile.createIfNotFoundSafe();
		}

		logsFile.unlock();
		
		isForceSave.set(false);
	}
	
	public boolean isNeedsToArchive(LogEvent logEvent)
	{
		return (isForceSave.get() && logsFile != null && logsFile.exists()) || isNeedsToArchiveFun != null && isNeedsToArchiveFun.apply(logsFile);
	}

	/** Задать {@link #isNeedsToArchiveFun} */
	public <T extends LogArchiverHandler> T setArchiveConditionFun(IFunction1<FileSWL, Boolean> conditionFun)
	{
		this.isNeedsToArchiveFun= conditionFun;
		return (T) this;
	}
	
	public <T extends LogArchiverHandler> T appendCondition(IFunction1<FileSWL, Boolean> conditionFun) 
	{
		IFunction1<FileSWL, Boolean> current = isNeedsToArchiveFun;
		
		if (current != null)
		{
			setArchiveConditionFun((f) -> conditionFun.apply(f) && current.apply(f));
		}
		else
		{
			setArchiveConditionFun(conditionFun);
		}
		
		return (T) this;
	}
	
	/** Задать {@link #archiveTemplate} */
	public <T extends LogArchiverHandler> T setFileNameTemplate(@ConcattedString Object... format)
	{
		this.archiveTemplate = StringUtils.concat(format);
		return (T) this;
	}
	
	/** Задать {@link #archiveFun} */
	public <T extends LogArchiverHandler> T setArchiveFun(IFunction3NoR<FileSWL, FileSWL, String> archiveFun)
	{
		this.archiveFun = archiveFun;
		return (T) this;
	}

}
