package ru.swayfarer.swl2.asm.transformer.exists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.resource.rlink.RLUtils;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Контейнер для условий существования элементов 
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class ExistsConditionContainer {

	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Карта с условиями, где ключ - название условия, значение - значение */
	public Map<String, Boolean> conditions = new HashMap<>();
	
	/** Кастомные условия */
	public List<ICustomExistsCondition> customConditions = new ArrayList<>();
	
	/** Задать условие */
	public <ExistsManager_Type extends ExistsConditionContainer> ExistsManager_Type setCondition(String name, boolean value)
	{
		name = name.toLowerCase();
		conditions.put(name, value);
		return (ExistsManager_Type) this;
	}
	
	/** Получить условие */
	public boolean getCondition(String name)
	{
		for (ICustomExistsCondition condition : customConditions)
		{
			try
			{
				if (condition.isCompleted(name))
					return true;
			}
			catch (Throwable e)
			{
				logger.error(e, "Error while checking custom condition", condition, "for", name);
			}
		}
		return conditions.containsKey(name) ? conditions.get(name) : false;
	}
	
	public boolean isConditionsCompleted(List<String> conditions)
	{
		if (conditions == null || conditions.isEmpty())
			return true;
		
		for (String condition : conditions)
		{
			if (!getCondition(condition))
				return false;
		}
		
		return true;
	}
	
	public <ExistsConditionContainer_Type extends ExistsConditionContainer> ExistsConditionContainer_Type addClassPathCondition()
	{
		return addCustomCondition(new ClasspathExistsCondition());
	}
	
	public <ExistsConditionContainer_Type extends ExistsConditionContainer> ExistsConditionContainer_Type addCustomCondition(ICustomExistsCondition condition)
	{
		if (!customConditions.contains(condition))
			customConditions.add(condition);
		
		return (ExistsConditionContainer_Type) this;
	}
	
	public static interface ICustomExistsCondition {
		public boolean isCompleted(String condition) throws Throwable;
	}
	
	public static class ClasspathExistsCondition implements ICustomExistsCondition {

		@Override
		public boolean isCompleted(String condition) throws Throwable
		{
			if (StringUtils.isEmpty(condition))
				return false;
			
			if (condition.toLowerCase().startsWith("cl:"))
			{
				condition = condition.substring(3);
				
				condition = prepareName(condition);
				
				return RLUtils.exists(condition);
			}
			
			return false;
		}
		
		public String prepareName(String name)
		{
			if (StringUtils.isEmpty(name))
			{
				return name;
			}
			
			if (name.contains("."))
				name = name.replace(".", "/");
			
			if (!name.endsWith(".class"))
				name += ".class";
			
			if (!name.startsWith("/"))
				name = "/" + name;
			
			return name;
		}
		
	}
}
