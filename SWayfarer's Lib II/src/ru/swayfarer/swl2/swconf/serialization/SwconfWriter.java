package ru.swayfarer.swl2.swconf.serialization;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.swconf.format.SwconfFormat;
import ru.swayfarer.swl2.swconf.primitives.SwconfArray;
import ru.swayfarer.swl2.swconf.primitives.SwconfBoolean;
import ru.swayfarer.swl2.swconf.primitives.SwconfNum;
import ru.swayfarer.swl2.swconf.primitives.SwconfObject;
import ru.swayfarer.swl2.swconf.primitives.SwconfPrimitive;
import ru.swayfarer.swl2.swconf.primitives.SwconfString;
import ru.swayfarer.swl2.swconf.serialization.formatter.ISwconfFormatter;

@SuppressWarnings("unchecked")
public class SwconfWriter {

	public StringBuilder stringBuilder = new StringBuilder();
	public SwconfFormat swconfFormat = new SwconfFormat();
	
	public IExtendedList<ISwconfFormatter> registeredFormatters = CollectionsSWL.createExtendedList();
	
	public SwconfWriter()
	{
		reset();
	}
	
	public void write(SwconfPrimitive primitive)
	{
		if (primitive.isArray())
			write(primitive.asArray());
		
		else if (primitive.isBoolean())
			write(primitive.asBoolean());
		
		else if (primitive.isNum())
			write(primitive.asNum());
		
		else if (primitive.isObject())
			write(primitive.asObject());
		
		else if (primitive.isString())
			write(primitive.asString());
	}
	
	public void write(SwconfString str)
	{
		writePrefix(str);
		stringBuilder.append(str.getValue());
	}
	
	public void write(SwconfBoolean bool)
	{
		writePrefix(bool);
		stringBuilder.append(bool.getValue());
	}
	
	public void write(SwconfNum num)
	{
		writePrefix(num);
		stringBuilder.append(num.getDouble());
	}
	
	public void write(SwconfArray array)
	{
		writePrefix(array);
		
		writeArrayStart();
		
		boolean isFirst = true;
		
		for (SwconfPrimitive primitive : array.elements)
		{
			if (!isFirst)
			{
				writeSplitter();
			}
			
			write(primitive);
			
			isFirst = false;
		}
		
		writeArrayEnd();
	}
	
	public void write(SwconfObject object)
	{
		boolean isNamed = !StringUtils.isEmpty(object.name);
		
		writePrefix(object);
		
		if (isNamed)
			writeBlockStart();
		
		boolean isFirst = true;
		
		for (SwconfPrimitive primitive : object.children.values())
		{
			if (!isFirst)
			{
				writeSplitter();
			}
			
			write(primitive);
			
			isFirst = false;
		}
		
		if (isNamed)
			writeBlockEnd();
	}
	
	public <T extends SwconfWriter> T reset() 
	{
		stringBuilder = new StringBuilder();
		return (T) this;
	}
	
	public <T extends SwconfWriter> T startWriting() 
	{
		registeredFormatters.each((e) -> e.onStart(stringBuilder));
		return (T) this;
	}
	
	public <T extends SwconfWriter> T endWriting() 
	{
		registeredFormatters.each((e) -> e.onEnd(stringBuilder));
		return (T) this;
	}
	
	public void writePrefix(SwconfPrimitive primitive)
	{
		String name = primitive.name;
		
		if (!StringUtils.isEmpty(name))
		{
			registeredFormatters.each((e) -> e.onNameStarts(stringBuilder));
			stringBuilder.append(name);
			registeredFormatters.each((e) -> e.onNameEnds(stringBuilder));
			writeEqual(); 
		}
	}
	
	public void writeLiteral()
	{
		registeredFormatters.each((e) -> e.onLiteralStarts(stringBuilder));
		stringBuilder.append(swconfFormat.literalBounds.getFirstElement());
		registeredFormatters.each((e) -> e.onLiteralEnds(stringBuilder));
	}
	
	public void writeEqual()
	{
		registeredFormatters.each((e) -> e.onEqualStarts(stringBuilder));
		stringBuilder.append(swconfFormat.equals.getFirstElement());
		registeredFormatters.each((e) -> e.onEqualEnds(stringBuilder));
	}
	
	public void writeArrayStart()
	{
		registeredFormatters.each((e) -> e.onArrayStartStarts(stringBuilder));
		stringBuilder.append(swconfFormat.arrayStarts.getFirstElement());
		registeredFormatters.each((e) -> e.onArrayStartEnds(stringBuilder));
	}
	
	public void writeArrayEnd()
	{
		registeredFormatters.each((e) -> e.onArrayEndStarts(stringBuilder));
		stringBuilder.append(swconfFormat.arrayEnds.getFirstElement());
		registeredFormatters.each((e) -> e.onArrayEndEnds(stringBuilder));
	}
	
	public void writeBlockStart()
	{
		registeredFormatters.each((e) -> e.onBlockStartStarts(stringBuilder));
		stringBuilder.append(swconfFormat.blockStarts.getFirstElement());
		registeredFormatters.each((e) -> e.onBlockStartEnds(stringBuilder));
	}
	
	public void writeBlockEnd()
	{
		registeredFormatters.each((e) -> e.onBlockEndStarts(stringBuilder));
		stringBuilder.append(swconfFormat.blockEnds.getFirstElement());
		registeredFormatters.each((e) -> e.onBlockEndEnds(stringBuilder));
	}
	
	public void writeSplitter()
	{
		registeredFormatters.each((e) -> e.onElementSplitterStarts(stringBuilder));
		stringBuilder.append(swconfFormat.elementSplitters.getFirstElement());
		registeredFormatters.each((e) -> e.onElementSplitterEnds(stringBuilder));
	}
	
	public String toSwconfString()
	{
		return stringBuilder.toString();
	}
	
}
