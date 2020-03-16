package ru.swayfarer.swl2.swconf.config;

import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.resource.rlink.RLUtils;
import ru.swayfarer.swl2.resource.rlink.ResourceLink;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.swconf.format.SwconfFormat;
import ru.swayfarer.swl2.swconf.primitives.SwconfObject;
import ru.swayfarer.swl2.swconf.serialization.SwconfSerialization;
import ru.swayfarer.swl2.swconf.serialization.comments.IgnoreSwconf;
import ru.swayfarer.swl2.swconf.serialization.reader.SwconfReader;
import ru.swayfarer.swl2.swconf.serialization.writer.ISwconfWriter;
import ru.swayfarer.swl2.threads.ThreadsUtils;

/**
 * Автоматически-(де)сериализуемый конфиг 
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class AutoSerializableConfig {
	
	/** Логгер */
	@InternalElement @IgnoreSwconf
	public ILogger logger = LoggingManager.getLogger(getClass());
	
	/** Был ли проинициализиорован конфиг? */
	@InternalElement @IgnoreSwconf
	public boolean isInited = false;
	
	/** Формат конфига, в котором он хранится где-либо */
	@InternalElement @IgnoreSwconf
	public SwconfFormat configurationFormat = new SwconfFormat();
	
	/** Ссылка на ресурс конфига */
	@InternalElement @IgnoreSwconf
	public ResourceLink resourceLink;
	
	/** Конструктор без указания ссылки на конфиг. Она всегда может быть задана через {@link #setResourceLink(ResourceLink)} */
	public AutoSerializableConfig() { }
	
	/** Конструктор с указанием ссылки на конфиг */
	public AutoSerializableConfig(ResourceLink resourceLink)
	{
		setResourceLink(resourceLink);
	}
	
	/** Конструктор с указан */
	public AutoSerializableConfig(@ConcattedString Object... text)
	{
		setResourceLink(text);
	}
	
	/** Загрузить конфиг из ссылки {@link #resourceLink} */
	public <T extends AutoSerializableConfig> T load() 
	{
		SwconfReader reader = configurationFormat.getReader();
		SwconfObject object = reader.readSwconf(resourceLink.toSingleString());
		System.out.println(object);
		SwconfSerialization serialization = getSwconfSerialization();
		serialization.deserialize(this, object);
		
		return (T) this;
	}
	
	/** Сохранить конфиг в ссылку {@link #resourceLink} */
	public <T extends AutoSerializableConfig> T save() 
	{
		if (!resourceLink.isWritable())
		{
			logger.error("Can't save configuration", this, "to link", resourceLink, "because writing is not available to it's link!");
			return (T) this;
		}
		
		SwconfSerialization serialization = getSwconfSerialization();
		ISwconfWriter writer = configurationFormat.getWriter(true);
		SwconfObject object = serialization.serialize(this);
		
		writer.write(object);
		resourceLink.toOutStream().writeString(writer.toSwconfString());
		
		return (T) this;
	}
	
	/** Получить экзмепляр {@link SwconfSerialization} для текущего потока */
	@InternalElement
	public static SwconfSerialization getSwconfSerialization()
	{
		return ThreadsUtils.getThreadLocal(() -> new SwconfSerialization());
	}
	
	/** Инициализация конфига */
	@InternalElement
	public void init()
	{
		if (!isInited)
		{
			if (resourceLink != null)
				load();
			
			isInited = true;
		}
	}
	
	/** Задать ссылку на конфиг */
	public <T extends AutoSerializableConfig> T setResourceLink(ResourceLink rlink) 
	{
		this.resourceLink = rlink;
		return (T) this;
	}
	
	/** Задать ссылку на конфиг */
	public <T extends AutoSerializableConfig> T setResourceLink(@ConcattedString Object... text) 
	{
		return setResourceLink(RLUtils.createLink(StringUtils.concat(text)));
	}

}
