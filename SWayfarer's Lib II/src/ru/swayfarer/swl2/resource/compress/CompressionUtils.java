package ru.swayfarer.swl2.resource.compress;

import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction3NoR;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.resource.streams.DataOutputStreamSWL;
import ru.swayfarer.swl2.resource.streams.StreamsUtils;

/** 
 * Утилиты для работы со сжатием
 * @author swayfarer
 *
 */
public class CompressionUtils {

	/** Функции-архиверы */
	public static class Archivers {
		
		public static IFunction3NoR<FileSWL, FileSWL, String> gzArchiver = (file, target, name) -> {
			
			if (!file.exists())
				return;
			
			target.createIfNotFoundDir();
			
			FileSWL archiveFile = new FileSWL(target, name + ".gz").createIfNotFoundSafe();
			
			DataOutputStreamSWL stream = archiveFile.toOutputStream().gz();
			
			StreamsUtils.copyStreamSafe(archiveFile.toInputStream(), stream);
			
			stream.closeSafe();
		};
		
	}
	
}
