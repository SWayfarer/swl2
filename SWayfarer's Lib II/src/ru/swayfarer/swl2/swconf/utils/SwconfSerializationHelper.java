package ru.swayfarer.swl2.swconf.utils;

import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction0;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.resource.rlink.ResourceLink;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.swconf.format.StandartSwconfFormats;
import ru.swayfarer.swl2.swconf.format.SwconfFormat;
import ru.swayfarer.swl2.swconf.primitives.SwconfObject;
import ru.swayfarer.swl2.swconf.primitives.SwconfPrimitive;
import ru.swayfarer.swl2.swconf.serialization.SwconfSerialization;
import ru.swayfarer.swl2.swconf.serialization.writer.ISwconfWriter;
import ru.swayfarer.swl2.threads.ThreadsUtils;

/**
 * Помощник в сериализации Swconf
 * <br> Позволяет сериализовывать и десериализовывать объекты при помощи Swconf
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class SwconfSerializationHelper {

	/** {@link SwconfSerializationHelper} со стандартным форматом Swconf */
	public static SwconfSerializationHelper standart = new SwconfSerializationHelper();
	
	/** {@link SwconfSerializationHelper} с json форматом */
	public static SwconfSerializationHelper json = new SwconfSerializationHelper().setFormat(StandartSwconfFormats.JSON_FORMAT);
	
	/** {@link SwconfSerializationHelper} с форматом проперти, разделенной символами CRLF ('\n\r') */
	public static SwconfSerializationHelper propertyCrLf = new SwconfSerializationHelper().setFormat(StandartSwconfFormats.getPropertyFormat(StringUtils.CRLF));
	
	/** {@link SwconfSerializationHelper} с форматом пропети, разделенный символами LF ('\n') */
	public static SwconfSerializationHelper propertyLf = new SwconfSerializationHelper().setFormat(StandartSwconfFormats.getPropertyFormat(StringUtils.LF));
	
	/** {@link SwconfSerializationHelper} с форматом lua, разделенный символами LF ('\n') */
	public static SwconfSerializationHelper lua = new SwconfSerializationHelper().setFormat(StandartSwconfFormats.LUA_FORMAT);
	
	/** Формат, который будет использован для (де)сериализации Swconf */
	@InternalElement
	public SwconfFormat swconfFormat = new SwconfFormat();
	
	/** Функция, создающая новые {@link SwconfSerialization} когда потребуется */
	@InternalElement
	public IFunction0<SwconfSerialization> serializationFactoryFun = SwconfSerialization::new;

	/** Получить {@link SwconfSerialization} для работы */
	@InternalElement
	public SwconfSerialization getSerialization()
	{
		return ThreadsUtils.getThreadLocal(serializationFactoryFun);
	}
	
	/** Сохранить в Swconf-строку */
	public String saveToSwconf(Object obj)
	{
		SwconfSerialization serialization = getSerialization();
		SwconfPrimitive swconf = serialization.serialize(obj);
		ISwconfWriter writer = swconfFormat.getWriter(true);
		writer.write(swconf);
		return writer.toSwconfString();
	}
	
	/** Сохранить в Swconf-строку */
	public <T extends SwconfPrimitive> T saveToSwconfPrimitive(Object obj)
	{
		return getSerialization().serialize(obj);
	}
	
	/** Прочитать из Swconf строки */
	public <T extends SwconfObject> T readFromSwconf(String swconfString)
	{
		return (T) swconfFormat.getReader().apply(swconfString);
	}
	
	/** Прочитать из Swconf строки */
	public <T> T readFromSwconf(String swconfString, Class<?> cl)
	{
		IFunction1<String, SwconfObject> reader = swconfFormat.getReader();
		SwconfObject object = reader.apply(swconfString);
		return readFromSwconf(object, cl);
	}
	
	/** Прочитать из Swconf примитива */
	public <T> T readFromSwconf(SwconfPrimitive swconfPrimitive, Class<?> cl)
	{
		SwconfSerialization serialization = new SwconfSerialization();
		return (T) serialization.deserialize(cl, swconfPrimitive);
	}
	
	/** Прочитать из Swconf строки */
	public <T> T readFromSwconf(String swconfString, T instance)
	{
		IFunction1<String, SwconfObject> reader = swconfFormat.getReader();
		SwconfObject object = reader.apply(swconfString);
		return readFromSwconf(object, instance);
	}
	
	/** Прочитать из Swconf примитива */
	public <T> T readFromSwconf(SwconfPrimitive swconfPrimitive, T instance)
	{
		SwconfSerialization serialization = new SwconfSerialization();
		return (T) serialization.deserialize(instance, swconfPrimitive);
	}
	
	/** Задать формат */
	public <T extends SwconfSerializationHelper> T setFormat(SwconfFormat format) 
	{
		this.swconfFormat = format;
		return (T) this;
	}
	
	public static SwconfSerializationHelper forRlink(ResourceLink resourceLink)
	{
		if (resourceLink == null)
			return null;
		
		String ext = resourceLink.getSimpleName();
		
		if (StringUtils.isEmpty(ext))
			return null;
		
		if (ext.endsWith(".json"))
			return json;
		else if (ext.endsWith(".swconf"))
			return standart;
		else if (ext.endsWith(".properties"))
			return propertyCrLf;
		else if (ext.endsWith(".lua"))
			return lua;
		
		return standart;
	}
	
}
