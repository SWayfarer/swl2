package ru.swayfarer.swl2.swconf2.mapper;

import ru.swayfarer.swl2.swconf2.types.SwconfObject;

public interface ISwconfMapper<Swconf_Type extends SwconfObject, Result_Type> {

	public boolean isAccepts(MappingInfo mappingInfo);
	
	public void read(Swconf_Type swconf, MappingInfo mappingInfo);
	public Swconf_Type write(MappingInfo mappingInfo);
	
}
