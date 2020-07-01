package ru.swayfarer.swl2.swconf.lua;

import lombok.var;
import ru.swayfarer.swl2.string.DynamicString;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.swconf2.types.SwconfTable;

public class Lua2SwconfWriter {

	public void write(SwconfTable table, int currentIndent, boolean isPlacingSplitters, DynamicString buffer)
	{
		for (var entry : table.entries)
		{
			buffer.append("\n");
			buffer.indent(currentIndent);
			
			var name = entry.key;
			var value = entry.value;
			
			var comment = value.getComment();
			
			if (!StringUtils.isBlank(comment))
			{
				var lines = comment.split("\n");
				
				for (var line : lines)
				{
					buffer.append("-- ");
					buffer.append(line);
					buffer.append("\n");
					buffer.indent(currentIndent);
				}
			}
			
			if (value.isTable())
			{
				if (table.hasKeys)
				{
					buffer.append(name);
					buffer.append(" = ");
				}
				
				buffer.append("{");
				
				buffer.indent(currentIndent + 1);
				write(value.asTable(), currentIndent + 1, true, buffer);
				
				buffer.append("\n");
				buffer.indent(currentIndent);
				buffer.append("}");
				
				if (isPlacingSplitters)
					buffer.append(",");
			}
			else
			{

				var str = String.valueOf(value.getValue());
				
				if (value.isString())
				{
					str = "\"" + str.replace("\"", "\\\"") + "\"";
				}
				
				if (table.hasKeys)
				{
					buffer.append(name);
					buffer.append(" = ");
				}
				
				buffer.append(str);
				
				if (isPlacingSplitters)
					buffer.append(",");
			}
		}
	}
}
