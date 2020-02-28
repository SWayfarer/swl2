package ru.swayfarer.swl2.resource.pathtransformers;

import java.text.SimpleDateFormat;
import java.util.Date;

import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Преобразователь дат в формате %date{format}%
 * @author swayfarer
 */
public class DatePathTransformer implements IFunction1<String, String> {

	/** Стандартный формат, который поставляется, если явно не указано даты */
	public static String defaultDateFormat = "(DD.MM.YYYY-HH.mm)";
	
	/** Регулярка, по которой находятся даты для замены */
	@InternalElement
	public String dateRegex;
			
	/** Константная дата. Если не будет указана, то при каждой замене будет использована актуальная */
	@InternalElement
	public Date date;
	
	/** Префикс (слово между % и перед { */
	@InternalElement
	public String prefix;
	
	/** Конструктор для создания трансформера с актуальной датой */
	public DatePathTransformer(String prefix)
	{
		this(null, prefix);
	}
	
	/** Конструктор для создания транфсормера с постоянной датой */
	public DatePathTransformer(Date date, String prefix)
	{
		ExceptionsUtils.IfEmpty(prefix, IllegalArgumentException.class, "Date transformer prefix can't be null or empty!");
		
		this.date = date;
		this.prefix = prefix;
		
		dateRegex = StringUtils.regex()
				.text("%" + prefix + "[")
				.something()
				.text("]%")
		.build();
	}

	/** Преобразовать путь */
	@Override
	public String apply(String path)
	{
		Date currentDate = this.date == null ? new Date() : this.date;
		
		if (StringUtils.isEmpty(path))
			return path;
		
		IExtendedList<String> dates = StringUtils.getAllMatches(dateRegex, path);
		
		for (String date : dates)
		{
			String format = date.substring(2 + prefix.length(), date.length() - 2);
			
			if (format.isEmpty())
				format = defaultDateFormat;
			
			path = path.replace(date, new SimpleDateFormat(format).format(currentDate));
		}
		
		return path;
	}
}
