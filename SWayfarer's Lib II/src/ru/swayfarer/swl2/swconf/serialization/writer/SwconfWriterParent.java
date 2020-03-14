package ru.swayfarer.swl2.swconf.serialization.writer;

import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.swconf.format.SwconfFormat;
import ru.swayfarer.swl2.swconf.primitives.SwconfArray;
import ru.swayfarer.swl2.swconf.primitives.SwconfBoolean;
import ru.swayfarer.swl2.swconf.primitives.SwconfNum;
import ru.swayfarer.swl2.swconf.primitives.SwconfObject;
import ru.swayfarer.swl2.swconf.primitives.SwconfPrimitive;
import ru.swayfarer.swl2.swconf.primitives.SwconfString;

/**
 * Обертка на {@link ISwconfWriter}, позволяющая использовать функционал обернутого {@link ISwconfWriter}'а, 
 * добавляя ему свой без наследования 
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class SwconfWriterParent implements ISwconfWriter {

	/** Родительский {@link ISwconfWriter} */
	@InternalElement
	public ISwconfWriter parent = this;
	
	/** Обернутый {@link ISwconfWriter} */
	@InternalElement
	public ISwconfWriter wrappedWriter;
	
	/** Конструктор */
	public SwconfWriterParent(ISwconfWriter wrappedWriter)
	{
		super();
		ExceptionsUtils.IfNullArg(wrappedWriter, "Wrapped writer can't be null!");
		this.wrappedWriter = wrappedWriter;
		this.wrappedWriter.setParent(this);
	}

	@Override
	public void write(SwconfPrimitive primitive)
	{
		wrappedWriter.write(primitive);
	}

	@Override
	public void write(SwconfString str)
	{
		wrappedWriter.write(str);
	}

	@Override
	public void write(SwconfBoolean bool)
	{
		wrappedWriter.write(bool);
	}

	@Override
	public void write(SwconfNum num)
	{
		wrappedWriter.write(num);
	}

	@Override
	public void write(SwconfArray array)
	{
		wrappedWriter.write(array);
	}

	@Override
	public void write(SwconfObject object)
	{
		wrappedWriter.write(object);
	}

	@Override
	public void writeLiteral()
	{
		wrappedWriter.writeLiteral();
	}

	@Override
	public void writeEqual()
	{
		wrappedWriter.writeEqual();
	}

	@Override
	public void writeArrayStart()
	{
		wrappedWriter.writeArrayStart();
	}

	@Override
	public void writeArrayEnd()
	{
		wrappedWriter.writeArrayEnd();
	}

	@Override
	public void writeBlockStart()
	{
		wrappedWriter.writeBlockStart();
	}

	@Override
	public void writeBlockEnd()
	{
		wrappedWriter.writeBlockEnd();
	}

	@Override
	public void writeSplitter()
	{
		wrappedWriter.writeSplitter();
	}

	@Override
	public String toSwconfString()
	{
		return wrappedWriter.toSwconfString();
	}

	@Override
	public void writeExclusionStart()
	{
		wrappedWriter.writeExclusionStart();
	}

	@Override
	public void writeExclusionEnd()
	{
		wrappedWriter.writeExclusionEnd();
	}

	@Override
	public <T extends ISwconfWriter> T reset()
	{
		wrappedWriter = wrappedWriter.reset();
		return (T) this;
	}

	@Override
	public <T extends ISwconfWriter> T startWriting()
	{
		wrappedWriter = wrappedWriter.startWriting();
		return (T) this;
	}

	@Override
	public <T extends ISwconfWriter> T endWriting()
	{
		wrappedWriter = wrappedWriter.endWriting();
		return (T) this;
	}

	@Override
	public void writeRaw(@ConcattedString Object... text)
	{
		wrappedWriter.writeRaw(text);
	}

	@Override
	public <T extends ISwconfWriter> T setParent(ISwconfWriter writer)
	{
		parent = writer;
		return (T) this;
	}

	@Override
	public void writeExclusion(String comment)
	{
		wrappedWriter.writeExclusion(comment);
	}

	@Override
	public <T extends ISwconfWriter> T setFormat(SwconfFormat format)
	{
		wrappedWriter.setFormat(format);
		return (T) this;
	}

	@Override
	public void writeName(String name)
	{
		wrappedWriter.writeRaw(name);
	}

}
