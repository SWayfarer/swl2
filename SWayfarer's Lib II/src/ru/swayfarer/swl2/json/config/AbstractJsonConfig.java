package ru.swayfarer.swl2.json.config;

import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils.StacktraceOffsets;
import ru.swayfarer.swl2.json.JsonUtils;
import ru.swayfarer.swl2.json.NonJson;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.resource.rlink.RLUtils;
import ru.swayfarer.swl2.resource.rlink.ResourceLink;
import ru.swayfarer.swl2.resource.streams.DataInputStreamSWL;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Конфиг, который автоматически сериализует и десериализует свои поля с помощью json
 * @author swayfarer
 */
@SuppressWarnings("unchecked")
public class AbstractJsonConfig {

	/** Логгер */
	@InternalElement @NonJson
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Расположение конфига */
	@InternalElement @NonJson
	public ResourceLink location;
	
	/** Кодировка конфига, в которой он будет читаться и сохраняться */
	@InternalElement @NonJson
	public String encoding = "UTF-8";
	
	/** Необходимо ли обновить конфиг */
	@NonJson
	public boolean isDirty = false;
	
	public AbstractJsonConfig()
	{
		ConfigSaveThread.addConfig(this);
	}
	
	/**
	 * Создать конфиг и проиниицализировать (загрузить + сохранить) его.
	 */
	public static <T extends AbstractJsonConfig> T create(String rlink, String encoding)
	{
		String type = ExceptionsUtils.getStacktraceClassAt(StacktraceOffsets.OFFSET_CALLER);
		
		try
		{
			return create(RLUtils.createLink(rlink), (Class<T>) Class.forName(type), encoding);
		}
		catch (ClassNotFoundException e)
		{
			logger.error("Error while creting config of type", type, "at path", rlink);
		}
		
		return null;
	}
	
	/**
	 * Создать конфиг и проиниицализировать (загрузить + сохранить) его.
	 */
	public static <T extends AbstractJsonConfig> T create(Class<T> classOfConfig, String rlink, String encoding)
	{
		return create(RLUtils.createLink(rlink), classOfConfig, encoding);
	}
	
	/**
	 * Создать конфиг и проиниицализировать (загрузить + сохранить) его.
	 */
	public static <T extends AbstractJsonConfig> T create(ResourceLink location, Class<T> classOfCfg, String encoding)
	{
		if (classOfCfg == null)
			return null;
		
		if (location == null)
			return null;
		
		if (StringUtils.isEmpty(encoding))
			encoding = "UTF-8";
		
		try
		{
			T config = classOfCfg.newInstance();
			config.location = location;
			config.encoding = encoding;
			config.load();
			config.save();
			
			return config;
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while creting config of type", classOfCfg, "at path", location);
		}
		
		return null;
	}
	
	/**
	 * Сохранить конфиг в его локацию 
	 */
	public synchronized <T extends AbstractJsonConfig> T save()
	{
		if (location == null)
		{
			logger.error("Can't save json to null location! Skiping...");
			return (T) this;
		}
		
		FileSWL file = location.toFile();
		
		if (file == null)
		{
			logger.error("Can't save json to non file location!");
			return (T) this;
		}
		
		JsonUtils.saveToFile(this, file);
		
		return (T) this;
	}
	
	/**
	 * Загрузить конфиг из его локации
	 */
	public synchronized <T extends AbstractJsonConfig> T load()
	{
		if (location == null)
		{
			logger.error("Can't load json from null location! Skiping...");
			return (T) this;
		}
		
		if (!location.isExists())
			return (T) this;
		
		DataInputStreamSWL inputStream = location.toStream();
		
		if (inputStream == null)
		{
			logger.error("Can't load json from non existing location! Skiping...");
			return (T) this;
		}
		
		JsonUtils.loadFromJson(inputStream.readAllAsString(encoding), this);
		
		if (!inputStream.closeSafe())
			logger.warning("Error occured while closing input stream for location", location);
		
		return (T) this;
	}
}
