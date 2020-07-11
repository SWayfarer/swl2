package ru.swayfarer.swl2.asm.transformer.ditransformer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.transformer.ditransformer.regetter.visitor.DIDynamicClassVisitor;
import ru.swayfarer.swl2.asm.transformer.ditransformer.visitor.DIClassVisitor;
import ru.swayfarer.swl2.asm.transformer.informated.InformatedClassTransformer;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.collections.streams.DataStream;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassReader;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassWriter;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;

/**
 * Класс-трансформер, осуществляющий работу автоматичекой иньекции зависимостей
 * 
 * @author swayfarer
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@SuppressWarnings("unchecked")
public class DIClassTransformer extends InformatedClassTransformer {

	
	/** Имя класса, к котрому поля будут обращаться за контекстом */
	public String contextClassInternalName;
	
	/** Имя класса, через который будет работать динамический DI */
	public String dynamicClassInternalName;
	
	/** Имя класса, к котрому поля будут обращаться за контекстом */
	public String injectionMethodName = "injectContextElements";
	
	/** Имя класса, к котрому поля будут обращаться за контекстом */
	public String dynamicInjectionMethodName = "getContextElementValue";
	
	public boolean isLoggingBytecodeInjections = false;
	
	public IExtendedList<String> diAnnotationsDescs = CollectionsSWL.createExtendedList();
	
	public IExtendedList<String> dynamicAnnotationsDescs = CollectionsSWL.createExtendedList();
	
	public DIClassTransformer() {}
	
	public <T extends DIClassTransformer> T enableDynamicInjections(Class<?> diClass, Class<?>... classes)
	{
		dynamicClassInternalName = Type.getInternalName(diClass);
		
		dynamicAnnotationsDescs = DataStream.of(classes)
				.map((e) -> Type.getDescriptor(e))
		.toList();
		
		return (T) this;
	}
	
	public <T extends DIClassTransformer> T enableConstructorInjections(Class<?> diClass, Class<?>... classes)
	{
		contextClassInternalName = Type.getInternalName(diClass);
		
		diAnnotationsDescs = DataStream.of(classes)
				.map((e) -> Type.getDescriptor(e))
		.toList();
		
		return (T) this;
	}
	
	@Override
	public void transform(String name, byte[] bytes, ClassReader reader, ClassWriter writer, ClassInfo info)
	{
		ClassVisitor cv = writer;
		
		if (!StringUtils.isBlank(diAnnotationsDescs))
		{
			if (info.fields.stream().anyMatch((field) -> diAnnotationsDescs.stream().anyMatch((desc) -> field.findAnnotationRec(desc) != null)))
			{
				cv = new DIClassVisitor(this, cv, info);
			}
		}
		
		if (!StringUtils.isBlank(dynamicClassInternalName))
			cv = new DIDynamicClassVisitor(this, cv, info);
		
		acceptCV(reader, bytes, cv);
	}
	
}
