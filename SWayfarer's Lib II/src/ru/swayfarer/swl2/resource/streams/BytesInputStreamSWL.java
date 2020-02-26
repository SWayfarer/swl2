package ru.swayfarer.swl2.resource.streams;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import ru.swayfarer.swl2.markers.InternalElement;

/** {@link ByteArrayInputStream}, с которым можно работать через {@link DataInputStreamSWL} */
public class BytesInputStreamSWL extends DataInputStreamSWL{

	/** Обернутый поток */
	@InternalElement
	public ByteArrayInputStream bis;
	
	/** Конструктор */
	protected BytesInputStreamSWL(InputStream in)
	{
		super(in);
	}
	
	/** Получить байты */
	public byte[] getBytes()
	{
		return StreamsUtils.readAllAndClose(bis);
	}
	
	/** Создать поток */
	public static BytesInputStreamSWL createStream(byte[] bytes)
	{
		if (bytes == null)
			return null;
		
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		BytesInputStreamSWL ret = new BytesInputStreamSWL(bis);
		ret.bis = bis;
		
		return ret;
	}
}
