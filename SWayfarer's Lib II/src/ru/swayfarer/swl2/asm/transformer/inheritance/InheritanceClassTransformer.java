package ru.swayfarer.swl2.asm.transformer.inheritance;

import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.transformer.exists.ExistsConditionContainer;
import ru.swayfarer.swl2.asm.transformer.informated.InformatedClassTransformer;
import ru.swayfarer.swl2.asm.transformer.inheritance.visitor.InheritanceClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassReader;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassWriter;

public class InheritanceClassTransformer extends InformatedClassTransformer{

	public ExistsConditionContainer conditionContainer;
	
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
