package ru.swayfarer.swl2.logger.configurator.entry;

import java.util.Map;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.config.files.archiver.DateArchiveCondition;
import ru.swayfarer.swl2.logger.config.files.archiver.DelayType;
import ru.swayfarer.swl2.logger.config.files.archiver.EveryDelayType;
import ru.swayfarer.swl2.logger.config.files.archiver.OverDelayType;
import ru.swayfarer.swl2.logger.handlers.LogArchiverHandler;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.resource.pathtransformers.PathTransforms;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.swconf2.mapper.annotations.CommentedSwconf;
import ru.swayfarer.swl2.swconf2.mapper.annotations.IgnoreSwconf;
import ru.swayfarer.swl2.tasks.TaskManager;

/**
 * Конфигуратор архивации лог. файлов
 * @author swayfarer
 *
 */
public class ConfiguratorArchivingEntry {

	/** Архивировать ли файл логов, оставшийся с прошлого раза? */
	@CommentedSwconf("Archive already existing logfile on start?")
	public boolean ifPrefExists = false;
	
	/** Архивировать ли файл логов при закрытии приложения? */
	@CommentedSwconf("Archive logfile on application close?")
	public boolean onClose = true;
	
	/** Максимальный размер файла с указанием единиц измерения, который будет переведен в число при помощи {@link #getMaxFileBytes()} */
	@CommentedSwconf("File size at which it is archived")
	public String maxFileSize = "10mb";
	
	/** 
	 * Период архивации <br>
	 * Состоит из способа подсчета, числа и единиц измерерия <br>
	 *
	 * <h1> Способы подсчета: </h1>
	 * per - абсолютное значение, т.е. per 1 day = раз в 24 часа, а не календарные сутки. Принимает единицы измерения <= неделе <br>
	 * every - Календарные значение, т.е. every 1 day = каждый календарный день. Принимает единицы измерения >= дню
	 *
	 * <h1> Единицы измерения: </h1>
	 * sec, secs, second, seconds, s - секунды <br>
	 * min, minute, minutes, mins - минуты <br>
	 * hour, h, hours - часы
	 * days, day, d - дни <br>
	 * week, weeks, w - неделя <br>
	 * month, months - месяц <br>
	 * year, years, y - год <br>
	 */
	@CommentedSwconf("Archiving delay")
	public String delay = "every 1 days";
	
	/**
	 * Паттерн имени файла, согласно которому он будет назван при создании <br>
	 * В паттерне могут быть использованы любые ключевые актеры из {@link PathTransforms} + %file% - имя лог. файла до архивации
	 */
	@CommentedSwconf("Archive file pattern")
	public String archiveFilePattern = "%date[YYYY.MM.DD-HH.mm.ssss]%.log";
	
	/** Путь до директории, в которую будут сохраняться архивы */
	@CommentedSwconf("Directory where arhived logfiles will be located")
	public String archivesDir = "logs/archives/";
	
	/** Кэшированный максимальный размер лог. файла в байтах */
	@IgnoreSwconf
	public long maxFileBytes = Short.MIN_VALUE;
	
	/** Зарегистированные {@link DelayType}'ы */
	@IgnoreSwconf
	public Map<String, DelayType> registeredDelayTypes = CollectionsSWL.createHashMap(
			"every", new EveryDelayType(),
			"per", new OverDelayType()
	);
	
	/** Применить на логгер */
	public void applyToLogger(ILogger logger, ConfiguratorFileEntry fileEntry)
	{
		FileSWL logFile = fileEntry.getLogFile();
		FileSWL archivesDir = getArchivesDir();
		
		LogArchiverHandler archiverHandler = new LogArchiverHandler(archivesDir, logFile);
		
		if (!StringUtils.isEmpty(this.archivesDir))
		{
			IFunction1<FileSWL, Boolean> archiveCondition = (e) -> false;
			
			long maxFileBytes = getMaxFileBytes();
			
			if (maxFileBytes > 0)
			{
				archiveCondition = (f) -> f.length() >= maxFileBytes;
				archiverHandler.appendCondition(archiveCondition);
			}
			
			if (!StringUtils.isEmpty(delay))
			{
				DateArchiveCondition dateConition = new DateArchiveCondition();
				archiveCondition = dateConition;
				
				String[] split = delay.toLowerCase().split(" ");

				String delayType = split[0];
				long delayValue = Long.valueOf(split[1]);
				String timeUnit = split[2];
				
				DelayType type = registeredDelayTypes.get(delayType);
				
				if (type == null)
					return;
				
				dateConition.dateConditionFun = type.getCondition(timeUnit, delayValue);
				
				archiverHandler.appendCondition(archiveCondition);
			}
		}
		
		archiverHandler.isForceSave.set(ifPrefExists);
		
		if (onClose)
		{
			TaskManager.onClose(() -> archiverHandler.save(true, false));
		}
		
		if (!StringUtils.isEmpty(archiveFilePattern))
			archiverHandler.setFileNameTemplate(archiveFilePattern);
		
		logger.evtPostLogging().subscribe(archiverHandler);
	}
	
	/** 
	 * Получить директорию, в которую будут сохраняться логи
	 * @return Директория
	 */
	public FileSWL getArchivesDir()
	{
		return new FileSWL(archivesDir);
	}
	
	/** Метод первоначально читает из записи с ед. измерения кол-во байт, а потом возвращает его */
	public long getMaxFileBytes()
	{
		if (maxFileBytes == Short.MIN_VALUE)
		{
			if (StringUtils.isEmpty(maxFileSize))
				maxFileBytes = -1;
			else
			{
				int coef = 1;
				
				String maxSize = this.maxFileSize.toLowerCase();
				
				int endIndex = 0;
				
				if (maxSize.endsWith("mb"))
				{
					coef = 1024 * 1024;
					endIndex = -2;
				}
				else if (maxSize.endsWith("kb"))
				{
					coef = 1024;
					endIndex = -2;
				}
				else if (maxSize.endsWith("gb"))
				{
					coef = 1024 * 1024 * 1024;
					endIndex = -2;
				}
				
				if (endIndex != 0)
					maxSize = StringUtils.subString(0, endIndex, maxSize);
				
				if (StringUtils.isEmpty(maxSize))
					maxFileBytes = -1;
				else
					maxFileBytes = Long.valueOf(maxSize) * coef;
			}
		}
		
		return maxFileBytes;
	}
}
