package ru.swayfarer.swl2.updater.progress;

import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.Observables;
import ru.swayfarer.swl2.observable.property.ObservableProperty;
import ru.swayfarer.swl2.resource.streams.DataInputStreamSWL;
import ru.swayfarer.swl2.resource.streams.DataOutputStreamSWL;
import ru.swayfarer.swl2.resource.streams.StreamsUtils;

/**
 * Загрузка, отслеживающая свой прогресс
 * см. {@link IProgressLoading}
 * @author swayfarer
 *
 */
public class ProgressDownloading implements IProgressLoading {

	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** {@link URL}, из которого проиходит загрузка */
	@InternalElement
	public URL targetUrl;
	
	/** Логировать ли процесс? */
	@InternalElement
	public boolean isLogging = false;
	
	/** Максимальное кол-во загружаемых байт */
	@InternalElement
	public int maxBytes;
	
	/** Событие создания соединения (На этом этапе можно кастомно сконфигурировать соединение) */
	@InternalElement
	public IObservable<URLConnection> eventCreareConnection = Observables.createObservable();
	
	/** {@link ObservableProperty}, содержащая прогресс загрузки */
	@InternalElement
	public ObservableProperty<Integer> progress = Observables.createProperty(0);
	
	/**
	 * Конструктор
	 * @param targetUrl Целевой {@link URL}, из которого будет производиться загрузка
	 */
	public ProgressDownloading(URL targetUrl)
	{
		this.targetUrl = targetUrl;
	}
	
	@Override
	public boolean start(OutputStream saveTarget, boolean isCloseOut)
	{
		try
		{	
			URLConnection connection = targetUrl.openConnection();
			eventCreareConnection.next(connection);
			
			maxBytes = connection.getContentLength();
			
			DataInputStreamSWL dis = DataInputStreamSWL.of(connection.getInputStream());
			DataOutputStreamSWL dos = DataOutputStreamSWL.of(saveTarget);
			StreamsUtils.copyStream(dis, dos, progress,  true, isCloseOut);
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while downloading", targetUrl, "by", this);
			return false;
		}
		
		return true;
	}

	@Override
	public ObservableProperty<Integer> getProgress()
	{
		return progress;
	}
}
