package ru.swayfarer.swl2.swconf.serialization;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.swconf.primitives.SwconfPrimitive;

@SuppressWarnings( {"rawtypes", "unchecked"} )
public class SwconfSerialization {

	public static ILogger logger = LoggingManager.getLogger();
	
	/** Зарегистрированные провайдеры для (де)сериализации */
	public IExtendedList<ISwconfSerializationProvider> registeredProviders = CollectionsSWL.createExtendedList();
	
	/** Зарегистрировать провайдера для (де)сериализации */
	public <PrimitiveType extends SwconfPrimitive, ObjectType, T extends SwconfSerialization> T registerProvider(ISwconfSerializationProvider<PrimitiveType, ObjectType> provider) 
	{
		registeredProviders.addExclusive(provider);
		return (T) this;
	}
	
	/** Найти подходящего провайдера для класса и {@link SwconfPrimitive}'а */
	public <PrimitiveType extends SwconfPrimitive, ObjectType> ISwconfSerializationProvider<PrimitiveType, ObjectType> getProviderFor(Class<ObjectType> cl)
	{
		return registeredProviders.dataStream().find((e) -> e.isAccetps(cl));
	}
	
	/** Сериализовать */
	public SwconfPrimitive serialize(Class<?> cl, Object obj, ISwconfSerializationProvider provider)
	{
		if (provider == null)
			provider = getProviderFor(cl);
		
		if (provider == null)
		{
			logger.warning("Serialization provider for class", cl, "was not found! Please, &{gr}register some provider that accepts it &{}to using (de)serialization! Skiping...");
			return null;
		}
		
		return provider.serialize(obj);
	}
	
	/** Десериализовать */
	public <T> T deserialize(Class<T> cl, T instance, SwconfPrimitive primitive, ISwconfSerializationProvider provider)
	{
		if (provider == null)
			provider = getProviderFor(cl);
		
		if (provider == null)
		{
			logger.warning("Serialization provider for class", cl, "was not found! Please, &{gr}register some provider that accepts it &{}to using (de)serialization! Skiping...");
			return null;
		}
		
		return (T) provider.deserialize(cl, instance, primitive);
	}
}
