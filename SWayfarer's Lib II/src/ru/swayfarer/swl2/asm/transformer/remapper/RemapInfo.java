package ru.swayfarer.swl2.asm.transformer.remapper;

import java.util.HashMap;
import java.util.Map;

import ru.swayfarer.swl2.json.JsonUtils;
import ru.swayfarer.swl2.resource.file.FileSWL;

/**
 * Информация о ремаппинге
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class RemapInfo {

	/** Маппинги классов */
	public Map<String, String> classMappings = new HashMap<>();
	
	/** Маппинги полей */
	public Map<String, String> fieldMappings = new HashMap<>();
	
	/** Маппинги методов*/
	public Map<String, String> methodMappings = new HashMap<>();
	
	/** Получить маппинг филда */
	public String getFieldMapping(String name, String owner, String desc)
	{
		return fieldMappings.get(getFieldKey(owner, name, desc));
	}

	/** Получить маппинг метода */
	public String getMethodMapping(String name, String owner, String desc)
	{
		return methodMappings.get(getMethodKey(owner, name, desc));
	}
	
	/** Получить маппинг класса */
	public String getClassMapping(String name)
	{
		return classMappings.get(getClassKey(name));
	}
	
	/** Получить ключ поля для карты маппингов */
	public String getFieldKey(String owner, String name, String desc)
	{
		return owner+"::"+name+"::"+desc;
	}
	
	/** Получить ключ метода для карты маппингов */
	public String getMethodKey(String owner, String name, String desc)
	{
		return owner+"::"+name+"::"+desc;
	}
	
	/** Задать маппинг класса для карты маппингов */
	public String getClassKey(String name)
	{
		return name;
	}
	
	/** Задать маппинг для класса */
	public <RemapInfo_Type extends RemapInfo> RemapInfo_Type setClassMapping(String before, String after)
	{
		classMappings.put(getClassKey(before), after);
		
		return (RemapInfo_Type) this;
	}
	
	/** Задать маппинг для поля */
	public <RemapInfo_Type extends RemapInfo> RemapInfo_Type setFieldMapping(String owner, String name, String desc, String newName)
	{
		fieldMappings.put(getFieldKey(owner, name, desc), newName);
		
		return (RemapInfo_Type) this;
	}
	
	/** Задать маппинг для метода */
	public <RemapInfo_Type extends RemapInfo> RemapInfo_Type setMethodMapping(String owner, String name, String desc, String newName)
	{
		methodMappings.put(getMethodKey(owner, name, desc), newName);
		
		return (RemapInfo_Type) this;
	}
	
	/** Загрузить маппинг из Json */
	public static RemapInfo valueOf(String rlink)
	{
		return JsonUtils.loadFromJson(rlink, RemapInfo.class);
	}
	
	/** Сохранить маппинг в Json */
	public void saveTo(FileSWL file)
	{
		JsonUtils.saveToFile(this, file);
	}
}
