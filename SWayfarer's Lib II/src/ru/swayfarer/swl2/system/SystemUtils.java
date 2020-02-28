package ru.swayfarer.swl2.system;


/** 
 * Утилиты для работы с системой 
 * @author swayfarer
 *
 */
public class SystemUtils {

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
	
}
