package ru.swayfarer.swl2.asm.transformer.informated;

import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.transformer.basic.AbstractAsmTransformer;
import ru.swayfarer.swl2.asm.transformer.informated.visitor.ClassScanner;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassReader;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassWriter;

/**
 * Сканнер классов, который создает {@link ClassInfo} для своей цели
 * @author swayfarer
 *
 */
public class ClassScannerTransformer extends AbstractAsmTransformer{

	/** Настраиваемая информация о классе */
	@InternalElement
	public ClassInfo info;
	
	@Override
	public void transform(String name, byte[] bytes, ClassReader reader, ClassWriter writer)
	{
		ClassScanner scan = new ClassScanner(writer);
		acceptCV(reader, bytes, scan);
		info = scan.info;
	}
	
	/** Получить настроенную информацию */
	public ClassInfo getInfo()
	{
		return info;
	}

}
