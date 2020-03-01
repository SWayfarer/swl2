package ru.swayfarer.swl2.math;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Random;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.math.hash.MessageDigestHashFun;

/**
 * Математические утилиты 
 * @author swayfarer
 *
 */
public class MathUtils {

	/** Id MD5-хэша */
	public static String HASH_MD5 = "md5";
	
	/** Id Jenkins-хэша */
	public static String HASH_JENKINS = "jenkins";
	
	/** Id SHA-1-хэша */
	public static String HASH_SHA_1 = "sha-1";
	
	/** Id SHA-256-хэша */
	public static String HASH_SHA_256 = "sha-256";
	
	/** Логгер*/
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Карта зарегистированных хеш-функций */
	@InternalElement
	public static Map<String, IFunction1<byte[], byte[]>> registeredHashFuns = CollectionsSWL.createConcurrentHashMap();
	
	/** Генератор случайных чисел */
	public static Random rand = new Random();
	
	/** Привести число к 1 к виду 000001, где lenght = размеру числа*/
	public static String standartizeNumToLenght(int lenght, int num)
	{
		lenght -= getNumCount(num);
		
		if (lenght < 0)
		{
			throw new IllegalArgumentException("Can't standartize num's length '"+num+"', because it lenght bigger then standart lenght");
		}
		
		String ret = "";
		
		for (int i1 = 0; i1 < lenght; i1 ++)
		{
			ret += 0;
		}
		
		ret += num;
		
		return ret;
	}
	
	/** Получить Jenkins Hash из строки */
	public static long getJenkinsHash(String s)
	{
		try
		{
			return JenkinsHasher.instance.hash(s.getBytes("UTF-8"));
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		
		return 0;
	}
	
	/** Получить Jenkins Hash из байтов */
	public static long getJenkinsHash(byte[] bytes)
	{
		return JenkinsHasher.instance.hash(bytes);
	}
	
	/** Кол-во чисел */
	public static int getNumCount(int num)
	{
		for (int i1 = 0;; i1 ++)
		{
			num /= 10;
			
			if (num == 0)
			{
				return i1 + 1;
			}
		}
	}
	
	/** Кол-во знаков после запятой переводимого числа*/
	public static final int MAX_FRACT = 10;
	
	/** Округлить */
	public static int floor(double d)
	{
		return d < 0 ? (int)(d - 0.5) : (int)(d + 0.5);
	}
	
	/** Перевести число из fromBase-чной СС в toBase-ную*/
	public static String toBase(Object num, int fromBase, int toBase)
	{
		return toBase(toDex(num, fromBase), toBase);
	}
	
	/** Перевести число из 10-чной СС в base-ную*/
	public static String toBase(Object dexNum, int base)
	{
		String ret = "";
		
		String num = dexNum+"";
		
		String eString = null;
		
		int pointIndex = num.indexOf(".");
		
		int eIndex = num.indexOf("E");
		
		if (eIndex != -1)
		{
			eString = num.substring(eIndex + 1, num.length());
			num = num.substring(0, eIndex);
		}
		
		if (eString != null)
		{
			String s = "";
			
			int e = Integer.valueOf(eString);
			
			if (e > 0)
			{
				pointIndex = ret.indexOf(".") + e;
				
				ret = ret.replace(".", "");
				
				int max = Math.max(ret.length(), pointIndex);
				
				for (int i1 = 0; i1 < max; i1 ++)
				{
					if (ret.length() > i1)
					{
						s += ret.charAt(i1)+"";
					}
					else
					{
						s += "0";
					}
					
					if (i1 == pointIndex)
					{
						s += ".";
					}
				}
			}
			else
			{
				
			}
			
			num = s;
		}
		
		if (pointIndex == -1)
		{
			ret = Integer.toString(Integer.valueOf(num), base);
		}
		else if (base != 10)
		{
			ret = Integer.toString(Integer.valueOf(num.substring(0, pointIndex)), base)+".";
			
			num = num.substring(pointIndex);
			
			int i1 = 0;
			double d = Double.valueOf("0"+num) * base;
			
			while (i1 < MAX_FRACT && d > 0.0000001D)
			{
				ret += (int)d;
				
				d = (d - ((int)d)) * base;
			}
		}
		else
		{
			ret = num;
		}
		
		if (ret.endsWith("."))
			ret += "0";
		
		return ret;
	}
	
	/** Минимальное значение из укзаанных */
	public static float min(boolean isIgnoresZero, float... floats)
	{
		float ret = floats[0];
		
		for (float f : floats)
		{
			if (isIgnoresZero && ret == 0)
				continue;
			
			if (ret > f)
				ret = f;
		}
		
		return ret;
	}
	
	/** Максимальное значение из укзаанных */
	public static float max(boolean isIgnoresZero, float... floats)
	{
		float ret = floats[0];
		
		for (float f : floats)
		{
			if (isIgnoresZero && ret == 0)
				continue;
			
			if (ret < f)
				ret = f;
		}
		
		return ret;
	}
	
	/** Перевести в 10-чную СС из base-чной*/
	public static double toDex(Object obj, int base)
	{
		String num = ""+obj;
		
		String ret = "";
		
		int pointIndex = num.indexOf(".");
		
		if (pointIndex == -1)
		{
			ret = ""+Integer.parseInt(num, base);
		}
		else
		{
			ret += Integer.parseInt(num.substring(0, pointIndex), base);
			
			num = num.substring(pointIndex+1);
			
			double d = 0.0D;
			
			for (int i1 = 0; i1 < num.length(); i1 ++)
			{	
				d += Integer.parseInt(num.charAt(i1)+"", base) * Math.pow(base, -(i1+1));
			}
			
			ret += (d+"").substring(1);
		}
		
		return Double.valueOf(ret);
	}

	/** Случайное число между a и b включительно. Работает только для положительных чисел.*/
	public static int getRandomIntBetween(int min, int max)
	{
		return rand.nextInt(min)+(max - min);
	}

	/** Получить вероятность от 0.0 до 1.0*/
	public static boolean getChance(float f)
	{
		return rand.nextFloat() <= f;
	}
	
	/** 
	 * Получить хэш указанного типа
	 * <h1> Примечание: </h1> 
	 * Все id хэшей хранятся в нижнем регистре (т.е. маленькими буквами)
	 */
	public static byte[] getHash(String hashType, byte[] source)
	{
		String key = hashType.toLowerCase();
		
		IFunction1<byte[], byte[]> hashFun = registeredHashFuns.get(key);
		
		if (hashFun == null)
		{
			logger.error("Hash function with id", hashType, "does not exists! Returning null hash...");
			return null;
		}
		
		return hashFun.apply(source);
	}

	/** 
	 * Зарегистрировать хэш-функцию по ее id
	 * <h1> Примечание: </h1> 
	 * Все id хэшей хранятся в нижнем регистре (т.е. маленькими буквами)
	 */
	public static void registerHashFun(String id, IFunction1<byte[], byte[]> hashFun)
	{
		String key = id.toLowerCase();
		
		IFunction1<byte[], byte[]> registeredHashFun = registeredHashFuns.get(key);
		
		if (registeredHashFun != null)
			logger.warning("Overwriting already registered hash fun with id", id);
		
		registeredHashFuns.put(id, hashFun);
	}
	
	public static void registerDefaultHashFuns()
	{
		registerHashFun(HASH_MD5, new MessageDigestHashFun("MD5"));
		registerHashFun(HASH_SHA_1, new MessageDigestHashFun("SHA-1"));
		registerHashFun(HASH_SHA_256, new MessageDigestHashFun("SHA-256"));
	}
	
	/** Хешер, который делает Jenkins Hash'и */
	public static class JenkinsHasher {
		
		public static final long MAX_VALUE = 4294967295L;
		long a;
		long b;
		long c;
		
		public static final JenkinsHasher instance = new JenkinsHasher();

		private long byteToLong(final byte b)
		{
			long val = b & 0x7F;
			if ((b & 0x80) != 0x0)
			{
				val += 128L;
			}
			return val;
		}

		private long add(final long val, final long add)
		{
			return val + add & 0xFFFFFFFFL;
		}

		private long subtract(final long val, final long subtract)
		{
			return val - subtract & 0xFFFFFFFFL;
		}

		private long xor(final long val, final long xor)
		{
			return (val ^ xor) & 0xFFFFFFFFL;
		}

		private long leftShift(final long val, final int shift)
		{
			return val << shift & 0xFFFFFFFFL;
		}

		private long fourByteToLong(final byte[] bytes, final int offset)
		{
			return this.byteToLong(bytes[offset + 0]) + (this.byteToLong(bytes[offset + 1]) << 8) + (this.byteToLong(bytes[offset + 2]) << 16) + (this.byteToLong(bytes[offset + 3]) << 24);
		}

		private void hashMix()
		{
			this.a = this.subtract(this.a, this.b);
			this.a = this.subtract(this.a, this.c);
			this.a = this.xor(this.a, this.c >> 13);
			this.b = this.subtract(this.b, this.c);
			this.b = this.subtract(this.b, this.a);
			this.b = this.xor(this.b, this.leftShift(this.a, 8));
			this.c = this.subtract(this.c, this.a);
			this.c = this.subtract(this.c, this.b);
			this.c = this.xor(this.c, this.b >> 13);
			this.a = this.subtract(this.a, this.b);
			this.a = this.subtract(this.a, this.c);
			this.a = this.xor(this.a, this.c >> 12);
			this.b = this.subtract(this.b, this.c);
			this.b = this.subtract(this.b, this.a);
			this.b = this.xor(this.b, this.leftShift(this.a, 16));
			this.c = this.subtract(this.c, this.a);
			this.c = this.subtract(this.c, this.b);
			this.c = this.xor(this.c, this.b >> 5);
			this.a = this.subtract(this.a, this.b);
			this.a = this.subtract(this.a, this.c);
			this.a = this.xor(this.a, this.c >> 3);
			this.b = this.subtract(this.b, this.c);
			this.b = this.subtract(this.b, this.a);
			this.b = this.xor(this.b, this.leftShift(this.a, 10));
			this.c = this.subtract(this.c, this.a);
			this.c = this.subtract(this.c, this.b);
			this.c = this.xor(this.c, this.b >> 15);
		}

		public long hash(final byte[] buffer, final long initialValue)
		{
			this.a = 2654435769L;
			this.b = 2654435769L;
			this.c = initialValue;
			int pos = 0;
			int len;
			for (len = buffer.length; len >= 12; len -= 12)
			{
				this.a = this.add(this.a, this.fourByteToLong(buffer, pos));
				this.b = this.add(this.b, this.fourByteToLong(buffer, pos + 4));
				this.c = this.add(this.c, this.fourByteToLong(buffer, pos + 8));
				this.hashMix();
				pos += 12;
			}
			this.c += buffer.length;
			switch (len)
			{
				case 11:
				{
					this.c = this.add(this.c, this.leftShift(this.byteToLong(buffer[pos + 10]), 24));
				}
				case 10:
				{
					this.c = this.add(this.c, this.leftShift(this.byteToLong(buffer[pos + 9]), 16));
				}
				case 9:
				{
					this.c = this.add(this.c, this.leftShift(this.byteToLong(buffer[pos + 8]), 8));
				}
				case 8:
				{
					this.b = this.add(this.b, this.leftShift(this.byteToLong(buffer[pos + 7]), 24));
				}
				case 7:
				{
					this.b = this.add(this.b, this.leftShift(this.byteToLong(buffer[pos + 6]), 16));
				}
				case 6:
				{
					this.b = this.add(this.b, this.leftShift(this.byteToLong(buffer[pos + 5]), 8));
				}
				case 5:
				{
					this.b = this.add(this.b, this.byteToLong(buffer[pos + 4]));
				}
				case 4:
				{
					this.a = this.add(this.a, this.leftShift(this.byteToLong(buffer[pos + 3]), 24));
				}
				case 3:
				{
					this.a = this.add(this.a, this.leftShift(this.byteToLong(buffer[pos + 2]), 16));
				}
				case 2:
				{
					this.a = this.add(this.a, this.leftShift(this.byteToLong(buffer[pos + 1]), 8));
				}
				case 1:
				{
					this.a = this.add(this.a, this.byteToLong(buffer[pos + 0]));
					break;
				}
			}
			this.hashMix();
			return this.c;
		}

		public long hash(final byte[] buffer)
		{
			return this.hash(buffer, 0L);
		}
	}
}