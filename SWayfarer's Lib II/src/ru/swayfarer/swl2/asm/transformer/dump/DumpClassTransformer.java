package ru.swayfarer.swl2.asm.transformer.dump;

import ru.swayfarer.swl2.asm.TransformedClassInfo;
import ru.swayfarer.swl2.asm.transformer.basic.AbstractAsmTransformer;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Класс-трансформер, который ничего не трансформирует, но пользуется положением, чтобы дампить нужные классы
 * @author swayfarer
 *
 */
public class DumpClassTransformer extends AbstractAsmTransformer{

	/** Маска имени класса */
	@InternalElement
	public String nameMask;
	
	/** Папка, в которую буду складываться дампы */
	public FileSWL dumpDir;
	
	/** Конструктор */
	public DumpClassTransformer(String nameMask, String dumpDir)
	{
		this(nameMask, new FileSWL(dumpDir));
	}
	
	/** Конструктор */
	public DumpClassTransformer(String nameMask, FileSWL dumpDir)
	{
		super();
		if (StringUtils.isEmpty(nameMask))
			nameMask = "*";
		
		this.nameMask = nameMask;
		
		if (dumpDir == null)
			dumpDir = new FileSWL("dumped_classes");
		
		this.dumpDir = dumpDir;
		dumpDir.createIfNotFoundDir();
	}

	@Override
	public byte[] transform(String name, byte[] bytes, TransformedClassInfo info)
	{
		if (StringUtils.isMatchesByMask(nameMask, name))
		{
			new FileSWL(dumpDir.getAbsolutePath(), name.replace(".", "/")+".class").createIfNotFoundSafe().setData(bytes);
		}
		
		return bytes;
	}
	
}
