package ru.swayfarer.swl2.swconf.serialization.formatter;

import ru.swayfarer.swl2.swconf.serialization.writer.ISwconfWriter;
import ru.swayfarer.swl2.swconf.serialization.writer.SwconfWriterParent;

public class PropertyFormattedWriter extends SwconfWriterParent {

	public PropertyFormattedWriter(ISwconfWriter wrappedWriter)
	{
		super(wrappedWriter);
	}
	
	@Override
	public void writeLiteral()
	{
		// Nope!
	}

}
