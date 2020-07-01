package ru.swayfarer.swl2.swconf2.formats;

import lombok.var;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.resource.rlink.ResourceLink;
import ru.swayfarer.swl2.resource.streams.DataInputStreamSWL;
import ru.swayfarer.swl2.swconf2.types.SwconfTable;
import ru.swayfarer.swl2.swconf2.yaml.Yaml2SwconfProvider;

public class SwconfFormats {

	public IExtendedList<IFormatProvider> defaultFormatProviders = CollectionsSWL.createExtendedList(
			new Yaml2SwconfProvider()
	);
	
	public IExtendedList<IFormatProvider> registeredFormatProviders = CollectionsSWL.createExtendedList();
	
	public IFormatProvider findProviderForResource(String resourceName)
	{
		var extension = ResourceLink.getExtension(resourceName);
		IFormatProvider ret = registeredFormatProviders.dataStream().first((provider) -> provider.isAcceptingExtension(extension));
		
		if (ret != null)
			return ret;
		
		ret = defaultFormatProviders.dataStream().first((provider) -> provider.isAcceptingExtension(extension));
		
		return ret;
	}
	
	public SwconfTable readResource(ResourceLink rlink)
	{
		var provider = findProviderForResource(rlink.content);
		
		if (provider == null)
		{
			return null;
		}
		
		DataInputStreamSWL stream = rlink.toStream();
		
		return stream == null ? null : provider.readSwconf(stream);
	}
	
	public void writeResource(SwconfTable swconfTable, ResourceLink rlink)
	{
		var provider = findProviderForResource(rlink.content);
		
		if (provider == null)
		{
			return;
		}
		
		provider.writeSwconf(swconfTable, rlink.toOutStream());
	}
}
