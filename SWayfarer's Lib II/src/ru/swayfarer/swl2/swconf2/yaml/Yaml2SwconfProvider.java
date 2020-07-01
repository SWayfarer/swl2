package ru.swayfarer.swl2.swconf2.yaml;

import lombok.var;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.resource.streams.DataInputStreamSWL;
import ru.swayfarer.swl2.resource.streams.DataOutputStreamSWL;
import ru.swayfarer.swl2.swconf2.formats.IFormatProvider;
import ru.swayfarer.swl2.swconf2.types.SwconfTable;

public class Yaml2SwconfProvider implements IFormatProvider{

	private static IExtendedList<String> extensions = CollectionsSWL.createExtendedList("yaml", "yml");

	@Override
	public boolean isAcceptingExtension(String ext)
	{
		return extensions.contains(ext);
	}

	@Override
	public SwconfTable readSwconf(DataInputStreamSWL is)
	{
		return new Yaml2SwconfReader().readYaml(is);
	}

	@Override
	public void writeSwconf(SwconfTable swconfTable, DataOutputStreamSWL os)
	{
		var str = new Yaml2SwconfWriter().write(swconfTable);
		os.writeString(str);
	}

}
