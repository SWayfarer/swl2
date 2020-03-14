package ru.swayfarer.swl2.swconf.serialization.writer;

import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.swconf.format.SwconfFormat;
import ru.swayfarer.swl2.swconf.primitives.SwconfArray;
import ru.swayfarer.swl2.swconf.primitives.SwconfBoolean;
import ru.swayfarer.swl2.swconf.primitives.SwconfNum;
import ru.swayfarer.swl2.swconf.primitives.SwconfObject;
import ru.swayfarer.swl2.swconf.primitives.SwconfPrimitive;
import ru.swayfarer.swl2.swconf.primitives.SwconfString;

/**
 * Писалка Swconf
 * @author swayfarer
 *
 */
public interface ISwconfWriter {

	/** Записать примитив */
	public void write(SwconfPrimitive primitive);

	/** Записать строку */
	public void write(SwconfString str);

	/** Записать булеанку */
	public void write(SwconfBoolean bool);

	/** Записать число */
	public void write(SwconfNum num);

	/** Записать массив */
	public void write(SwconfArray array);

	/** Записать объкет */
	public void write(SwconfObject object);

	/** Сбросить к изначальному состоянию */
	public <T extends ISwconfWriter> T reset();

	/** Начать запись */
	public <T extends ISwconfWriter> T startWriting();

	/** Завершить запись */
	public <T extends ISwconfWriter> T endWriting();

	/** Записать границу литерала */
	public void writeLiteral();

	/** Записать знак равенства */
	public void writeEqual();

	/** Записать начало массива */
	public void writeArrayStart();

	/** Записать конец массива */
	public void writeArrayEnd();

	/** Записать начало блока */
	public void writeBlockStart();

	/** Записать конец блока */
	public void writeBlockEnd();

	/** Записать разделитель элементов */
	public void writeSplitter();

	/** Получить строку, записанную этим райтером */
	public String toSwconfString();

	/** Записать начало исключения */
	public void writeExclusionStart();

	/** Записать конец исключения */
	public void writeExclusionEnd();
	
	/** Записать сырую строку */
	public void writeRaw(@ConcattedString Object... text);
	
	/** Записать текст в виде исключения */
	public void writeExclusion(String comment);
	
	/** Записать имя */
	public void writeName(String name);
	
	/** Задать родителя */
	public <T extends ISwconfWriter> T setParent(ISwconfWriter writer);
	
	/** Задать формат */
	public <T extends ISwconfWriter> T setFormat(SwconfFormat format);

}