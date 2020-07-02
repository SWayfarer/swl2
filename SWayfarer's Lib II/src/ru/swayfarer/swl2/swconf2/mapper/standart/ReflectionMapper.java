package ru.swayfarer.swl2.swconf2.mapper.standart;

import java.util.Map;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.var;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.Observables;
import ru.swayfarer.swl2.observable.events.AbstractCancelableEvent;
import ru.swayfarer.swl2.swconf2.mapper.ISwconfMapper;
import ru.swayfarer.swl2.swconf2.mapper.MappingInfo;
import ru.swayfarer.swl2.swconf2.mapper.SwconfMappingException;
import ru.swayfarer.swl2.swconf2.mapper.annotations.CommentedSwconf;
import ru.swayfarer.swl2.swconf2.mapper.annotations.IgnoreSwconf;
import ru.swayfarer.swl2.swconf2.types.SwconfObject;
import ru.swayfarer.swl2.swconf2.types.SwconfTable;

@SuppressWarnings("unchecked")
public class ReflectionMapper implements ISwconfMapper<SwconfObject, Object> {

	public static ILogger logger = LoggingManager.getLogger();
	
	public IObservable<MappingEvent> eventMapping = Observables.createObservable();
	
	@Override
	public boolean isAccepts(MappingInfo mappingInfo)
	{
		return mappingInfo.getObjType() instanceof Object;
	}

	@Override
	public void read(SwconfObject swconf, MappingInfo mappingInfo)
	{
		ExceptionsUtils.IfNot(swconf.isTable(), SwconfMappingException.class, "Swconf", swconf.toString(0), " must be a table!");
		SwconfTable table = swconf.asTable();
		ExceptionsUtils.IfNot(table.hasKeys, SwconfMappingException.class, "Swconf table", swconf.toString(0), " must has a keys!");
		
		Map<String, SwconfObject> elements = table.entries.dataStream()
				.toMap((e) -> e.key, (e) -> e.value);
		
		Object obj = mappingInfo.getObj();
		
		Class<?> classOfObj = mappingInfo.getObjType();
		
		if (obj == null)
		{
			obj = ReflectionUtils.newInstanceOf(mappingInfo.objType);
			
			ExceptionsUtils.IfNull(obj, SwconfMappingException.class, "Can't create a new instance of class", classOfObj, " maybe default(no args) constructor does not exist?");
		}
		
		final Object instace = obj;
		
		ReflectionUtils.fields(classOfObj)
		.each((field) -> {
			
			logger.safe(() -> {
				
				SwconfObject swconfValue = elements.get(field.getName());
				
				if (swconfValue != null)
				{
					var mapping = MappingInfo.ofField(field, classOfObj, instace)
							.serialization(mappingInfo.getSerialization())
							.obj(null)
					.build();
					
					var mappingEvent = MappingEvent.builder()
							.mappingInfo(mapping)
					.build();
					
					CommentedSwconf commentAnnotation = field.getAnnotation(CommentedSwconf.class);
					
					if (commentAnnotation != null)
					{
						swconfValue.setComment(commentAnnotation.value());
					}
					
					eventMapping.next(mappingEvent);
					
					if (!mappingEvent.isCanceled())
					{
						Object deserialize = mappingInfo.getSerialization().deserialize(mapping, swconfValue);
						field.set(instace, deserialize);
					}
				}
			
			}, "Error while setting field", field, "value");
		});
		
		mappingInfo.setObj(obj);
	}

	@Override
	public SwconfTable write(MappingInfo mappingInfo)
	{
		Object obj = mappingInfo.getObj();
		
		if (obj == null)
			return null;
		
		SwconfTable table = new SwconfTable();
		
		Class<?> classOfObj = obj.getClass();
		
		ReflectionUtils.fields(classOfObj)
		.each((field) -> {
			
			logger.safe(() -> {
				
				var fieldValue = field.get(obj);
				
				if (fieldValue != null)
				{
					var fieldValueMapping = MappingInfo.ofField(field, classOfObj, obj)
							.serialization(mappingInfo.getSerialization())
					.build();
					
					var mappingEvent = MappingEvent.builder()
							.mappingInfo(fieldValueMapping)
					.build();
					
					eventMapping.next(mappingEvent);
					
					if (!mappingEvent.isCanceled())
					{
						SwconfObject swconfValue = mappingInfo.getSerialization().serialize(fieldValueMapping);
						
						CommentedSwconf commentAnnotation = field.getAnnotation(CommentedSwconf.class);
						
						if (commentAnnotation != null)
						{
							swconfValue.setComment(commentAnnotation.value());
						}
						
						table.put(field.getName(), swconfValue);
					}
				}
				
			}, "Error while mapping field", field, "value");
		});
		
		return table;
	}
	
	public <T extends ReflectionMapper> T registerDeaultEvents()
	{
		eventMapping.subscribe((e) -> {
			var field = e.getMappingInfo().getObjField();
			
			if (ReflectionUtils.hasAnnotation(field, IgnoreSwconf.class))
			{
				e.setCanceled(true);
			}
		});
		
		return (T) this;
	}
	
	@EqualsAndHashCode(callSuper = false)
	@Data
	@Builder
	public static class MappingEvent extends AbstractCancelableEvent {
		public MappingInfo mappingInfo;
	}

}
