package ru.swayfarer.swl2.asm.transformer.patcher;

public class PatchInfo {

	public String className;
	public byte[] bytes;
	
	public boolean isAvaliableFor(String classInternalName)
	{
		return className.equals(classInternalName);
	}
	
	public byte[] getBytes()
	{
		return bytes;
	}
	
}
