package ru.swayfarer.swl2.generics;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.string.DynamicString;
import ru.swayfarer.swl2.string.reader.StringReaderSWL;

public class GenericsParser {

	public static ILogger logger = LoggingManager.getLogger();
	
	public static String genericStart = "<";
	public static String genericEnd = ">";
	public static String elementSplitter = ", ";

	public DynamicString typeBuffer = new DynamicString();
	public DynamicString childBuffer = new DynamicString();
	
	public StringReaderSWL reader;
	
	public IExtendedList<GenericObject> result = CollectionsSWL.createExtendedList();
	
	public void parse(String genericString)
	{
		reader = new StringReaderSWL(genericString);
		
		int openedGenerics = 0;
		
		newGeneric();
		
		while (reader.hasNextElement())
		{
			if (reader.skipSome(genericStart))
			{
				openedGenerics ++;
				
				if (openedGenerics > 1)
				{
					childBuffer.append(genericStart);
				}
			}
			else if (reader.skipSome(genericEnd))
			{
				openedGenerics --;
				
				if (openedGenerics < 0)
					throw new GenericParseException("Invalid generic close at " +reader.pos + "! No opened generics found there!");
				else if (openedGenerics == 0)
				{
					parseChildren();
				}
				else
				{
					childBuffer.append(genericEnd);
				}
			}
			else if (openedGenerics == 0)
			{
				if (reader.skipSome(elementSplitter))
				{
					newGeneric();
				}
				else 
				{
					typeBuffer.append(reader.next());
				}
			}
			else
			{
				childBuffer.append(reader.next());
			}
		}
		
		closeGeneric();
	}
	
	public void parseChildren()
	{
		String genericString = childBuffer.toString();
		childBuffer.clear();
		
		GenericsParser genericsParser = new GenericsParser();
		genericsParser.parse(genericString);
		GenericObject current = getCurrentObject();
		
		if (current != null && !CollectionsSWL.isNullOrEmpty(genericsParser.result))
		{
			current.childs.addAll(genericsParser.result);
		}
	}
	
	public void newGeneric()
	{
		closeGeneric();
		GenericObject object = new GenericObject();
		result.add(object);
	}
	
	public void closeGeneric()
	{
		GenericObject object = getCurrentObject();
		
		if (object != null)
		{
			String string = typeBuffer.toString();
			object.setTypeCanonicalName(string);
			typeBuffer.clear();
			childBuffer.clear();
		}
	}
	
	public GenericObject getCurrentObject()
	{
		return result.getLastElement();
	}
	
}