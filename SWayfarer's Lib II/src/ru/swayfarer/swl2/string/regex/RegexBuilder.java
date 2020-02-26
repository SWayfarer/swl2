package ru.swayfarer.swl2.string.regex;

import ru.swayfarer.swl2.markers.InternalElement;

/**
 * Билдер для регулярок
 * <br> Позволяет писать регулярки читаемо и не упарывась в бэкслэши
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class RegexBuilder {

	/** Символы, имеющие в регулярках спеуиальное значение */
	@InternalElement
	public static final String REGEX_SYMBOLS = "\\<([{^-=$!|]})?*+.>";
	
	/** Билдер, в который добавляется контент регулярки */
	@InternalElement
	public StringBuilder builder = new StringBuilder();
	
	/** Запомненная строка, обозначающая кол-во символов */
	@InternalElement
	public String countString = "";
	
	/**
	 * Добавить текст, игнорируя значения символов регулярки
	 */
	public <T extends RegexBuilder> T text(String text)
	{
		for (char ch : REGEX_SYMBOLS.toCharArray())
		{
			text = text.replace(""+ch, "\\"+ch);
		}
		
		builder.append(text);
		return onAppendEnd();
	}
	
	/** НЕ указаные символы */
	public <T extends RegexBuilder> T not(String text)
	{
		builder.append("[^");
		text(text);
		builder.append("]");
		return (T) this;
	}
	
	/** Текст, повторяемый некоторое кол-во раз */
	public <T extends RegexBuilder> T text(int count, String text)
	{
		of(count);
		text(text);
		return (T) this;
	}
	
	/** Текст, повторяемый некоторое кол-во раз */
	public <T extends RegexBuilder> T text(int min, int max, String text)
	{
		of(min, max);
		text(text);
		return (T) this;
	}
	
	/** Добавить текст к регулярке */
	public <T extends RegexBuilder> T raw(String text)
	{
		builder.append(text);
		return onAppendEnd();
	}
	
	/** Сырой набор символов указанное кол-во раз */
	public <T extends RegexBuilder> T raw(int count, String text)
	{
		of(count);
		builder.append("[");
		raw(text);
		builder.append("]");
		return (T) this;
	}
	
	/** Сырой набор символов указанное в пределах от минимального, до максимального кол-ва раз */
	public <T extends RegexBuilder> T raw(int min, int max, String text)
	{
		of(min, max);
		builder.append("[");
		raw(text);
		builder.append("]");
		return (T) this;
	}
	
	/** Конец предыдущего совпадения */
	public <T extends RegexBuilder> T match()
	{
		builder.append("\\G");
		return onAppendEnd();
	}
	
	/** Начало строки */
	public <T extends RegexBuilder> T lns()
	{
		builder.append("^");
		return onAppendEnd();
	}
	
	/** Конец строки */
	public <T extends RegexBuilder> T lne()
	{
		builder.append("$");
		return onAppendEnd();
	}
	
	/** Граница строки */
	public <T extends RegexBuilder> T word()
	{
		builder.append("\\b");
		return onAppendEnd();
	}
	
	/** Граница слова */
	public <T extends RegexBuilder> T word(int count)
	{
		of(count);
		word();
		return (T) this;
	}
	
	/** Не граница слова */
	public <T extends RegexBuilder> T nword()
	{
		builder.append("\\B");
		return onAppendEnd();
	}
	
	/** Любой символ */
	public <T extends RegexBuilder> T any()
	{
		builder.append(".");
		return onAppendEnd();
	}
	
	/** Что угодно и сколько угодно */
	public <T extends RegexBuilder> T something()
	{
		return of().any();
	}
	
	/** Любой символ указанное кол-во раз */
	public <T extends RegexBuilder> T any(int count)
	{
		of(count);
		any();
		return (T) this;
	}
	
	/** Любой символ указанное в указанных пределах */
	public <T extends RegexBuilder> T any(int min, int max)
	{
		of(min, max);
		any();
		return (T) this;
	}
	
	/** Пробел */
	public <T extends RegexBuilder> T space()
	{
		builder.append("\\s");
		return onAppendEnd();
	}
	
	/** Пробельный символ указанное кол-во раз */
	public <T extends RegexBuilder> T space(int count)
	{
		of(count);
		space();
		return (T) this;
	}
	
	/** Пробельный символ в указанных пределах*/
	public <T extends RegexBuilder> T space(int min, int max)
	{
		of(min, max);
		space();
		return (T) this;
	}
	
	/** Любой символ, кроме пробела */
	public <T extends RegexBuilder> T nspace()
	{
		builder.append("\\S");
		return onAppendEnd();
	}
	
	/** НЕ пробельный символ укзанное кол-во раз */
	public <T extends RegexBuilder> T nspace(int count)
	{
		of(count);
		nspace();
		return (T) this;
	}
	
	/** НЕ пробельный символ в указанных пределах */
	public <T extends RegexBuilder> T nspace(int min, int max)
	{
		of(min, max);
		nspace();
		return (T) this;
	}
	
	/** Числовой символ */
	public <T extends RegexBuilder> T num()
	{
		builder.append("\\d");
		return onAppendEnd();
	}
	
	/** Числовой символ указанное кол-во раз */
	public <T extends RegexBuilder> T num(int count)
	{
		of(count);
		num();
		return (T) this;
	}
	
	/** Числовой символ в указанных пределах */
	public <T extends RegexBuilder> T num(int min, int max)
	{
		of(min, max);
		num();
		return (T) this;
	}
	
	/** Не-числовой символ указанное кол-во раз */
	public <T extends RegexBuilder> T nnum()
	{
		builder.append("\\D");
		return onAppendEnd();
	}
	
	/** Не-числовой символ */
	public <T extends RegexBuilder> T nnum(int count)
	{
		of(count);
		nnum();
		return (T) this;
	}
	
	/** Не-числовой символ в указанных пределах */
	public <T extends RegexBuilder> T nnum(int min, int max)
	{
		of(min, max);
		nnum();
		return (T) this;
	}
	
	/** Количиство символов, указанных после */
	public <T extends RegexBuilder> T of(int min, int max)
	{
		builder.append("{"+min+","+max+"}");
		return onAppendEnd();
	}
	
	/** Количиство символов, указанных после */
	public <T extends RegexBuilder> T of(int count)
	{
		countString = "{" + count + "}";
		return (T) this;
	}
	
	/** Сколько угодно символов, указанных после */
	public <T extends RegexBuilder> T of()
	{
		return some();
	}
	
	/** Сколько угодно символов, указанных после */
	public <T extends RegexBuilder> T some()
	{
		countString = "*";
		return (T) this;
	}
	
	/** Обработчик добавления символа regex'а */
	@InternalElement
	public <T extends RegexBuilder> T onAppendEnd()
	{
		if (countString != null)
			builder.append(countString);
		
		countString = null;
		
		return (T) this;
	}
	
	/** Получить готовую строку */
	public String build()
	{
		return builder.toString();
	}
}
