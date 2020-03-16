package ru.swayfarer.swl2.swconf.serialization.providers;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.observable.property.ObservableProperty;
import ru.swayfarer.swl2.swconf.primitives.SwconfPrimitive;
import ru.swayfarer.swl2.swconf.serialization.ISwconfSerializationProvider;
import ru.swayfarer.swl2.swconf.serialization.SwconfSerialization;

@SuppressWarnings( {"rawtypes", "unchecked"} )
public class ObservablePropertySwconfSerializationProvider implements ISwconfSerializationProvider<SwconfPrimitive, ObservableProperty> {

	public static ILogger logger = LoggingManager.getLogger();
	
	public SwconfSerialization serialization;
	
	public ObservablePropertySwconfSerializationProvider(SwconfSerialization serialization)
	{
		super();
		this.serialization = serialization;
	}

	@Override
	public boolean isAccetps(Class<?> type)
	{
		return ObservableProperty.class.isAssignableFrom(type);
	}

	@Override
	public ObservableProperty deserialize(Class<?> cl, ObservableProperty obj, SwconfPrimitive swconfObject)
	{
		throw new UnsupportedOperationException("Deserialization without field is not supported!");
	}
	
	@Override
	public ObservableProperty deserialize(Field field, Class<?> cl, ObservableProperty obj, SwconfPrimitive swconfObject)
	{
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
		
		Class<?> typeOfElement = null;
		
		try
		{ 
			typeOfElement = Class.forName(genericType);
		}
		catch (ClassNotFoundException e)
		{
			logger.error("Generic type of deserializing list &{rd}must be an existing class!&{} Class", genericType, "does not exists!");
			return null;
		}
		
		Object value = serialization.deserialize(typeOfElement, swconfObject);
		obj.setValue(value);
		return obj;
	}

	@Override
	public SwconfPrimitive serialize(ObservableProperty obj)
	{
		return serialization.serialize(obj.getValue());
	}

	@Override
	public ObservableProperty createNewInstance(Class<?> classOfObject, SwconfPrimitive swconfObject)
	{
		return new ObservableProperty<>();
	}

}
