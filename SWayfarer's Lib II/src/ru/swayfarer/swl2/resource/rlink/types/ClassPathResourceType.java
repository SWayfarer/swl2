package ru.swayfarer.swl2.resource.rlink.types;

import java.net.URL;

import ru.swayfarer.swl2.resource.rlink.ResourceLink;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Тип ресурсов, размещенных в Classpath
 * @author swayfarer
 *
 */
public class ClassPathResourceType extends UrlResourceType {
	
	/** Получить {@link URL} из {@link ResourceLink} */
	@Override
	public URL rlinkToURL(ResourceLink rlink)
	{
		String content = rlink.content;
		
		if (StringUtils.isEmpty(content))
			return null;
		
		if (content.startsWith("/"))
			content = content.substring(1);
		
		return ClassPathResourceType.class.getClassLoader().getResource(content);
	}

}
