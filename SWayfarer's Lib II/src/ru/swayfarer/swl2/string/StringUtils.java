package ru.swayfarer.swl2.string;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.collections.streams.DataStream;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.string.regex.RegexBuilder;

/**
 * Утилиты для работы со строками
 * @author swayfarer
 *
 */
public class StringUtils {

	/** Символ CR */
	public final static String CR  = ""+(char) 0x0D;
	
	/** Символ RF*/
	public final static String LF  = ""+(char) 0x0A; 
	
	/** Символ CRLF*/
	public final static String CRLF  = CR+LF;
	
	/** Символ BOM*/
	public static final String BOM = "\ufeff";
	
	/** Символ табуляции */
	public static final String TAB = "	";
	
	/** Разделитель строк, используемый по-умолчанию */
	public static String lineSplitter = LF;
	
	/** Соединить все объекты в строку, поставив пробелы между ними */
	public static String concatWithSpaces(Object... text)
	{
		return concat(" ", text);
	}
	
	/**
	 * Составить Camel Case строку из слов
	 * @param lowerCaseToAll Сделать ли заглавные буквы только в начале слов?
	 * @param words Слова
	 * @return Полученная строка
	 */
	public static String toCamelCase(boolean lowerCaseToAll, Object... words) 
	{
		List<String> strings = CollectionsSWL.createExtendedList();
		int lenght = 0;
		
		for (Object obj : words)
		{
			String word = ""+obj;
			strings.add(word);
			lenght += word.length();
		}
		
		StringBuilder sb = new StringBuilder(lenght);
		
		boolean isFirst = true;
		
		for (String word : strings)
		{
			if (lowerCaseToAll)
				word = word.toLowerCase();
			
			sb.append(isFirst ? word : firstCharToUpperCase(word));
			isFirst = false;
		}
		
		return sb.toString();
	}
	
	/**
	 * Перевести первый символ в заглавный регистр
	 * @param s Строка, первый символ которой переводим в заглавный регистр
	 * @return Полученная строка
	 */
	public static String firstCharToUpperCase(String s)
	{
		return s == null ? null : s.isEmpty() ? "" : (s.charAt(0)+"").toUpperCase()+(s.length() > 1 ? s.substring(1) : "");
	}
	
	/**
	 * Перевести первый символ в прописной регистр
	 * @param s Строка, первый символ которой переводим в прописной регистр
	 * @return Полученная строка
	 */
	public static String firstCharToLowerCase(String s)
	{
		return s == null ? null : s.isEmpty() ? "" : (s.charAt(0)+"").toLowerCase()+(s.length() > 1 ? s.substring(1) : "");
	}
	
	/**
	 * Получить длину минимальной строки(разделенной /n) в тексте
	 * @param s Исходная строка
	 * @return Длина минимальной строки
	 */
	public static int getMinLineLenght(String s)
	{
		return getLineLenght(s, false);
	}
	
	/**
	 * Получить длину максимальной строки(разделенной /n) в тексте
	 * @param s Исходная строка
	 * @return Длина максимальной строки
	 */
	public static int getMaxLineLenght(String s)
	{
		return getLineLenght(s, true);
	}
	
	/**
	 * Является ли строка пустой?
	 * @param obj Проверяемая строка
	 * @return True, если строка будет пустой или null
	 */
	public static boolean isEmpty(Object obj)
	{
		return obj == null || obj.toString().isEmpty();
	}
	
	/** */
	protected static int getLineLenght(String s, boolean isMax)
	{
		String[] lines = s.split("\n");
		
		int ret = -1;
		
		for (String line : lines)
			if (isMax && line.length() > ret || !isMax && line.length() < ret)
				ret = line.length();
		
		return ret;
	}
	
	public static String subString(String indexOfString, @ConcattedString Object... text)
	{
		String s = concat(text);
		
		return s.substring(s.indexOf(indexOfString) + indexOfString.length());
	}
	
	/** Соединить все объекты в строку */
	public static String concat(Object... text)
	{
		return concat(null, text);
	}
	
	/**
	 * Начинается ли строка с одного из вариантов (игнорируя пробелы перед началом)?
	 * @param line Проверяемая строка
	 * @param starts Варианты начала строки
	 * @return True, если начинается
	 */
	public static boolean isStringStartsWith(String line, Object... starts)
	{
		return DataStream.of(starts).someMatches((s) -> line.startsWith(s+""));
	}
	
	public static String subString(int fromStart, int fromEnd, @ConcattedString Object... text)
	{
		String s = concat(text);
		
		if (fromStart < 0)
			fromStart = 0;
		
		if (fromEnd < 0)
			fromEnd *= -1;
		
		return s.substring(fromStart, s.length() + fromEnd);
	}
	
	/** Получить билдер для регулярки */
	public static RegexBuilder regex()
	{
		return new RegexBuilder();
	}
	
	/**
	 * Сколько раз встречается подстрока в строке ?
	 * @param str Строка, в которой ищем регулярки
	 * @param sub Подстрока
	 * @return Кол-во соотвествий
	 */
	public static int countMatches(String str, String sub)
	{
		if (isEmpty(str) || isEmpty(sub))
		{
			return 0;
		}
		int count = 0;
		int idx = 0;
		while ((idx = str.indexOf(sub, idx)) >= 0)
		{
			count++;
			idx += sub.length();
		}
		return count;
	}
	
	/** Получить первое совпадение по регулярке в строке */
	public static String firstMatches(String regex, @ConcattedString Object... text)
	{
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		
		String s = StringUtils.concat(text);
		Matcher matcher = pattern.matcher(s);
		
		if (matcher.find())
		{
			return s.substring(matcher.start(), matcher.end());
		}
		
		return null;
	}
	
	/** Получить все совпадения по регулярке в строке */
	public static IExtendedList<String> getAllMatches(String regex, @ConcattedString Object... text)
	{
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		
		String s = StringUtils.concat(text);
		Matcher matcher = pattern.matcher(s);
		
		IExtendedList<String> ret = CollectionsSWL.createExtendedList();
		
		while (matcher.find())
		{
			ret.add(s.substring(matcher.start(), matcher.end()));
		}
		
		return ret;
	}
	
	/**
	 * Создать строку из пробелов
	 * @param count Кол-во пробелов
	 * @return Полученная строка
	 */
	public static String createSpacesSeq(int count)
	{
		return createSeq(" ", count);
	}
	
	/**
	 * Создать последовательность символов
	 * @param part Текст, который будет дублироваться, пока строка не будет необходимой длины
	 * @param count Длина строки, которую необходимо получить
	 * @return Полученная строка
	 */
	public static String createSeq(String part, int count)
	{
		if (count == 0)
			return "";
		else if (part == null || count < 1)
			return null;
		
		StringBuilder builder = new StringBuilder(part.length()*count);
		
		for (int i1 = 0; i1 < count; i1 ++)
		{
			if (builder.length() < count)
				builder.append(part);
			else if (builder.length() == count)
				break;
			else
				return builder.substring(0, count);
		}
		
		return builder.toString();
	}
	
	/**
	 * Сколько раз встречается текст, соответсвтуюший регулярке ?
	 * @param str Строка, в которой ищем регулярки
	 * @param regex Регулярка
	 * @return Кол-во соотвествий
	 */
	public static int countMatchesRegex(String str, String regex)
	{
		if (isEmpty(str) || isEmpty(regex))
		{
			return 0;
		}
		
		int count = 0;
		int idx = 0;
		
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		
		while (matcher.find(idx))
		{
			idx += matcher.end() - matcher.start();
			count ++;
		}
		
		return count;
	}
	
	/** */
	public static String regexByMask(String mask)
	{
		return mask.replace("*", ".*").replace("#", "\\d").replace("?", ".").replace("[!", "[^");
	}
	
	/** */
	public static boolean isMatchesByMask(String mask, Object... objects)
	{
		return isMatchesByRegex(regexByMask(mask), objects);
	}
	
	public static boolean isMatchesByRegex(String regex, Object... objects)
	{
		if (regex == null)
			return false;
		
		if (objects == null || objects.length == 0)
			return false;
		
		for (Object obj : objects)
		{
			if (!(obj+"").matches(regex))
				return false;
		}
		
		return true;
	}
	
	/** Соединить объекты, разместив разделитель между ними */
	public static String concat(String splitter, Object... text)
	{
		if (text == null || text.length == 0)
			return null;
		
		if (splitter == null || splitter.isEmpty())
		{
			boolean isSomeNotNull = false;
			
			for (Object obj : text) 
			{
				if (obj != null)
				{
					isSomeNotNull = true;
					break;
				}
			}
			
			if (!isSomeNotNull)
				return null;
		}
		
		StringBuilder builder = new StringBuilder();
		
		boolean isNoFirst = false;
		
		for (Object obj : text)
		{
			if (isNoFirst && splitter != null)
				builder.append(splitter);
			
			
			if (ReflectionUtils.isArray(obj))
			{
				obj = CollectionsSWL.getArrayString(obj);
			}
			
			builder.append(obj+"");
			
			isNoFirst = true;
		}
		
		return builder.toString();
		
	}
	
	/** Определить, пустая ли строка, которая будет сконкачена. */
	public static boolean isEmpty(@ConcattedString Object... text)
	{
		String s = concat(text);
		
		return s == null || s.isEmpty();
	}
	
	/** Определить, пустая ли хотя бы одна строка */
	public static boolean isSomeEmpty(Object... strings)
	{
		if (CollectionsSWL.isNullOrEmpty(strings))
			return false;
		
		for (Object obj : strings)
		{
			if (obj == null)
				return true;
			
			if ((obj + "").isEmpty())
				return true;
		}
		
		return false;
	}
	
}
