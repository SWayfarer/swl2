package ru.swayfarer.swl2.swconf2.mapper.standart;

import java.lang.reflect.Method;

import lombok.var;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.swconf2.mapper.ISwconfMapper;
import ru.swayfarer.swl2.swconf2.mapper.MappingInfo;
import ru.swayfarer.swl2.swconf2.types.SwconfNum;

public class NumberMapper implements ISwconfMapper<SwconfNum, Object> {

	public static ILogger logger = LoggingManager.getLogger();
	
	public Method valueMethod;
	
	public IExtendedList<Class<?>> acceptableTypes = CollectionsSWL.createExtendedList();
	
	public NumberMapper(String valueMethodName, Class<?>... acceptableClasses)
	{
		acceptableTypes = CollectionsSWL.createExtendedList(acceptableClasses);
		valueMethod = ReflectionUtils.findMethod(Double.class, valueMethodName + "Value");
	}
	
	@Override
	public boolean isAccepts(MappingInfo mappingInfo)
	{
		return acceptableTypes.contains(mappingInfo.getObjType());
	}

	@Override
	public void read(SwconfNum swconf, MappingInfo mappingInfo)
	{
		var value = swconf.getValue();
		
		logger.safe(() -> {
			
			var obj = valueMethod.invoke(value);
			
			mappingInfo.setObj(obj);
			
		}, "Error while creating number value");
	}

	@Override
	public SwconfNum write(MappingInfo mappingInfo)
	{
		return new SwconfNum(Double.valueOf(mappingInfo.getObj().toString()));
	}

}
