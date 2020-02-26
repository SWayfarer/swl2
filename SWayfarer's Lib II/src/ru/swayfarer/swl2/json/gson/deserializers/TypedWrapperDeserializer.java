package ru.swayfarer.swl2.json.gson.deserializers;

import java.lang.reflect.Type;

import ru.swayfarer.swl2.json.JsonTypedWrapper;
import ru.swayfarer.swl2.z.dependencies.com.google.gson.JsonDeserializationContext;
import ru.swayfarer.swl2.z.dependencies.com.google.gson.JsonDeserializer;
import ru.swayfarer.swl2.z.dependencies.com.google.gson.JsonElement;
import ru.swayfarer.swl2.z.dependencies.com.google.gson.JsonParseException;

public class TypedWrapperDeserializer implements JsonDeserializer<JsonTypedWrapper> {

	@Override
	public JsonTypedWrapper deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		return JsonTypedWrapper.of(json.toString());
	}

}
