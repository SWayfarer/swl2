package ru.swayfarer.swl2.swconf2.mapper.standart;

import lombok.Builder;
import lombok.NonNull;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.swconf2.mapper.ISwconfMapper;
import ru.swayfarer.swl2.swconf2.mapper.MappingInfo;
import ru.swayfarer.swl2.swconf2.mapper.SwconfMappingException;
import ru.swayfarer.swl2.swconf2.types.SwconfObject;

@Builder
public class ContstructorMapper <Result_Type> implements ISwconfMapper<SwconfObject, Result_Type> {

	@Builder.Default
	public IExtendedList<Class<?>> acceptableClasses = CollectionsSWL.createExtendedList();
	
	@NonNull
	public Class<?> classOfObj;
	
	@NonNull
	public Class<? extends SwconfObject> classOfSwconf;

	@Override
	public boolean isAccepts(MappingInfo mappingInfo)
	{
		return acceptableClasses.stream()
				.anyMatch((type) -> type.isAssignableFrom(mappingInfo.getObjType()));
	}

	@Override
	public void read(SwconfObject swconf, MappingInfo mappingInfo)
	{
		ExceptionsUtils.IfNot(swconf.hasValue(), SwconfMappingException.class, "Can't read value from", swconf);
		mappingInfo.setObj(ReflectionUtils.newInstanceOf(classOfObj, swconf.getValue()));
	}

	@Override
	public SwconfObject write(MappingInfo mappingInfo)
	{
		SwconfObject swconf = ReflectionUtils.newInstanceOf(classOfSwconf);
		ExceptionsUtils.IfNot(swconf.hasValue(), SwconfMappingException.class, "Can't write value to", swconf);
		swconf.setRawValue(mappingInfo.getObj());
		return swconf;
	}
}
