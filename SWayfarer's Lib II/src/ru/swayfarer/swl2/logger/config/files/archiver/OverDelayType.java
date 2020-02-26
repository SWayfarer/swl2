package ru.swayfarer.swl2.logger.config.files.archiver;

import ru.swayfarer.swl2.date.DateUtils;

public class OverDelayType extends DelayType {

	public OverDelayType()
	{
		// Секунды
		registerUnit((unit, delay) -> 
			(date, actual) -> Math.abs(DateUtils.toSecs(DateUtils.asTime(date)) - DateUtils.toSecs(DateUtils.asTime(actual))) >= delay,
			"sec", "secs", "s");
		
		// Минуты
		registerUnit((unit, delay) -> 
			(date, actual) -> Math.abs(DateUtils.toMinutes(DateUtils.asTime(date)) - DateUtils.toMinutes(DateUtils.asTime(actual))) >= delay,
			"min", "minute", "minutes", "mins");
		
		// Часы
		registerUnit((unit, delay) -> 
			(date, actual) -> Math.abs(DateUtils.toHours(DateUtils.asTime(date)) - DateUtils.toHours(DateUtils.asTime(actual))) >= delay,
			"hour", "h", "hours");
		
		// Дни
		registerUnit((unit, delay) -> 
			(date, actual) -> Math.abs(DateUtils.toDays(DateUtils.asTime(date)) - DateUtils.toDays(DateUtils.asTime(actual))) >= delay,
			"day", "days", "d");
		
		// Недели
		registerUnit((unit, delay) -> 
			(date, actual) -> Math.abs(DateUtils.toWeeks(DateUtils.asTime(date)) - DateUtils.toWeeks(DateUtils.asTime(actual))) >= delay,
			"week", "weeks", "w");
	}
	
}
