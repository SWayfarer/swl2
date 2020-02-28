package ru.swayfarer.swl2.logger.config.files.archiver;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2;
import ru.swayfarer.swl2.markers.InternalElement;

public abstract class AbstractTimeUnit {

	/** Подходит ли текст под юнит? (например, min - минуты) */
	public abstract boolean isAccepts(String unit);
	
	/** Получить условие для логгера, при выполнении которого будет записываться файл */
	public abstract IFunction2<LocalDateTime, LocalDateTime, Boolean> getCondition(String timeUnit, long delayValue);
	
	/** Получить новую {@link AbstractTimeUnit} с указанными условием и сокращенными записями (см {@link #isAccepts(String)}) */
	public static AbstractTimeUnit of(IFunction2<String, Long, IFunction2<LocalDateTime, LocalDateTime, Boolean>> conditionGenerator, String... units)
	{
		return WithFun.of(conditionGenerator, CollectionsSWL.createExtendedList(units));
	}
	
	/** {@link AbstractTimeUnit}, принимаюший функцию в качестве условия для архивирования */
	@AllArgsConstructor(staticName = "of")
	public static class WithFun extends AbstractTimeUnit {

		/** Генератор условий */
		@InternalElement
		public IFunction2<String, Long, IFunction2<LocalDateTime, LocalDateTime, Boolean>> conditionGenerator;
		
		/** Возможные записи (см {@link AbstractTimeUnit#isAccepts(String)} */
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
