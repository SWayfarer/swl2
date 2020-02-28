package ru.swayfarer.swl2.json.gson.serializers;

import java.lang.reflect.Type;

import ru.swayfarer.swl2.json.JsonTypedWrapper;
import ru.swayfarer.swl2.json.JsonUtils;
import ru.swayfarer.swl2.json.gson.deserializers.TypedWrapperDeserializer;
import ru.swayfarer.swl2.z.dependencies.com.google.gson.JsonElement;
import ru.swayfarer.swl2.z.dependencies.com.google.gson.JsonSerializationContext;
import ru.swayfarer.swl2.z.dependencies.com.google.gson.JsonSerializer;

/**
 * Сериализатор для {@link TypedWrapperDeserializer} 
 * @author swayfarer
 *
 */
public class TypedWrapperSerializer implements JsonSerializer<JsonTypedWrapper> {

	@Override
	public JsonElement serialize(JsonTypedWrapper src, Type typeOfSrc, JsonSerializationContext context)
	{
		if (src != null)
		{
			return JsonUtils.parseJsonObjectSafe(src.toJsonString());
		}
		
		return null;
	}

}
