package ru.swayfarer.swl2.swconf2.mapper.standart;

import java.util.HashMap;
import java.util.Map;

import lombok.var;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.collections.extended.IExtendedMap;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.swconf2.mapper.ISwconfMapper;
import ru.swayfarer.swl2.swconf2.mapper.MappingInfo;
import ru.swayfarer.swl2.swconf2.mapper.StringParsers;
import ru.swayfarer.swl2.swconf2.mapper.SwconfMappingException;
import ru.swayfarer.swl2.swconf2.types.SwconfTable;

@SuppressWarnings( {"rawtypes", "unchecked"} )
public class MapMapper implements ISwconfMapper<SwconfTable, Map> {

	public StringParsers stringParsers = new StringParsers().registerDefaultProviders();
	
	public IExtendedList<IFunction1<MappingInfo, Map<?,?>>> registeredFactories = CollectionsSWL.createExtendedList();
	
	public <T extends MapMapper> T registerCreationFun(IFunction1<MappingInfo, Map> fun)
	{
		this.registeredFactories.addExclusive(ReflectionUtils.forceCast(fun));
		return (T) this;
	}
	
	public Map createMap(MappingInfo mappingInfo)
	{
		for (var factoryFun : registeredFactories)
		{
			if (factoryFun != null)
			{
				var ret = factoryFun.apply(mappingInfo);
				
				if (ret != null)
				{
					return ret;
				}
			}
		}
		
		return null;
	}
	
	@Override
	public boolean isAccepts(MappingInfo mappingInfo)
	{
		return Map.class.isAssignableFrom(mappingInfo.getObjType());
	}

	@Override
	public void read(SwconfTable swconf, MappingInfo mappingInfo)
	{
		var generics = mappingInfo.getGenerics();
		
		ExceptionsUtils.If(CollectionsSWL.isNullOrEmpty(generics) || generics.size() != 2, SwconfMappingException.class, "Invalid generic type of field", mappingInfo.getObjField(), "! It must be a 2 generics(as <A, B>)!");
		ExceptionsUtils.If(!swconf.hasKeys, SwconfMappingException.class, "Can't read map from non-keyed table", swconf.toString(0));

		var keyGeneric = generics.get(0);
		var typeGeneric = generics.get(1);
			
		Class<?> classOfKey = keyGeneric.loadClassSafe();
		
		ExceptionsUtils.IfNull(classOfKey, SwconfMappingException.class, "Can't get key class for field", mappingInfo.getObjField());
		
		Map map = (Map) mappingInfo.getObj();
		
		if (map == null)
		{
			map = createMap(mappingInfo);
		}
		
		if (typeGeneric != null)
		{
			for (var entry : swconf.entries)
			{
				var key = entry.key;
				var value = entry.value;
				var childGenerics = typeGeneric.getChilds();
				
				if (!StringUtils.isBlank(key) && value != null)
				{
					var entryMappingInfo = MappingInfo.builder()
							.objType(typeGeneric.loadClassSafe())
							.generics(childGenerics)
							.serialization(mappingInfo.getSerialization())
					.build();
					
					var readedValue = mappingInfo.getSerialization().deserialize(entryMappingInfo, value);
					
					Object keyValue = key;
					
					if (classOfKey != String.class)
					{
						if (stringParsers.isSomeAccepting(key, classOfKey))
						{
							keyValue = stringParsers.parse(key, classOfKey);
						}
						else
						{
							throw new SwconfMappingException("Can't parse key '" + key + "' to type " + classOfKey.getName() + "!. Please, register specified mapper in StringParsers class or use another key type!");
						}
					}
					
					map.put(keyValue, readedValue);
				}
			}
		}
		
		mappingInfo.setObj(map);
	}

	@Override
	public SwconfTable write(MappingInfo mappingInfo)
	{
		Map<?, ?> map = (Map) mappingInfo.getObj();
		
		if (map == null)
			return null;
		
		SwconfTable swconf = new SwconfTable();

		for (var entry : map.entrySet())
		{
			var key = entry.getKey().toString();
			var value = entry.getValue();
			
			var valueMappingInfo = MappingInfo.builder()
					.obj(value)
					.objType(value.getClass())
					.serialization(mappingInfo.getSerialization())
			.build();
			
			var swconfValue = mappingInfo.getSerialization().serialize(valueMappingInfo);
			
			if (swconfValue != null)
			{
				swconf.put(key, swconfValue);
			}
		}
		
		return swconf;
	}

	public <T extends MapMapper> T registerDefaultCreationFuns()
	{
		registerCreationFun((mappingInfo) -> {
			var type = mappingInfo.getObjType();
			
			if (IExtendedMap.class.isAssignableFrom(type))
				return CollectionsSWL.createExtendedMap();
			
			return null;
		});
		
		registerCreationFun((mappingInfo) -> {
			var type = mappingInfo.getObjType();
			
			if (Map.class.isAssignableFrom(type))
				return new HashMap<>();
			
			return null;
		});
		return (T) this;
	}
}
