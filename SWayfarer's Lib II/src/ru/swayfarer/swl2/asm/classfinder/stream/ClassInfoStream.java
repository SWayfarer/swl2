package ru.swayfarer.swl2.asm.classfinder.stream;

import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.collections.streams.DataStream;
import ru.swayfarer.swl2.collections.streams.IDataStream;

/**
 * Поток информации о классах <br>
 * {@link IDataStream} для {@link ClassInfo} 
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class ClassInfoStream extends DataStream<ClassInfo> implements IClassInfoStream {

	/** 
	 * Конструктор
	 * @param elements Элементы потока
	 */
	public ClassInfoStream(IExtendedList<ClassInfo> elements)
	{
		super(elements);
	}
	
	@Override
	public <E, T extends IDataStream<E>> T wrap(IExtendedList<E> list)
	{
		return (T) new ClassInfoStream(ReflectionUtils.forceCast(list)).setParrallel(isParallel);
	}

}
