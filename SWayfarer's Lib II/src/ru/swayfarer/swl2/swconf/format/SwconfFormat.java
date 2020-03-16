package ru.swayfarer.swl2.swconf.format;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction0;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.swconf.serialization.formatter.PrettyFormattedWriter;
import ru.swayfarer.swl2.swconf.serialization.reader.SwconfReader;
import ru.swayfarer.swl2.swconf.serialization.writer.ISwconfWriter;
import ru.swayfarer.swl2.swconf.serialization.writer.SwconfWriter;

/**
 * Пользовательский формат Swconf
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class SwconfFormat {

	/** Функция, возвращающая {@link ISwconfWriter} для этого формата */
	public IFunction0<ISwconfWriter> writerFun = () -> new SwconfWriter().setFormat(this);
	
	/** Функция, возвращаюшая {@link SwconfReader} для этого формата */
	public IFunction0<SwconfReader> readerFun = () -> new SwconfReader().setFormat(this);
	
	/** Разворачивалка имен пропертей */
	public IFunction1<String, String> propertyNameUnwrapper = (s) -> s;
	
	/** Оборачивалка имен пропертей s*/
	public IFunction1<String, String> propertyNameWrapper = (s) -> s; 
	
	/** Начала массива */
	public IExtendedList<String> arrayStarts = CollectionsSWL.createExtendedList("[");
	
	/** Конец массива */
	public IExtendedList<String> arrayEnds = CollectionsSWL.createExtendedList("]");
	
	/** Начала блоков */
	public IExtendedList<String> blockStarts = CollectionsSWL.createExtendedList("{");
	
	/** Концы блоков */
	public IExtendedList<String> blockEnds = CollectionsSWL.createExtendedList("}");
	
	/** Разделители запсей */
	public IExtendedList<String> elementSplitters = CollectionsSWL.createExtendedList(",");
	
	/** Границы литералов */
	public IExtendedList<String> literalBounds = CollectionsSWL.createExtendedList("'");
	
	/** Символ равенства */
	public IExtendedList<String> equals = CollectionsSWL.createExtendedList("=");
	
	/** Игнорируемые вне литералов символы */
	public IExtendedList<String> ignore = CollectionsSWL.createExtendedList(" ", "	", StringUtils.CR, StringUtils.LF, StringUtils.CRLF);
	
	/** Начало блока исключения (Игнорируемого читалкой) */
	public IExtendedList<String> exclusionStarts = CollectionsSWL.createExtendedList("/**", "--[");
	
	/** Конец блока-исключения (Игнорируемого читалкой) */
	public IExtendedList<String> exclusionEnds = CollectionsSWL.createExtendedList("*/", "]--");
	
	/** Получить {@link ISwconfWriter} для этого формата */
	public <T extends ISwconfWriter> T getWriter()
	{
		return getWriter(false);
	}
	
	/** Получить {@link ISwconfWriter} для этого формата */
	public <T extends ISwconfWriter> T getWriter(boolean isPretty)
	{
		T ret = (T) writerFun.apply();
		
		if (isPretty)
		{
			ret = (T) PrettyFormattedWriter.of(ret);
		}
		
		return ret;
	}
	
	/** Получить {@link SwconfReader} для этого формата */
	public <T extends SwconfReader> T getReader()
	{
		return (T) readerFun.apply();
	}
}
