package ru.swayfarer.swl2.process.utils;

import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction3NoR;
import ru.swayfarer.swl2.jvm.JavaUtils;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.process.funs.unix.UnixChildsGetterFun;
import ru.swayfarer.swl2.system.SystemUtils;

/**
 * Утилиты для работы с процессами 
 * @author swayfarer
 *
 */
public class ProcessUtils {

	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** 
	 * Функция, убивающая процесс
	 * <h1> Аргументы </h1>
	 * Id процесса
	 * <br> Принудительно ли убивать?
	 * <br> Убивать ли дочерние процессы? 
	 */
	@InternalElement
	public static IFunction3NoR<Long, Boolean, Boolean> killFunction;
	
	public static IFunction2<Long, Boolean, IExtendedList<Long>> childsFunction;
	
	/** Получить функцию для убийства процессов */
	public static IFunction3NoR<Long, Boolean, Boolean> getKillFunction()
	{
		if (killFunction == null)
		{
			killFunction = SystemUtils.isLinux() || SystemUtils.isMac() ? new UnixProcessKiller() : new WindowsProcessKiller();
		}
		
		return killFunction;
	}
	
	public static IFunction2<Long, Boolean, IExtendedList<Long>> getChildrensFunction()
	{
		if (childsFunction == null)
		{
			childsFunction = SystemUtils.isWindows() ? null : new UnixChildsGetterFun();
		}
		
		return childsFunction;
	}
	
	/**
	 * Жив ли процесс?
	 * @param process Проверяемый процесс
	 * @return True, если процесс жив
	 */
	public static boolean isAlive(Process process)
	{
		try
		{
			process.exitValue();
			return false;
		}
		catch (Throwable e)
		{
			
		}
		
		return true;
	}
	
	/**
	 * Убить процесс
	 * @param pid Id процесса
	 */
	public static void killProcess(long pid)
	{
		killProcess(pid, false, false);
	}
	
	/**
	 * Убить процесс
	 * @param pid Id процесса
	 * @param isForce Принудительно ли?
	 */
	public static void killProcess(long pid, boolean isForce)
	{
		killProcess(pid, isForce, false);
	}
	
	/**
	 * Убить процесс
	 * @param pid Id процесса
	 * @param isForce Принудительно ли?
	 * @param isKillChildren Убивать ли дочерние процессы?
	 */
	public static void killProcess(long pid, boolean isForce, boolean isKillChildren)
	{
		getKillFunction().apply(pid, isForce, isKillChildren);
	}
	
	/**
	 * Получить id процесса
	 * @param process Целевой процесс
	 * @return id процесса 
	 */
	public static long getProcessId(Process process)
	{
		int javaVersion = JavaUtils.getJavaVersion();
		
		if (javaVersion >= 9)
		{
			return ReflectionUtils.invokeMethod(process, "pid").getResult();
		}
		else
		{
			return (long) (int) ReflectionUtils.getFieldValue(process, "pid");
		}
	}
	
	/**
	 * Функция, убивающая процессы на системе Windows 
	 * @author swayfarer
	 *
	 */
	@InternalElement
	public static class WindowsProcessKiller implements IFunction3NoR<Long, Boolean, Boolean> {

		@Override
		public void applyNoR(Long pid, Boolean isForce, Boolean isKillChildren)
		{
			IExtendedList<String> command = CollectionsSWL.createExtendedList();
			
			command.add("cmd");
			
			if (isForce)
				command.add("/F");
			
			if (isKillChildren)
				command.add("/T");
			
			command.add(String.valueOf(pid));
			
			ProcessBuilder pb = new ProcessBuilder(command);
			
			logger.safe(pb::start);
		}
	}
	
	/**
	 * Функция, убивающая процессы на Unix-системах 
	 * @author swayfarer
	 *
	 */
	public static class UnixProcessKiller implements IFunction3NoR<Long, Boolean, Boolean> {

		@Override
		public void applyNoR(Long pid, Boolean isForce, Boolean isKillChildren)
		{
			IExtendedList<String> command = CollectionsSWL.createExtendedList();
			
			command.add("kill");
			
			IExtendedList<Long> children = null;
			
			if (isKillChildren)
				children = getChildrensFunction().apply(pid, true);
			
			if (isForce)
				command.add("-9");
			
			command.add(pid + "");
			
			ProcessBuilder pb = new ProcessBuilder(command);
			
			logger.safe(pb::start);
			
			if (isKillChildren)
			{
				children.each((e) -> applyNoR(e, isForce, false));
			}
		}
	}
}
