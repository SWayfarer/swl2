package ru.swayfarer.swl2.asm.transformer.remapper;

import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.transformer.informated.InformatedClassTransformer;
import ru.swayfarer.swl2.asm.transformer.remapper.visitor.RemapperClassScannerVisitor;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassReader;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassWriter;

/**
 * Сканнер аннотаций {@link RemapAsm} 
 * Находит информацию о ремапах и сохраняет ее в {@link #remapInfo}
 * @author swayfarer
 *
 */
public class RemapperClassScanner extends InformatedClassTransformer {

	/** Информация о ремаппин*/
	@InternalElement
	public RemapInfo remapInfo;
	
	/** Конструктор */
	public RemapperClassScanner() 
	{
		remapInfo = new RemapInfo();
	}
	
	/** Конструктор */
	public RemapperClassScanner(RemapInfo remapInfo)
	{
		super();
		this.remapInfo = remapInfo;
	}

	@Override
	public void transform(String name, byte[] bytes, ClassReader reader, ClassWriter writer, ClassInfo info)
	{
		acceptCV(reader, bytes, new RemapperClassScannerVisitor(writer, info, remapInfo));
	}

}
