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
	
	@Override
	public void writeExclusion(String comment)
	{
		String[] split = comment.split("\n");
		
		if (split.length > 1)
		{
			for (String s : split)
			{
				parent.writeExclusion(s);
			}
		}
		else
		{
			super.writeExclusion(comment);
		}
	}
	
	public static PropertyFormattedWriter of(ISwconfWriter writer)
	{
		return new PropertyFormattedWriter(writer);
	}

}
