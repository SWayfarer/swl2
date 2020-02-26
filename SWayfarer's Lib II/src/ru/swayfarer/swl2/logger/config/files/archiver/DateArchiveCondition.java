package ru.swayfarer.swl2.logger.config.files.archiver;

import java.time.LocalDateTime;

import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2;
import ru.swayfarer.swl2.resource.file.FileSWL;

public class DateArchiveCondition implements IFunction1<FileSWL, Boolean> {
	
	public IFunction2<LocalDateTime, LocalDateTime, Boolean> dateConditionFun;
	
	public LocalDateTime prevDate = LocalDateTime.now();
	
	public Boolean apply(FileSWL file)
	{
		LocalDateTime actualDate = LocalDateTime.now();
		
		return dateConditionFun.apply(prevDate, actualDate);
	}
}