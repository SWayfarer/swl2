package ru.swayfarer.swl2.resource.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1NoR;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.math.MathUtils;

/** 
 * Утилиты для работы с файлами 
 * @author swayfarer
 *
 */
public class FilesUtils {
	
	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Для каждого файла, проходящего указанный фильтр, выполнить функцию */
	public static void forEachFile(IFunction1<FileSWL, Boolean> filter, IFunction1NoR<FileSWL> fun, FileSWL initialFile)
	{
		if (filter == null)
			filter = (f) -> true;

		ExceptionsUtils.IfNull(fun, IllegalArgumentException.class, "Files fun can't be null!");
		ExceptionsUtils.IfNull(initialFile, IllegalArgumentException.class, "Initial file can't be null!");

		if (!initialFile.exists())
			return;

		final IFunction1<FileSWL, Boolean> finallyFilter = filter;

		if (initialFile.isFile() && filter.apply(initialFile))
			fun.apply(initialFile);
		else
			initialFile.getSubFiles((file) -> file.isDirectory() || finallyFilter.apply(file)).parrallelDataStream().each((file) -> forEachFile(finallyFilter, fun, file));
	}
	
	/** Получить хэш файла */
	public static String getFileHash(FileSWL file, String hashType)
	{
		if (!MathUtils.isMessageDigestsSupported(hashType))
			return new String(MathUtils.getHash(hashType, file.getData()));
		
		return getFileHashByMessageDigest(file, hashType);
	}
	
	/** Получить хэш файла */
	public static String getFileHashByMessageDigest(File file, String hashType)
	{
		InputStream fis = null;
		
		try
		{
			if (!file.exists())
				return "no hash!";
			
			fis = new FileInputStream(file);
	
			byte[] buffer = new byte[1024];
			MessageDigest complete = MessageDigest.getInstance(hashType); 
			int numRead;
	
			do
			{
				numRead = fis.read(buffer);
				if (numRead > 0)
				{
					complete.update(buffer, 0, numRead);
				}
			} 
			while (numRead != -1);
	
			fis.close();
			
			byte[] bytes = complete.digest();
			
			String result = "";
	
			for (int i = 0; i < bytes.length; i++)
			{
				result += Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1);
			}
			
			return result;
		}
		catch (NoSuchAlgorithmException e)
		{
			logger.error("Error while creating hash for file '"+file.getAbsolutePath()+"'. Alghroritm '"+hashType+"' does not exists!");
			
			
			
			if (fis != null)
			{
				try {fis.close();}catch(Throwable e2) {}
			}
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			
			if (fis != null)
			{
				try {}catch(Throwable e2) {}
			}
		}
		
		return null;
	}
}
