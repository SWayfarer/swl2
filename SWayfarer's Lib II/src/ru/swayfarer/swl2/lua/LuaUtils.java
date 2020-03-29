package ru.swayfarer.swl2.lua;

import ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.LuaTable;
import ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.LuaValue;
import ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.lib.jse.JavaArray;
import ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.lib.jse.JavaClass;
import ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.lib.jse.JavaInstance;

public class LuaUtils {
	
	public static LuaTable getOrCreateTable(String name, LuaValue root)
	{
		LuaValue value = root.get(name);
		if (value == null || !value.istable())
		{
			LuaTable ret = new LuaTable();
			root.set(name, ret);
			return ret;
		}
		
		return (LuaTable) root.get(name);
	}
	
	public static LuaValue toLuaValue(Object obj)
	{
		if (obj == null)
			return LuaValue.NIL;
		
		Class<?> cl = obj.getClass();
		
		if (LuaValue.class.isAssignableFrom(cl))
			return (LuaValue)obj;
		else if (cl == void.class)
			return LuaValue.NIL;
		else if (cl == int.class || cl == Integer.class)
			return LuaValue.valueOf((int)obj);
		else if (cl == double.class || cl == Double.class)
			return LuaValue.valueOf((double)obj);
		else if (cl == boolean.class || cl == Boolean.class)
			return LuaValue.valueOf((boolean)obj);
		else if (cl == float.class || cl == Float.class)
			return LuaValue.valueOf((float)obj);
		else if (cl == String.class)
			return LuaValue.valueOf((String)obj);
		else if (cl == Class.class)
			return JavaClass.forClass((Class<?>) obj);
		else if (cl.isArray())
			return new JavaArray(obj);
		
		JavaInstance instance = new JavaInstance(obj);
		return instance;
	}
	
	
	public static Object toJavaValue(LuaValue luaValue, Class<?> classOfObject)
	{
		if (classOfObject == int.class || classOfObject == Integer.class)
		{
			return (luaValue.checkint());
		}
		else if (classOfObject == float.class || classOfObject == Float.class)
		{
			return ((float) luaValue.checkdouble());
		}
		else if (classOfObject == double.class || classOfObject == Double.class)
		{
			return (luaValue.checkdouble());
		}
		else if (classOfObject == Number.class)
		{
			return luaValue.checkdouble();
		}
		else if (classOfObject == boolean.class || classOfObject == Boolean.class)
		{
			return (luaValue.checkdouble());
		}
		else if (classOfObject == String.class)
		{
			return (luaValue.checkjstring());
		}
		else if (classOfObject.isArray())
		{
			JavaArray arr = (JavaArray) luaValue;
			return (arr.m_instance);
		}
		else if (LuaValue.class.isAssignableFrom(classOfObject))
		{
			return  (luaValue);
		}
		else if (luaValue.isnil())
			return null;
		else 
		{
			if (classOfObject == Object.class)
			{
				if (luaValue.isstring())
					return luaValue.checkstring();
				else if (luaValue.isnumber())
					return luaValue.checkdouble();
			}
			
			JavaInstance instance = (JavaInstance) luaValue;
			return (instance.m_instance);
		}
	}
}
