package ru.swayfarer.swl2.resource.file;

import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.markers.ConcattedString;

public interface IHasSubfiles {

	/** Получить список подфайлов, если это директория */
	public default IExtendedList<FileSWL> getSubfiles()
	{
		return getSubFiles((f) -> true);
	}
	
	/** Получить список подфайлов, удовлетворяющих фильтру, если это директория */
	public IExtendedList<FileSWL> getSubFiles(IFunction1<FileSWL, Boolean> filter);
	
	/** Получить подфайл */
	public FileSWL subFile(@ConcattedString Object... filepath);
	
}
