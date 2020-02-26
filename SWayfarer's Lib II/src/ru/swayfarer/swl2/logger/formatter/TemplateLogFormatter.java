package ru.swayfarer.swl2.logger.formatter;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LogInfo;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.string.StringUtils;

@SuppressWarnings("unchecked")
@Data
public class TemplateLogFormatter implements IFunction2NoR<ILogger, LogInfo>{

	public String template = "[%logger%] [%level%]: %text%";
	
	public List<IFunction2NoR<ILogger, LogInfo>> formatRules = new ArrayList<>();
	
	public TemplateLogFormatter()
	{
		initDefaultRules();
	}
	
	public void initDefaultRules()
	{
		addRule((logger, logInfo) -> {
			
			StackTraceElement callSource = logInfo.callTrace[0];
			
			String text = template
				.replace("%file%", callSource.getFileName())
				.replace("%level%", logInfo.getLevel().getPrefix())
				.replace("%thread%", logInfo.getThreadName())
				.replace("%logger%", logInfo.getLogger().getName())
				.replace("%text%", logInfo.getContent()+"")
				.replace("%from%", callSource.getFileName()+":"+callSource.getLineNumber())
				.replace("%fromFull%", callSource.toString())
			;
				
			logInfo.content = text;
		});
		
		formatRules.add(new DateFormatRule());
	}
	
	public <T extends TemplateLogFormatter> T setFormat(@ConcattedString Object... text)
	{
		String s = StringUtils.concat(text);
		
		ExceptionsUtils.If(s == null || s.isEmpty(), IllegalArgumentException.class, "Formatter template can't be null or empty!");
		
		this.template = s;
		
		return (T) this;
	}
	
	@Override
	public void applyNoR(ILogger logger, LogInfo info)
	{
		formatRules.forEach((rule) -> rule.apply(logger, info));
	}
	
	public <T extends TemplateLogFormatter> T addRule(IFunction2NoR<ILogger, LogInfo> rule)
	{
		formatRules.add(rule);
		return (T) this;
	}
	
	public static TemplateLogFormatter of(@ConcattedString Object... format)
	{
		TemplateLogFormatter ret = new TemplateLogFormatter();
		ret.template = StringUtils.concat(format);
		
		return ret;
	}
}
