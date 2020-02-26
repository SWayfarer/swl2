package ru.swayfarer.swl2.asm.transformer.patcher;

import java.util.ArrayList;
import java.util.List;

import ru.swayfarer.swl2.asm.TransformedClassInfo;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.transformer.informated.InformatedClassTransformer;
import ru.swayfarer.swl2.asm.transformer.patcher.visitor.PatcherClassVisitor;
import ru.swayfarer.swl2.resource.rlink.RLUtils;
import ru.swayfarer.swl2.version.SimpleVersion;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassReader;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassWriter;

@SuppressWarnings("unchecked")
public class PatcherClassTransformer extends InformatedClassTransformer {

	public SimpleVersion version;
	
	public List<PatchInfo> patches = new ArrayList<>();
	
	@Override public void transform(String name, byte[] bytes, ClassReader reader, ClassWriter writer, ClassInfo info)
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
			acceptCV(reader, bytes, new PatcherClassVisitor(writer, info, version));
		}
	}
	
	public <PatcherClassTransformer_Type extends PatcherClassTransformer> PatcherClassTransformer_Type addPatch(String classInternalName, byte[] patchBytes)
	{
		PatchInfo info = new PatchInfo();
		info.bytes = patchBytes;
		info.className = classInternalName;
		patches.add(info);
		
		return (PatcherClassTransformer_Type) this;
	}
	
	public void applyPatch(byte[] patchBytes, ClassWriter writer)
	{
		ClassInfo clInfo = ClassInfo.valueOf(patchBytes);
		ClassReader patchReader = createClassReader(patchBytes);
		acceptCV(patchReader, patchBytes, new PatcherClassVisitor(writer, clInfo, version));
	}
	
	public static byte[] takeSnapshoot(Class<?> cl)
	{
		String link = "/"+cl.getName().replace(".", "/")+".class";
		return takeSnapshoot(RLUtils.toBytes(link));
	}
	
	public static byte[] takeSnapshoot(byte[] bytes)
	{
		PatcherClassTransformer transformer = new PatcherClassTransformer();
		TransformedClassInfo transformedClassInfo = new TransformedClassInfo();
		
		return transformer.transform("", bytes, transformedClassInfo);
	}
}
