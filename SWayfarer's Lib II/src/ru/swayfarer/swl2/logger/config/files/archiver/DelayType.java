package ru.swayfarer.swl2.logger.config.files.archiver;

import java.time.LocalDateTime;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2;

@SuppressWarnings("unchecked")
public class DelayType {

	public IExtendedList<AbstractTimeUnit> registeredTimeUnits = CollectionsSWL.createExtendedList(); 
	
	public IFunction2<LocalDateTime, LocalDateTime, Boolean> getCondition(String timeUnit, long delayValue)
	{
		AbstractTimeUnit unit = registeredTimeUnits.dataStream().filter((time) -> time.isAccepts(timeUnit)).first();
		return unit == null ? null : unit.getCondition(timeUnit, delayValue);
	}
	
	public <T extends DelayType> T registerUnit(AbstractTimeUnit unit)
	{
		registeredTimeUnits.addExclusive(unit);
		return (T) this;
	}
	
	public <T extends DelayType> T registerUnit(IFunction2<String, Long, IFunction2<LocalDateTime, LocalDateTime, Boolean>> conditionGenerator, String... units)
	{
		return registerUnit(AbstractTimeUnit.of(conditionGenerator, units));
	}
}
