package ru.swayfarer.swl2.json;

import java.io.PrintWriter;
import java.io.StringWriter;

import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.json.gsonformatters.PrettyGsonFormatter;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.z.dependencies.com.google.gson.JsonFormatter;
import ru.swayfarer.swl2.z.dependencies.com.google.gson.JsonObject;
import ru.swayfarer.swl2.z.dependencies.com.google.gson.JsonPrimitive;

/**
 * Обертка, знающая тип записанного объекта 
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class JsonTypedWrapper {

	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Тип записанного объекта */
	@InternalElement
	public String type;
	
	/** Записанный объект */
	@InternalElement
	public Object content;
	
	/** Форматтер */
	@InternalElement
	public JsonFormatter formatter;
	
	public JsonTypedWrapper()
	{
		this(new Object());
	}
	
	public JsonTypedWrapper(Object obj)
	{
		this(obj, null);
	}
	
	public JsonTypedWrapper(Object obj, String type)
	{
		ExceptionsUtils.IfNull(obj, IllegalArgumentException.class, "Wrapped object cannot be null!");
		
		if (StringUtils.isEmpty(type))
			type = obj.getClass().getName().toString();
		
		this.content = obj;
		this.type = type;
	}
	
	/** Включить красивое форматирование */
	public <T extends JsonTypedWrapper> T setPrettyFormatting()
	{
		formatter = new PrettyGsonFormatter();
		
		return (T) this;
	}
	
	/** Получить значение */
	public <T> T getContent()
	{
		return (T) content;
	}
	
	/** Получить Json-строку */
	public String toJsonString()
	{
		JsonObject contentObject = JsonUtils.parseJsonObjectSafe(JsonUtils.saveToJson(content));
		JsonObject ret = new JsonObject();
		
		ret.add("type", new JsonPrimitive(type));
		ret.add("content", contentObject);
		
		if (formatter != null)
		{
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			formatter.format(ret, printWriter);
			return stringWriter.toString();
		}
		
		return ret.toString();
	}
	
	/** Десериализовать (загрузить) обертку из json-строки */
	public static JsonTypedWrapper of(String jsonString)
	{
		JsonObject jsonObject = JsonUtils.parseJsonObjectSafe(jsonString);
		
		if (jsonObject == null)
			return null;
		
		try
		{
			String type = jsonObject.get("type").getAsString();
			
			if (!StringUtils.isEmpty(type))
			{
				Class<?> cl = Class.forName(type);
				
				String content = jsonObject.get("content").toString();
				
				Object contentObject = JsonUtils.loadFromJson(content, cl);
				
				return new JsonTypedWrapper(contentObject, type);
			}
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while loading JsonTypedWrapper from \n", jsonString);
		}
		
		return null;
	}

	@Override
	public String toString()
	{
		return "JsonTypedWrapper [type=" + type + ", content=" + content + ", formatter=" + formatter + "]";
	}
	
}
