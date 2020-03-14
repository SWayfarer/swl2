package ru.swayfarer.swl2.swconf.serialization.writer;

import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.swconf.format.SwconfFormat;
import ru.swayfarer.swl2.swconf.primitives.SwconfArray;
import ru.swayfarer.swl2.swconf.primitives.SwconfBoolean;
import ru.swayfarer.swl2.swconf.primitives.SwconfNum;
import ru.swayfarer.swl2.swconf.primitives.SwconfObject;
import ru.swayfarer.swl2.swconf.primitives.SwconfPrimitive;
import ru.swayfarer.swl2.swconf.primitives.SwconfString;

@SuppressWarnings("unchecked")
public class SwconfWriter implements ISwconfWriter {

	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Кэш строки, записываемой этим {@link ISwconfWriter}'ом */
	@InternalElement
	public StringBuilder stringBuilder = new StringBuilder();
	
	/** Формат, используемый при записи */
	@InternalElement
	public SwconfFormat swconfFormat = new SwconfFormat();
	
	/** Родительский {@link ISwconfWriter} */
	@InternalElement
	public ISwconfWriter parent = this;
	
	/** Конструктор */
	public SwconfWriter()
	{
		reset();
	}
	
	@Override
	public void write(SwconfPrimitive primitive)
	{
		if (primitive.isArray())
			parent.write(primitive.asArray());
		
		else if (primitive.isBoolean())
			parent.write(primitive.asBoolean());
		
		else if (primitive.isNum())
			parent.write(primitive.asNum());
		
		else if (primitive.isObject())
			parent.write(primitive.asObject());
		
		else if (primitive.isString())
			parent.write(primitive.asString());
	}
	
	@Override
	public void write(SwconfString str)
	{
		writeComment(str);
		writePrefix(str);
		parent.writeLiteral();
		parent.writeRaw(str.getValue());
		parent.writeLiteral();
	}
	
	@Override
	public void write(SwconfBoolean bool)
	{
		writeComment(bool);
		writePrefix(bool);
		parent.writeRaw(bool.getValue());
	}
	
	@Override
	public void write(SwconfNum num)
	{
		writeComment(num);
		writePrefix(num);
		parent.writeRaw(num.getDouble());
	}
	
	@Override
	public void write(SwconfArray array)
	{
		writeComment(array);
		
		writePrefix(array);
		
		parent.writeArrayStart();
		
		boolean isFirst = true;
		
		for (SwconfPrimitive primitive : array.elements)
		{
			if (!isFirst)
			{
				parent.writeSplitter();
			}
			
			parent.write(primitive);
			
			isFirst = false;
		}
		
		parent.writeArrayEnd();
	}
	
	@Override
	public void write(SwconfObject object)
	{
		writeComment(object);
		
		boolean isNamed = !StringUtils.isEmpty(object.name);
		
		writePrefix(object);
		
		if (isNamed)
			parent.writeBlockStart();
		
		boolean isFirst = true;
		
		for (SwconfPrimitive primitive : object.children.values())
		{
			if (!isFirst)
			{
				parent.writeSplitter();
			}
			
			parent.write(primitive);
			
			isFirst = false;
		}
		
		if (isNamed)
			parent.writeBlockEnd();
	}
	
	@Override
	public <T extends ISwconfWriter> T reset() 
	{
		stringBuilder = new StringBuilder();
		return (T) this;
	}
	
	@Override
	public <T extends ISwconfWriter> T startWriting() 
	{
		return (T) this;
	}
	
	@Override
	public <T extends ISwconfWriter> T endWriting() 
	{
		return (T) this;
	}
	
	/** Записать префикс (имя и знак равенства) */
	public void writePrefix(SwconfPrimitive primitive)
	{
		String name = primitive.name;
		
		if (!StringUtils.isEmpty(name))
		{
			name = swconfFormat.propertyNameWrapper.apply(name);
			
			parent.writeName(name);
			parent.writeEqual(); 
		}
	}
	
	public void writeName(String name)
	{
		parent.writeRaw(name);
	}
	
	@Override
	public void writeLiteral()
	{
		parent.writeRaw(swconfFormat.literalBounds.getFirstElement());
	}
	
	@Override
	public void writeEqual()
	{
		parent.writeRaw(swconfFormat.equals.getFirstElement());
	}
	
	@Override
	public void writeArrayStart()
	{
		parent.writeRaw(swconfFormat.arrayStarts.getFirstElement());
	}
	
	@Override
	public void writeArrayEnd()
	{
		parent.writeRaw(swconfFormat.arrayEnds.getFirstElement());
	}
	
	@Override
	public void writeBlockStart()
	{
		parent.writeRaw(swconfFormat.blockStarts.getFirstElement());
	}
	
	@Override
	public void writeBlockEnd()
	{
		parent.writeRaw(swconfFormat.blockEnds.getFirstElement());
	}
	
	@Override
	public void writeSplitter()
	{
		parent.writeRaw(swconfFormat.elementSplitters.getFirstElement());
	}
	
	/** Достать и записать коментарий для примитива, если он не пуст */
	public void writeComment(SwconfPrimitive primitive)
	{
		if (!StringUtils.isEmpty(primitive.comment))
		{
			parent.writeExclusion(primitive.comment);
		}
	}
	
	@Override
	public String toSwconfString()
	{
		return stringBuilder.toString();
	}
	
	@Override
	public void writeExclusionStart()
	{
		parent.writeRaw(swconfFormat.exclusionStarts.getFirstElement());
	}
	
	@Override
	public void writeExclusionEnd()
	{
		parent.writeRaw(swconfFormat.exclusionEnds.getFirstElement());
	}

	@Override
	public void writeRaw(@ConcattedString Object... text)
	{
		String s = StringUtils.concat(text);
		
		if (!StringUtils.isEmpty(s))
		{
			stringBuilder.append(s);
		}
	}

	@Override
	public <T extends ISwconfWriter> T setParent(ISwconfWriter writer)
	{
		this.parent = writer;
		return (T) this;
	}

	@Override
	public void writeExclusion(String comment)
	{
		if (!StringUtils.isEmpty(comment))
		{
			parent.writeExclusionStart();
			parent.writeRaw(comment);
			parent.writeExclusionEnd();
		}
	}

	@Override
	public <T extends ISwconfWriter> T setFormat(SwconfFormat format)
	{
		this.swconfFormat = format;
		return (T) this;
	}
	
}
