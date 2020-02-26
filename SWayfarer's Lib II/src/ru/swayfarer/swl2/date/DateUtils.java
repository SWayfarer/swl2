package ru.swayfarer.swl2.date;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {

	public static final long MILISIS_IN_SECOND = 1000;
	public static final long MILISIS_IN_MINUTE = MILISIS_IN_SECOND * 60;
	public static final long MILISIS_IN_HOUR = MILISIS_IN_MINUTE * 60;
	public static final long MILISIS_IN_DAY = MILISIS_IN_HOUR * 24;
	public static final long MILISIS_IN_WEEK = MILISIS_IN_DAY * 7;

	public static long toSecs(long milisis)
	{
		return milisis / MILISIS_IN_SECOND;
	}
	
	public static long toMinutes(long milisis)
	{
		return milisis / MILISIS_IN_MINUTE;
	}
	
	public static String getCurrentDate(String format)
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(new Date());
	}
	
	public static long toHours(long milisis)
	{
		return milisis / MILISIS_IN_HOUR;
	}
	
	public static long toDays(long milisis)
	{
		return milisis / MILISIS_IN_DAY;
	}
	
	public static long toWeeks(long milisis)
	{
		return milisis / MILISIS_IN_WEEK;
	}
	
	public static Date asDate(LocalDate localDate) {
        return asDate(localDate.atStartOfDay());
    }
	
	public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
	
	public static long asTime(LocalDate localDate) {
        return asDate(localDate).getTime();
    }
	
	public static long asTime(LocalDateTime localDateTime) {
        return asDate(localDateTime).getTime();
    }
	
	public static LocalDate getCurrentLocalDate()
	{
		Date date = new Date();
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return localDate;
	}
	
}
