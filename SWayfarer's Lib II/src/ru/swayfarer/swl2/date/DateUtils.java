package ru.swayfarer.swl2.date;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import lombok.var;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedMap;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.string.reader.StringReaderSWL;

/**
 * Утилиты для работы с датами
 * @author swayfarer
 *
 */
public class DateUtils {

	
	
	/** Кол-во милисекунд в секунде */
	public static final long MILISIS_IN_SECOND = 1000;
	
	/** Кол-во милисекунд в секунде */
	public static final long MILISIS_IN_MINUTE = MILISIS_IN_SECOND * 60;
	
	/** Кол-во милисекунд в минуте */
	public static final long MILISIS_IN_HOUR = MILISIS_IN_MINUTE * 60;
	
	/** Кол-во милисекунд в часу */
	public static final long MILISIS_IN_DAY = MILISIS_IN_HOUR * 24;
	
	/** Кол-во милисекунд в недели */
	public static final long MILISIS_IN_WEEK = MILISIS_IN_DAY * 7;

	public static IExtendedMap<String, Long> registeredTimeUnits = CollectionsSWL.createExtendedMap(
			"sec", MILISIS_IN_SECOND,
			"min", MILISIS_IN_MINUTE,
			"hour", MILISIS_IN_HOUR,
			"day", MILISIS_IN_DAY,
			"week", MILISIS_IN_WEEK
	);
	
	/** Перевести милисекунды в секунды */
	public static long toSecs(long milisis)
	{
		return milisis / MILISIS_IN_SECOND;
	}
	
	/** Перевести секунды в милисекунды */
	public static long fromSecs(long secs)
	{
		return secs * MILISIS_IN_SECOND;
	}
	
	/** Перевести минуты в милисекунды */
	public static long fromMins(long mins)
	{
		return mins * MILISIS_IN_MINUTE;
	}
	
	/** Перевести часы в милисекунды */
	public static long fromHours(long hours)
	{
		return hours * MILISIS_IN_HOUR;
	}
	
	/** Перевести дни в милисекунды */
	public static long fromDays(long days)
	{
		return days * MILISIS_IN_DAY;
	}
	
	/** Перенести недели в милисекунды */
	public static long fromWeeks(long weeks)
	{
		return weeks * MILISIS_IN_WEEK;
	}
	
	/** Перевести милисекунды в минуты */
	public static long toMinutes(long milisis)
	{
		return milisis / MILISIS_IN_MINUTE;
	}
	
	/** Получить текущую дату в указанном формате */
	public static String getCurrentDate(String format)
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(new Date());
	}
	
	/** Перевести милисекунды в часы */
	public static long toHours(long milisis)
	{
		return milisis / MILISIS_IN_HOUR;
	}
	
	/** Перевести милисекунды в дни */
	public static long toDays(long milisis)
	{
		return milisis / MILISIS_IN_DAY;
	}
	
	/** Перевести милисекунды в недели */
	public static long toWeeks(long milisis)
	{
		return milisis / MILISIS_IN_WEEK;
	}
	
	/** {@link LocalDate} в {@link Date} */
	public static Date asDate(LocalDate localDate) {
        return asDate(localDate.atStartOfDay());
    }
	
	/** {@link LocalDateTime} в {@link Date} */
	public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
	
	/** {@link LocalDate} в милисекунды*/
	public static long asTime(LocalDate localDate) {
        return asDate(localDate).getTime();
    }
	
	/** {@link LocalDateTime} в милисекунды */
	public static long asTime(LocalDateTime localDateTime) {
        return asDate(localDateTime).getTime();
    }
	
	/** Получить текущуб дату в формате {@link LocalDate} */
	public static LocalDate getCurrentLocalDate()
	{
		Date date = new Date();
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return localDate;
	}
	
	/** 
	 * 
	 * @param time
	 * @return
	 */
	public static long getMilisis(String time)
	{
		var reader = new StringReaderSWL(time);
		
		int index = 0;
		
		while (reader.hasNextElement())
		{
			if (reader.skipSome(" ") || reader.skipSome(StringUtils.TAB)) 
			{
				index ++;
				// Nope!
			}
			else if (!StringUtils.isDouble(reader.next()))
				break;
			else
				index ++;
		}
		
		if (index < time.length())
		{
			var value = time.substring(0, index);
			var unit = time.substring(index);
			unit = StringUtils.removeLastWhitespaces(unit);
			
			Long unitModifier = registeredTimeUnits.get(unit);
			
			ExceptionsUtils.IfNull(unitModifier, IllegalArgumentException.class, "Time unit '" + unit + "' is not registered! Please, register it or use another! Available units:", registeredTimeUnits.keySet());
			
			reader.close();
			return Long.valueOf(value.trim()) * unitModifier;
			
		}
		
		reader.close();
		return Long.valueOf(time);
	}
}
