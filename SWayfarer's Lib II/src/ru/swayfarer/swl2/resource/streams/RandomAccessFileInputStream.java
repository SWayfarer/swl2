package ru.swayfarer.swl2.resource.streams;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;

@SuppressWarnings("unchecked")
public class RandomAccessFileInputStream extends InputStream {

	public static ILogger logger = LoggingManager.getLogger();
	
	public int markedPos;
	public RandomAccessFile file;
	
	public long start, end;
	
	public RandomAccessFileInputStream(RandomAccessFile file)
	{
		super();
		this.file = file;
		setStart(0);
		ExceptionsUtils.safe(() -> setEnd(file.length()));
	}
	
	public <T extends RandomAccessFileInputStream> T setStart(long pos) 
	{
		logger.safe(() -> {
			if (file.getFilePointer() < pos)
				file.seek(pos);
		}, "Error while setting start pos to", pos);
		
		this.start = pos;
		return (T) this;
	}
	
	public <T extends RandomAccessFileInputStream> T setEnd(long pos) 
	{
		this.end = pos;
		return (T) this;
	}

	@Override
	public int read() throws IOException
	{
		if (file.getFilePointer() >= end)
			return -1;
		
		return file.readUnsignedByte();
	}
	
	@Override
	public int available() throws IOException
	{
		return (int) end - (int) file.getFilePointer();
	}
	
	@Override
	public synchronized void mark(int readlimit)
	{
		this.markedPos = readlimit;
	}
	
	@Override
	public synchronized void reset() throws IOException
	{
		file.seek(markedPos);
	}
	
	@Override
	public boolean markSupported()
	{
		return true;
	}
	
	@Override
	public void close() throws IOException
	{
		file.close();
	}

}
