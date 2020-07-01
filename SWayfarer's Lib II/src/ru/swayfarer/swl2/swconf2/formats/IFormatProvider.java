package ru.swayfarer.swl2.swconf2.formats;

import ru.swayfarer.swl2.resource.streams.DataInputStreamSWL;
import ru.swayfarer.swl2.resource.streams.DataOutputStreamSWL;
import ru.swayfarer.swl2.swconf2.types.SwconfTable;

public interface IFormatProvider {

	public boolean isAcceptingExtension(String ext);
	public SwconfTable readSwconf(DataInputStreamSWL is);
	public void writeSwconf(SwconfTable swconfTable, DataOutputStreamSWL os);
	
}
