package ru.swayfarer.swl2.logger.decorators;

import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Форматированный декоратор элементов стактрейса <br> 
 * 
 * @author swayfarer
 *
 */
public class FormattedStacktraceDecorator implements IFunction1<StackTraceElement, String> {

	/** Ключевое слово, которое будет заменено на имя класса, на который ссылается элемент стактрейса */
	@InternalElement
	public static String CLASS_NAME = "%class%";
	
	/** Ключевое слово, которое будет заменено на источник, на который ссылается элемент стактрейса */
	@InternalElement
	public static String SOURCE = "%source%";
	
	/** Ключевое слово, которое будет заменено на строку, на которую ссылается элемент стактрейса */
	@InternalElement
	public static String LINE_NUMBER = "%lineNumber%";
	
	/** Ключевое слово, которое будет заменено на метод, на который ссылается элемент стактрейса */
	@InternalElement
	public static String METHOD_NAME = "%method%";
	
	/** Ключевое слово, которое будет заменено на файл, в котором лежит класс, на который ссылается элемент стактрейса */
	@InternalElement
	public static String FILE = "%file%";
	
	/** Формат стактрейса, который будет использоваться по-умолчанию */
	@InternalElement
	public static String defaultStacktraceFormat = CLASS_NAME + "." + METHOD_NAME + "&{153,,4}(" + SOURCE + ":" + LINE_NUMBER + ")&{}&{h:2} &{147}[~" + FILE + "] &{h:1}";
	
	/** Формат элемента стактрейса */
	@InternalElement
	public String format;
	
	/** Конструктор с форматом по-умолчанию */
	public FormattedStacktraceDecorator()
	{
		format = defaultStacktraceFormat;
	}
	
	/** Конструктор с указанным форматом */
	public FormattedStacktraceDecorator(String format)
	{
		if (StringUtils.isEmpty(format))
			format = defaultStacktraceFormat;
		
		this.format = format;
	}
	
	@Override
	public String apply(StackTraceElement st)
	{
		String ret = format
			.replace(CLASS_NAME, st.getClassName() + "")
			.replace(LINE_NUMBER, String.valueOf(st.getLineNumber()) + "")
			.replace(SOURCE, st.getFileName() + "")
			.replace(METHOD_NAME, st.getMethodName() + "")
		;
		
		if (ret.contains(FILE))
		{
			ret = ret.replace(FILE, ReflectionUtils.getClassSource(st.getClassName()));
		}
		
		return ret;
	}
}
