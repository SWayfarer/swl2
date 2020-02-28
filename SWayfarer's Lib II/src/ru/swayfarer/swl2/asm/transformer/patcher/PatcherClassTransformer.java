package ru.swayfarer.swl2.asm.transformer.patcher;

import ru.swayfarer.swl2.asm.TransformedClassInfo;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.transformer.exists.ExistsConditionContainer;
import ru.swayfarer.swl2.asm.transformer.informated.InformatedClassTransformer;
import ru.swayfarer.swl2.asm.transformer.patcher.visitor.PatcherClassVisitor;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.resource.rlink.RLUtils;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassReader;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassWriter;

/** 
 * Класс-транформер для применения/поиска патчей 
 * <br> Если {@link PatcherClassTransformer#patches} будет null или пустым, то будет включен режим поиска
 * <br> Иначе будет включен режим записи патчей 
 * @author swayfarer
 */
@SuppressWarnings("unchecked")
public class PatcherClassTransformer extends InformatedClassTransformer {

	/** Условия, в зависимости от которых будут применяться патчи */
	@InternalElement
	public ExistsConditionContainer conditions;
	
	/** Патчи */
	@InternalElement
	public IExtendedList<PatchInfo> patches = CollectionsSWL.createExtendedList();
	
	@Override 
	public void transform(String name, byte[] bytes, ClassReader reader, ClassWriter writer, ClassInfo info)
	{
		if (patches != null && !patches.isEmpty())
		{
			for (PatchInfo patchInfo : patches)
			{
				if (patchInfo.isAvaliableFor(name))
					applyPatch(patchInfo.getBytes(), writer);
			}
			
			acceptCV(reader, bytes, writer);
		}
		else
		{
			acceptCV(reader, bytes, new PatcherClassVisitor(writer, info, conditions));
		}
	}
	
	/** Зарегистрировать патч */
	public <T extends PatcherClassTransformer> T addPatch(String classInternalName, byte[] patchBytes)
	{
		PatchInfo info = new PatchInfo();
		info.bytes = patchBytes;
		info.className = classInternalName;
		patches.add(info);
		
		return (T) this;
	}
	
	/** Применить патч */
	public void applyPatch(byte[] patchBytes, ClassWriter writer)
	{
		ClassInfo clInfo = ClassInfo.valueOf(patchBytes);
		ClassReader patchReader = createClassReader(patchBytes);
		acceptCV(patchReader, patchBytes, new PatcherClassVisitor(writer, clInfo, conditions));
	}
	
	/** Сдедать снимок класса с примененными патчами */
	public static byte[] takeSnapshoot(Class<?> cl)
	{
		String link = "/"+cl.getName().replace(".", "/")+".class";
		return takeSnapshoot(RLUtils.toBytes(link));
	}
	
	/** Сдедать снимок класса с примененными патчами */
	public static byte[] takeSnapshoot(byte[] bytes)
	{
		PatcherClassTransformer transformer = new PatcherClassTransformer();
		TransformedClassInfo transformedClassInfo = new TransformedClassInfo();
		
		return transformer.transform("", bytes, transformedClassInfo);
	}
}
