package ru.swayfarer.swl2.asm.injar;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import ru.swayfarer.swl2.asm.IClassTransformer;
import ru.swayfarer.swl2.asm.TransformedClassInfo;
import ru.swayfarer.swl2.asm.transfomer.injection.InjectionAsm;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.reference.IReference;
import ru.swayfarer.swl2.reference.SimpleReference;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.resource.pathtransformers.PathTransforms;
import ru.swayfarer.swl2.resource.rlink.RLUtils;
import ru.swayfarer.swl2.resource.streams.StreamsUtils;

/**
 * Преобразователь Jar-файлов. Служит для трансформации классов внутри существующего Jar посредством {@link IClassTransformer}'ов и {@link IZipTransformer}'ов
 * @author User
 *
 */
public class InJarTransformer {

	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Записывать ли изменения в jar? */
	public boolean isWriting = false;
	
	/** Преобразователи классов */
	@InternalElement
	public List<IClassTransformer> classTransformers = new ArrayList<>();
	
	/** Преобразователи zip'ки */
	@InternalElement
	public List<IZipTransformer> zipTransformers = new ArrayList<>();
	
	/** Доавить {@link IZipTransformer} в список трансформеров*/
	public InJarTransformer addTransformer(IZipTransformer transformer)
	{
		if (!this.zipTransformers.contains(transformer))
			zipTransformers.add(transformer);
		
		return this;
	}
	
	/** Добавить в jar набор классов для работы {@link ru.swayfarer.swl2.asm.transfomer.injection.InjectionAsm} */
	public InJarTransformer addInjectionAsmKit()
	{
		addExistingClass(InjectionAsm.class);
		addExistingClass(IReference.class);
		addExistingClass(SimpleReference.class);
		
		return this;
	}
	
	/** Перенести существующий класс в jar файл*/
	public InJarTransformer addExistingClass(final Class<?> cl)
	{
		if (cl == null)
			return this;
		
		return addTransformer(new IZipTransformer()
		{
			
			@Override
			public ZipEntry onEntry(ZipEntry entry, byte[] entryBytes, ZipOutputStream zipOut, FileSWL fromFile, ZipFile zipFileFrom) throws Throwable
			{
				return entry;
			}
			
			@Override
			public void injectNewEntries(ZipOutputStream zipOut, FileSWL fromFile, ZipFile zipFileFrom) throws Throwable
			{
				String res = cl.getName().replace(".", "/")+".class";
				zipOut.putNextEntry(new ZipEntry(res));
				zipOut.write(RLUtils.toBytes("/"+res));
				zipOut.closeEntry();
			}
		});
	}
	
	/** Добавить {@link IClassTransformer} в список трансформеров */
	public InJarTransformer addTransformer(IClassTransformer transformer)
	{
		if (!this.classTransformers.contains(transformer))
			classTransformers.add(transformer);
		
		return this;
	}
	
	public FileSWL transform(String filePath, String dest)
	{
		return transform(new FileSWL(filePath), dest);
	}
	
	/** Трансформировать на основе добавленных трансформеров (см. {@link InJarTransformer#addTransformer(IClassTransformer)}) */
	public FileSWL transform(String filePath)
	{
		return transform(new FileSWL(filePath));
	}
	
	/** Трансформировать на основе добавленных трансформеров (см. {@link InJarTransformer#addTransformer(IClassTransformer)}) */
	public FileSWL transform(FileSWL file)
	{
		return transform(file, file.getParent() + "/" + file.getNameWithoutExtension() + "_transformed(%date[dd.MM.YYYY-HH.mm.ssss]%)." + file.getExtension());
	}
	
	/** Настроить режим записи */
	@SuppressWarnings("unchecked")
	public <InJarTransformer_Type extends InJarTransformer> InJarTransformer_Type setReadonly(boolean isReadonly)
	{
		this.isWriting = !isReadonly;
		
		return (InJarTransformer_Type) this;
	}
	
	/** Трансформировать на основе добавленных трансформеров (см. {@link InJarTransformer#addTransformer(IClassTransformer)}) */
	public FileSWL transform(FileSWL file, String dest)
	{
		try
		{
			
			FileSWL tmpFile = new FileSWL("%temp%/%rand%/transform_%rand%.jar");
			
			if (isWriting)
				tmpFile.createIfNotFound();
			
			ZipOutputStream zipOut = isWriting ? tmpFile.toOutputStream().zip() : null;
			
			ZipFile zipFile = file.toZipFile();
			
			if (zipFile != null && (!isWriting || zipOut != null))
			{
				for (IZipTransformer zipTransformer : zipTransformers)
				{
					try
					{
						zipTransformer.injectNewEntries(zipOut, file, zipFile);
					}
					catch (Throwable e)
					{
						logger.error(e, "Error while injecting new zip entries to", file, "b" + zipTransformer);
					}
				}
				
				for (ZipEntry zipInEntry : CollectionsSWL.arrayList(zipFile.entries()))
				{
					try
					{
						ZipEntry entry = new ZipEntry(zipInEntry.getName());
						
						InputStream stream = zipFile.getInputStream(entry);
						
						byte[] bytes = null;
						
						if (stream != null)
						{
							bytes = StreamsUtils.readAllAndClose(stream);
						}
						
						for (IZipTransformer zipTransformer : zipTransformers)
						{
							entry = zipTransformer.onEntry(entry, bytes, zipOut, file, zipFile);
							
							if (entry == null)
								break;
						}
						
						if (entry != null)
						{
							if (entry.getName().endsWith(".class"))
							{
								for (IClassTransformer transformer : classTransformers)
								{
									try
									{
										String entryName = entry.getName();
										entryName = entryName.substring(0, entryName.length() - ".class".length());
										entryName = entryName.replace("/", ".");
										
										int len = bytes.length;
										
										TransformedClassInfo transformedClassInfo = new TransformedClassInfo();
										transformedClassInfo.name = entryName;
										
										bytes = transformer.transform(entryName, bytes, transformedClassInfo);
										
										if (len != bytes.length)
										{
											System.out.println(PathTransforms.transform("%temp%"));
											System.out.println("Transformed "+entryName);
										}
										
										if (!transformedClassInfo.name.equals(entryName))
										{
											entry = new ZipEntry(transformedClassInfo.name+".class");
										}
									}
									catch (Throwable e)
									{
										logger.error(e, "Error while transforming class", entry.getName(), "with", transformer);
									}
								}
							}
							
							if (zipOut != null)
							{
								zipOut.putNextEntry(entry);
								zipOut.write(bytes);
								zipOut.closeEntry();
							}
							
						}
						
					}
					catch (Throwable e)
					{
						logger.error(e, "Error while processing zip entry", zipInEntry, "of file", file);
					}
					
				}
				
				if (zipOut != null)
				{
					zipOut.close();
					zipFile.close();
					
					FileSWL tmpParent = tmpFile.getParentFile();
					FileSWL destFile = new FileSWL(dest).createIfNotFound();
					
					tmpFile.copyTo(destFile);
					
					System.out.println(tmpFile.getAbsolutePath() + " | " + destFile.getAbsolutePath());
					
					tmpParent.remove();
				}
				
				return null;
			}
			else
			{
				logger.error("Can't transform file", file);
			}
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while transforming", file);
		}
		
		return null;
	}
	
	/**
	 * Преобразователь Zip-файлов
	 * @author swayfarer
	 *
	 */
	public static interface IZipTransformer {
		
		/** Вызывается перед обработкой ентрей {@link ZipFile}'а*/
		public void injectNewEntries(ZipOutputStream zipOut, FileSWL fromFile, ZipFile zipFileFrom) throws Throwable;
		
		/** Вызывается перед началом обработки ентри {@link ZipFile}'а. Если вернуть null, то обработка этой ентри прекратится*/
		public ZipEntry onEntry(ZipEntry entry, byte[] entryBytes, ZipOutputStream zipOut, FileSWL fromFile, ZipFile zipFileFrom) throws Throwable;
	}
}
