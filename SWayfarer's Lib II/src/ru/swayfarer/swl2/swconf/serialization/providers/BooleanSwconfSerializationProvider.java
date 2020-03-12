package ru.swayfarer.swl2.swconf.serialization.providers;

import ru.swayfarer.swl2.equals.EqualsUtils;
import ru.swayfarer.swl2.swconf.primitives.SwconfBoolean;
import ru.swayfarer.swl2.swconf.serialization.ISwconfSerializationProvider;


/**
 * Провайдер для {@link Boolean} 
 * @author swayfarer
 *
 */
public class BooleanSwconfSerializationProvider implements ISwconfSerializationProvider<SwconfBoolean, Boolean> {

	@Override
	public boolean isAccetps(Class<?> type)
	{
		return EqualsUtils.objectEqualsSome(type, boolean.class, Boolean.class);
	}

	@Override
	public Boolean deserialize(Class<?> cl, Boolean obj, SwconfBoolean swconfObject)
	{
		return swconfObject.getValue();
	}

	@Override
	public SwconfBoolean serialize(Boolean obj)
	{
		return new SwconfBoolean().setValue(obj);
	}

	@Override
	public Boolean createNewInstance(Class<?> classOfObject, SwconfBoolean swconfObject)
	{
		return null;
	}

}
