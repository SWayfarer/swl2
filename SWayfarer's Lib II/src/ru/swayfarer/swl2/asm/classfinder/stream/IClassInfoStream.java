package ru.swayfarer.swl2.asm.classfinder.stream;

import java.lang.reflect.Modifier;

import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.streams.IDataStream;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;

@SuppressWarnings("unchecked")
public interface IClassInfoStream extends IDataStream<ClassInfo> {

	default <T extends IClassInfoStream> T annotated(String desc)
	{
		return (T) filter((info) -> info.hasAnnotation(desc));
	}
	
	default <T extends IClassInfoStream> T annotated(Class<?> annotationClass)
	{
		return annotated(Type.getDescriptor(annotationClass));
	}
	
	default <T extends IClassInfoStream> T implement(String internalName)
	{
		return (T) filter((info) -> info.hasInterface(internalName));
	}
	
	default <T extends IClassInfoStream> T pulblics()
	{
		return (T) filter((info) -> Modifier.isPublic(info.access));
	}
	
	default <T extends IClassInfoStream> T privates()
	{
		return (T) filter((info) -> Modifier.isPrivate(info.access));
	}
	
	default <T extends IClassInfoStream> T protecteds()
	{
		return (T) filter((info) -> Modifier.isProtected(info.access));
	}
	
	default <T extends IClassInfoStream> T finals()
	{
		return (T) filter((info) -> Modifier.isFinal(info.access));
	}
	
	default <T extends IClassInfoStream> T abstracts()
	{
		return (T) filter((info) -> Modifier.isAbstract(info.access));
	}
	
	default <T extends IClassInfoStream> T interfaces()
	{
		return (T) filter((info) -> Modifier.isInterface(info.access));
	}
	
	default <T extends IClassInfoStream> T withModifiers(int... modifiers)
	{
		return (T) filter((info) -> {
			
			for (int modifier : modifiers)
			{
				if ((info.access & modifier) == 0)
				{
					return false;
				}
			}
			
			return true;
			
		});
	}
	
	default <T extends IClassInfoStream> T implement(Class<?> annotationClass)
	{
		return implement(Type.getInternalName(annotationClass));
	}
	
	default <T extends IClassInfoStream> T extend(String internalName)
	{
		return (T) filter((info) -> info.superName.equals(internalName));
	}
	
	default <T extends IClassInfoStream> T extend(Class<?> annotationClass)
	{
		return extend(Type.getInternalName(annotationClass));
	}
	
	default <T extends IDataStream<Class<?>>> T loadClasses()
	{
		return mapped((info) -> {
			return ReflectionUtils.findClass(info.getCanonicalName());
		});
	}
}
