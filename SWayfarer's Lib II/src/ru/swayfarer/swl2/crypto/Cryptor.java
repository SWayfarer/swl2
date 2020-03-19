package ru.swayfarer.swl2.crypto;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Шифровальщик 
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class Cryptor {

	/** Логгер */
	@InternalElement
	public ILogger logger = LoggingManager.getLogger();
	
	/** {@link Cipher} для расшифровки */
	@InternalElement
	public Cipher decryptCypher;
	
	/** {@link Cipher} для pашифровки */
	@InternalElement
	public Cipher encryptCipher;
	
	/** Ключ для шифровки */
	@InternalElement
	public SecretKey key;
	
	/** Загрузить, зная тип шифрования и ключ */
	public <T extends Cryptor> T load(String cryptType, byte[] keyBytes) 
	{
		logger.safe(() -> {
			
			int indexOfSlash = cryptType.indexOf("/");
			
			String algoritmName = cryptType;
			
			if (indexOfSlash > 0)
			{
				algoritmName = algoritmName.substring(0, indexOfSlash);
				System.out.println(algoritmName);
			}
			
			this.key = getKey(algoritmName, keyBytes);
			decryptCypher = Cipher.getInstance(cryptType);
			encryptCipher = Cipher.getInstance(cryptType);
			
			decryptCypher.init(Cipher.DECRYPT_MODE, this.key);
			encryptCipher.init(Cipher.ENCRYPT_MODE, this.key);
		
		}, "Error while loading cryptor", this, "from type", cryptType, "and key", key);
		
		return (T) this;
	}
	
	/** Загрузить, зная тип шифрования и ключ */
	public <T extends Cryptor> T load(String cryptType, String key) 
	{
		return load(cryptType, key == null ? null : key.getBytes());
	}
	
	/** Получить ключ */
	public SecretKey getKey(String keyType, byte[] keyBytes)
	{
		if (StringUtils.isEmpty(keyType) || CollectionsSWL.isNullOrEmpty(keyBytes))
		{
			try
			{
				KeyGenerator keyGenerator = KeyGenerator.getInstance(keyType);
				return keyGenerator.generateKey();
			}
			catch (Throwable e)
			{
				logger.error(e, "Error while creating crypto key of type", keyType);
			}
			
			return null;
		}
		
		return new SecretKeySpec(keyBytes, keyType);
	}
	
	/** Получить декриптор */
	public Cipher getDecryptCypher()
	{
		return decryptCypher;
	}
	
	/** Получить криптор */
	public Cipher getEncryptCipher()
	{
		return encryptCipher;
	}
	
	/** Зашифровать байты */
	public byte[] encrypt(byte[] bytes)
	{
		try
		{
			return getEncryptCipher().doFinal(bytes);
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while encrypting bytes");
		}
		
		return null;
	}
	
	/** Расшифровать байты */
	public byte[] decrypt(byte[] bytes)
	{
		try
		{
			return getDecryptCypher().doFinal(bytes);
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while encrypting bytes");
		}
		
		return null;
	}
}
