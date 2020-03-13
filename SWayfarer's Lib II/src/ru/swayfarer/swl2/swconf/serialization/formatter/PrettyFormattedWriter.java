package ru.swayfarer.swl2.swconf.serialization.formatter;

import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.swconf.serialization.writer.ISwconfWriter;
import ru.swayfarer.swl2.swconf.serialization.writer.SwconfWriterParent;

public class PrettyFormattedWriter extends SwconfWriterParent {

	public int indientSize = 4;
	public int indient = 0;
	
	public String lineSplitter = StringUtils.LF;
	
	public PrettyFormattedWriter(ISwconfWriter wrappedWriter)
	{
		super(wrappedWriter);
	}
	
	@Override
	public void writeEqual()
	{
		parent.writeRaw(" ");
		super.writeEqual();
		parent.writeRaw(" ");
	}
	
	@Override
	public <T extends ISwconfWriter> T startWriting()
	{
		decorate();
		return super.startWriting();
	}
	
	@Override
	public void writeExclusion(String comment)
	{
		super.writeExclusion(comment);
		decorate();
	}
	
	@Override
	public void writeExclusionStart()
	{
		super.writeExclusionStart();
		parent.writeRaw(" ");
	}
	
	@Override
	public void writeExclusionEnd()
	{
		parent.writeRaw(" ");
		super.writeExclusionEnd();
	}
	
	@Override
	public void writeBlockStart()
	{
		super.writeBlockStart();
		indient ++;
		decorate();
	}
	
	@Override
	public void writeBlockEnd()
	{
		indient --;
		decorate();
		super.writeBlockEnd();
	}
	
	@Override
	public void writeSplitter()
	{
		super.writeSplitter();
		decorate();
	}
	
	@Override
	public void writeArrayStart()
	{
		super.writeArrayStart();
		indient ++;
		decorate();
	}
	
	@Override
	public void writeArrayEnd()
	{
		indient --;
		decorate();
		super.writeArrayEnd();
	}
	
	public void decorate()
	{
		newLine();
		writeIndient();
	}
	
	public void writeIndient()
	{
		parent.writeRaw(StringUtils.createSpacesSeq(indientSize * indient));
	}
	
	public void newLine()
	{
		parent.writeRaw(lineSplitter);
	}
	
	public static PrettyFormattedWriter of(ISwconfWriter writer)
	{
		return new PrettyFormattedWriter(writer);
	}

}
