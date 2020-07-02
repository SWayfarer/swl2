package ru.swayfarer.swl2.logger.configurator.entry;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.swconf2.mapper.annotations.CommentedSwconf;

/**
 * Конфигуратор, отвечающий за вывод логов в консоль
 * @author swayfarer
 *
 */
public class ConfiguratorPrintingEntry {

	/**
	 * Формат лога.
	 * <h1> Принимаемые ключевые актеры: </h1>
	 * %file% - Файл, в котором располагается источник лога (jar или папка) <br>
	 * %level% - Уровень лога <br>
	 * %thread% - Имя потока, из которого вызван лог <br>
	 * %logger% - Имя логгера, который логирует лог <br>
	 * %text% - Текст лога <br>
	 * %from% - Ссылка на источник лога в формате файла и строки, например SomeFile.java:123 <br>
	 * %fromFull% - Ссылка на источник лога в формате полного пути до него, например some.path.to.SomeFile.java:123<br>
	 */
	@CommentedSwconf
	(
			  "\nFormat of logging. Use actors: \n"
			+ "%file% - file where logging source located\n"
			+ "%level% - level of log\n"
			+ "%thread% - name of thread from which log was called\n"
			+ "%logger% - name of log's logger\n"
			+ "%text% - text of log message\n"
			+ "%from% - line and source file simple name (e.g. SomeFile.java:123) from which log was called\n"
			+ "%fromFull% - line and source file full name (e.g. some.path.to.SomeFile.java:123) from which log was called\n"
	)
	public String format;
	
	/**
	 * Формат элемента стактрейса <br>
	 * <h1> Принимаемые ключевые актеры </h1>
	 * %class% - Имя класса, на который ссылается элемент стактрейса <br>
	 * %source% - Источник элемента стактрейса <br>
	 * %lineNumber% - Строка кода, на которую ссылается элемент <br>
	 * %method% - Метод, на который ссылается элемент <br>
	 * %file% - Файл, в котором лежит класс, на который ссылается элемент <br>
	 */
	@CommentedSwconf("Stacktrace element format")
	public String stacktraceFormat;
	
	/** Скрывать ли цвета? */
	@CommentedSwconf("If sets to true output colors will be hidden")
	public Boolean hideColors;
	
	/** Цветовой режим логгинга */
	@CommentedSwconf("Logs coloring mode")
	public EnumLoggingColorMode coloringMode;

	/** Декоратор {@link Throwable}'ов */
	@CommentedSwconf("Throwables decorator seq")
	public String decoratorSeq;
	
	/** Префиксы, элементы стактрейсов начинающиеся с которых будут скрыты из него */
	@CommentedSwconf("All stacktrace elements that starts with element of this list will be not shown")
	public IExtendedList<String> stacktraceBlocks = CollectionsSWL.createExtendedList();
	
	/** Применить на логгер */
	public void applyToLogger(ILogger logger)
	{
		if (!StringUtils.isBlank(format))
		{
			logger.setLogFormat(format);
		}
		
		if (!CollectionsSWL.isNullOrEmpty(stacktraceBlocks))
			logger.addStackstraceBlocker(stacktraceBlocks.toArray(String.class));
		
		if (!StringUtils.isEmpty(stacktraceFormat))
			logger.setStacktraceElementFormat(stacktraceFormat);
		
		if (hideColors == Boolean.TRUE)
			logger.hideColors();
		
		if (!StringUtils.isEmpty(decoratorSeq))
		{
			logger.setDecoratorSeq(decoratorSeq);
		}
	}
	
	public static enum EnumLoggingColorMode {
		Hide,
		Colors_8bit,
		Colors_ASCI
	}
}
