/*
 * RandomAccessFileSeekableSource.java
 *
 * Created on May 17, 2006, 1:45 PM
 * Copyright (c) 2006 Heiko Klein
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 *
 *
 */

package ru.swayfarer.swl2.z.dependencies.com.nothome.delta;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

import ru.swayfarer.swl2.exceptions.ExceptionsUtils;

/**
 * Wraps a random access file.
 */
@SuppressWarnings("unchecked")
public class RandomAccessFileSeekableSource implements SeekableSource {
    
	public int offset = 0;
    private RandomAccessFile raf;

    /**
     * Constructs a new RandomAccessFileSeekableSource.
     * @param raf
     */
    public RandomAccessFileSeekableSource(RandomAccessFile raf) {
        if (raf == null)
            throw new NullPointerException("raf");
        this.raf = raf;
    }

    public <T extends RandomAccessFileSeekableSource> T setOffset(int offset)
	{
		this.offset = offset;
		
		ExceptionsUtils.safe(() -> raf.seek(offset));
		
		return (T) this;
	}
    
    public void seek(long pos) throws IOException {
        raf.seek(pos + offset);
    }

    public int read(byte[] b, int off, int len) throws IOException {
        return raf.read(b, off + offset, len + offset);
    }

    public long length() throws IOException {
        return raf.length() - offset;
    }

    public void close() throws IOException {
        raf.close();
    }

    public int read(ByteBuffer bb) throws IOException {
        int c = raf.read(bb.array(), bb.position(), bb.remaining());
        if (c == -1)
            return -1;
        bb.position(bb.position() + c);
        return c;
    }
    
}
