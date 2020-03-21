package ru.swayfarer.swl2.asm.transformer.accesstransformer;

import ru.swayfarer.swl2.asm.transformer.accesstransformer.visitor.AccessVisitor;
import ru.swayfarer.swl2.asm.transformer.basic.AbstractAsmTransformer;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassReader;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassWriter;

/**
 * Трансформер доступа к содержимому классов. 
 * <h1>Применение:</h1> 
 * При использовании на классе делает публичными все его поля и методы, а также снимает метки final там, где они расставлены.
 * @author swayfarer
 *
 */
public class AccessOpenClassTransformer extends AbstractAsmTransformer{

	/** Фильтр */
	@InternalElement
	public IFunction1<String, Boolean> filter;
	
	/** Конструктор */
	public AccessOpenClassTransformer(IFunction1<String, Boolean> filter)
	{
		this.filter = filter;
	}
	
	@Override
	public void transform(String name, byte[] bytes, ClassReader reader, ClassWriter writer)
	{
		ClassVisitor cv = writer;
		
		cv = new AccessVisitor(writer);
		
		acceptCV(reader, bytes, cv);
	}
}
