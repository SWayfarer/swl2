package ru.swayfarer.swl2.swconf.serialization.providers;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.collections.observable.IObservableList;
import ru.swayfarer.swl2.collections.observable.ObservableListWrapper;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.swconf.primitives.SwconfArray;
import ru.swayfarer.swl2.swconf.primitives.SwconfPrimitive;
import ru.swayfarer.swl2.swconf.serialization.ISwconfSerializationProvider;
import ru.swayfarer.swl2.swconf.serialization.SwconfSerialization;

/**
 * Провайдер для листов 
 * @author swayfarer
 *
 */
@SuppressWarnings( { "rawtypes", "unchecked" } )
public class ListSwconfSerializationProvider implements ISwconfSerializationProvider<SwconfArray, List> {

	/** Сериализация, которая будет использована для элементов листа */
	@InternalElement
	public SwconfSerialization serialization;
	
	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Список функций-генераторов листов, которые сгенерируют наиболее подходящий лист под указанный класс. */
	@InternalElement
	public IExtendedList<IFunction1<Class<? extends List>, List>> listGenerators = CollectionsSWL.createExtendedList();
	
	/** Конструктор */
	public ListSwconfSerializationProvider(SwconfSerialization serialization)
	{
		super();
		this.serialization = serialization;
		registerDefaultListGenerators();
	}

	/** Зарегистрировать стандартные генераторы */
	@InternalElement
	public <T extends ListSwconfSerializationProvider> T registerDefaultListGenerators() 
	{
		registerListGenerator((cl) -> {
			
			if (cl.equals(List.class))
				return new ArrayList<>();
			
			return null;
		});
		
		registerListGenerator((cl) -> {
			
			if (cl.equals(IExtendedList.class))
				return CollectionsSWL.createExtendedList();
			
			return null;
		});
		
		registerListGenerator((cl) -> {
			
			if (cl.equals(IObservableList.class))
				return new ObservableListWrapper<>();
			
			return null;
		});
		
		return (T) this;
	}
	
	@Override
	public boolean isAccetps(Class<?> type)
	{
		return List.class.isAssignableFrom(type);
	}

	@Override
	public List deserialize(Field field, Class<?> cl, List obj, SwconfArray swconfObject)
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
		
		for (SwconfPrimitive primitive : swconfObject.elements)
		{
			obj.add(serialization.deserialize(typeOfElement, null, primitive, null));
		}
		
		return obj;
	}
	
	/** Зарегистрировать генератор листов по их классам */
	public <A, T extends ListSwconfSerializationProvider> T registerListGenerator(IFunction1<Class<? extends List>, List<A>> gen) 
	{
		this.listGenerators.addExclusive(0, ReflectionUtils.forceCast(gen));
		return (T) this;
	}
	
	/** Создать лист, зная тип, с которым он ассоциирован */
	public <T extends List> T generateList(Class cl)
	{
		for (IFunction1<Class<? extends List>, List> gen : listGenerators)
		{
			List ret = gen.apply(cl);
			
			if (ret != null)
			{
				return (T) ret;
			}
		}
		
		return null;
	}

	@Override
	public SwconfArray serialize(List obj)
	{
		SwconfArray arr = new SwconfArray();
		
		for (Object o : obj)
		{
			arr.addChild(serialization.serialize(o));
		}
		
		return arr;
	}

	@Override
	public List createNewInstance(Class<?> classOfObject, SwconfArray swconfObject)
	{
		return generateList(classOfObject);
	}

	@Override
	public List deserialize(Class<?> cl, List obj, SwconfArray swconfObject)
	{
		ExceptionsUtils.unsupportedOperation("Deserializing without field is not supported!");
		return null;
	} 

}
