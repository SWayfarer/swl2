package ru.swayfarer.swl2.swconf.lua;

import lombok.var;
import ru.swayfarer.swl2.resource.streams.DataInputStreamSWL;
import ru.swayfarer.swl2.resource.streams.DataOutputStreamSWL;
import ru.swayfarer.swl2.string.DynamicString;
import ru.swayfarer.swl2.swconf2.formats.IFormatProvider;
import ru.swayfarer.swl2.swconf2.types.SwconfTable;

public class Lua2SwconfProvider implements IFormatProvider {

	@Override
	public boolean isAcceptingExtension(String ext)
	{
		return ext.equals("lua");
	}

	@Override
	public SwconfTable readSwconf(DataInputStreamSWL is)
	{
		return new Lua2SwconfReader().readSwconf(is);
	}

	@Override
	public void writeSwconf(SwconfTable swconfTable, DataOutputStreamSWL os)
	{
		DynamicString buffer = new DynamicString();
		
		var writer = new Lua2SwconfWriter();
		writer.write(swconfTable, 0, false, buffer);
		os.writeString(buffer);
	}

}
