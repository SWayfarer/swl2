package ru.swayfarer.swl2.string.reader;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction0;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.DynamicString;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Читалка строк, отчасти скопированная с {@link StringReader} (а нефиг private юзать) 
 * @author User
 *
 */
@SuppressWarnings("unchecked")
public class StringReaderSWL extends Reader {

	/** Разделитель строк */
	@InternalElement
	public String lineSplitter = StringUtils.LF;
	
	/** Читаемая строка */
	@InternalElement
	public String str;
	
	/** Длина читаемой строки  */
	@InternalElement
	public int length;
	
	/** Позиция следующего элемента */
	@InternalElement
	public int pos = 0;
	
	/** Последняя закладка, на которую можно вернуться */
	@InternalElement
	public int mark = 0;

	/** Конструктор */
	public StringReaderSWL(String s) {
		this.str = s;
		this.length = s.length();
	}

	/** Убедиться, что строка не Null */
	public void ensureOpen() throws IOException {
		if (str == null)
			throw new IOException("Stream closed");
	}
	
	/** Читать, пока выполняется условие */
	public String readWhile(IFunction0<Boolean> condition)
	{
		DynamicString ret = new DynamicString();
		while (hasNextElement() && condition.apply())
			ret.append(next());
		return ret.toString();
	}
	
	/** Прочитать до конца */
	public String readAll()
	{
		DynamicString str = new DynamicString();
		
		while (hasNextElement())
		{
			str.append(next());
		}
		
		return str.toString();
	}

	/** Прочитать следующий символ. Вернет -1, если символа нет */
	public int read() throws IOException {
		synchronized (lock) {
			ensureOpen();
			if (pos >= length)
				return -1;
			return str.charAt(pos ++);
		}
	}
	
	/** Прочитать следующий символ. Вернет null, если символа нет */
	public Character next()
	{
		synchronized (lock) {
			if (pos >= length)
				return null;
			
			return str.charAt(pos ++);
		}
	}
	
	/** Просмотреть следующий символ, не переходя на него. Вернет null, если символа нет */
	public Character lookupNext()
	{
		int pos = this.pos + 1;
		
		synchronized (lock) {
			if (pos >= length)
				return null;
			
			return str.charAt(pos);
		}
	}
	
	/** В конце ли строки? */
	public boolean atEnd()
	{
		return pos >= length;
	}
	
	/** Есть ли, что еще прочитать? */
	public boolean hasNextElement()
	{
		return !atEnd();
	}
	
	/** Пуста ли строка? */
	public boolean isEmpty()
	{
		return StringUtils.isEmpty(str);
	}
	
	public <T extends StringReaderSWL> T skipLine() 
	{
		if (!hasNextElement())
			return null;
		
		while (!skipSome(lineSplitter) && hasNextElement())
		{
			pos ++;
		}
		
		return (T) this;
	}
	
	/** Читаем строку */
	public String readLine()
	{
		if (!hasNextElement())
			return null;
		
		StringBuilder builder = new StringBuilder();
		
		while (!skipSome(lineSplitter) && hasNextElement())
		{
			builder.append(next());
		}
		
		return builder.toString();
	}

	public int read(char cbuf[], int off, int len) throws IOException {
		synchronized (lock) {
			ensureOpen();
			if ((off < 0) || (off > cbuf.length) || (len < 0) || ((off + len) > cbuf.length) || ((off + len) < 0)) {
				throw new IndexOutOfBoundsException();
			} else if (len == 0) {
				return 0;
			}
			if (pos >= length)
				return -1;
			int n = Math.min(length - pos, len);
			str.getChars(pos, pos + n, cbuf, off);
			pos += n;
			return n;
		}
	}
	
	/** Задать разделитель строк */
	public <T extends StringReaderSWL> T setLineSplitter(@ConcattedString Object... text) 
	{
		this.lineSplitter = StringUtils.concat(text);
		return (T) this;
	}

	/** Пропустить строку */
	public <T extends StringReaderSWL> T skip(String s) 
	{
		pos += s.length();
		pos = Math.min(pos, length);
		return (T) this;
	}
	
	/** Пропустить строку  */
	public boolean skipSome(String s)
	{
		if (isPending(s))
		{
			skip(s);
			return true;
		}
		
		return false;
	}
	
	/** Пропустить одну из строк */
	public boolean skipSome(IExtendedList<String> list)
	{
		for (String s : list)
		{
			if (skipSome(s))
				return true;
		}
		
		return false;
	}
	
	/** Пропустить несколько символов */
	public long skip(long ns) throws IOException {
		synchronized (lock) {
			ensureOpen();
			if (pos >= length)
				return 0;
			// Bound skip by beginning and end of the source
			long n = Math.min(length - pos, ns);
			n = Math.max(-pos, n);
			pos += n;
			return n;
		}
	}
	
	/** Ожидается ли эта строка? */
	public boolean isPending(@ConcattedString Object... text)
	{
		String s = StringUtils.concat(text);
		String pendingString = getPending(s.length());
		return pendingString.equals(s);
	}
	
	/** Получить идущую строку, не читая ее */
	public String getPending(int lenght)
	{
		int end = pos + lenght;
		
		end = Math.min(this.length, end);
		
		return str.substring(pos, end);
	}

	/** Доступно ли для чтения? */
	public boolean ready() throws IOException {
		synchronized (lock) {
			ensureOpen();
			return true;
		}
	}

	/** Можно ли сделать закладку? */
	public boolean markSupported() {
		return true;
	}

	/** Сделать закладку */
	public void mark(int readAheadLimit) throws IOException {
		if (readAheadLimit < 0) {
			throw new IllegalArgumentException("Read-ahead limit < 0");
		}
		synchronized (lock) {
			ensureOpen();
			mark = pos;
		}
	}

	/** Откатиться к сделанной через {@link #mark(int)} закладке */
	public void reset() throws IOException {
		synchronized (lock) {
			ensureOpen();
			pos = mark;
		}
	}

	/** Закрыть */
	public void close() {
		str = null;
	}

}
