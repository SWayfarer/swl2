package ru.swayfarer.swl2.math.hash;

import java.security.MessageDigest;

import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;

/**
 * Фукнция, которая обращаяется к {@link MessageDigest} для создания хэша 
 * @author swayfarer
 *
 */
public class MessageDigestHashFun implements IFunction1<byte[], byte[]> {

	public static String DEFAULT_HASH_ALGHORITM = "MD5";
	
	/** Алгоритм, по которому будет делаться hash */
	@InternalElement
	public String alghoritm = "MD5";
	
	/** Логгер*/
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Конструктор с алгоритмом {@link #DEFAULT_HASH_ALGHORITM} */
	public MessageDigestHashFun()
	{
		this(DEFAULT_HASH_ALGHORITM);
	}
	
	/** Конструктор */
	public MessageDigestHashFun(String alghoritm)
	{
		this.alghoritm = alghoritm;
	}
	
	@Override
	public byte[] apply(byte[] source)
	{
		try
		{
			MessageDigest messageDigest = MessageDigest.getInstance(alghoritm);
			
			return messageDigest.digest(source);
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while creting hash for", source, "by alghoritm", alghoritm);
		}
		
		return null;
	}

}
