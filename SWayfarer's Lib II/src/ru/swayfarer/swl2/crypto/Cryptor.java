package ru.swayfarer.swl2.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;

@SuppressWarnings("unchecked")
public class Cryptor {

	public ILogger logger = LoggingManager.getLogger();
	public Cipher decryptCypher;
	public Cipher encryptCipher;
	public SecretKey key;
	
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
			
			this.key = new SecretKeySpec(keyBytes, algoritmName);
			decryptCypher = Cipher.getInstance(cryptType);
			encryptCipher = Cipher.getInstance(cryptType);
			
			decryptCypher.init(Cipher.DECRYPT_MODE, this.key);
			encryptCipher.init(Cipher.ENCRYPT_MODE, this.key);
		
		}, "Error while loading cryptor", this, "from type", cryptType, "and key", key);
		
		return (T) this;
	}
	
	public <T extends Cryptor> T load(String cryptType, String key) 
	{
		return load(cryptType, key.getBytes());
	}
	
	public Cipher getDecryptCypher()
	{
		return decryptCypher;
	}
	
	public Cipher getEncryptCipher()
	{
		return encryptCipher;
	}
}
