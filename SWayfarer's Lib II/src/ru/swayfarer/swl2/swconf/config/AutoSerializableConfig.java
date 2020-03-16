package ru.swayfarer.swl2.swconf.config;

import java.lang.reflect.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.SimpleObservable;
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
	
	/** Информация о конфиге */
	@InternalElement @IgnoreSwconf
	public ConfigInfo configInfo = new ConfigInfo();
	
	/** Событие постпроцессинга конфига по полям */
	@InternalElement
	public IObservable<FieldPostProcessingEvent> eventPostProcessing = new SimpleObservable<>();
	
	/** Логгер */
	@InternalElement @IgnoreSwconf
	public ILogger logger = LoggingManager.getLogger(getClass());
	
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
		SwconfReader reader = configInfo.configurationFormat.getReader();
		SwconfObject object = reader.readSwconf(configInfo.resourceLink.toSingleString());
		SwconfSerialization serialization = getSwconfSerialization();
		serialization.deserialize(this, object);
		
		return (T) this;
	}
	
	/** Сохранить конфиг в ссылку {@link #resourceLink} */
	public synchronized <T extends AutoSerializableConfig> T save() 
	{
		if (!configInfo.resourceLink.isWritable())
		{
			logger.error("Can't save configuration", this, "to link", configInfo.resourceLink, "because writing is not available to it's link!");
			return (T) this;
		}
		
		SwconfSerialization serialization = getSwconfSerialization();
		ISwconfWriter writer = configInfo.configurationFormat.getWriter(true);
		SwconfObject object = serialization.serialize(this);
		
		writer.write(object);
		configInfo.resourceLink.toOutStream().writeString(writer.toSwconfString());
		
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
		if (!configInfo.isInited)
		{
			if (configInfo.resourceLink != null)
				load();
			
			postProcessField();
			
			configInfo.isInited = true;
		}
	}
	
	/** Обработать поля при инициализации конфига */
	@InternalElement
	public void postProcessField()
	{
		ReflectionUtils.forEachField(this, (field, instance, value) -> {
			logger.safe(() -> {
				
				FieldPostProcessingEvent event = FieldPostProcessingEvent.of(this, field, value);
				eventPostProcessing.next(event);
				
			}, "Error while initing observable field", field, "with value", value, "of object", instance);
		});
	}
	
	/** Задать ссылку на конфиг */
	public <T extends AutoSerializableConfig> T setResourceLink(ResourceLink rlink) 
	{
		configInfo.resourceLink = rlink;
		return (T) this;
	}
	
	/** Задать ссылку на конфиг */
	public <T extends AutoSerializableConfig> T setResourceLink(@ConcattedString Object... text) 
	{
		return setResourceLink(RLUtils.createLink(StringUtils.concat(text)));
	}
	
	/**
	 * Информация о конфигурации {@link AutoSerializableConfig} 
	 * <br> Вынесен в отельный класс, чтобы не засорять занятые имена полей 
	 * @author swayfarer
	 *
	 */
	public static class ConfigInfo {
		
		/** Стандартное значение для {@link #saveDelayInMilisis} */
		@InternalElement
		public static long DEFAULT_SAVE_DELAY = 1000;
		
		/** Был ли проинициализиорован конфиг? */
		@InternalElement @IgnoreSwconf
		public boolean isInited = false;
		
		/** Формат конфига, в котором он хранится где-либо */
		@InternalElement @IgnoreSwconf
		public SwconfFormat configurationFormat = new SwconfFormat();
		
		/** Ссылка на ресурс конфига */
		@InternalElement @IgnoreSwconf
		public ResourceLink resourceLink;
		
		/** Необходимо ли сохранить конфиг? */
		@InternalElement @IgnoreSwconf
		public boolean isNeedsToSave = false;
		
		/** Время, раз в которое конфиг будет сохраняться, если это необходимо */
		@InternalElement
		public long saveDelayInMilisis = DEFAULT_SAVE_DELAY;
	}
	
	/**
	 * Событие постинита поля конфига
	 * @author swayfarer
	 *
	 */
	@Data @AllArgsConstructor(staticName = "of")
	@InternalElement
	public static class FieldPostProcessingEvent {
		
		/** Конфиг, с которым работаем */
		@InternalElement
		public AutoSerializableConfig config;
		
		/** Поле, которое обрабатывается */
		@InternalElement
		public Field field;
	
		/** Значение поля, которое обрабатывается */
		@InternalElement
		public Object value;
		
	}

}
