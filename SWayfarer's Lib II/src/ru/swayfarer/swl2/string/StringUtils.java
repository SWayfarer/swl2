package ru.swayfarer.swl2.string;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.collections.streams.DataStream;
import ru.swayfarer.swl2.equals.EqualsUtils;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.math.MathUtils;
import ru.swayfarer.swl2.resource.streams.BytesInputStreamSWL;
import ru.swayfarer.swl2.string.reader.StringReaderSWL;
import ru.swayfarer.swl2.string.regex.RegexBuilder;

/**
 * Утилиты для работы со строками
 * @author swayfarer
 *
 */
public class StringUtils {

	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Пробельный символ */
	public static final String SPACE = " ";
	
	/** Символ CR */
	public static final String CR  = ""+(char) 0x0D;
	
	/** Символ RF*/
	public static final String LF  = ""+(char) 0x0A; 
	
	/** Символ CRLF*/
	public static final String CRLF  = CR+LF;
	
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
	
	/** Заменить первое совпадение в строке */
	public static String replaceFirst(String target, String replacement, String newStr)
	{
		return target.replaceFirst(regex().text(replacement).build(), newStr);
	}
	
	public static boolean isMatchesByExpression(String expression, @ConcattedString Object... text)
	{
		if (isEmpty(expression))
			return true;
		
		if (isEmpty(text))
			text = new Object[] {""};
		
		if (expression.startsWith("regex:"))
		{
			return isMatchesByRegex(expression.substring(6), text);
		}
		else if (expression.startsWith("mask:"))
		{
			return isMatchesByMask(expression.substring(5), text);
		}
		
		return StringUtils.concat(text).startsWith(expression);
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
	
	public static String removeAllWhitespaces(String str)
	{
		return str == null ? null : str.replace(" ", "").replace("\t", "");
	}
	
	public static String removeFirstWhitespaces(String str)
	{
		int indexOfNonWhitespace = indexOfNotWhitespace(str, 0);
		
		if (indexOfNonWhitespace < 0)
			return null;
		
		return str.substring(indexOfNonWhitespace);
	}
	
	public static String removeBorderWhitespaces(String str)
	{
		return removeFirstWhitespaces(removeLastWhitespaces(str));
	}
	
	public static String removeLastWhitespaces(String str)
	{
		int indexOfNonWhitespace = indexOfLastNotWhitespace(str);
		
		if (indexOfNonWhitespace < 0)
			return null;
		
		return str.substring(0, indexOfNonWhitespace);
	}
	
	public static boolean isBlank(Object obj)
	{
		return obj == null || String.valueOf(obj).replace(" ", "").replace(TAB, "").isEmpty();
	}
	
	/**
	 * Заменить символы в формте \040 на соответсвующие им {@link Character}'ы 
	 * @param str Строка, в которой производится замена
	 * @return Строка после всех преобразований 
	 */
	public static String replaceASCIIFlags(String str)
	{
		return replaceCharactersFlags(str, "\\", 3, 8);
	}
	
	/**
	 * Заменить символы в формте \u0040 на соответсвующие им {@link Character}'ы 
	 * @param str Строка, в которой производится замена
	 * @return Строка после всех преобразований 
	 */
	public static String replaceUnicodeFlags(String str)
	{
		return replaceCharactersFlags(str, "\\u", 4, 16);
	}
	
	/**
	 * Заменить символы на коды в формате \u0040
	 * @param str Строка, в которой производится замена
	 * @return Строка после всех преобразований 
	 */
	public static String replaceCharsToUnicodeFlags(String str)
	{
		return replaceCharsToFlags(str, "\\u", 4, -1, 16);
	}
	
	/**
	 * Заменить символы на коды в формте \040
	 * @param str Строка, в которой производится замена
	 * @return Строка после всех преобразований 
	 */
	public static String replaceCharsToASCIIFlags(String str)
	{
		return replaceCharsToFlags(str, "\\", 3, 255, 8);
	}
	
	/**
	 * Заменить символы на коды в формте префикс + число, например, \u0040
	 * @param str Строка, в которой производится замена
	 * @param prefix Префикс, который будет поставляться перед каждым id 
	 * @param maxId Максимальный id, который может быть закодирован. Если встретится больший, то вернется null
	 * @param codeBase Основание системы счисления, в которой будут записаны коды символов
	 * @param idLenght Длина id (у 0040 - 4)
	 * @return Строка после всех преобразований 
	 */
	public static String replaceCharsToFlags(String str, String prefix, int idLenght, int maxId, int codeBase)
	{
		DynamicString ret = new DynamicString();
		
		for (char ch : str.toCharArray())
		{
			int charCode = (int) ch;
			
			if (maxId > 0 && charCode > maxId)
			{
				logger.warning("Character", ch, "is not in 0 -", maxId, "bounds!");
				return null;
			}
			
			ret.append(prefix);
			ret.append(MathUtils.standartizeNumToLenght(idLenght, MathUtils.toBase(charCode, 16)));
		}
		
		return ret.toString();
	}
	
	/**
	 * Заменить все символы в формате префикс + число, например, \u0040, на соответствующие им {@link Character}'ы
	 * @param str Строка, в которой происходит замена 
	 * @param prefix Префикс, который подставляется перед каждым id
	 * @param idLenght Длина id (у 0040 - 4)
	 * @param codeBase Основание системы счисления
	 * @return Строка после всех преобразований 
	 */
	public static String replaceCharactersFlags(String str, String prefix, int idLenght, int codeBase)
	{
		StringReaderSWL reader = new StringReaderSWL(str);
		DynamicString ret = new DynamicString();
		boolean isReplaced = false;
		
		while (reader.hasNextElement())
		{
			if (reader.skipSome(prefix))
			{
				if (reader.hasNextChars(idLenght))
				{
					String id = reader.getPending(idLenght);
					
					if (!StringUtils.isEmpty(id) && id.length() == idLenght)
					{
						if (StringUtils.isInteger(id))
						{
							id = MathUtils.toBase(id, codeBase, 10);
							int intId =  Double.valueOf(id).intValue();
							
							ret.append((char) intId);
							reader.skipSafe(idLenght);
							isReplaced = true;
						}
					}
				}
				
				if (!isReplaced)
				{
					reader.pos -= prefix.length();
					ret.append(reader.next());
				}
			}
			else
			{
				ret.append(reader.next().charValue());
			}
		}
		
		reader.close();
		
		return ret.toString();
	}
	
	/** Вывести в консоль все поддерживаемые кодировки */
	public static void printAvailableEncodings()
	{
		Charset.availableCharsets().keySet().forEach(System.out::println);
	}
	
	/**
	 * Создать лист со всеми комбинаряцми в формате iterables[0].rand()+iterables[1].rand()+iterables[?].rand()
	 * @param iterables Строки для комбинации
	 * @return Лист полученных комбинаций 
	 */
	public static IExtendedList<String> unite(Collection<?>... iterables)
	{
		int lenght = 1;
		int size = 0;
		
		for (int i1 = 0; i1 < iterables.length; i1 ++)
		{
			size = iterables[i1].size();
			
			if (size > 0)
				lenght *= size;
		}
		
		String[] result = new String[lenght];
		
		int index = 0;
		int lenghtMin1 = 0;
		
		for (int i1 = 0; i1 < lenght; i1 ++)
			result[i1] = "";
		
		for (int i1 = 0; i1 < iterables.length; i1 ++)
		{
			Object[] objs = iterables[i1].toArray();
			
			if (objs != null && objs.length > 0)
			{
				lenghtMin1 = objs.length - 1;
				
				for (int i2 = 0; i2 < lenght; i2 ++)
				{
					index = lenghtMin1 == 0 ? 0 : i2 % lenghtMin1;
					
					result[i2] += ""+objs[index];
				}
			}
		}
		
		return CollectionsSWL.createExtendedList(result);
	}
	
	/**
	 * Получить {@link Charset} по его имени
	 * @param name Имя {@link Charset}'а
	 */
	public static Charset getCharset(String name)
	{
		try
		{
			if (isEncodingSupported(name))
				return Charset.forName(name);
			else
				logger.error("Charset '", name, "' is not supported!");
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while getting charset for name", name);
		}
		
		return null;
	}
	
	/**
	 * Получить {@link Charset} по его имени
	 * @param name Имя {@link Charset}'а
	 * @param defaultCharset чарсет, который вернется, если не найдется искомого. Не может быть null
	 */
	public static Charset getCharset(String name, Charset defaultCharset)
	{
		ExceptionsUtils.IfNull(defaultCharset, IllegalArgumentException.class, "Default charset can't be null!");
		
		Charset ret = getCharset(name);
		
		return ret == null ? defaultCharset : ret;
	}
	
	/** Поддерживается ли эта кодировка? */
	public static boolean isEncodingSupported(@ConcattedString Object... text)
	{
		String enc = concat(text);
		return !isEmpty(enc) && Charset.isSupported(enc);
	}
	
	public static int indexOfNotWhitespace(String str, int start)
	{
		if (str == null)
			return -1;
		
		StringReaderSWL reader = new StringReaderSWL(str);
		reader.pos = start;
		
		while (reader.hasNextElement())
		{
			Character ch = reader.next();
			
			if (!EqualsUtils.objectEqualsSome(ch, ' ', '\t'))
			{
				reader.close();
				return reader.pos - 1;	
			}
		}
		
		reader.close();
		
		return -1;
	}
	
	public static int indexOfLastNotWhitespace(String str)
	{
		StringReaderSWL reader = new StringReaderSWL(new DynamicString(str).reverse().toString());
		
		while (reader.hasNextElement())
		{
			Character ch = reader.next();
			
			if (!EqualsUtils.objectEqualsSome(ch, ' ', '\t'))
			{
				reader.close();
				return str.length() - (reader.pos - 1);	
			}
		}
		
		reader.close();
		
		return -1;
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
	
	/**
	 * Является ли строка Float'ом?
	 * @param s Проверяемая строка
	 * @return True, если является
	 */
	public static boolean isFloat(Object s)
	{
		try
		{
			Float.valueOf(s+"");
			return true;
		}
		catch (Throwable e) {}
		
		return false;
	}
	
	/**
	 * Является ли строка Double'ом?
	 * @param s Проверяемая строка
	 * @return True, если является
	 */
	public static boolean isDouble(Object s)
	{
		try
		{
			Double.valueOf(s+"");
			return true;
		}
		catch (Throwable e) {}
		
		return false;
	}
	
	/**
	 * Является ли строка {@link Integer}'ом?
	 * @param s Проверяемая строка
	 * @return True, если является
	 */
	public static boolean isInteger(String s)
	{
		try
		{
			Integer.valueOf(s);
			
			return true;
		}
		catch (Throwable e)
		{
			return false;
		}
	}
	
	/**
	 * Является ли строка {@link Long}'ом?
	 * @param s Проверяемая строка
	 * @return True, если является
	 */
	public static boolean isLong(String s)
	{
		try
		{
			Long.valueOf(s);
			
			return true;
		}
		catch (Throwable e)
		{
			return false;
		}
	}
	
	/**
	 * Является ли строка {@link Boolean}'ом?
	 * @param s Проверяемая строка
	 * @return True, если является
	 */
	public static boolean isBoolean(String s)
	{
		try
		{
			Boolean.valueOf(s);
			
			return true;
		}
		catch (Throwable e)
		{
			return false;
		}
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
	
	public static BytesInputStreamSWL getBytesStream(String encoding, @ConcattedString Object... text)
	{
		return BytesInputStreamSWL.createStream(getBytes(encoding, text));
	}
	
	public static byte[] getBytes(String encoding, @ConcattedString Object... text)
	{
		String str = concat(text);
		
		if (str == null)
			return null;
		
		return str.getBytes(getCharset(encoding));
	}
	
	public static String subStringByFirst(String indexOfString, @ConcattedString Object... text)
	{
		String s = concat(text);
		
		return s.substring(s.indexOf(indexOfString) + indexOfString.length());
	}
	
	public static String subStringByLast(String indexOfString, @ConcattedString Object... text)
	{
		String s = concat(text);
		
		return s.substring(0, s.lastIndexOf(indexOfString));
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

	/**
	 * Заканчивается ли строка с одного из вариантов (игнорируя пробелы перед началом)?
	 * @param line Проверяемая строка
	 * @param ends Варианты окончания строки
	 * @return True, если заканчивается
	 */
	public static boolean isStringEndsWith(String line, Object... ends)
	{
		return DataStream.of(ends).someMatches((s) -> line.endsWith(s+""));
	}
	
	public static String subString(int fromStart, int fromEnd, @ConcattedString Object... text)
	{
		String s = concat(text);
		
		if (fromStart < 0)
			fromStart = 0;
		
		if (fromEnd < 0)
			fromEnd *= -1;
		
		return s.substring(fromStart, s.length() - fromEnd);
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
	
	public static String maskToRegExp(String str)
	{
		// Присваиваем в качестве результата маску
		String res = str;
		// Этап 1: экранируем служебные символы
		String[] arr = new String[] { "\\", "#", "|", "(", ")", "[", "]", "{", "}", "^", "$", "+", "." };

		int len = arr.length;

		for (int i = 0; i < len; i++)
		{
			res = res.replace(arr[i], "\\" + arr[i]);
		}

		// Этап 2: заменяем служебные символы маски
		// на соответствующие аналоги в регулярных выражениях
		res = res.replace("*", ".*");
		res = res.replace("?", ".");

		// Этап 3: добавляем символы начала и конца строки
		return "^" + res + "$";
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
		return isMatchesByRegex(maskToRegExp(mask), objects);
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
		return concatWith(splitter, 0, text.length, text);
	}
	
	/** Соединить объекты, разместив разделитель между ними */
	public static String concatWith(String splitter, int start, int end, Object... text)
	{
		if (text == null || text.length == 0)
			return null;
		
		if (end < 0)
			end = text.length;
		else
			end = Math.min(end, text.length);
		
		if (start < 0)
			start = 0;
		else if (start > text.length)
			start = text.length;
		
		if (splitter == null || splitter.isEmpty())
		{
			boolean isSomeNotNull = false;
			
			for (int i1 = start; i1 < end; i1 ++) 
			{
				if (text[i1] != null)
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
		
		for (int i1 = start; i1 < end; i1 ++)
		{
			Object obj = text[i1];
			
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
