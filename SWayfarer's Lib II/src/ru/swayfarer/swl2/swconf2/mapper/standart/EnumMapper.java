package ru.swayfarer.swl2.swconf2.mapper.standart;

import lombok.var;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.swconf2.mapper.ISwconfMapper;
import ru.swayfarer.swl2.swconf2.mapper.MappingInfo;
import ru.swayfarer.swl2.swconf2.types.SwconfString;

public class EnumMapper implements ISwconfMapper<SwconfString, Object> {

	@Override
	public boolean isAccepts(MappingInfo mappingInfo)
	{
		Class<?> objType = mappingInfo.getObjType();
		return objType.isEnum();
	}

	@Override
	public void read(SwconfString swconf, MappingInfo mappingInfo)
	{
		var obj = mappingInfo.getObj();
		
		if (obj != null)
			return;

		var classOfEnum = mappingInfo.getObjType();
		
		obj = ReflectionUtils.invokeMethod("valueOf", classOfEnum, swconf.getValue()).getReturnValue();
		
		mappingInfo.setObj(obj);
	}

	@Override
	public SwconfString write(MappingInfo mappingInfo)
	{
		var obj = mappingInfo.getObj();
		
		if (obj == null)
			return null;
		
		return new SwconfString(obj + "");
	}

}
