package ru.swayfarer.swl2.system;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.resource.file.IHasSubfiles;
import ru.swayfarer.swl2.resource.file.ListedSubfilesContainer;
import ru.swayfarer.swl2.resource.streams.DataInputStreamSWL;

/** 
 * Утилиты для работы с системой 
 * @author swayfarer
 *
 */
public class SystemUtils {

	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Получить кол-во ядер процессора */
	public static int getCpuCoresCount()
	{
		return Runtime.getRuntime().availableProcessors();
	}
	
	/** Является ли система 32-bit'ной? */
	public static boolean is32bit()
	{
		return !is64bit();
	}
	
	/** Является ли система 64-bit'ной? */
	public static boolean is64bit()
	{
		return System.getProperty("sun.arch.data.model").equals("64");
	}
	
	/** Является ли система Windows? */
	public static boolean isWindows()
	{
		return System.getProperty("os.name").toLowerCase().contains("win");
	}
	
	/** Является ли система Mac? */
	public static boolean isMac()
	{
		return System.getProperty("os.name").toLowerCase().contains("mac");
	}
	
	/** Является ли система Linux? */
	public static boolean isLinux()
	{
		return !isWindows() && !isMac();
	}
	
	/** Получить примонтированные разделы дисков */
	public static IHasSubfiles getMountedDevices()
	{
		return logger.safeReturn(() -> {

			if (isLinux())
			{
				ProcessBuilder pb = new ProcessBuilder("/usr/bin/cat", "/proc/mounts");
				pb.redirectErrorStream(true);
				Process process = pb.start();
				
				DataInputStreamSWL dis = DataInputStreamSWL.of(process.getInputStream());
				dis.setLineSplitter(System.lineSeparator());
				
				IExtendedList<FileSWL> mountedFolders = CollectionsSWL.createExtendedList();
				
				while (dis.hasNextByte())
				{
					String ln = dis.readStringLn();
					
					if (ln.startsWith("/dev/sd"))
					{
						String[] split = ln.split(" ");
						
						if (split.length >= 2)
						{
							String folderPath = split[1];
							
							FileSWL file = new FileSWL(folderPath);
							
							if (file.isExistingDir())
							{
								mountedFolders.addExclusive(file);
							}
							else
							{
								logger.info("Found mounted not-directory file", file.getAbsolutePath());
							}
						}
						else
						{
							logger.warning("Found unknown mount info formated line:", ln);
						}
					}
				}
				
				return new ListedSubfilesContainer(mountedFolders);
			}

			return null;
			
		}, null, "Error while getting mounted io devices");
	}
	
}
