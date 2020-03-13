package ru.swayfarer.swl2.swconf.serialization;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.equals.EqualsUtils;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.string.reader.StringReaderSWL;
import ru.swayfarer.swl2.swconf.format.SwconfFormat;
import ru.swayfarer.swl2.swconf.primitives.SwconfArray;
import ru.swayfarer.swl2.swconf.primitives.SwconfBoolean;
import ru.swayfarer.swl2.swconf.primitives.SwconfNum;
import ru.swayfarer.swl2.swconf.primitives.SwconfObject;
import ru.swayfarer.swl2.swconf.primitives.SwconfPrimitive;
import ru.swayfarer.swl2.swconf.primitives.SwconfString;

@SuppressWarnings("unchecked")
public class SwconfReader {

	public StringReaderSWL reader;
	public SwconfFormat swconfFormat = new SwconfFormat();
	
	public StringBuilder stringBuilder = new StringBuilder();
	
	public StringBuilder commentBuilder = new StringBuilder();
	
	public CurrentReadingInfo currentReadingInfo;
	
	public IExtendedList<CurrentReadingInfo> readingInfos = CollectionsSWL.createExtendedList();
	
	public <T extends SwconfReader> T reset() 
	{
		readingInfos.clear();
		currentReadingInfo = new CurrentReadingInfo();
		readingInfos.add(currentReadingInfo);
		cleanBuilder();
		return (T) this;
	}
	
	public <T extends SwconfReader> T setFormat(SwconfFormat format) 
	{
		this.swconfFormat = format;
		return (T) this;
	}
	
	public <T extends SwconfReader> T setString(@ConcattedString Object... text) 
	{
		this.reader = new StringReaderSWL(StringUtils.concat(text));
		return (T) this;
	}
	
	public SwconfObject readSwconf(String swconfString)
	{
		reset();
		setString(swconfString);
		currentReadingInfo = getReadingInfo();
		currentReadingInfo.root = new SwconfObject();
		
		while (reader.hasNextElement())
		{
			if (currentReadingInfo.isInExlusion)
			{
				if (reader.skipSome(swconfFormat.exclusionEnds))
				{
					onExclusionClosed();
				}
				else
				{
					commentBuilder.append(reader.next());
				}
			}
			else if (currentReadingInfo.isInLiteral)
			{
				if (reader.skipSome(swconfFormat.literalBounds))
				{
					onLiteral();
				}
				else
				{
					readCurrent();
				}
			}
			else if (!reader.skipSome(swconfFormat.ignore))
			{
				if (reader.skipSome(swconfFormat.blockStarts))
				{
					newLayer(currentReadingInfo.lastReadedName);
				}
				
				else if (reader.skipSome(swconfFormat.blockEnds))
				{
					closeLayer();
				}
				
				else if (reader.skipSome(swconfFormat.arrayStarts))
				{
					newArray();
				}
				
				else if (reader.skipSome(swconfFormat.arrayEnds))
				{
					endArray();
				}
				
				else if (reader.skipSome(swconfFormat.equals))
				{
					onEqual();
				}
				
				else if (reader.skipSome(swconfFormat.exclusionStarts))
				{
					onExclusionStarted();
				}
				
				else if (reader.skipSome(swconfFormat.elementSplitters))
				{
					readProperty();
					cleanBuilder();
				}
				
				else if (reader.skipSome(swconfFormat.literalBounds))
				{
					onLiteral();
				}
				
				else
				{
					readCurrent();
				}
			}
		}
		
		readProperty();
		
		return currentReadingInfo.root;
	}
	
	public void onEqual()
	{
		currentReadingInfo.thereWasLiteral = false;
		currentReadingInfo.lastReadedName = stringBuilder.toString();
		
		cleanBuilder();
	}
	
	public void onLiteral()
	{
		currentReadingInfo.isInLiteral = !currentReadingInfo.isInLiteral;
		
		if (currentReadingInfo.isInLiteral)
			currentReadingInfo.thereWasLiteral = true;
	}
	
	public void readCurrent()
	{
		stringBuilder.append(reader.next().charValue());
	}
	
	public <T extends SwconfReader> T cleanBuilder() 
	{
		stringBuilder = new StringBuilder();
		return (T) this;
	}
	
	public boolean hasProperty()
	{
		return !StringUtils.isEmpty(currentReadingInfo.lastReadedName);
	}
	
	public void readProperty()
	{
		if (!hasProperty())
			return;
		
		SwconfObject root = currentReadingInfo.root;
		SwconfPrimitive primitive = null;
		
		String value = stringBuilder.toString();
		
		if (currentReadingInfo.thereWasLiteral)
			primitive = new SwconfString().setValue(value);
		else if (EqualsUtils.objectEqualsSome(value.toLowerCase(), "true", "false"))
			primitive = new SwconfBoolean().setValue(Boolean.valueOf(value));
		else if (StringUtils.isDouble(value))
			primitive = new SwconfNum().setValue(Double.valueOf(value));
		else if (!StringUtils.isEmpty(value))
			primitive = new SwconfString().setValue(value);
		
		if (primitive != null)
		{
			checkComment(primitive);
			
			if (currentReadingInfo.isInArray())
			{
				currentReadingInfo.array.addChild(primitive);
			}
			else
			{
				primitive.setName(currentReadingInfo.lastReadedName);
				
				root.addChild(primitive);

				currentReadingInfo.reset();
				
			}
			cleanBuilder();
		}
	}
	
	public void checkComment(SwconfPrimitive primitive)
	{
		String comment = currentReadingInfo.lastComment;
		
		if (!StringUtils.isEmpty(comment))
		{
			primitive.setComment(comment);
			currentReadingInfo.lastComment = null;
		}
	}
	
	public void newArray()
	{
		currentReadingInfo.array = new SwconfArray();
		currentReadingInfo.array.setName(currentReadingInfo.lastReadedName);
		currentReadingInfo.root.addChild(currentReadingInfo.array);
		checkComment(currentReadingInfo.array);
	}
	
	public void endArray()
	{
		onArrayClosed();
		currentReadingInfo.array = null;
	}
	
	public void onArrayClosed()
	{
		readProperty();
	}
	
	public void onBlockClosed()
	{
		readProperty();
		currentReadingInfo.reset();
	}
	
	public void onExclusionStarted()
	{
		currentReadingInfo.isInExlusion = true;
	}
	
	public void onExclusionClosed()
	{
		currentReadingInfo.isInExlusion = false;
		currentReadingInfo.lastComment = commentBuilder.toString();
		commentBuilder.delete(0, commentBuilder.length());
	}
	
	public CurrentReadingInfo closeLayer()
	{
		onBlockClosed();
		readingInfos.removeFirstElement();
		currentReadingInfo = getReadingInfo();
		return currentReadingInfo;
	}
	
	public CurrentReadingInfo newLayer(@ConcattedString Object... name)
	{
		SwconfObject object = new SwconfObject();
		object.setName(name);
		currentReadingInfo.root.addChild(object);
		
		CurrentReadingInfo readingInfo = new CurrentReadingInfo();
		readingInfo.root = object;
		
		readingInfos.add(0, readingInfo);
		
		this.currentReadingInfo = readingInfo;
		
		checkComment(object);
		
		return readingInfo;
	}
	
	public CurrentReadingInfo getReadingInfo()
	{
		return readingInfos.getFirstElement();
	}
	
	public class CurrentReadingInfo {
		
		public boolean isInExlusion;
		
		public SwconfObject root;
		
		public SwconfArray array;
		
		public String lastReadedName;
		
		public boolean isInLiteral;
		
		public boolean thereWasLiteral;
		
		public String lastComment;
		
		public boolean isInArray()
		{
			return array != null;
		}
		
		public void reset()
		{
			lastReadedName = "";
			isInLiteral = false;
			thereWasLiteral = false;
			isInExlusion = false;
			array = null;
		}
	}
	
}
