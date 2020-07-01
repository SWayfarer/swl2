package ru.swayfarer.swl2.swconf2.helper;

import lombok.var;
import ru.swayfarer.swl2.resource.rlink.ResourceLink;
import ru.swayfarer.swl2.resource.streams.DataInputStreamSWL;
import ru.swayfarer.swl2.resource.streams.DataOutputStreamSWL;
import ru.swayfarer.swl2.swconf2.formats.SwconfFormats;
import ru.swayfarer.swl2.swconf2.mapper.SwconfSerialization;
import ru.swayfarer.swl2.swconf2.types.SwconfTable;

public class SwconfIO {

	public SwconfSerialization serialization = SwconfSerialization.defaultMappers();
	public SwconfFormats formats = new SwconfFormats();
	
	public <T> T deserialize(T instance, ResourceLink rlink)
	{
		return deserialize(instance, rlink.toStream(), rlink.content);
	}
	
	public <T> T deserialize(T instance, DataInputStreamSWL stream, String resourceName)
	{
		var swconf = formats.readResource(stream, resourceName);
		
		if (swconf != null)
		{
			return serialization.deserialize(instance, swconf);
		}
		
		return null;
	}
	
	public <T> T deserialize(Class<T> classOfObj, ResourceLink rlink)
	{
		return deserialize(classOfObj, rlink.toStream(), rlink.content);
	}
	
	public <T> T deserialize(Class<T> classOfObj, DataInputStreamSWL stream, String resourceName)
	{
		var swconf = formats.readResource(stream, resourceName);
		
		if (swconf != null)
		{
			return serialization.deserialize(classOfObj, swconf);
		}
		
		return null;
	}
	
	public void serialize(Object instance, ResourceLink rlink)
	{
		serialize(instance, rlink.toOutStream(), rlink.content);
	}
	
	public void serialize(Object instance, DataOutputStreamSWL dos, String resourceName)
	{
		var swconf = serialization.serialize(instance);
		
		if (swconf != null && swconf instanceof SwconfTable)
		{
			formats.writeResource(swconf.asTable(), dos, resourceName);
		}
	}
}
