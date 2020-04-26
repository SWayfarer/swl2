package ru.swayfarer.swl2.resource.pathtransformers;

import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.string.StringUtils;

public class KeyCodesTransformer implements IFunction1<String, String> {

	@Override
	public String apply(String str)
	{
		str = StringUtils.replaceUnicodeFlags(str);
		str = StringUtils.replaceASCIIFlags(str);
		
		return str;
	}

}
