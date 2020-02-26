package ru.swayfarer.swl2.system;

public class SystemUtils {

	public static int getCpuCoresCount()
	{
		return Runtime.getRuntime().availableProcessors();
	}
	
	public static boolean is32bit()
	{
		return !is64bit();
	}
	
	public static boolean is64bit()
	{
		return System.getProperty("sun.arch.data.model").equals("64");
	}
	
	public static boolean isWindows()
	{
		return System.getProperty("os.name").toLowerCase().contains("win");
	}
	
	public static boolean isMac()
	{
		return System.getProperty("os.name").toLowerCase().contains("mac");
	}
	
	public static boolean isLinux()
	{
		return !isWindows() && !isMac();
	}
	
}
