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

/**
 * Форматтер логов, раскрывающий определенный шаблон:
 * <br> %file% - Источник лога (сорцы)
 * <br> %level% - Уровень лога
 * <br> %thread% - Поток, из которого выполняется лог
 * <br> %logger% - Логгер, который выполняет лог 
 * <br> %text% - Текст лога
 * <br> %from% - Сокращенная запись источника лога. Как в стакстрейсе (файл и строка)
 * <br> %fromFull% - Полная запись источника лога 
  * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
@Data
public class TemplateLogFormatter implements IFunction2NoR<ILogger, LogInfo>{

	/** Текущий шаблон */
	public String template = "[%logger%] [%level%]: %text%";
	
	/** Форматтеры */
	public List<IFunction2NoR<ILogger, LogInfo>> formatRules = new ArrayList<>();
	
	/** Конструктор */
	public TemplateLogFormatter()
	{
		initDefaultRules();
	}
	
	/** Загрузка стандартных правил */
	public void initDefaultRules()
	{
		addRule((logger, logInfo) -> {
			
			StackTraceElement callSource = logInfo.callTrace.getFirstElement();
			
			String text = template
				.replace("%file%", callSource.getFileName())
				.replace("%level%", logInfo.getLevel().getPrefix())
				.replace("%thread%", logInfo.getThreadName())
				.replace("%logger%", logInfo.getLogger().getName())
				.replace("%text%", logInfo.getContent()+"")
				.replace("%from%", callSource.getFileName()+":"+callSource.getLineNumber())
				.replace("%fromFull%", callSource.toString())
			;
				
			logInfo.content = logInfo.getLevel().getLogPrefix() + text;
		});
		
		formatRules.add(new DateFormatRule());
	}
	
	/** Задать формат */
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
	
	/** Зарегистрировать правило */
	public <T extends TemplateLogFormatter> T addRule(IFunction2NoR<ILogger, LogInfo> rule)
	{
		formatRules.add(rule);
		return (T) this;
	}
	
	/** Создать форматтер с указанным форматом */
	public static TemplateLogFormatter of(@ConcattedString Object... format)
	{
		TemplateLogFormatter ret = new TemplateLogFormatter();
		ret.template = StringUtils.concat(format);
		
		return ret;
	}
}
