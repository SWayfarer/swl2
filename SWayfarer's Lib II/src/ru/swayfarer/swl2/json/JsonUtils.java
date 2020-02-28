package ru.swayfarer.swl2.json;

import java.io.StringReader;

import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction3NoR;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.z.dependencies.com.google.gson.JsonElement;
import ru.swayfarer.swl2.z.dependencies.com.google.gson.JsonObject;
import ru.swayfarer.swl2.z.dependencies.com.google.gson.JsonParser;
import ru.swayfarer.swl2.z.dependencies.com.google.gson.ParseException;

/**
 * Утилиты для работы с Json
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class JsonUtils {

	/** Логгер*/
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Стандартная кодировка для записи и чтения Json из потоков s*/
	public static String encoding = "UTF-8";
	
	/** Функция для десериализации с созданием нового инстанса */
	public static IFunction2<Class<?>, String, Object> deserializeFun;
	
	/** Функция для десериализации с использованием существующего инстанса */
	public static IFunction3NoR<Class<?>, String, Object> deserializeInstanceFun;
	
	/** Функция для сериализации */
	public static IFunction2<Class<?>, Object, String> serializeFun;
	
	/** Записать Json в файл */
	public static String saveToFile(Object obj, FileSWL file)
	{
		ExceptionsUtils.If(file == null || (file.isDirectory() && file.exists()), IllegalArgumentException.class, "Save file can't be a directory!");
		
		String jsonString = saveToJson(obj);
		
		try
		{
			if (jsonString != null)
			{
				file.setData(jsonString.getBytes(encoding));
				return jsonString;
			}
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while saving json string '", jsonString, "', generated from", obj, "to file", file.getAbsolutePath());
		}
		
		return null;
	}
	
	/** Распрарсить json-строку в {@link JsonObject} */
	public static JsonObject parseJsonObjectSafe(String jsonString)
	{
		try
		{
			return parseJsonObject(jsonString);
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while parsing json\n", jsonString);
		}
		
		return null;
	}
	
	/** Распарсить Json */
	public static JsonObject parseJsonObject(String jsonString) throws ParseException
	{
		JsonElement jsonElement = parseJson(jsonString);
		
		if (jsonElement == null)
			return null;
		
		if (!(jsonElement instanceof JsonObject))
			return null;
		
		return (JsonObject) jsonElement;
	}
	
	/** Распарсить Json */
	public static JsonElement parseJson(String jsonString) throws ParseException
	{
		if (StringUtils.isEmpty(jsonString))
		{
			return null;
		}
		
		StringReader reader = new StringReader(jsonString);
		JsonParser parser = new JsonParser(reader);
		
		return parser.parse();
	}
	
	/** Превратить объект в json строку */
	public static String saveToJson(Object obj)
	{
		ExceptionsUtils.IfNull(obj, IllegalArgumentException.class, "Save object cannot be null");
		
		return serializeFun.apply(obj.getClass(), obj);
	}
	
	/** Загрузить объект из json строки */
	public static <T> T loadFromJson(String jsonString, T instance)
	{
		ExceptionsUtils.IfNull(instance, IllegalArgumentException.class, "Load instance cannot be null");
		
		return (T) loadFromJson(jsonString, instance, instance.getClass());
	}
	
	/** Загрузить объект из json строки */
	public static <T> T loadFromJson(String jsonString, Class<? extends T> classOfObject)
	{
		return loadFromJson(jsonString, null, classOfObject);
	}
	
	/** Загрузить объект из json строки */
	public static <T> T loadFromJson(FileSWL file, T instance)
	{
		ExceptionsUtils.If(file == null || !file.isFile() || !file.exists(), IllegalArgumentException.class, "Loading file must be an existing file (and not a directory)");
		ExceptionsUtils.IfNull(instance, IllegalArgumentException.class, "Load instance cannot be null");
		
		return (T) loadFromJson(file.toInputStream().readAllAsString(encoding), instance, instance.getClass());
	}
	
	/** Загрузить объект из json строки */
	public static <T> T loadFromJson(FileSWL file, Class<? extends T> classOfObject)
	{
		ExceptionsUtils.If(file == null || !file.isFile() || !file.exists(), IllegalArgumentException.class, "Loading file must be an existing file (and not a directory)");
		
		return loadFromJson(file.toInputStream().readAllAsString(encoding), null, classOfObject);
	}
	
	/**
	 * Загрузить объект из Json строки
	 * @param jsonString Десериализуемая Json строка 
	 * @param instance Экземпляр, поля которого будут десериализованы
	 * @param classOfObject Класс объекта, который будет создан, если указанный экземпляр null
	 * @return Десериализованный объект. Null, если не выйдет. 
	 */
	public static <T> T loadFromJson(String jsonString, T instance, Class<? extends T> classOfObject)
	{
		ExceptionsUtils.IfEmpty(jsonString, IllegalArgumentException.class, "Json string for deserialization cannot be empty or null");
		ExceptionsUtils.IfNull(classOfObject, IllegalArgumentException.class, "Load class cannot be null");
		
		try
		{
			if (instance == null)
			{
				if (deserializeFun == null)
				{
					logger.error("Deserialization function is null! Returnrning null object for \n", jsonString);
					return null;
				}
				
				return (T) deserializeFun.apply(classOfObject, jsonString);
			}
			else
			{
				if (deserializeInstanceFun == null)
				{
					logger.error("Deserialization to instance function is null! Returnrning null object for \n", jsonString);
					return null;
				}
				
				deserializeInstanceFun.apply(classOfObject, jsonString, instance);
				return instance;
			}
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while loading(deserializing) object of type", classOfObject, "from json: \n", jsonString);
		}
		
		return null;
	}
	
}
