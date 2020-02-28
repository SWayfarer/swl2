package ru.swayfarer.swl2.asm.transformer.inheritance;

import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.transformer.exists.ExistsConditionContainer;
import ru.swayfarer.swl2.asm.transformer.informated.InformatedClassTransformer;
import ru.swayfarer.swl2.asm.transformer.inheritance.visitor.InheritanceClassVisitor;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassReader;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassWriter;

/**
 * Класс-трансформер, обсеспечивающий работоспособоность аннотации {@link InheritsIf} 
 * @author swayfarer
 *
 */
public class InheritanceClassTransformer extends InformatedClassTransformer{

	/** Контейнер с условиями, по которым будет определеться наследование */
	@InternalElement
	public ExistsConditionContainer conditionContainer;
	
	/**
	 *  Конструтор 
	 *  @param conditionContainer {@link #conditionContainer}
	 */
	public InheritanceClassTransformer(ExistsConditionContainer conditionContainer)
	{
		super();
		this.conditionContainer = conditionContainer;
	}

	@Override
	public void transform(String name, byte[] bytes, ClassReader reader, ClassWriter writer, ClassInfo info)
	{
		acceptCV(reader, bytes, new InheritanceClassVisitor(writer, info, conditionContainer));
	}
	
}
