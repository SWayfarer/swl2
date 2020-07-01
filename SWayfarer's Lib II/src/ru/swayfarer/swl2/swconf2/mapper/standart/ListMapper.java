package ru.swayfarer.swl2.swconf2.mapper.standart;

import java.util.ArrayList;
import java.util.List;

import lombok.var;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.collections.observable.IObservableList;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.swconf2.mapper.ISwconfMapper;
import ru.swayfarer.swl2.swconf2.mapper.MappingInfo;
import ru.swayfarer.swl2.swconf2.mapper.SwconfMappingException;
import ru.swayfarer.swl2.swconf2.types.SwconfObject;
import ru.swayfarer.swl2.swconf2.types.SwconfTable;

@SuppressWarnings( {"rawtypes", "unchecked"} )
public class ListMapper implements ISwconfMapper<SwconfTable, List>{

	public static ILogger logger = LoggingManager.getLogger();
	
	public IExtendedList<IFunction1<MappingInfo, List<Object>>> registeredFactories = CollectionsSWL.createExtendedList();
	
	@Override
	public boolean isAccepts(MappingInfo mappingInfo)
	{
		return List.class.isAssignableFrom(mappingInfo.getObjType());
	}
	
	public <T extends ListMapper> T registerCreationFun(IFunction1<MappingInfo, List<?>> fun)
	{
		this.registeredFactories.addExclusive(ReflectionUtils.forceCast(fun));
		return (T) this;
	}

	public List createList(MappingInfo mappingInfo)
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
	public void read(SwconfTable swconf, MappingInfo mappingInfo)
	{
		List list = (List) mappingInfo.getObj(); 
		
		if (list == null)
			list = createList(mappingInfo);
		
		var objField = mappingInfo.getObjField();
		
		ExceptionsUtils.If(mappingInfo.hasNoGenerics(), SwconfMappingException.class, "No generics found in field", objField, "! Select list element type in <> for map it!");
		
		var generics = mappingInfo.generics;
		
		ExceptionsUtils.If(generics.size() > 1, SwconfMappingException.class, "To many generics in field", objField, "! Field must has a single generic with list element type!");
		
		for (var entry : swconf.entries)
		{
			SwconfObject value = entry.value;
			
			if (value != null)
			{
				var typeGeneric = generics.getFirstElement();
				
				var entryMappingInfo = MappingInfo.builder()
						.objType(typeGeneric.loadClassSafe())
						.generics(typeGeneric.getChilds())
						.serialization(mappingInfo.getSerialization())
				.build();
				
				mappingInfo.getSerialization().deserialize(entryMappingInfo, value);
				
				list.add(entryMappingInfo.getObj());
			}
		}
		
		mappingInfo.setObj(list);
	}

	@Override
	public SwconfTable write(MappingInfo mappingInfo)
	{
		var list = (List) mappingInfo.getObj();
		
		if (list == null)
			return null;
		
		SwconfTable ret = new SwconfTable();
		ret.hasKeys = false;
		
		for (var element : list)
		{
			var elementMappingInfo = MappingInfo.builder()
					.obj(element)
					.objType(element.getClass())
					.serialization(mappingInfo.getSerialization())
			.build();
			
			var elementSwconf = mappingInfo.getSerialization().serialize(elementMappingInfo);
			
			if (elementSwconf != null)
			{
				ret.put(elementSwconf);
			}
		}
		
		return ret;
	}

	public <T extends ListMapper> T registerDefaultCreationFuns()
	{

		registerCreationFun((info) -> {
			
			var type = info.getObjType();
			
			if (IObservableList.class.isAssignableFrom(type))
				return CollectionsSWL.createObservableList();
			
			return null;
		});
		
		registerCreationFun((info) -> {
			
			var type = info.getObjType();
			
			if (IExtendedList.class.isAssignableFrom(type))
				return CollectionsSWL.createExtendedList();
			
			return null;
		});
		
		registerCreationFun((info) -> {
			
			var type = info.getObjType();
			
			if (List.class.isAssignableFrom(type))
				return new ArrayList<>();
			
			return null;
		});
		
		
		
		return (T) this;
	}
}
