package ru.swayfarer.swl2.swconf.config;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.observable.IObservableList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.SimpleObservable;
import ru.swayfarer.swl2.observable.property.ObservableProperty;
import ru.swayfarer.swl2.resource.rlink.RLUtils;
import ru.swayfarer.swl2.resource.rlink.ResourceLink;
import ru.swayfarer.swl2.resource.streams.DataInputStreamSWL;
import ru.swayfarer.swl2.resource.streams.DataOutputStreamSWL;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.swconf.format.SwconfFormat;
import ru.swayfarer.swl2.swconf.primitives.SwconfObject;
import ru.swayfarer.swl2.swconf.serialization.SwconfSerialization;
import ru.swayfarer.swl2.swconf.serialization.comments.IgnoreSwconf;
import ru.swayfarer.swl2.swconf.serialization.comments.TextContainerSwconf;
import ru.swayfarer.swl2.swconf.serialization.writer.ISwconfWriter;
import ru.swayfarer.swl2.swconf.utils.SwconfSerializationHelper;
import ru.swayfarer.swl2.threads.ThreadsUtils;

/**
 * Автоматически-(де)сериализуемый конфиг 
 * @author swayfarer
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class AutoSerializableConfig {
	
	/** Информация о конфиге */
	@InternalElement @IgnoreSwconf
	public ConfigInfo configInfo = new ConfigInfo();
	
	/** Событие постпроцессинга конфига по полям */
	@InternalElement @IgnoreSwconf
	public IObservable<FieldPostProcessingEvent> eventPostProcessing = new SimpleObservable<>();
	
	/** Логгер */
	@InternalElement @IgnoreSwconf
	public ILogger logger = LoggingManager.getLogger(getClass().getSimpleName());
	
	/** Конструктор без указания ссылки на конфиг. Она всегда может быть задана через {@link #setResourceLink(ResourceLink)} */
	public AutoSerializableConfig() { registerDefaultPostProcessors(); }
	
	/** Конструктор с указанием ссылки на конфиг */
	public AutoSerializableConfig(ResourceLink resourceLink)
	{
		setResourceLink(resourceLink);
		registerDefaultPostProcessors();
	}
	
	/** Конструктор с указан */
	public AutoSerializableConfig(@ConcattedString Object... text)
	{
		setResourceLink(text);
		registerDefaultPostProcessors();
	}
	
	public static <T extends AutoSerializableConfig> T loadFrom(Class<? extends AutoSerializableConfig> classOfConfig, @ConcattedString Object... rlink)
	{
		T config = ReflectionUtils.newInstanceOf(classOfConfig);
		
		config.setResourceLink(rlink);
		config.createIfNotFound();
		config.init();
		
		return config;
	}
	
	/** Загрузить конфиг из ссылки {@link #resourceLink} */
	public <T extends AutoSerializableConfig> T load() 
	{
		IFunction1<String, SwconfObject> reader = configInfo.configurationFormat.getReader();
		DataInputStreamSWL dis = configInfo.resourceLink.toStream();
		
		if (dis == null)
		{
			logger.warning("Can't read resource", configInfo.resourceLink, "by config", this);
			return (T) this;
		}
		
		configureIn(dis);
		
		SwconfObject object = reader.apply(dis.readAllAsString());
		
		if (object == null)
		{
			configInfo.isReadedSuccessfully.set(false);
			return (T) this;
		}
		
		SwconfSerialization serialization = getSwconfSerialization();
		serialization.deserialize(this, object);
		configInfo.isReadedSuccessfully.set(true);
		
		return (T) this;
	}
	
	/** Настроить читающий поток */
	@InternalElement
	public void configureIn(DataInputStreamSWL is)
	{
		TextContainerSwconf encodingSwconf = getClass().getAnnotation(TextContainerSwconf.class);
		
		if (encodingSwconf != null)
		{
			String lineSplitter = encodingSwconf.lineSplitter();
			String newEncoding = encodingSwconf.encoding();
			
			if (StringUtils.isEncodingSupported(newEncoding))
			{
				is.setEncoding(newEncoding);
			}
			else
			{
				logger.warning("Encoding '", newEncoding, "' for config", this, "is not supported by this jvm! Falling back to UTF-8!");
			}
			
			if (!StringUtils.isEmpty(lineSplitter))
			{
				is.setLineSplitter(lineSplitter);
			}
		}
	}
	
	/** Настроить пишуший поток */
	@InternalElement
	public void configureOut(DataOutputStreamSWL os)
	{
		TextContainerSwconf encodingSwconf = getClass().getAnnotation(TextContainerSwconf.class);
		
		if (encodingSwconf != null)
		{
			String lineSplitter = encodingSwconf.lineSplitter();
			String newEncoding = encodingSwconf.encoding();
			
			if (StringUtils.isEncodingSupported(newEncoding))
			{
				os.setEncoding(newEncoding);
			}
			else
			{
				logger.warning("Encoding '", newEncoding, "' for config", this, "is not supported by this jvm! Falling back to UTF-8!");
			}
			
			if (!StringUtils.isEmpty(lineSplitter))
			{
				os.setLineSplitter(lineSplitter);
			}
		}
	}
	
	/** Сохранить конфиг в ссылку {@link #resourceLink} */
	public synchronized <T extends AutoSerializableConfig> T save() 
	{
		synchronized (configInfo.isNeedsToSave)
		{
			if (!configInfo.resourceLink.isWritable())
			{
				logger.error("Can't save configuration", this, "to link", configInfo.resourceLink, "because writing is not available to it's link!");
				return (T) this;
			}
			
			SwconfSerialization serialization = getSwconfSerialization();
			ISwconfWriter writer = configInfo.configurationFormat.getWriter(true);
			SwconfObject object = serialization.serialize(this);
			
			writer.startWriting();
			writer.write(object);
			writer.endWriting();
			
			DataOutputStreamSWL dos = configInfo.resourceLink.toOutStream();
			
			configureOut(dos);
			
			dos.writeString(writer.toSwconfString());
			
			logger.info("Configuration saved to", configInfo.resourceLink.toRlinkString());
			
			configInfo.setIsNeedsToSave(false);
		}
		
		return (T) this;
	}
	
	/** Получить экзмепляр {@link SwconfSerialization} для текущего потока */
	@InternalElement
	public static SwconfSerialization getSwconfSerialization()
	{
		return ThreadsUtils.getThreadLocal(() -> new SwconfSerialization());
	}
	
	/** Создать когфиг, если не было до этого */
	public <T extends AutoSerializableConfig> T createIfNotFound() 
	{
		if (!configInfo.resourceLink.isExists())
		{
			save();
		}
		
		return (T) this;
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
	
	/** Слушать изменения в отдельном потоке раз в {@link ConfigInfo#DEFAULT_SAVE_DELAY} */
	public <T extends AutoSerializableConfig> T listen() 
	{
		return listen(ConfigInfo.DEFAULT_SAVE_DELAY);
	}
	
	/** Слушать изменения в конфиге раз в указанное в милисекундах время */
	public <T extends AutoSerializableConfig> T listen(long delay) 
	{
		configInfo.saveDelayInMilisis = delay;
		ConfigurationSaveThread thread = new ConfigurationSaveThread(this);
		thread.start();
		return (T) this;
	}
	
	public static <T extends AutoSerializableConfig> T ofRLink(ResourceLink rlink, Class<T> classOfConfig)
	{
		T ret = ReflectionUtils.newInstanceOf(classOfConfig);
		
		if (ret == null)
			return null;
		
		ret.configInfo.configurationFormat = SwconfSerializationHelper.forRlink(rlink).swconfFormat;
		ret.setResourceLink(rlink);
		ret.createIfNotFound();
		ret.init();
		
		return ret;
	}
	
	/** Задать ссылку на конфиг */
	public <T extends AutoSerializableConfig> T setResourceLink(ResourceLink rlink) 
	{
		configInfo.resourceLink = rlink;
		return (T) this;
	}
	
	public <T extends AutoSerializableConfig> T autoSerialization()
	{
		return (T) this;
	}
	
	/** Задать ссылку на конфиг */
	public <T extends AutoSerializableConfig> T setResourceLink(@ConcattedString Object... text) 
	{
		return setResourceLink(RLUtils.createLink(StringUtils.concat(text)));
	}
	
	public void registerDefaultPostProcessors()
	{
		eventPostProcessing.subscribe((e) -> {
			Object obj = e.value;
			
			if (obj instanceof ObservableProperty)
			{
				ObservableProperty property = (ObservableProperty) obj;
				
				property.eventChange.subscribe((event) -> {
					configInfo.setIsNeedsToSave(true);
				});
			}
		});
		
		eventPostProcessing.subscribe((e) -> {
			
			Object obj = e.value;
			
			
			if (obj instanceof IObservableList)
			{
				
				IObservableList list = (IObservableList) obj;
				
				list.evtPostUpdate().subscribe((event) -> {
					configInfo.setIsNeedsToSave(true);
				});
			}
		});
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
		public AtomicBoolean isNeedsToSave = new AtomicBoolean();
		
		@InternalElement @IgnoreSwconf
		public AtomicBoolean isReadedSuccessfully = new AtomicBoolean();
		
		/** Время, раз в которое конфиг будет сохраняться, если это необходимо */
		@InternalElement
		public long saveDelayInMilisis = DEFAULT_SAVE_DELAY;
		
		public boolean isNeedsToSave()
		{
			return isNeedsToSave.get();
		}
		
		public void setIsNeedsToSave(boolean isNeedsToSave)
		{
			synchronized (this.isNeedsToSave)
			{
				this.isNeedsToSave.set(isNeedsToSave);
			}
		}
		
		public boolean isReadedSuccessfully()
		{
			return isReadedSuccessfully.get();
		}
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
