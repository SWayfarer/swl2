package ru.swayfarer.swl2.asm.classfinder.stream;

import java.lang.reflect.Modifier;

import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.streams.IDataStream;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;

/**
 * Поток информации о классах <br>
 * {@link IDataStream} для {@link ClassInfo} 
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public interface IClassInfoStream extends IDataStream<ClassInfo> {

	/**
	 * Оставить только классы, отмеченные аннотацией
	 * @param desc Дескриптор искомой аннотации
	 * @return Отфильтрованный поток
	 */
	default <T extends IClassInfoStream> T annotated(String desc)
	{
		return (T) filter((info) -> info.hasAnnotation(desc));
	}
	
	/**
	 * Оставить только классы, отмеченные аннотацией
	 * @param annotationClass Искомая аннотация
	 * @return Отфильтрованный поток
	 */
	default <T extends IClassInfoStream> T annotated(Class<?> annotationClass)
	{
		return annotated(Type.getDescriptor(annotationClass));
	}
	
	/**
	 * Оставить только классы, имплементирующие интерфейс
	 * @param internalName Искомый интерфейс
	 * @return Отфильтрованный поток
	 */
	default <T extends IClassInfoStream> T implement(String internalName)
	{
		return (T) filter((info) -> info.hasInterface(internalName));
	}
	
	/**
	 * Оставить только классы, имплементирующие интерфейс
	 * @param interfaceClass Искомый интерфейс
	 * @return Отфильтрованный поток
	 */
	default <T extends IClassInfoStream> T implement(Class<?> interfaceClass)
	{
		return implement(Type.getInternalName(interfaceClass));
	}
	
	/**
	 * Оставить только публичные классы
	 * @return Отфильтрованный поток
	 */
	default <T extends IClassInfoStream> T pulblics()
	{
		return (T) filter((info) -> Modifier.isPublic(info.access));
	}
	
	/**
	 * Оставить только приватные классы
	 * @return Отфильтрованный поток
	 */
	default <T extends IClassInfoStream> T privates()
	{
		return (T) filter((info) -> Modifier.isPrivate(info.access));
	}
	
	/**
	 * Оставить только защищенные классы
	 * @return Отфильтрованный поток
	 */
	default <T extends IClassInfoStream> T protecteds()
	{
		return (T) filter((info) -> Modifier.isProtected(info.access));
	}
	
	/**
	 * Оставить только финальные классы
	 * @return Отфильтрованный поток
	 */
	default <T extends IClassInfoStream> T finals()
	{
		return (T) filter((info) -> Modifier.isFinal(info.access));
	}
	
	/**
	 * Оставить только абстрактные классы
	 * @return Отфильтрованный поток
	 */
	default <T extends IClassInfoStream> T abstracts()
	{
		return (T) filter((info) -> Modifier.isAbstract(info.access));
	}
	
	/**
	 * Оставить только интерфейсы
	 * @return Отфильтрованный поток
	 */
	default <T extends IClassInfoStream> T interfaces()
	{
		return (T) filter((info) -> Modifier.isInterface(info.access));
	}
	
	/**
	 * Оставить только классы, имеющие все указанные модификаторы
	 * @param modifiers Искомые модификаторы. Например, {@link Modifier#PUBLIC} или {@link Modifier#FINAL}
	 * @return Отфильтрованный поток
	 */
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
	
	/**
	 * Оставить только классы, наследующие указанный
	 * @param internalName Класс-родитель искомых классов
	 * @return Отфильтрованный поток
	 */
	default <T extends IClassInfoStream> T extend(String internalName)
	{
		return (T) filter((info) -> info.superName.equals(internalName));
	}
	
	/**
	 * Оставить только классы, наследующие указанный
	 * @param annotationClass Класс-родитель искомых классов
	 * @return Отфильтрованный поток
	 */
	default <T extends IClassInfoStream> T extend(Class<?> annotationClass)
	{
		return extend(Type.getInternalName(annotationClass));
	}
	
	/**
	 * Получить поток загруженных {@link Class}'ов. <br>
	 * До этого этапа вся работа происходила с информацией, полученной из байткода без загрузки.
	 * @return Поток загруженных классов.
	 */
	default <T extends IDataStream<Class<?>>> T loadClasses()
	{
		return mapped((info) -> {
			return ReflectionUtils.findClass(info.getCanonicalName());
		});
	}
}
