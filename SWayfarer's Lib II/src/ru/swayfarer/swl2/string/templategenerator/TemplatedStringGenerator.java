package ru.swayfarer.swl2.string.templategenerator;

import java.util.Map;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.equals.EqualsUtils;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.string.DynamicString;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.string.property.StringProperty;
import ru.swayfarer.swl2.string.reader.StringReaderSWL;

@SuppressWarnings("unchecked")
public class TemplatedStringGenerator {

	public static ILogger logger = LoggingManager.getLogger();
	
	public IExtendedList<String> ignoreText = CollectionsSWL.createExtendedList(StringUtils.SPACE, StringUtils.TAB);
	public StringReaderSWL reader;
	public DynamicString dynamicString = new DynamicString();
	public String macroStart = "#";
	public IExtendedList<String> commentStarts = CollectionsSWL.createExtendedList("/--");
	
	public IExtendedList<Boolean> conditions = CollectionsSWL.createExtendedList();
	
	public Map<String, StringProperty> properties = CollectionsSWL.createHashMap(); 
	
	public boolean isStarted = false;
	
	public TemplatedStringGenerator(String template)
	{
		reader = new StringReaderSWL(template);
	}
	
	public <T extends TemplatedStringGenerator> T clear() 
	{
		properties.clear();
		dynamicString.clear();
		return (T) this;
	}
	
	public String generate()
	{
		reader.pos = 0;
		
		while (reader.hasNextElement())
		{
			if (!skipComment() && !processMacro())
			{
				if (isNeedsToWrite())
				{
					String str = applyProperties(reader.readLine()); 
					dynamicString.append(str, reader.lineSplitter);
				}
				else
				{
					// Пропускаем строку 
					reader.skipLine();
				}
			}
		}
		
		return dynamicString.toString();
	}
	
	public <T extends TemplatedStringGenerator> T setProperty(String key, @ConcattedString Object... value) 
	{
		this.properties.put(key, new StringProperty(value));
		return (T) this;
	}
	
	public boolean skipComment()
	{
		if (reader.skipSome(commentStarts))
		{
			reader.readLine();
			return true;
		}
		
		return false;
	}
	
	public String applyProperties(String str)
	{
		DynamicString dyn = new DynamicString(str);
		
		for (Map.Entry<String, StringProperty> property : properties.entrySet())
		{
			String replace = "${" + property.getKey() + "}";
			dyn.replace(replace, property.getValue().getValue());
		}
		
		return dyn.toString();
	}
	
	public boolean processCondition(String condition)
	{
		System.out.println("Processing condition " + condition);
		condition = toValue(condition);
		
		if (condition.contains("||"))
		{
			String[] split = condition.split("||");
			
			boolean ret = true;
			boolean isFirst = true;
			
			for (String str : split)
			{
				if (isFirst)
					ret = processCondition(str);
				else
					ret = ret || processCondition(condition);
			}
			
			return ret;
		}
		else if (condition.contains("&&"))
		{
			String[] split = condition.split("&&");
			
			boolean ret = true;
			boolean isFirst = true;
			
			for (String str : split)
			{
				if (isFirst)
					ret = processCondition(str);
				else
					ret = ret && processCondition(condition);
			}
			
			return ret;
		}
		else if (condition.contains("=="))
		{
			String[] split = condition.split("==");
			
			boolean ret = true;
			boolean isFirst = true;
			
			int max = split.length - 1;
			
			for (int i1 = 0; i1 < max; i1 ++)
			{
				String str = split[i1];
				String cmpStr = split[i1 + 1];
				
				str = toValue(str);
				cmpStr = toValue(cmpStr);
				
				if (isFirst)
					ret = EqualsUtils.objectEqualsAll(str, cmpStr);
				else
					ret = ret && EqualsUtils.objectEqualsAll(str, cmpStr);
				
				isFirst = false;
			}
			
			return ret;
		}
		
		return Boolean.valueOf(condition);
	}
	
	public String toValue(String str)
	{
		int off = 0;
		
		while (str.startsWith(" ", off))
			off ++;
		
		str = str.substring(off);
		
		while (str.endsWith(" "))
			str = StringUtils.subString(0, -1, str);
		
		if (str.startsWith("$") && !str.contains(" "))
		{
			String propertyName = str.substring(1);
			
			StringProperty property = properties.get(propertyName);
			return property == null ? null : property.getValue();
		}
		else
			return str;
	}
	
	public boolean isNeedsToWrite()
	{
		return !isInFalseCondition() && isStarted;
	}
	
	public boolean isInFalseCondition()
	{
		return conditions.contains(Boolean.FALSE);
	}
	
	public void processMacroCommand(String macro)
	{
		if (macro.equals("text"))
		{
			isStarted = true;
		}
		else if (macro.equals("endText"))
		{
			isStarted = false;
		}
		else if (!isStarted)
		{
			// Nope!
		}
		if (macro.startsWith("if (") && macro.endsWith(")"))
		{
			macro = StringUtils.subString(4, -1, macro);
			conditions.add(0, processCondition(macro));
		}
		else if (macro.startsWith("endif"))
		{
			conditions.removeFirstElement();
		}
		else if (isInFalseCondition())
		{
			return;
		}
		else if (macro.startsWith("set "))
		{
			macro = StringUtils.subString(4, 0, macro);
			
			if (!StringUtils.isEmpty(macro))
			{
				macro = removeFirstSpaces(macro);
				int indexOfSpace = macro.indexOf(" ");
				
				if (indexOfSpace > 0)
				{
					String name = macro.substring(0, indexOfSpace);
					macro = macro.substring(indexOfSpace + 1);
					macro = applyProperties(macro);
					String value = toValue(macro);
					properties.put(name, new StringProperty(value));
				}
			}
		}
	}
	
	public String removeFirstSpaces(String macro)
	{
		int pos = 0;
		
		while (macro.startsWith(" ", pos))
			pos ++;
		
		return macro.substring(pos);
	}
	
	public boolean processMacro()
	{
		int currentPos = reader.pos;
		skipSpacesAtStart();
		
		if (skipComment())
		{
			// Nope!
			return true;
		}
		else if (reader.skipSome(macroStart))
		{
			processMacroCommand(reader.readLine());
			return true;
		}
		else
		{
			reader.pos = currentPos;
			return false;
		}
	}
	
	/** Пропустить пробелы и табы из {@link #ignoreText} */
	public void skipSpacesAtStart()
	{
		while (reader.hasNextElement() && reader.skipSome(ignoreText)) {}
	}
	
}
