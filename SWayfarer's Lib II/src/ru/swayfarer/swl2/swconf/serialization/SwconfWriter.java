package ru.swayfarer.swl2.swconf.serialization;

import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.swconf.format.SwconfFormat;
import ru.swayfarer.swl2.swconf.primitives.SwconfArray;
import ru.swayfarer.swl2.swconf.primitives.SwconfBoolean;
import ru.swayfarer.swl2.swconf.primitives.SwconfNum;
import ru.swayfarer.swl2.swconf.primitives.SwconfObject;
import ru.swayfarer.swl2.swconf.primitives.SwconfPrimitive;
import ru.swayfarer.swl2.swconf.primitives.SwconfString;

@SuppressWarnings("unchecked")
public class SwconfWriter {

	public StringBuilder stringBuilder = new StringBuilder();
	public SwconfFormat swconfFormat = new SwconfFormat();
	
	public SwconfWriter()
	{
		reset();
	}
	
	public String toSwconfString()
	{
		return stringBuilder.toString();
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
	
	public void writePrefix(SwconfPrimitive primitive)
	{
		String name = primitive.name;
		
		if (!StringUtils.isEmpty(name))
		{
			stringBuilder.append(name);
			writeEqual(); 
		}
	}
	
	public void writeLiteral()
	{
		stringBuilder.append(swconfFormat.literalBounds.getFirstElement());
	}
	
	public void writeEqual()
	{
		stringBuilder.append(swconfFormat.equals.getFirstElement());
	}
	
	public void writeArrayStart()
	{
		stringBuilder.append(swconfFormat.arrayStarts.getFirstElement());
	}
	
	public void writeArrayEnd()
	{
		stringBuilder.append(swconfFormat.arrayEnds.getFirstElement());
	}
	
	public void writeBlockStart()
	{
		stringBuilder.append(swconfFormat.blockStarts.getFirstElement());
	}
	
	public void writeBlockEnd()
	{
		stringBuilder.append(swconfFormat.blockEnds.getFirstElement());
	}
	
	public void writeSplitter()
	{
		stringBuilder.append(swconfFormat.elementSplitters.getFirstElement());
	}
	
}
