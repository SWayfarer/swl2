package ru.swayfarer.swl2.swconf2.mapper.standart;

import lombok.var;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.observable.Observables;
import ru.swayfarer.swl2.observable.property.ObservableProperty;
import ru.swayfarer.swl2.swconf2.mapper.ISwconfMapper;
import ru.swayfarer.swl2.swconf2.mapper.MappingInfo;
import ru.swayfarer.swl2.swconf2.mapper.SwconfMappingException;
import ru.swayfarer.swl2.swconf2.types.SwconfObject;

public class ObservablePropertyMapper implements ISwconfMapper<SwconfObject, ObservableProperty<?>> {

	@Override
	public boolean isAccepts(MappingInfo mappingInfo)
	{
		return ObservableProperty.class.isAssignableFrom(mappingInfo.getObjType());
	}

	@Override
	public void read(SwconfObject swconf, MappingInfo mappingInfo)
	{
		var field = mappingInfo.getObjField();
		var generics = mappingInfo.getGenerics();
		
		ExceptionsUtils.If(CollectionsSWL.isNullOrEmpty(generics) || generics.size() != 1, SwconfMappingException.class, "Invalid generics for field", field, "! It must be single generic, as <String> or <List<Double>>!");
		
		var typeGeneric = generics.getFirstElement();
		var classOfObject = typeGeneric.loadClassSafe();
		
		ExceptionsUtils.IfNull(classOfObject, SwconfMappingException.class, "Can't load class", typeGeneric.getTypeCanonicalName(), "!");
		
		var valueMapping = mappingInfo.copy()
				.objType(classOfObject)
				.generics(typeGeneric.getChilds())
		.build();
		
		mappingInfo.setObj(Observables.createProperty(mappingInfo.getSerialization().deserialize(valueMapping, swconf)));
	}

	@Override
	public SwconfObject write(MappingInfo mappingInfo)
	{
		var property = (ObservableProperty<?>) mappingInfo.getObj();
		var value = property.get();
		
		if (value == null)
			return null;
		
		var generics = mappingInfo.getGenerics();
		var typeGeneric = generics.getFirstElement();
		
		var valueMappingBuilder = mappingInfo.copy()
				.obj(value)
				.objType(value.getClass())
		;
		
		if (typeGeneric != null)
		{
			valueMappingBuilder.generics(typeGeneric.getChilds());
		}
		
		var valueMapping = valueMappingBuilder.build();
		
		return mappingInfo.getSerialization().serialize(valueMapping);
	}

}
