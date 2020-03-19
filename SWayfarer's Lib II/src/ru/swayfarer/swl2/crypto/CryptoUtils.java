package ru.swayfarer.swl2.crypto;

public class CryptoUtils {
	
	public static Cryptor getCryptor(String ctype, byte[] key)
	{
		return new Cryptor().load(ctype, key);
	}
	
}
