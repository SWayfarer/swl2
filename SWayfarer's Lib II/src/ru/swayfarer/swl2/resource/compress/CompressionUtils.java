package ru.swayfarer.swl2.resource.compress;

import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import ru.swayfarer.swl2.collections.streams.DataStream;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
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
	public static void packDirToZip(FileSWL file, FileSWL packedFile)
	{
		logger.safe(() -> {
			ZipOutputStream zos = packedFile.toOutputStream().zip();
			
			file.getAllSubfiles(FileSWL::isFile).each((f) -> {
				
				String entryName = f == file ? f.getName() : f.getLocalPath(file);
				
				logger.safe(() -> {
					
					ZipEntry entry = new ZipEntry(entryName);
					
					zos.putNextEntry(entry);
					
					StreamsUtils.copyStream(f.toInputStream(), zos, true, false);
					
					zos.closeEntry();
				}, "Error while packing entry", entryName);
			});
			
			zos.close();
		}, "Error while packing", file, false);
	}
	
	/** Распаковать файл из Zip */
	public static void unpackDirFromZip(FileSWL dir, FileSWL compressedFile)
	{
		if (!dir.isDirectory())
			dir = dir.getParentFile();
		
		final FileSWL diretoryToUnpack = dir;
		
		logger.safe(() -> {
			
			ZipFile zipFile = compressedFile.toZipFile();
			
			logger.info("Decompressing file", compressedFile, "to", diretoryToUnpack, diretoryToUnpack.isDirectory());
			ZipInputStream zis = compressedFile.toInputStream().zip();
			
			if (zis != null)
			{
				ZipEntry entry = null;
				
				while ((entry = zis.getNextEntry()) != null)
				{
					InputStream is = zipFile.getInputStream(entry);
					FileSWL file = diretoryToUnpack.subFile(entry.getName()).createIfNotFound();
					StreamsUtils.copyStream(is, file.toOutputStream(), true, true);
				}
			}
			
			zis.close();
			zipFile.close();
			
		}, "Error while unpacking", compressedFile, "to", diretoryToUnpack);
	}
	
	/** Функции-архиверы */
	public static class Archivers {
		
		public static IFunction3NoR<FileSWL, FileSWL, String> gzArchiver = (file, target, name) -> {
			
			if (!file.exists())
				return;
			
			target.createIfNotFoundDir();
			
			FileSWL archiveFile = new FileSWL(target, name + ".gz").createIfNotFoundSafe();
			
			DataOutputStreamSWL stream = archiveFile.toOutputStream().gz();
			
			StreamsUtils.copyStreamSafe(file.toInputStream(), stream);
			
			stream.closeSafe();
		};
		
		public static IFunction2NoR<FileSWL, FileSWL> zipDecompressingFun = (directoryToUnpack, decompessFile) -> {
			
			logger.safe(() -> {
				
				ZipFile zipFile = decompessFile.toZipFile();
				
				DataStream<ZipEntry> entries = DataStream.of(zipFile.entries());
				entries.setParrallel(true);
				
				entries.each((e) -> {
					logger.safe(() -> {
						InputStream is = zipFile.getInputStream(e);
						
						DataInputStreamSWL dis = DataInputStreamSWL.of(is);
						
						FileSWL unpackedEntryFile = new FileSWL(directoryToUnpack, e.getName()).createIfNotFoundSafe();
						
						StreamsUtils.copyStreamSafe(dis, unpackedEntryFile.toOutputStream());
						
					}, "Error while unpacking zip entry", e);
				});
				
				
			}, "Error while creating restore file for", directoryToUnpack);
		};
		
		public static IFunction2NoR<FileSWL, FileSWL> zipCompressingFun = (file, zipFile) -> {
			
			logger.safe(() -> {
				
				ZipOutputStream zos = zipFile.toOutputStream().zip();
				
				FilesUtils.forEachFile(null, (f) -> {
					
					String entryName = f.getLocalPath(file);
					ZipEntry entry = new ZipEntry(entryName);
					
					logger.safe(() -> {
						logger.info("Entry", entry.getName());
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
