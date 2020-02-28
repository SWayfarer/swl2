package ru.swayfarer.swl2.asm.transformer.patcher;

import java.util.List;

import ru.swayfarer.swl2.asm.TransformedClassInfo;
import ru.swayfarer.swl2.asm.transformer.basic.AbstractAsmTransformer;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;

public class PatcherInJarScanner extends AbstractAsmTransformer {

	/** Класс-трансформер, который ищет патчи */
	public PatcherClassTransformer patcherClassTransformer = new PatcherClassTransformer();
	
	/** Найденные патчи */
	public IExtendedList<PatchInfo> foundPatches = CollectionsSWL.createExtendedList();
	
	@Override
	public byte[] transform(String name, byte[] bytes, TransformedClassInfo info)
	{
		byte[] ret = patcherClassTransformer.transform(name, bytes, info);
		
		PatchInfo patchInfo = new PatchInfo();
		patchInfo.bytes = ret;
		patchInfo.className = name;
		foundPatches.add(patchInfo);
		
		return ret;
	}
	
	/** Получить найденные патчи */
	public List<PatchInfo> getFoundPatches()
	{
		return foundPatches.copy();
	}
	
}
