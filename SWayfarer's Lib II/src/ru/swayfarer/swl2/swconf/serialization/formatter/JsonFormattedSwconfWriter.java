package ru.swayfarer.swl2.swconf.serialization.formatter;

import ru.swayfarer.swl2.swconf.serialization.writer.ISwconfWriter;
import ru.swayfarer.swl2.swconf.serialization.writer.SwconfWriter;
import ru.swayfarer.swl2.swconf.serialization.writer.SwconfWriterParent;

public class JsonFormattedSwconfWriter extends SwconfWriterParent {

	public JsonFormattedSwconfWriter()
	{
		this(new SwconfWriter());
	}
	
	public JsonFormattedSwconfWriter(ISwconfWriter wrappedWriter)
	{
		super(wrappedWriter);
	}

	@Override
	public void writeExclusion(String comment)
	{
		// Nope!
	}
	
	public static JsonFormattedSwconfWriter of(ISwconfWriter writer)
	{
		return new JsonFormattedSwconfWriter(writer);
	}
	
	@Override
	public <T extends ISwconfWriter> T startWriting()
	{
		parent.writeRaw("{");
		return super.startWriting();
	}
	
	@Override
	public <T extends ISwconfWriter> T endWriting()
	{
		T ret = super.endWriting();
		parent.writeRaw("}");
		return ret;
	}

	@Override
	public void writeln(String str)
	{
		parent.writeRaw(str);
	}
}
