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
import ru.swayfarer.swl2.resource.streams.StreamsUtils;

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
	public static String getFileHash(File file, String hashType)
	{
		InputStream fis = null;
		
		try
		{
			if (!file.exists())
				return "Sorry, but file must be... existing? =)";
			
			fis = new FileInputStream(file);
	
			byte[] readBuffer = new byte[StreamsUtils.BUFFER_SIZE];
			MessageDigest digest = MessageDigest.getInstance(hashType); 
			int numRead;
	
			do
			{
				numRead = fis.read(readBuffer);
				if (numRead > 0)
				{
					digest.update(readBuffer, 0, numRead);
				}
			} 
			while (numRead != -1);
	
			fis.close();
			
			byte[] hashBytes = digest.digest();
			
			String result = "";
	
			for (int i = 0; i < hashBytes.length; i++)
			{
				result += Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1);
			}
			
			return result;
		}
		catch (NoSuchAlgorithmException e)
		{
			logger.error("Error while creating hash for file '", file.getAbsolutePath(), "'. Alghroritm '", hashType, "' does not exists!");
			
			ExceptionsUtils.safe(fis::close);
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while creating hash for file '", file.getAbsolutePath());
			
			ExceptionsUtils.safe(fis::close);
		}
	    
		return null;
	}
}
