package ru.swayfarer.swl2.asm.transformer.remapper;

import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.transformer.informated.InformatedClassTransformer;
import ru.swayfarer.swl2.asm.transformer.remapper.visitor.RemapperClassScannerVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassReader;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassWriter;

public class RemapperClassScanner extends InformatedClassTransformer {

	public RemapInfo remapInfo;
	
	public RemapperClassScanner() 
	{
		remapInfo = new RemapInfo();
	}
	
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
