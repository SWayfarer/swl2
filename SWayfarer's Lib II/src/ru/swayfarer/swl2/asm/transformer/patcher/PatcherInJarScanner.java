package ru.swayfarer.swl2.asm.transformer.patcher;

import java.util.ArrayList;
import java.util.List;

import ru.swayfarer.swl2.asm.TransformedClassInfo;
import ru.swayfarer.swl2.asm.transformer.basic.AbstractAsmTransformer;

public class PatcherInJarScanner extends AbstractAsmTransformer {

	public PatcherClassTransformer patcherClassTransformer = new PatcherClassTransformer();
	public List<PatchInfo> foundPatches = new ArrayList<>();
	
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
	
	public List<PatchInfo> getFoundPatches()
	{
		return foundPatches;
	}
	
}
