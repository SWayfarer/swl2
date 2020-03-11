package ru.swayfarer.swl2.swconf.serialization.formatter;

import ru.swayfarer.swl2.string.StringUtils;

public class PrettySwconfFormatter extends AbstractSwconfFormatter {

	public int indientSize = 4;
	public int indient = 0;
	
	public String lineSplitter = StringUtils.LF;
	
	@Override
	public void onEqualStarts(StringBuilder sb)
	{
		sb.append(" ");
	}
	
	@Override
	public void onEqualEnds(StringBuilder sb)
	{
		sb.append(" ");
	}
	
	@Override
	public void onStart(StringBuilder sb)
	{
		decorate(sb);
	}
	
	@Override
	public void onBlockStartEnds(StringBuilder sb)
	{
		indient ++;
		decorate(sb);
	}
	
	@Override
	public void onBlockEndStarts(StringBuilder sb)
	{
		indient --;
		decorate(sb);
	}
	
	@Override
	public void onElementSplitterEnds(StringBuilder sb)
	{
		decorate(sb);
	}
	
	@Override
	public void onArrayStartEnds(StringBuilder sb)
	{
		indient ++;
		decorate(sb);
	}
	
	@Override
	public void onArrayEndStarts(StringBuilder sb)
	{
		indient --;
		decorate(sb);
	}
	
	public void decorate(StringBuilder builder)
	{
		newLine(builder);
		writeIndient(builder);
	}
	
	public void writeIndient(StringBuilder builder)
	{
		builder.append(StringUtils.createSpacesSeq(indientSize * indient));
	}
	
	public void newLine(StringBuilder builder)
	{
		builder.append(lineSplitter);
	}
	
}
