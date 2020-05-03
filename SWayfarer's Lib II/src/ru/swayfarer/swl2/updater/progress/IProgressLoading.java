package ru.swayfarer.swl2.updater.progress;

import java.io.OutputStream;

import ru.swayfarer.swl2.observable.property.ObservableProperty;
import ru.swayfarer.swl2.resource.file.FileSWL;

/**
 * Загрузка, знающая свой прогресс
 * <br> Позволяет проводить загрузку с отслеживанием процесса 
 * @author swayfarer
 *
 */
public interface IProgressLoading {

	/** Получить {@link ObservableProperty} со значением прогресса */
	public ObservableProperty<Integer> getProgress();
	
	/** 
	 * Начать загрузку
	 * @param saveTarget {@link OutputStream}, в который происходит загрузка
	 * @param isCloseOut Закрывать ли поток, в который происходит записть по окончанию процесса?
	 * @return Началась ли загрука? 
	 */
	public boolean start(OutputStream saveTarget, boolean isCloseOut);
	
	/** 
	 * Начать загрузку. Закрывает поток вывода.
	 * @param saveTarget {@link OutputStream}, в который происходит загрузка
	 * @return Началась ли загрука? 
	 */
	public default boolean start(OutputStream saveTarget)
	{
		return start(saveTarget, true);
	}
	
	/**
	 * Начать загрузку. Закрывает поток вывода.
	 * @param file Начать загрузку в файл
	 * @return Началась ли загрука? 
	 */
	public default boolean start(FileSWL file)
	{
		return start(file.createIfNotFoundSafe().toOutputStream(), true);
	}
	
}
