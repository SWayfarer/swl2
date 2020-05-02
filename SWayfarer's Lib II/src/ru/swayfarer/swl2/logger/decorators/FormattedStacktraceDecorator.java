package ru.swayfarer.swl2.logger.decorators;

import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.string.StringUtils;

public class FormattedStacktraceDecorator implements IFunction1<StackTraceElement, String> {

	public static String CLASS_NAME = "%class%";
	public static String SOURCE = "%source%";
	public static String LINE_NUMBER = "%lineNumber%";
	public static String METHOD_NAME = "%method%";
	public static String FILE = "%file%";
	
	public static String defaultStacktraceFormat = CLASS_NAME + "." + METHOD_NAME + "&{153,,4}(" + SOURCE + ":" + LINE_NUMBER + ")&{}&{h:2} &{147}[~" + FILE + "] &{h:1}";
	
	public String format;
	
	public FormattedStacktraceDecorator()
	{
		format = defaultStacktraceFormat;
	}
	
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
