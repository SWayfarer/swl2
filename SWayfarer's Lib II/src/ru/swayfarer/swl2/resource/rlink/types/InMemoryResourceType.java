package ru.swayfarer.swl2.resource.rlink.types;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import lombok.var;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.resource.rlink.ResourceLink;
import ru.swayfarer.swl2.resource.rlink.ResourceType;
import ru.swayfarer.swl2.resource.streams.BytesInputStreamSWL;
import ru.swayfarer.swl2.resource.streams.BytesOutputStreamSWL;
import ru.swayfarer.swl2.resource.streams.DataInputStreamSWL;
import ru.swayfarer.swl2.resource.streams.DataOutputStreamSWL;

@SuppressWarnings("unchecked")
public class InMemoryResourceType extends ResourceType {

	public InMemoryResource resource = new InMemoryResource();
	
	@Override
	public FileSWL getFile(ResourceLink rlink)
	{
		return null;
	}

	@Override
	public URL getURL(ResourceLink rlink)
	{
		return null;
	}

	@Override
	public <T> T getSource(ResourceLink rlink)
	{
		return (T) resource;
	}

	@Override
	public boolean isExists(ResourceLink rlink)
	{
		return true;
	}

	@Override
	public <T extends InputStream> T getInputStream(ResourceLink rlink)
	{
		return (T) resource.getInputStream();
	}

	@Override
	public IExtendedList<ResourceLink> getAdjacentLinks(ResourceLink rlink, boolean isDeep, IFunction1<ResourceLink, Boolean> filter)
	{
		return CollectionsSWL.createExtendedList();
	}

	@Override
	public OutputStream getOutStream(ResourceLink rlink)
	{
		return resource.getOutStream();
	}

	@Override
	public boolean isWritable(ResourceLink rlink)
	{
		return false;
	}

	public static class InMemoryResource
	{
		public byte[] content = new byte[0];
		
		public synchronized DataInputStreamSWL getInputStream()
		{
			return BytesInputStreamSWL.createStream(content);
		}
		
		public synchronized DataOutputStreamSWL getOutStream()
		{
			var ret = BytesOutputStreamSWL.createStream();
			
			ret.eventClose.subscribe(() -> {
				
				synchronized (content)
				{
					content = ret.toBytesArray();
				}
			});
			
			return ret;
		}
	}
}
