package ru.swayfarer.swl2.asm.transformer.remapper;

import java.util.HashMap;
import java.util.Map;

public class FastRemapInfo extends RemapInfo {

	public Map<String, FastRemapInfo> fastFieldsMap = new HashMap<>();
	public Map<String, FastRemapInfo> fastMethodsMap = new HashMap<>();
	
	
	@Override
	public String getFieldKey(String owner, String name, String desc)
	{
		return name+"::"+desc;
	}
	
	@Override
	public String getMethodKey(String owner, String name, String desc)
	{
		return name+"::"+desc;
	}
	
}
