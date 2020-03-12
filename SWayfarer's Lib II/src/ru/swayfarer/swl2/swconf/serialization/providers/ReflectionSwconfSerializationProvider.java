package ru.swayfarer.swl2.swconf.serialization.providers;

import java.lang.reflect.Field;
import java.util.Map;

import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.swconf.primitives.SwconfObject;
import ru.swayfarer.swl2.swconf.primitives.SwconfPrimitive;
import ru.swayfarer.swl2.swconf.serialization.ISwconfSerializationProvider;
import ru.swayfarer.swl2.swconf.serialization.SwconfSerialization;

public class ReflectionSwconfSerializationProvider implements ISwconfSerializationProvider<SwconfObject, Object> {

	public static ILogger logger = LoggingManager.getLogger();
	
	public SwconfSerialization serialization;
	
	public Map<Class<?>, Map<String, Field>> cachedFields = CollectionsSWL.createIdentityMap();
	
	public Map<String, Field> getAccessibleFields(Class<?> cl)
	{
		 Map<String, Field> ret = cachedFields.get(cl);
		 
		 if (ret == null)
		 {
			 ret = ReflectionUtils.getAccessibleFields(cl)
					 .dataStream()
					 .toMap((e) -> e.getName(), (e) -> e);
			 
			 cachedFields.put(cl, ret);
		 }
		 
		 return ret;
	}
	
	@Override
	public boolean isAccetps(Class<?> type)
	{
		return type != Object.class;
	}

	@Override
	public Object deserialize(Class<?> cl, Object obj, SwconfObject swconfObject)
	{
		Map<String, Field> accessibleFields = getAccessibleFields(obj.getClass());
		
		IExtendedList<SwconfObject> roots = CollectionsSWL.createExtendedList();
		roots.add(swconfObject);
		
		for (Field field : accessibleFields.values())
		{
			SwconfPrimitive primitive = swconfObject.getChild(field.getName());
			
			if (primitive != null)
			{
				Object value = serialization.deserialize(field.getType(), null, primitive, null);
				
				logger.safe(() -> field.set(obj, value), "Error while setting field " + field);
			}
		}
		
		return obj;
	}

	@Override
	public Object createNewInstance(Class<?> classOfObject, SwconfObject swconfObject)
	{
		return ReflectionUtils.newInstanceOf(classOfObject);
	}

	@Override
	public SwconfObject serialize(Object obj)
	{
		SwconfObject object = new SwconfObject();
		
		Map<String, Field> fields = getAccessibleFields(obj.getClass());
		
		for (Field field : fields.values())
		{
			logger.safe(() -> {
				SwconfPrimitive primitive = serialization.serialize(field.getType(), field.get(obj), null);
				primitive.setName(field.getName());
				object.addChild(primitive);
			}, "Error while serializing field", field, "of object", obj);
		}
		
		return object;
	}

}
