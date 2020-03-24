package ru.swayfarer.swl2.xml;

import java.util.concurrent.atomic.AtomicBoolean;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.string.reader.StringReaderSWL;

/**
 * Парсер XML'ек в {@link XmlTag} 
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class XmlParser {

	/** Логгер */
	@InternalElement
	public ILogger logger = LoggingManager.getLogger();
	
	/** Начала исключений */
	@InternalElement
	public IExtendedList<String> exclusionStarts = CollectionsSWL.createExtendedList("<--");
	
	/** Концы исключений */
	@InternalElement
	public IExtendedList<String> exclusionEnds = CollectionsSWL.createExtendedList("-->");
	
	/** Читалка строк */
	@InternalElement
	public StringReaderSWL reader;
	
	/** Информация о читаемом таге */
	@InternalElement
	public XmlReadingInfo readingInfo;
	
	/** {@link StringBuilder} для кэширования прочитанных строк */
	@InternalElement
	public StringBuilder builder = new StringBuilder();
	
	/** Находится ли {@link XmlParser} в исключении */
	@InternalElement
	public boolean isInExclusion;
	
	/** Результат парсинга */
	@InternalElement
	public XmlTag result;
	
	/** Символы, которые будут скипаться и делить атрибуты */
	@InternalElement
	public IExtendedList<String> spaces = CollectionsSWL.createExtendedList(" ", StringUtils.CR, StringUtils.LF, StringUtils.TAB);

	/** Парсится ли уже строка? */
	@InternalElement
	public AtomicBoolean isParsing = new AtomicBoolean(false);
	
	/** Прочитать {@link XmlTag} из строки */
	public XmlTag readXml(@ConcattedString Object... text)
	{
		ExceptionsUtils.If(isParsing.get(), IllegalStateException.class, "Already parsing! Parser can't parse more than one xml at one time!");
		
		reset();
		isParsing.set(true);

		String str = StringUtils.concat(text);
		
		try
		{
			reader = new StringReaderSWL(str);

			while (reader.hasNextElement())
			{
				if (isInExlusion())
				{
					if (isExlusionsEnds())
						isInExclusion = false;
					
					reader.next();
				}
				else if (isExlusionStarts())
				{
					isInExclusion = true;
				}
				else if (isClosingBlockStarts())
				{
					skipClosingBlock();
					closeTag();
				}
				else if (isTagSelfClosed())
				{
					closeTag();
				}
				else if (isBlockClosed())
				{
					 closeBlock();
				}
				else if (isNewTagStarts())
				{
					startNewTag();
				}
				else if (isInAttributes() && reader.skipSome(spaces) || isEqual())
				{
					if (builder.length() > 0)
					{
						if (!hasName())
						{
							readTagName();
						}
						else if (!hasAttributeName())
						{
							readAttributeName();
						}
						else
						{
							readAttributeValue();
						}
						
						clearBuilder();
					}
				}
				else if (reader.skipSome("\""))
				{
					readingInfo.inAttributeValue = !readingInfo.inAttributeValue;
				}
				else
				{
					builder.append(reader.next());
				}
			}
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while parsing xml-string", str);
		}
		
		isParsing.set(false);

		return result;
	}
	
	/** Сбросить {@link XmlParser} для чтения новой xml-строки */
	public <T extends XmlParser> T reset() 
	{
		clearBuilder();
		isParsing.set(false);
		result = null;
		readingInfo = null;
		return (T) this;
	}
	
	/** Прочитать имя тага */
	public void readTagName()
	{
		readingInfo.tag.name = builder.toString();
	}
	
	/** Прочитать имя атрибута */
	public void readAttributeName()
	{
		readingInfo.attributeName = builder.toString();
	}
	
	/** Прочитать значение атрибута */
	public void readAttributeValue()
	{
		readingInfo.tag.attributes.put(readingInfo.attributeName, builder.toString());
		readingInfo.attributeName = null;
	}
	
//	public boolean isLastSymbolSpace()
//	{
//		return readingInfo != null && readingInfo.lastSymbolIsSpace;
//	}
	
	/** Читаются ли сейчас атрибуты текущего тага? */
	public boolean isInAttributes()
	{
		return readingInfo != null && readingInfo.inAttributes;
	}
	
	/** Создать блок */
	public void closeBlock()
	{
		if (readingInfo != null)
		{
			readingInfo.inAttributes = false;
			
			if (!hasName())
			{
				readTagName();
			}
			else if (hasAttributeName())
			{
				readAttributeValue();
			}
		}
		
		clearBuilder();
	}
	
	/** Прочитано ли имя текущего тага? */
	public boolean hasName()
	{
		XmlTag tag = getCurrentTag();
		
		return tag != null && !StringUtils.isEmpty(tag.name);
	}
	
	/** Закрыть таг */
	public void closeTag()
	{
		if (readingInfo != null)
		{
			readingInfo.tag.content = builder.toString();
			
			if (readingInfo.parent == null)
				result = readingInfo.tag;
			
			readingInfo = readingInfo.parent;
		}
		
		clearBuilder();
	}
	
	/** Пропустить закрывающий блок в {@link #reader}'е */
	public void skipClosingBlock()
	{
		while (reader.hasNextElement())
		{
			Character next = reader.next();
			
			if (next.equals('>'))
				return;
		}
	}
	
	/** Прочитано ли имя текущего атрибута? */
	public boolean hasAttributeName()
	{
		return readingInfo != null && !StringUtils.isEmpty(readingInfo.attributeName);
	}
	
	/** Очистить {@link #builder} */
	public void clearBuilder()
	{
		builder.delete(0, builder.length());
	}

	/** Находимся ли мы на знаке '='? */
	public boolean isEqual()
	{
		return reader.skipSome("=");
	}
	
	/** Встречен знак '=' */
	public void onEqual()
	{
		if (readingInfo != null)
		{
			readingInfo.attributeName = builder.toString();
			clearBuilder();
		}
	}
	
	/** Начато исключение? */
	public boolean isExlusionStarts()
	{
		return reader.skipSome(exclusionStarts);
	}
	
	/** Закончено исключение? */
	public boolean isExlusionsEnds()
	{
		return reader.skipSome(exclusionEnds);
	}
	
	/** Находится ли {@link XmlParser} в исключении (пропускает ли символы до его окончания?) */
	public boolean isInExlusion()
	{
		return isInExclusion;
	}
	
	public void startNewTag()
	{
		XmlTag tagParent = getCurrentTag();
		
		readingInfo = new XmlReadingInfo(readingInfo);
		
		readingInfo.tag = new XmlTag(tagParent);
		
		clearBuilder();
	}
	
	public boolean isBlockClosed()
	{
		return reader.skipSome(">");
	}
	
	public boolean isClosingBlockStarts()
	{
		return reader.skipSome("</");
	}
	
	public boolean isTagSelfClosed()
	{
		return reader.skipSome("/>");
	}

	public boolean isNewTagStarts()
	{
		return reader.skipSome("<");
	}

	public XmlTag getCurrentTag()
	{
		return readingInfo != null ? readingInfo.tag : null;
	}

	public static class XmlReadingInfo {

		public String attributeName;
		
		public XmlReadingInfo parent;
		public XmlTag tag;
		
		public boolean inAttributes = true;
		public boolean inAttributeValue;
 
		public XmlReadingInfo(XmlReadingInfo parent)
		{
			this.parent = parent;
		}

	}
}
