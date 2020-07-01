package ru.swayfarer.swl2.swconf2.yaml;

import lombok.var;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.string.DynamicString;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.swconf2.types.SwconfTable;

public class Yaml2SwconfWriter {

	public String equalsSymbol = ": ";
	public String listSymbol = "- ";
	public String commentSymbol = "# ";
	public int indent = 2;
	
	public String write(SwconfTable table)
	{
		var buffer = new DynamicString();
		buffer.indentSpacesCount = indent;
		write(table, 0, buffer);
		return buffer.toString();
	}
	
	public void write(SwconfTable table, int currentIndent, DynamicString ret)
	{
		for (var entry : table.entries)
		{
			ret.indent(currentIndent);
			
			var value = entry.value;
			
			var comment = value.getComment();
			
			if (!StringUtils.isBlank(comment))
			{
				var lines = comment.split("\n");
				
				for (var line : lines)
				{
					ret.append(commentSymbol);
					ret.append(line);
					
					ret.append("\n");
					ret.indent(currentIndent);
				}
			}
			
			boolean isListElement = false;
			
			if (table.hasKeys)
			{
				String key = entry.key;
				
				if (key.contains(": "))
					ExceptionsUtils.unsupportedOperation("Can't write key that contains ': ' to yaml syntax! It's system word. Please, don't use that key or try another format!");
				
				ret.append(key);
				ret.append(equalsSymbol);
			}
			else
			{
				ret.append(listSymbol);
			}
			
			if (value.isTable())
			{
				ret.append("\n");
				write(value.asTable(), currentIndent + 1, ret);
			}
			else
			{
				String str = value.getValue().toString();
				
				if (value.isString())
				{
					if (str.contains("'"))
					{
						str = "\"" + str.replace("\"", "\\\"") + "\"";
					}
					else
					{
						str = "'" + str.replace("'", "\\'") + "'";
					}
				}
				
				ret.append(str);
				
				ret.append("\n");
			}
		}
	}
	
}
