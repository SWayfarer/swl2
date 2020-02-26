package ru.swayfarer.swl2.resource.streams;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import ru.swayfarer.swl2.markers.InternalElement;

/**
 * {@link ByteArrayOutputStream}, с которым можно работать как с {@link DataOutputStreamSWL} 
 * @author swayfarer
 */
public class BytesOutputStreamSWL extends DataOutputStreamSWL{

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

}
