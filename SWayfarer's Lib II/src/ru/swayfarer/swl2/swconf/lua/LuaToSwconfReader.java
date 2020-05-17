package ru.swayfarer.swl2.swconf.lua;

import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.lua.LuaInterpreter;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.swconf.primitives.SwconfArray;
import ru.swayfarer.swl2.swconf.primitives.SwconfBoolean;
import ru.swayfarer.swl2.swconf.primitives.SwconfNum;
import ru.swayfarer.swl2.swconf.primitives.SwconfObject;
import ru.swayfarer.swl2.swconf.primitives.SwconfPrimitive;
import ru.swayfarer.swl2.swconf.primitives.SwconfString;
import ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.LuaString;
import ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.LuaTable;
import ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.LuaValue;

public class LuaToSwconfReader implements IFunction1<String, SwconfObject> {
	
	public SwconfObject readSwconf(String str)
	{
		LuaInterpreter configInterpreter = getConfigInterpreter();
		configInterpreter.clearGlobals();
		configInterpreter.process(StringUtils.getBytesStream("UTF-8", str), "ConfigStript");
		
		SwconfObject root = new SwconfObject();
		
		readLuaTo(root, configInterpreter.globalsTable);
		
//		LoggingManager.getLogger().info(root.toString(0));
		
		return root;
	}
	
	public void readLuaTo(SwconfArray root, LuaTable tbl)
	{
		LuaValue[] keys = tbl.keys();
		
		for (LuaValue key : keys)
		{
			LuaValue luaValue = tbl.get(key);
			
			if (luaValue != null)
			{
				SwconfPrimitive primitive = readPrim(luaValue);
				primitive.setName(key.checkstring());
				root.addChild(primitive);
			}
		}
	}
	
	public void readLuaTo(SwconfObject root, LuaTable tbl)
	{
		LuaValue[] keys = tbl.keys();
		
		for (LuaValue key : keys)
		{
			LuaValue luaValue = tbl.get(key);
			
			if (luaValue != null)
			{
				SwconfPrimitive primitive = readPrim(luaValue);
				primitive.setName(key.checkstring());
				root.addChild(primitive);
			}
		}
	}
	
	public SwconfPrimitive readPrim(LuaValue luaValue)
	{
		SwconfPrimitive primitive = null;
		
		if (luaValue.istable())
		{
			LuaTable tbl = (LuaTable) luaValue;
			
			if (isArray(tbl))
			{
				SwconfArray obj = new SwconfArray();
				primitive = obj;
				readLuaTo(obj, tbl);
			}
			else
			{
				SwconfObject obj = new SwconfObject();
				primitive = obj;
				readLuaTo(obj, tbl);
			}
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
			obj.setValue(luaValue.checkstring());
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

	@Override
	public SwconfObject apply(String swconfString)
	{
		return readSwconf(swconfString);
	}
}
