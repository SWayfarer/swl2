package ru.swayfarer.swl2.resource.file;

import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1NoR;

/** 
 * Утилиты для работы с файлами 
 * @author swayfarer
 *
 */
public class FilesUtils {
	
	/** Для каждого файла, проходящего указанный фильтр, выполнить функцию */
	public static void forEachFile(IFunction1<FileSWL, Boolean> filter, IFunction1NoR<FileSWL> fun, FileSWL initialFile)
	{
		if (filter == null)
			filter = (f) -> true;

		ExceptionsUtils.IfNull(fun, IllegalArgumentException.class, "Files fun can't be null!");
		ExceptionsUtils.IfNull(initialFile, IllegalArgumentException.class, "Initial file can't be null!");

		if (!initialFile.exists())
			return;

		final IFunction1<FileSWL, Boolean> finallyFilter = filter;

		if (initialFile.isFile() && filter.apply(initialFile))
			fun.apply(initialFile);
		else
			initialFile.getSubFiles((file) -> file.isDirectory() || finallyFilter.apply(file)).parrallelDataStream().each((file) -> forEachFile(finallyFilter, fun, file));
	}
}
