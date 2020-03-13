package ru.swayfarer.swl2.swconf.format;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Пользовательский формат Swconf
 * @author swayfarer
 *
 */
public class SwconfFormat {

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
}
