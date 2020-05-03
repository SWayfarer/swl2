package ru.swayfarer.swl2.swconf.serialization.providers;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.collections.extended.IExtendedMap;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.swconf.primitives.SwconfObject;
import ru.swayfarer.swl2.swconf.primitives.SwconfPrimitive;
import ru.swayfarer.swl2.swconf.serialization.ISwconfSerializationProvider;
import ru.swayfarer.swl2.swconf.serialization.SwconfSerialization;

@SuppressWarnings({"rawtypes", "unchecked"})
public class MapSwconfSerializationProvider implements ISwconfSerializationProvider<SwconfObject, Map> {

	public SwconfSerialization serialization;
	
	public static ILogger logger = LoggingManager.getLogger();
	
	public static IExtendedList<IFunction1<Class<?>, Map>> mapCreatorsFuns = CollectionsSWL.createExtendedList();
	
	public MapSwconfSerializationProvider(SwconfSerialization serialization)
	{
		super();
		this.serialization = serialization;
		registerDefaultMapGenerators();
	}

	public void registerDefaultMapGenerators()
	{
		registerDefaultMapGenerator((cl) -> {
			if (cl.equals(Map.class))
				return new HashMap<>();
			
			return null;
		});
		
		registerDefaultMapGenerator((cl) -> {
			if (cl.equals(IExtendedMap.class))
				return CollectionsSWL.createExtendedMap();
			
			return null;
		});
	}
	
	public <T extends MapSwconfSerializationProvider> T registerDefaultMapGenerator(IFunction1<Class<?>, Map> fun) 
	{
		mapCreatorsFuns.addExclusive(fun);
		return (T) this;
	}
	
	@Override
	public boolean isAccetps(Class<?> type)
	{
		return Map.class.isAssignableFrom(type);
	}

	@Override
	public Map deserialize(Class<?> cl, Map obj, SwconfObject swconfObject)
	{
		ExceptionsUtils.unsupportedOperation("Deserialization withot field is not supported!");
		return null;
	}
	
	@Override
	public Map deserialize(Field field, Class<?> cl, Map obj, SwconfObject swconfObject)
	{
		if (obj == null)
			obj = createNewInstance(cl, swconfObject);
		
		Type type = field.getGenericType();
		
		if (type == null)
		{
			logger.warning("Generic type of deserialing list &{rd}must be one and presented!");
			return null;
		}
		
		String genericType = type.getTypeName();
		
		int start = genericType.indexOf("<");
		int end = genericType.indexOf(">");
		
		if (start < 0 || end < 0)
		{
			logger.warning("Can't get generic type from", genericType);
			return null;
		}
		
		genericType = genericType.substring(start + 1, end);
		
		String[] split = genericType.replace(" ", "").split(",");
		
		if (split.length != 2)
		{
			logger.warning("Invalid generics configuration", split);
			return null;
		}
		
		Class<?> typeOfElement = null;
		
		try
		{ 
			typeOfElement = Class.forName(split[1]);
		}
		catch (ClassNotFoundException e)
		{
			logger.error("Generic type of deserializing list &{rd}must be an existing class!&{} Class", genericType, "does not exists!");
			return null;
		}
		
		for (Map.Entry<String, SwconfPrimitive> child : swconfObject.children.entrySet())
		{
			obj.put(child.getKey(), serialization.deserialize(typeOfElement, null, child.getValue(), null));
		}
		
		return obj;
	}

	@Override
	public SwconfObject serialize(Map obj)
	{
		SwconfObject object = new SwconfObject();
		
		for (Object entry : obj.entrySet())
		{
			Map.Entry e = (Map.Entry) entry;
			SwconfPrimitive primitive = serialization.serialize(e.getValue());
			primitive.setName(e.getKey() + "");
			object.addChild(primitive);
		}
		
		return object;
	}

	@Override
	public Map createNewInstance(Class<?> classOfObject, SwconfObject swconfObject)
	{
		for (IFunction1<Class<?>, Map> generator : mapCreatorsFuns)
		{
			Map ret = generator.apply(classOfObject);
			
			if (ret != null)
				return ret;
		}
		
		logger.warning("No generator found for type", classOfObject);
		
		return null;
	}
}
