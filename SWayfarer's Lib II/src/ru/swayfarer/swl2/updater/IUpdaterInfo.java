package ru.swayfarer.swl2.updater;

import ru.swayfarer.swl2.resource.file.FileSWL;

/**
 * Информация об обновлении
 * @author swayfarer
 *
 */
public interface IUpdaterInfo {

	/**
	 * Трогать ли файл?
	 * @param root Корневая папка обновления
	 * @param file Проверяемый файл
	 * @return Нужно ли трогать файл?
	 */
	public boolean isAcceptsFile(FileSWL root, FileSWL file);
	
	/** Получить контент обновления в формате {@link UpdateContent} */
	public UpdateContent getUpdateContent();
	
}
