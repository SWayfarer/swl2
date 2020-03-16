package ru.swayfarer.swl2.swconf.serialization;

import java.lang.reflect.Field;

import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.swconf.primitives.SwconfPrimitive;
import ru.swayfarer.swl2.swconf.serialization.providers.ArraySwconfSerialization;
import ru.swayfarer.swl2.swconf.serialization.providers.BooleanSwconfSerializationProvider;
import ru.swayfarer.swl2.swconf.serialization.providers.ListSwconfSerializationProvider;
import ru.swayfarer.swl2.swconf.serialization.providers.NumberSwconfSerializationProvider;
import ru.swayfarer.swl2.swconf.serialization.providers.ReflectionSwconfSerializationProvider;
import ru.swayfarer.swl2.swconf.serialization.providers.StringSwconfSerializationProvider;

@SuppressWarnings( {"rawtypes", "unchecked"} )
public class SwconfSerialization {

	/** Провайдер, использующий рефлексию */
	@InternalElement
	public ReflectionSwconfSerializationProvider reflectionProvider;
	
	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Зарегистрированные провайдеры для (де)сериализации */
	@InternalElement
	public IExtendedList<ISwconfSerializationProvider> registeredProviders = CollectionsSWL.createExtendedList();
	
	/** Конструктор */
	public SwconfSerialization()
	{
		registerDefaultProviders();
	}
	
	/** Найти зарегистрированного провайдера по его классу */
	public <T extends ISwconfSerializationProvider> T findRegisteredProvider(Class<T> cl)
	{
		return (T) registeredProviders.dataStream().find((e) -> e.getClass().equals(cl));
	}
	
	/** Зарегистрировать стандартных провайдеров */
	public <T extends SwconfSerialization> T registerDefaultProviders() 
	{
		registerProvider(reflectionProvider = new ReflectionSwconfSerializationProvider(this));
		registerProvider(new NumberSwconfSerializationProvider());
		registerProvider(new StringSwconfSerializationProvider());
		registerProvider(new BooleanSwconfSerializationProvider());
		registerProvider(new ArraySwconfSerialization(this));
		registerProvider(new ListSwconfSerializationProvider(this));
		
		return (T) this;
	}
	
	/** Зарегистрировать провайдера для (де)сериализации */
	public <PrimitiveType extends SwconfPrimitive, ObjectType, T extends SwconfSerialization> T registerProvider(ISwconfSerializationProvider<PrimitiveType, ObjectType> provider) 
	{
		registeredProviders.addExclusive(0, provider);
		return (T) this;
	}
	
	/** Найти подходящего провайдера для класса и {@link SwconfPrimitive}'а */
	public <PrimitiveType extends SwconfPrimitive, ObjectType> ISwconfSerializationProvider<PrimitiveType, ObjectType> getProviderFor(Class<ObjectType> cl)
	{
		return registeredProviders.dataStream().find((e) -> e.isAccetps(cl));
	}
	
	/** Сериализовать */
	public <T extends SwconfPrimitive> T serialize(Object obj)
	{
		return serialize(obj.getClass(), obj, null);
	}
	
	/** Сериализовать */
	public <T extends SwconfPrimitive> T serialize(Class<?> cl, Object obj, ISwconfSerializationProvider provider)
	{
		if (provider == null)
			provider = getProviderFor(cl);
		
		if (provider == null)
		{
			logger.warning("Serialization provider for class", cl, "was not found! Please, &{gr}register some provider that accepts it &{}to using (de)serialization! Skiping...");
			return null;
		}
		
		return (T) provider.serialize(obj);
	}
	
	/** Десериализовать */
	public <T> T deserialize(Class<?> cl, SwconfPrimitive primitive)
	{
		return deserialize(ReflectionUtils.forceCast(cl), null, primitive, null);
	}
	
	/** Десериализовать */
	public <T> T deserialize(T instance, SwconfPrimitive primitive)
	{
		return deserialize(ReflectionUtils.forceCast(instance.getClass()), instance, primitive, null);
	}
	
	/** Десериализовать */
	public <T> T deserialize(Class<T> cl, T instance, SwconfPrimitive primitive, ISwconfSerializationProvider provider)
	{
		return deserialize(cl, instance, primitive, null, provider);
	}
	
	/** Десериализовать */
	public <T> T deserialize(Class<T> cl, T instance, SwconfPrimitive primitive, Field field, ISwconfSerializationProvider provider)
	{
		if (provider == null)
			provider = getProviderFor(cl);
		
		if (provider == null)
		{
			logger.warning("Serialization provider for class", cl, "was not found! Please, &{gr}register some provider that accepts it &{}to using (de)serialization! Skiping...");
			return null;
		}
		
		if (instance == null)
		{
			instance = (T) provider.createNewInstance(cl, primitive);
		}
		
		return (T) provider.deserialize(field, cl, instance, primitive);
	}
}
