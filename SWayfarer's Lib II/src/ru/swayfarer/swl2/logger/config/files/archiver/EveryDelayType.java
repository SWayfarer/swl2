package ru.swayfarer.swl2.logger.config.files.archiver;

public class EveryDelayType extends DelayType {

	public EveryDelayType()
	{
		// Года
		registerUnit((unit, delay) -> 
			(date, actual) -> Math.abs(date.getYear() - actual.getYear()) >= delay,
			"year", "years", "y");
				
		// Месяцы
		registerUnit((unit, delay) -> 
			(date, actual) -> Math.abs(date.getDayOfMonth() - actual.getDayOfYear()) >= delay,
			"month", "months");
		
		// Недели
		registerUnit((unit, delay) -> 
			(date, actual) -> Math.abs(date.getDayOfYear() - actual.getDayOfYear()) >= delay * 7,
			"week", "weeks", "w");
		
		// Дни
		registerUnit((unit, delay) -> 
			(date, actual) -> Math.abs(date.getDayOfYear() - actual.getDayOfYear()) >= delay,
			"days", "day", "d");
	
	}
	
}
