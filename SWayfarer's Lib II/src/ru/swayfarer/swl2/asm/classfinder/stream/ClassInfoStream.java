package ru.swayfarer.swl2.asm.classfinder.stream;

import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.collections.streams.DataStream;
import ru.swayfarer.swl2.collections.streams.IDataStream;

@SuppressWarnings("unchecked")
public class ClassInfoStream extends DataStream<ClassInfo> implements IClassInfoStream {

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
