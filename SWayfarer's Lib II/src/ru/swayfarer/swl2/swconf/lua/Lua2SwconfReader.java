package ru.swayfarer.swl2.swconf.lua;

import lombok.var;
import ru.swayfarer.swl2.lua.LuaInterpreter;
import ru.swayfarer.swl2.resource.streams.DataInputStreamSWL;
import ru.swayfarer.swl2.swconf2.types.SwconfBoolean;
import ru.swayfarer.swl2.swconf2.types.SwconfNum;
import ru.swayfarer.swl2.swconf2.types.SwconfObject;
import ru.swayfarer.swl2.swconf2.types.SwconfString;
import ru.swayfarer.swl2.swconf2.types.SwconfTable;
import ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.LuaString;
import ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.LuaTable;
import ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.LuaValue;

public class Lua2SwconfReader {

	public SwconfTable readSwconf(DataInputStreamSWL is)
	{
		LuaInterpreter configInterpreter = getConfigInterpreter();
		configInterpreter.clearGlobals();
		configInterpreter.process(is, "ConfigStript");
		
		var root = new SwconfTable();
		
		readLuaTo(root, configInterpreter.globalsTable);
		
		return root;
	}
	
	public void readLuaTo(SwconfTable root, LuaTable tbl)
	{
		LuaValue[] keys = tbl.keys();
		
		boolean isArray = isArray(tbl);
		
		if (isArray)
		{
			root.hasKeys = false;
		}
		
		for (LuaValue key : keys)
		{
			LuaValue luaValue = tbl.get(key);
			
			if (luaValue != null)
			{
				var name = isArray ? String.valueOf(key.checkint()) : key.checkstring().toString();
				SwconfObject primitive = readPrim(luaValue);
				
				primitive.setName(name);
				
				if (isArray)
					root.put(primitive);
				else
					root.put(name, primitive);
			}
		}
	}
	
	public SwconfObject readPrim(LuaValue luaValue)
	{
		SwconfObject primitive = null;
		
		if (luaValue.istable())
		{
			LuaTable tbl = (LuaTable) luaValue;
			
			SwconfTable obj = new SwconfTable();
			primitive = obj;
			readLuaTo(obj, tbl);
		}
		else if (luaValue.isboolean())
		{
			SwconfBoolean obj = new SwconfBoolean();
			obj.setValue(luaValue.checkboolean());
			primitive = obj;
		}
		else if (luaValue instanceof LuaString)
		{
			SwconfString obj = new SwconfString();
			obj.setValue(luaValue.checkstring().toString());
			primitive = obj;
		}
		else if (luaValue.isnumber())
		{
			SwconfNum obj = new SwconfNum();
			obj.setValue(luaValue.checkdouble());
			primitive = obj;
		}
		
		return primitive;
	}
	
	public boolean isArray(LuaTable table)
	{
		LuaValue[] keys = table.keys();
		
		for (LuaValue key : keys)
		{
			if (!key.isint())
				return false;
		}
		
		return true;
	}
	
	public LuaInterpreter getConfigInterpreter()
	{
		return new LuaInterpreter();
	}
	
}
