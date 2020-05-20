package ru.swayfarer.swl2.crypto;

import java.util.Map;

import javax.crypto.KeyGenerator;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;

/**
 * Утилиты для работы с криптографией
 * @author swayfarer
 *
 */
public class CryptoUtils {
	
	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Стандартные генераторы ключей шифрования */
	@InternalElement
	public static Map<String, KeyGenerator> defaultKeyGenerators = CollectionsSWL.createHashMap(
		"aes", getKeyGenerator("AES")
	);
	
	/** Получить криптор у казанным типом шифрования и ключем */
	public static Cryptor getCryptor(String ctype, byte[] key)
	{
		return new Cryptor().load(ctype, key);
	}
	
	/** Получить рандомный AES-ключ указанной длины */
	public static byte[] getRandomAESKey(int lenght)
	{
		KeyGenerator generator = defaultKeyGenerators.get("aes");
		
		if (generator == null)
			return null;
		
		generator.init(lenght);
		
		return generator.generateKey().getEncoded();
	}
	
	/** Получить генеатор ключей для указанного типа шифрования */
	@InternalElement
	public static KeyGenerator getKeyGenerator(String type)
	{
		try
		{
			return KeyGenerator.getInstance(type);
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while creating key generator of type", type);
		}
		
		return null;
	}
	
}
