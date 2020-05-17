package ru.swayfarer.swl2.swconf.lua;

import ru.swayfarer.swl2.swconf.format.SwconfFormat;

public class LuaSwconfFormat extends SwconfFormat {

	public LuaSwconfFormat()
	{
		readerFun = () -> new LuaToSwconfReader();
		writerFun = () -> new LuaToSwconfWriter();
	}
	
}
