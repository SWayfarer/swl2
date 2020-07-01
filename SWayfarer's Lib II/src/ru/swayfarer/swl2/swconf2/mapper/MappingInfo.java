package ru.swayfarer.swl2.swconf2.mapper;

import java.lang.reflect.Field;

import lombok.Builder;
import lombok.Data;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.generics.GenericObject;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.swconf2.mapper.annotations.IgnoreSwconf;

@Builder
@Data
public class MappingInfo {

	@IgnoreSwconf
	public static ILogger logger = LoggingManager.getLogger();
	
	public SwconfSerialization serialization;
	public Object obj;
	public Object ownerInstance;
	public Class<?> objType;
	public Class<?> ownerClass;
	public Field objField;
	public IExtendedList<GenericObject> generics;
	
	public boolean hasNoGenerics()
	{
		return CollectionsSWL.isNullOrEmpty(generics);
	}
	
	public MappingInfoBuilder copy()
	{
		return MappingInfo.builder()
				.obj(this.getObj())
				.objField(this.getObjField())
				.objType(this.getObjType())
				.generics(this.getGenerics())
				.ownerClass(this.getOwnerClass())
				.ownerInstance(this.getOwnerInstance())
				.serialization(this.getSerialization())
		;
	}
	
	public static MappingInfoBuilder ofField(Field field, Class<?> ownerClass, Object ownerInstance)
	{
		return logger.safeReturn(() -> {
			return builder()
					.generics(GenericObject.ofField(field))
					.objField(field)
					.obj(field.get(ownerInstance))
					.objType(field.getType())
					.ownerClass(ownerClass)
					.ownerInstance(ownerInstance)
			;
		}, null, "Error while creating mapping info builder");
	}
}
