package ru.swayfarer.swl2.swconf2.types;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.var;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.string.DynamicString;

@SuppressWarnings("unchecked")
public class SwconfTable extends SwconfObject {

	public boolean hasKeys = true;
	public IExtendedList<TableEntry> entries = CollectionsSWL.createExtendedList();
	
	public <T extends SwconfTable> T put(SwconfObject value)
	{
		return put("", value);
	}
	
	public <T extends SwconfTable> T put(String key, SwconfObject value)
	{
		entries.add(new TableEntry(key, value));
		return (T) this;
	}
	
	@Override
	public boolean isTable()
	{
		return true;
	}
	
	public int size()
	{
		return entries.size();
	}

	
	@Override
	public String toString(int indent)
	{
		DynamicString ret = new DynamicString();
		
		ret.append("table: {");
		
		if (CollectionsSWL.isNullOrEmpty(entries))
		{
			ret.append("}");
		}
		else
		{
			ret.append("\n");
			
			for (var entry : entries)
			{
				ret.indent(indent + 1);
				
				if (hasKeys)
				{
					ret.append(entry.key);
					ret.append(" = ");
				}
				
				ret.append(entry.value.toString(indent + 1));
				ret.append("\n");
			}
			
			ret.indent(indent);
			ret.append("}");
		}
		
		return ret.toString();
	}
	
	@ToString
	@AllArgsConstructor
	public static class TableEntry {
		public String key;
		public SwconfObject value;
	}
	
}
