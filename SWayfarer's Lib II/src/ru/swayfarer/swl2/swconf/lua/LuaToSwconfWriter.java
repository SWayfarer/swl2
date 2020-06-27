package ru.swayfarer.swl2.swconf.lua;

import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.string.DynamicString;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.swconf.primitives.SwconfArray;
import ru.swayfarer.swl2.swconf.primitives.SwconfBoolean;
import ru.swayfarer.swl2.swconf.primitives.SwconfNum;
import ru.swayfarer.swl2.swconf.primitives.SwconfObject;
import ru.swayfarer.swl2.swconf.primitives.SwconfPrimitive;
import ru.swayfarer.swl2.swconf.primitives.SwconfString;
import ru.swayfarer.swl2.swconf.serialization.writer.SwconfWriter;

public class LuaToSwconfWriter extends SwconfWriter {

	public String lineSplitter = StringUtils.LF;
	
	public static ILogger logger = LoggingManager.getLogger();
	
	public boolean isWaiting = true;
	public int isPlacingSplitters = 0;
	public int indent = 0;
	
	public DynamicString sb = new DynamicString();
	
	public void write(SwconfObject obj)
	{
		if (!isWaiting)
		{
			isPlacingSplitters ++;
			indent ++;
			
			if (!StringUtils.isBlank(obj.name))
			{
				sb.append(obj.name);
				sb.append(" = {");
			}
			else
			{
				sb.append("{");
			}
			
			newLine();
		}
		
		isWaiting = false;
		
		for (SwconfPrimitive prim : obj.children.values())
		{
			if (prim != null)
			{
				indent();
				write(prim);
				placeSplitter();
				newLine();
			}
		}
		
		if (isPlacingSplitters > 0)
		{
			indent --;
			indent();
			sb.append("}");
		}
		
		isPlacingSplitters --;
	}
	
	public void writeComment(String str)
	{
		if (!StringUtils.isBlank(str))
		{
			for (String ln : str.split("\n"))
			{
				sb.append("-- ");
				sb.append(ln);
				newLine();
				indent();
			}
		}
	}
	
	public void write(SwconfPrimitive obj)
	{
		writeComment(obj.comment);
		
		if (obj instanceof SwconfObject)
			write((SwconfObject) obj);
		
		else if (obj instanceof SwconfString)
			write((SwconfString) obj);
		
		else if (obj instanceof SwconfNum)
			write((SwconfNum) obj);
		
		else if (obj instanceof SwconfBoolean)
			write((SwconfBoolean) obj);
		
		else if (obj instanceof SwconfArray)
			write((SwconfArray) obj);
	}
	
	public void write(SwconfNum i)
	{
		if (!StringUtils.isBlank(i.name))
		{
			sb.append(i.name);
			sb.append(" = ");
		}
		
		sb.append(i.rawValue);
		//placeSplitter();
	}
	
	public void write(SwconfBoolean b)
	{
		if (!StringUtils.isBlank(b.name))
		{
			sb.append(b.name);
			sb.append(" = ");
		}
		
		sb.append(b.rawValue);
		
		//placeSplitter();
	}
	
	public void write(SwconfString str)
	{
		String value = str.getValue();
		
		String openLiteralSymbol = value.contains(lineSplitter) ? "[[" : value.contains("'") ? "\"" : "'";
		String closeLiteralSymbol = value.contains(lineSplitter) ? "]]" : value.contains("'") ? "\"" : "'";
		
		
		if (!StringUtils.isBlank(str.name))
		{
			sb.append(str.name);
			sb.append(" = ");
		}
		
		sb.append(openLiteralSymbol);
		sb.append(value);
		sb.append(closeLiteralSymbol);
		
		//placeSplitter();
	}
	
	public void newLine()
	{
		sb.append(lineSplitter);
	}
	
	public void placeSplitter()
	{
		if (isPlacingSplitters > 0)
		{
			sb.append(",");
		}
	}
	
	public void indent()
	{
		sb.append(StringUtils.createSpacesSeq(4 * indent));
	}
	
	public String toSwconfString()
	{
		return sb.toString();
	}

	@Override
	public void write(SwconfArray array)
	{
		if (!StringUtils.isBlank(array.name))
		{
			sb.append(array.name);
			sb.append(" = {");
			newLine();
		}
		
		indent ++;
		isPlacingSplitters ++;
		
		for (SwconfPrimitive prim : array.elements)
		{
			indent();
			write(prim);
			placeSplitter();
			newLine();
		}
		
		indent --;
		isPlacingSplitters --;
		
		if (!StringUtils.isBlank(array.name))
		{
			indent();
			sb.append("}");
		}
	}
}
