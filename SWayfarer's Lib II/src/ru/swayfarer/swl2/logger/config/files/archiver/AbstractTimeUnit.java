package ru.swayfarer.swl2.logger.config.files.archiver;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2;

public abstract class AbstractTimeUnit {

	public abstract boolean isAccepts(String unit);
	public abstract IFunction2<LocalDateTime, LocalDateTime, Boolean> getCondition(String timeUnit, long delayValue);
	
	public static AbstractTimeUnit of(IFunction2<String, Long, IFunction2<LocalDateTime, LocalDateTime, Boolean>> conditionGenerator, String... units)
	{
		return WithFun.of(conditionGenerator, CollectionsSWL.createExtendedList(units));
	}
	
	@AllArgsConstructor(staticName = "of")
	public static class WithFun extends AbstractTimeUnit {

		public IFunction2<String, Long, IFunction2<LocalDateTime, LocalDateTime, Boolean>> conditionGenerator;
		public IExtendedList<String> units = CollectionsSWL.createExtendedList();
		
		@Override
		public boolean isAccepts(String unit)
		{
			return units != null && units.contains(unit);
		}

		@Override
		public IFunction2<LocalDateTime, LocalDateTime, Boolean> getCondition(String timeUnit, long delayValue)
		{
			return conditionGenerator == null ? null : conditionGenerator.apply(timeUnit, delayValue);
		}
		
	}
	
}
