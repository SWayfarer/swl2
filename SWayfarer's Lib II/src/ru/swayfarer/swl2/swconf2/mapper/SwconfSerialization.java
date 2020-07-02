package ru.swayfarer.swl2.swconf2.mapper;

import static ru.swayfarer.swl2.collections.CollectionsSWL.createExtendedList;

import java.io.File;

import lombok.var;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.swconf2.mapper.standart.ContstructorMapper;
import ru.swayfarer.swl2.swconf2.mapper.standart.EnumMapper;
import ru.swayfarer.swl2.swconf2.mapper.standart.ListMapper;
import ru.swayfarer.swl2.swconf2.mapper.standart.MapMapper;
import ru.swayfarer.swl2.swconf2.mapper.standart.NumberMapper;
import ru.swayfarer.swl2.swconf2.mapper.standart.ObservablePropertyMapper;
import ru.swayfarer.swl2.swconf2.mapper.standart.ReflectionMapper;
import ru.swayfarer.swl2.swconf2.types.SwconfBoolean;
import ru.swayfarer.swl2.swconf2.types.SwconfObject;
import ru.swayfarer.swl2.swconf2.types.SwconfString;

@SuppressWarnings({ "rawtypes", "unchecked"} )
public class SwconfSerialization {

	public static ILogger logger = LoggingManager.getLogger();
	
	public IExtendedList<ISwconfMapper> registeredSwconfMappers = createExtendedList();
	
	public ISwconfMapper findMapperFor(MappingInfo mappingInfo)
	{
		return registeredSwconfMappers.dataStream()
				.first((e) -> e.isAccepts(mappingInfo));
	}
	
	public <T> T deserialize(Class<T> classOfObject, SwconfObject swconfObj)
	{
		var mappingInfo = MappingInfo.builder()
				.objType(classOfObject)
				.serialization(this)
		.build();
		
		return deserialize(mappingInfo, swconfObj);
	}
	
	public <T> T deserialize(T obj, SwconfObject swconfObj)
	{
		var mappingInfo = MappingInfo.builder()
				.obj(obj)
				.objType(obj.getClass())
				.serialization(this)
		.build();
		
		return deserialize(mappingInfo, swconfObj);
	}
	
	public <T extends SwconfObject> T serialize(Object obj)
	{
		var mappingInfo = MappingInfo.builder()
				.obj(obj)
				.objType(obj.getClass())
				.serialization(this)
		.build();
		
		return (T) serialize(mappingInfo);
	}
	
	public SwconfObject serialize(MappingInfo mappingInfo)
	{
		ISwconfMapper mapper = findMapperFor(mappingInfo);
		ExceptionsUtils.IfNull(mapper, SwconfMappingException.class, "No mapper found for mapping info", mappingInfo);
		return mapper.write(mappingInfo);
	}
	
	public <T> T deserialize(MappingInfo mappingInfo, SwconfObject swconfObj)
	{
		ISwconfMapper mapper = findMapperFor(mappingInfo);
		
		ExceptionsUtils.IfNull(mapper, SwconfMappingException.class, "No mapper found for mapping info", mappingInfo);
		
		mapper.read(swconfObj, mappingInfo);
		
		return (T) mappingInfo.getObj();
	}
	
	public <T extends SwconfSerialization> T registerConstructorMapper(Class<?> classOfObj, Class<? extends SwconfObject> classOfSwconf, Class... acceptableTypes)
	{
		return registerMapper(ContstructorMapper.builder()
				.classOfObj(classOfObj)
				.classOfSwconf(classOfSwconf)
				.acceptableClasses(createExtendedList(
						acceptableTypes
				)
			)
			.build()
		);
	}
	
	public <T extends SwconfSerialization> T registerMapper(ISwconfMapper<?, ?> mapper)
	{
		registeredSwconfMappers.add(mapper);
		return (T) this;
	}
	
	public static SwconfSerialization defaultMappers()
	{
		var ret = new SwconfSerialization();
		
		ret

			.registerMapper(new ObservablePropertyMapper())
			
			.registerConstructorMapper(FileSWL.class, SwconfString.class, File.class)
			
			.registerMapper(new NumberMapper("byte", byte.class, Byte.class))
			.registerMapper(new NumberMapper("short", short.class, Short.class))
			.registerMapper(new NumberMapper("int", int.class, Integer.class))
			.registerMapper(new NumberMapper("long", long.class, Long.class))
			
			.registerMapper(new NumberMapper("float", float.class, Float.class))
			.registerMapper(new NumberMapper("double", double.class, Double.class))
			
			.registerConstructorMapper(Boolean.class, SwconfBoolean.class, boolean.class, Boolean.class)
			
			.registerConstructorMapper(String.class, SwconfString.class, String.class)
			
			
			.registerMapper(new ListMapper().registerDefaultCreationFuns())
			.registerMapper(new MapMapper().registerDefaultCreationFuns())
			
			.registerMapper(new EnumMapper())

			.registerMapper(new ReflectionMapper().registerDeaultEvents())
		;
		
		return ret;
	}
}
