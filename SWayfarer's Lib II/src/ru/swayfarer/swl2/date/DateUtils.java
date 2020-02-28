package ru.swayfarer.swl2.date;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

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

	/** Перевести милисекунды в секунды */
	public static long toSecs(long milisis)
	{
		return milisis / MILISIS_IN_SECOND;
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
	
}
