package ru.swayfarer.swl2.resource.streams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.Observables;

/**
 * {@link ByteArrayOutputStream}, с которым можно работать как с {@link DataOutputStreamSWL} 
 * @author swayfarer
 */
public class BytesOutputStreamSWL extends DataOutputStreamSWL{

	/** Событие закрытия потока */
	@InternalElement
	public IObservable<BytesOutputStreamSWL> eventClose = Observables.createObservable();
	
	/** Обернутый {@link ByteArrayOutputStream}*/
	@InternalElement
	public ByteArrayOutputStream bytesOut;
	
	/** Конструктор */
	protected BytesOutputStreamSWL(OutputStream stream)
	{
		super(stream);
	}
	
	/** Получить все байты */
	public byte[] toBytesArray()
	{
		return bytesOut == null ? null : bytesOut.toByteArray();
	}
	
	/** Создать поток */
	public static BytesOutputStreamSWL createStream()
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		BytesOutputStreamSWL ret = new BytesOutputStreamSWL(bytes);
		ret.bytesOut = bytes;
		return ret;
	}
	
	@Override
	public void close() throws IOException
	{
		super.close();
		eventClose.next(this);
	}

}
