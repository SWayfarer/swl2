package ru.swayfarer.swl2.logger.config.files.archiver;

import java.util.HashMap;
import java.util.Map;

import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.json.NonJson;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.config.files.FileInfo;
import ru.swayfarer.swl2.logger.handlers.LogArchiverHandler;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.string.StringUtils;

public class ArchivingInfo {

	public String maxSize;
	public String archiveTime;
	public String archiveFolder;
	public String archiveName;
	
	@NonJson
	@InternalElement
	public long maxFileBytes = Short.MIN_VALUE;
	
	@NonJson
	@InternalElement
	public FileInfo fileInfo;
	
	@NonJson
	@InternalElement
	public Map<String, DelayType> registeredDelayTypes = new HashMap<>();
	
	public ArchivingInfo()
	{
		registeredDelayTypes.put("every", new EveryDelayType());
		registeredDelayTypes.put("per", new OverDelayType());
	}
	
	public void apply(ILogger logger)
	{
		if (!StringUtils.isEmpty(archiveFolder))
		{
			IFunction1<FileSWL, Boolean> archiveCondition = (e) -> false;
			
			long maxFileBytes = getMaxFileBytes();
			
			if (maxFileBytes > 0)
			{
				archiveCondition = (logFile) -> logFile.length() >= maxFileBytes;
			}
			
			if (!StringUtils.isEmpty(archiveTime))
			{
				DateArchiveCondition dateConition = new DateArchiveCondition();
				archiveCondition = dateConition;
				
				String[] split = archiveTime.toLowerCase().split(" ");

				String delayType = split[0];
				long delayValue = Long.valueOf(split[1]);
				String timeUnit = split[2];
				
				DelayType type = registeredDelayTypes.get(delayType);
				
				if (type == null)
					return;
				
				dateConition.dateConditionFun = type.getCondition(timeUnit, delayValue);
			}
			
			if (archiveCondition != null)
				logger.evtPostLogging().subscribe(new LogArchiverHandler(new FileSWL(archiveFolder), new FileSWL(fileInfo.filePath).createIfNotFoundSafe()).setArchiveConditionFun(archiveCondition).setFileNameTemplate(archiveName));
		}
	}
	
	public long getMaxFileBytes()
	{
		if (maxFileBytes == Short.MIN_VALUE)
		{
			if (StringUtils.isEmpty(maxSize))
				maxFileBytes = -1;
			else
			{
				int coef = 1;
				
				String maxSize = this.maxSize.toLowerCase();
				
				if (maxSize.endsWith("mb"))
					coef = 1024 * 1024;
				else if (maxSize.endsWith("kb"))
					coef = 1024;
				else if (maxSize.endsWith("gb"))
					coef = 1024 * 1024 * 1024;
				
				if (coef != 1)
					maxSize = maxSize.substring(2);
				
				if (StringUtils.isEmpty(maxSize))
					maxFileBytes = -1;
				else
					maxFileBytes = Long.valueOf(maxSize) * coef;
			}
		}
		
		return maxFileBytes;
	}
}
