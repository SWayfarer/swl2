package ru.swayfarer.swl2.swconf.format;

import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.swconf.lua.LuaSwconfFormat;
import ru.swayfarer.swl2.swconf.serialization.formatter.PropertyFormattedWriter;
import ru.swayfarer.swl2.swconf.serialization.writer.SwconfWriter;

public interface StandartSwconfFormats {

	public static final SwconfFormat PROPERTY_FORMAT = getPropertyFormat(System.lineSeparator());
	public static final SwconfFormat JSON_FORMAT = getJsonFormat();
	public static final SwconfFormat LUA_FORMAT = new LuaSwconfFormat();
		
	public static SwconfFormat getJsonFormat()
	{
		SwconfFormat format = new SwconfFormat();
		
		format.exclusionEnds.clear();
		format.exclusionStarts.clear();
		
		format.elementSplitters.setAll(",");
		format.equals.setAll(":");
		
		format.blockStarts.setAll("{");
		format.blockEnds.setAll("}");
		
		format.literalBounds.setAll("\"");
		
		format.arrayStarts.setAll("[");
		format.arrayEnds.setAll("]");
		
		format.literalBounds.setAll("\"");
		format.name = "Json";
		format.propertyNameUnwrapper = (s) -> StringUtils.subString(1, -1, s);
		format.propertyNameWrapper = (s) -> "\"" + s + "\"";
		
		format.onStart = "{";
		format.onEnd = "}";
		
		format.isCommentsEnabled = false;
		
		SwconfWriter w = new SwconfWriter();
		w.swconfFormat = format;
		
		format.writerFun = () -> w;
		
		return format;
	}
	
	public static SwconfFormat getPropertyFormat(String lineSplitter)
	{
		SwconfFormat format = new SwconfFormat();
		format.ignore.setAll(" ", StringUtils.TAB);
		format.exclusionEnds.setAll(lineSplitter);
		format.exclusionStarts.setAll("#");
		format.elementSplitters.setAll(lineSplitter);
		format.literalBounds.clear();
		
		format.writerFun = () -> new PropertyFormattedWriter(new SwconfWriter().setFormat(format));
		
		return format;
	}
}
