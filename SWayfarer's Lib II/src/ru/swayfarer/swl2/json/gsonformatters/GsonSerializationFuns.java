package ru.swayfarer.swl2.json.gsonformatters;

import java.lang.reflect.Field;

import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction3NoR;
import ru.swayfarer.swl2.json.JsonType;
import ru.swayfarer.swl2.json.JsonTypedWrapper;
import ru.swayfarer.swl2.json.JsonUtils;
import ru.swayfarer.swl2.json.NonJson;
import ru.swayfarer.swl2.json.gson.deserializers.TypedWrapperDeserializer;
import ru.swayfarer.swl2.json.gson.serializers.TypedWrapperSerializer;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.z.dependencies.com.google.gson.Gson;

/**
 * Функции для работы с (де)сериализацией данных через {@link Gson}
 * @author swayfarer
 */
public class GsonSerializationFuns {
	
	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Экземпляр {@link Gson} для работы */
	@InternalElement
	public static Gson gson = new Gson();
	
	/** Функция для десериализации с созданием нового инстанса */
	public static IFunction2<Class<?>, String, Object> deserializeFun = (cl, jsonString) -> gson.fromJson(jsonString, cl);
	
	/** Функция для десериализации с использованием существующего инстанса */
	public static IFunction3NoR<Class<?>, String, Object> deserializeInstanceFun = (cl, jsonString, instance) -> gson.fromJson(jsonString, instance);
	
	/** Функция для сериализации */
	public static IFunction2<Class<?>, Object, String> serializeFun = (cl, instance) -> gson.toJson(instance, cl);
	
	/** Инициализация для работы с {@link JsonUtils} */
	public static void init()
	{
		gson.formatter = new PrettyGsonFormatter();
		
		gson.registerDeserializer(JsonTypedWrapper.class, new TypedWrapperDeserializer());
		gson.registerSerializer(JsonTypedWrapper.class, new TypedWrapperSerializer());
		
		gson.eventFieldSerialization.subscribe((sub, event) -> {
			if (event.field.getAnnotation(NonJson.class) != null)
				event.setCanceled(true);
		});
		
		gson.eventFieldSerialization.subscribe((sub, event) -> {
			
			logger.safe(() -> {
				
				Field field = event.field;
				
				JsonType jsonTypeAnnotation = field.getAnnotation(JsonType.class);
				
				if (jsonTypeAnnotation != null)
				{
					String as = jsonTypeAnnotation.as();
					
					if (!StringUtils.isEmpty(as))
					{
						Field asField = event.cl.getField(as);
						
						Object asValue = asField.get(event.obj);
						
						if (asValue != null)
						{
							event.fieldClass = Class.forName((asValue+""));
						}
						else
							logger.warning("To use annotation", JsonType.class, "on field", field, "value of", asField, "must not be null! Skiping...");
					}
				}
				
			}, "Error while changing type of", event.field, "by annotation", JsonType.class);
		});
		
		JsonUtils.deserializeFun = deserializeFun;
		JsonUtils.deserializeInstanceFun = deserializeInstanceFun;
		JsonUtils.serializeFun = serializeFun;
	}
	
}
