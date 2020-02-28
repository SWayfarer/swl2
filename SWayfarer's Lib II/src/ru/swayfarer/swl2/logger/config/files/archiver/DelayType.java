package ru.swayfarer.swl2.logger.config.files.archiver;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2;
import ru.swayfarer.swl2.markers.InternalElement;

/**
 * Типы разницы 
 * <br> Первое слово в формате записи архивации. Например, в every 10 min, тип разницы - every
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class DelayType {

	/** Зарегистрированные {@link TimeUnit}'ы */
	@InternalElement
	public IExtendedList<AbstractTimeUnit> registeredTimeUnits = CollectionsSWL.createExtendedList(); 
	
	/** Получить условие */
	public IFunction2<LocalDateTime, LocalDateTime, Boolean> getCondition(String timeUnit, long delayValue)
	{
		AbstractTimeUnit unit = registeredTimeUnits.dataStream().filter((time) -> time.isAccepts(timeUnit)).first();
		return unit == null ? null : unit.getCondition(timeUnit, delayValue);
	}
	
	/** Регистрация новой еденицы измерения времени */
	public <T extends DelayType> T registerUnit(AbstractTimeUnit unit)
	{
		registeredTimeUnits.addExclusive(unit);
		return (T) this;
	}
	
	/** Регистрация новой еденицы измерения времени */
	public <T extends DelayType> T registerUnit(IFunction2<String, Long, IFunction2<LocalDateTime, LocalDateTime, Boolean>> conditionGenerator, String... units)
	{
		return registerUnit(AbstractTimeUnit.of(conditionGenerator, units));
	}
}
