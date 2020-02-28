package ru.swayfarer.swl2.asm.transformer.patcher;

/**
 * Информация о патче
 * @author swayfarer
 */
public class PatchInfo {

	/** Имя целевого класса */
	public String className;
	
	/** Байты патча */
	public byte[] bytes;
	
	/** Применим ли к классу? */
	public boolean isAvaliableFor(String classInternalName)
	{
		return className.equals(classInternalName);
	}
	
	/** Получить байты патча */
	public byte[] getBytes()
	{
		return bytes.clone();
	}
	
}
