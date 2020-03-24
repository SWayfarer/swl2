package ru.swayfarer.swl2.resource.compress;

import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import ru.swayfarer.swl2.collections.streams.DataStream;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction3NoR;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.resource.file.FilesUtils;
import ru.swayfarer.swl2.resource.streams.DataInputStreamSWL;
import ru.swayfarer.swl2.resource.streams.DataOutputStreamSWL;
import ru.swayfarer.swl2.resource.streams.StreamsUtils;

/** 
 * Утилиты для работы со сжатием
 * @author swayfarer
 *
 */
public class CompressionUtils {

	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Запаковать в zip */
	public static void packToZip(FileSWL file, FileSWL packedFile, IFunction1<FileSWL, ZipOutputStream> streamCreatorFun)
	{
		Archivers.zipCompressingFun.apply(file, packedFile, streamCreatorFun);
	}
	
	/** Запаковать в zip */
	public static void packToZip(FileSWL file, FileSWL packedFile)
	{
		packToZip(file, packedFile, (f) -> f.toOutputStream().zip());
	}
	
	/** Распаковать из Zip*/
	public static void unpackFromZip(FileSWL diretoryToUnpack, FileSWL compressedFile)
	{
		unpackFromZip(diretoryToUnpack, compressedFile, (is) -> is);
	}
	
	/** Распаковать файл из Zip */
	public static void unpackFromZip(FileSWL diretoryToUnpack, FileSWL compressedFile, IFunction1<InputStream, InputStream> streamCreationFun)
	{
		Archivers.zipDecompressingFun.apply(diretoryToUnpack, compressedFile, streamCreationFun);
	}
	
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
		
		public static IFunction3NoR<FileSWL, FileSWL, IFunction1<InputStream, InputStream>> zipDecompressingFun = (directoryToUnpack, decompessFile, streamCreationFun) -> {
			
			logger.safe(() -> {
				
				ZipFile zipFile = decompessFile.toZipFile();
				
				DataStream<ZipEntry> entries = DataStream.of(zipFile.entries());
				entries.setParrallel(true);
				
				entries.each((e) -> {
					logger.safe(() -> {
						InputStream is = zipFile.getInputStream(e);
						if (streamCreationFun != null)
							is = streamCreationFun.apply(is);
						
						DataInputStreamSWL dis = DataInputStreamSWL.of(is);
						
						FileSWL unpackedEntryFile = new FileSWL(directoryToUnpack, e.getName()).createIfNotFoundSafe();
						
						StreamsUtils.copyStreamSafe(dis, unpackedEntryFile.toOutputStream());
						
					}, "Error while unpacking zip entry", e);
				});
				
				
			}, "Error while creating restore file for", directoryToUnpack);
		};
		
		public static IFunction3NoR<FileSWL, FileSWL, IFunction1<FileSWL, ZipOutputStream>> zipCompressingFun = (file, zipFile, streamCreationFun) -> {
			
			logger.safe(() -> {
				
				ZipOutputStream zos = streamCreationFun.apply(zipFile);
				
				FilesUtils.forEachFile(null, (f) -> {
					
					String entryName = f.getLocalPath(file);
					ZipEntry entry = new ZipEntry(entryName);
					
					logger.safe(() -> {
						zos.putNextEntry(entry);
						StreamsUtils.copyStream(f.toInputStream(), zos, true, false);
						zos.closeEntry();
					}, "Error while packing zip entry", entry);
					
				}, file, false);
				
				zos.close();
				
			}, "Error while creating restore file for", file);
		};
	}
}
