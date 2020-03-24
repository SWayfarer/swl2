package ru.swayfarer.swl2.resource.streams;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class RandomAccessFileOutputStream extends OutputStream {

	public RandomAccessFile randomAccessFile;
	
	public RandomAccessFileOutputStream(RandomAccessFile randomAccessFile)
	{
		super();
		this.randomAccessFile = randomAccessFile;
	}

	@Override
	public void write(int b) throws IOException
	{
		randomAccessFile.write(b);
	}
	
	@Override
	public void close() throws IOException
	{
		randomAccessFile.close();
	}

}
