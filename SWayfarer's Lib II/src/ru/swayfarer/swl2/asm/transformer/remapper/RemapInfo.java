package ru.swayfarer.swl2.asm.transformer.remapper;

import java.util.HashMap;
import java.util.Map;

import ru.swayfarer.swl2.json.JsonUtils;
import ru.swayfarer.swl2.resource.file.FileSWL;

@SuppressWarnings("unchecked")
public class RemapInfo {

	public Map<String, String> classMappings = new HashMap<>();
	public Map<String, String> fieldMappings = new HashMap<>();
	public Map<String, String> methodMappings = new HashMap<>();
	
	public String getFieldMapping(String name, String owner, String desc)
	{
		return fieldMappings.get(getFieldKey(owner, name, desc));
	}
	
	public String getMethodMapping(String name, String owner, String desc)
	{
		return methodMappings.get(getMethodKey(owner, name, desc));
	}
	
	public String getClassMapping(String name)
	{
		return classMappings.get(getClassKey(name));
	}
	
	public String getFieldKey(String owner, String name, String desc)
	{
		return owner+"::"+name+"::"+desc;
	}
	
	public String getMethodKey(String owner, String name, String desc)
	{
		return owner+"::"+name+"::"+desc;
	}
	
	public <RemapInfo_Type extends RemapInfo> RemapInfo_Type setClassMapping(String before, String after)
	{
		classMappings.put(getClassKey(before), after);
		
		return (RemapInfo_Type) this;
	}
	
	public <RemapInfo_Type extends RemapInfo> RemapInfo_Type setFieldMapping(String owner, String name, String desc, String newName)
	{
		fieldMappings.put(getFieldKey(owner, name, desc), newName);
		
		return (RemapInfo_Type) this;
	}
	
	public <RemapInfo_Type extends RemapInfo> RemapInfo_Type setMethodMapping(String owner, String name, String desc, String newName)
	{
		methodMappings.put(getMethodKey(owner, name, desc), newName);
		
		return (RemapInfo_Type) this;
	}
	
	public String getClassKey(String name)
	{
		return name;
	}
	
	public static RemapInfo valueOf(String rlink)
	{
		return JsonUtils.loadFromJson(rlink, RemapInfo.class);
	}
	
	public void saveTo(FileSWL file)
	{
		JsonUtils.saveToFile(this, file);
	}
}
