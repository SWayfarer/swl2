package ru.swayfarer.swl2.asm.transformer.exists;

import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.transformer.exists.visitor.ExistsClassVisitor;
import ru.swayfarer.swl2.asm.transformer.informated.InformatedClassTransformer;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassReader;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassWriter;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;

@SuppressWarnings("unchecked")
public class ExistsClassTransformer extends InformatedClassTransformer {

	public static final String ANNOTATION_DESC = Type.getType(ExistsIf.class).getDescriptor();
	
	public ExistsConditionContainer conditions = new ExistsConditionContainer();
	
	@Override
	public void transform(String name, byte[] bytes, ClassReader reader, ClassWriter writer, ClassInfo info)
	{
		acceptCV(reader, bytes, new ExistsClassVisitor(writer, info, conditions));
	}

	public <ExistsClassTransformer_Type extends ExistsClassTransformer> ExistsClassTransformer_Type setCondition(String name, boolean value)
	{
		conditions.setCondition(name, value);
		
		return (ExistsClassTransformer_Type) this;
	}
	
	public <ExistsClassTransformer_Type extends ExistsClassTransformer> ExistsClassTransformer_Type setConditions(ExistsConditionContainer conditions)
	{
		if (conditions == null)
			conditions = new ExistsConditionContainer();
		
		this.conditions = conditions;
		
		return (ExistsClassTransformer_Type) this;
	}
}
