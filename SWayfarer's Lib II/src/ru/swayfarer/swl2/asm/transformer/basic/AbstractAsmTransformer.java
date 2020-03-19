package ru.swayfarer.swl2.asm.transformer.basic;

import ru.swayfarer.swl2.asm.IClassTransformer;
import ru.swayfarer.swl2.asm.TransformedClassInfo;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassReader;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassWriter;

/**
 * Абстрактный ASM-Преобразователь Классов (На уровне байткода)
 * @author swayfarer
 *
 */
public abstract class AbstractAsmTransformer implements IClassTransformer {

	/** Логгер */
	@InternalElement
	public ILogger logger = LoggingManager.getLogger();

	/**
	 * Преобразовать класс
	 * @param name Каноничное имя класса. То, под которым его знает класслоадер
	 * @param bytes Байты, которые будут трасформированы
	 * @param reader Читалка классов, которая аццептит визиторов
	 * @param writer Записывалка классов, которую нужно передавать в свои визиторы
	 * @param info Информация о трансформациях класса
	 * @return трансформированные байты класса, из которых он будет загружен
	 */
	public void transform(String name, byte[] bytes, ClassReader reader, ClassWriter writer, TransformedClassInfo info)
	{
		transform(name, bytes, reader, writer);
	}

	/**
	 * Преобразовать класс
	 * @param name Каноничное имя класса. То, под которым его знает класслоадер
	 * @param bytes Байты, которые будут трасформированы
	 * @param reader Читалка классов, которая аццептит визиторов
	 * @param writer Записывалка классов, которую нужно передавать в свои визиторы
	 * @return трансформированные байты класса, из которых он будет загружен
	 */
	public void transform(String name, byte[] bytes, ClassReader reader, ClassWriter writer) {}


	@Override
	public byte[] transform(String name, byte[] bytes, TransformedClassInfo info)
	{
		try
		{
			ClassReader reader = createClassReader(bytes);
			ClassWriter writer = createClassWriter(reader, bytes);

			transform(name, bytes, reader, writer, info);

			byte[] ret = writer.toByteArray();

			return ret;
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while transforming class", name, "by", this);
		}

		return bytes;
	}

	/** Создать читалку классов */
	protected ClassReader createClassReader(byte[] bytecode)
	{
		return new ClassReader(bytecode);
	}

	/** Создать писалку классов */
	protected ClassWriter createClassWriter(ClassReader reader, int flags)
	{
		return new ClassWriter(reader, flags);
	}

	/** Применить визитора к читалке */
	protected void acceptCV(ClassReader reader, byte[] bytes, ClassVisitor visit)
	{
		reader.accept(visit, isJava7(bytes) ? ClassReader.SKIP_FRAMES : ClassReader.EXPAND_FRAMES);
	}

	/** Создать писалку классов на основе читалки и байтов класса */
	protected ClassWriter createClassWriter(ClassReader reader, byte[] bytes)
	{
		return new ClassWriter(reader, isJava7(bytes) ? ClassWriter.COMPUTE_MAXS : ClassWriter.COMPUTE_FRAMES);
	}

	/** Является ли класс классом Java7? */
	protected boolean isJava7(byte[] bytes)
	{
		return ((((bytes[6] & 0xFF) << 8) | (bytes[7] & 0xFF)) > 50);
	}

}
