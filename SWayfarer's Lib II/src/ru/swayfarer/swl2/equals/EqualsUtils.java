package ru.swayfarer.swl2.equals;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;

/**
 * Утилиты для проверки равенства 
 * @author swayfarer
 */
public class EqualsUtils {

	/**
	 * Проверка на равенство объектов через {@link Object#equals(Object)} со всеми указанными объектами 
	 * @param obj Объект, который сравнивается
	 * @param objects Объекты, с которыми сравниваются 
	 */
	public static boolean objectEqualsAll(Object obj, Object... objects)
	{
		return objectEquals(obj, true, objects);
	}
	
	/**
	 * Проверка на равенство объектов через {@link Object#equals(Object)} с хотя бы одним указанным объектом 
	 * @param obj Объект, который сравнивается
	 * @param objects Объекты, с которыми сравниваются 
	 */
	public static boolean objectEqualsSome(Object obj, Object... objects)
	{
		return objectEquals(obj, false, objects);
	}
	
	/**
	 * Проверка на равенство объектов через {@link Object#equals(Object)}
	 * @param obj Объект, который сравнивается
	 * @param allMatches Все ли должны быть равны? 
	 * @param objects Объекты, с которыми сравниваются 
	 */
	public static boolean objectEquals(Object obj, boolean allMatches, Object... objects)
	{
		ExceptionsUtils.If(CollectionsSWL.isNullOrEmpty(objects), IllegalArgumentException.class, "Comparable objects array cannot be null or empty!");
		
		for (Object o : objects)
		{
			if (obj == null && o != null && allMatches)
				return false;
			else if (!allMatches && obj == null && o == null)
				return true;
			else if (obj != null)
			{
				boolean isMatches = obj.equals(o);
				
				if (allMatches && !isMatches)
					return false;
				else if (isMatches)
					return true;
			}
		}
		
		return allMatches;
	}
	
}
