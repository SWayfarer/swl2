package ru.swayfarer.swl2.swconf.serialization.providers;

import ru.swayfarer.swl2.equals.EqualsUtils;
import ru.swayfarer.swl2.swconf.primitives.SwconfString;
import ru.swayfarer.swl2.swconf.serialization.ISwconfSerializationProvider;

/**
 * Провайдер для строк и символов
 * @author swayfarer
 *
 */
public class StringSwconfSerializationProvider implements ISwconfSerializationProvider<SwconfString, Object> {

	@Override
	public boolean isAccetps(Class<?> type)
	{
		return EqualsUtils.objectEqualsSome(type, String.class, char.class, Character.class);
	}

	@Override
	public Object deserialize(Class<?> cl, Object obj, SwconfString swconfObject)
	{
		String ret = swconfObject.getValue();
		return EqualsUtils.objectEqualsSome(cl, Character.class, char.class) ? ret.charAt(0) : ret;
	}

	@Override
	public SwconfString serialize(Object obj)
	{
		return new SwconfString().setValue(obj);
	}

	@Override
	public String createNewInstance(Class<?> classOfObject, SwconfString swconfObject)
	{
		return null;
	}

}
