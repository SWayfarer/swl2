package ru.swayfarer.swl2.logger.config.files.archiver;

import java.time.LocalDateTime;

import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2;
import ru.swayfarer.swl2.resource.file.FileSWL;

/** 
 * Условие архивации, зависящее от даты
 * @author swayfarer
 */
public class DateArchiveCondition implements IFunction1<FileSWL, Boolean> {
	
	/** Функция, которая возвращает, было ли выполенено условие */
	public IFunction2<LocalDateTime, LocalDateTime, Boolean> dateConditionFun;
	
	/** Предыдущая дата */
	public LocalDateTime prevDate = LocalDateTime.now();
	
	public Boolean apply(FileSWL file)
	{
		LocalDateTime actualDate = LocalDateTime.now();
		
		return dateConditionFun.apply(prevDate, actualDate);
	}
}