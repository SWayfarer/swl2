package ru.swayfarer.swl2.process.funs.unix;

import java.util.Scanner;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.string.DynamicString;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.string.reader.StringReaderSWL;

public class UnixChildsGetterFun implements IFunction2<Long, Boolean, IExtendedList<Long>>{

	public static ILogger logger = LoggingManager.getLogger();
	
	public static int STATE_ON_START = 0;
	public static int STATE_WAS_IN_PARENT = 1;
	public static int STATE_WAS_AFTER_PARENT = 2;
	public static int STATE_WAS_IN_VALUE = 3;
	public static int STATE_READED = 4;
	
	public void find(IExtendedList<Long> ret, long pid, boolean isRecursive) throws Throwable
	{
		ProcessBuilder processBuilder = new ProcessBuilder("ps", "-A", "-o", "ppid,pid");
		Process process = processBuilder.start();
		
		Scanner scan = new Scanner(process.getInputStream());
		
		while (true)
		{
			try
			{
				String ln = scan.nextLine();
				
				StringReaderSWL reader = new StringReaderSWL(ln);
				
				DynamicString buf = new DynamicString();
				
				long parent = 0;
				
				int state = STATE_ON_START;
				
				while (reader.hasNextElement())
				{
					if (reader.skipSome(" "))
					{
						if (state == STATE_WAS_IN_PARENT)
						{
							String parentStr = buf.toString();
							state = STATE_WAS_AFTER_PARENT;
							buf.clear();
							
							if (StringUtils.isLong(parentStr))
							{
								parent = Long.valueOf(parentStr);
								
								if (parent != pid)
								{
									break;
								}
							}
							else
							{
								break;
							}
						}
						
						if (state == STATE_WAS_IN_VALUE)
						{
							String valueStr = buf.toString();
							
							if (StringUtils.isLong(valueStr))
							{
								long value = Long.valueOf(valueStr);
								
								state = STATE_READED;
								ret.add(value);
								
								if (isRecursive)
								{
									find(ret, value, isRecursive);
								}
							}
						}
					}
					else 
					{
						buf.append(reader.next());
						
						if (state == STATE_ON_START)
							state = STATE_WAS_IN_PARENT;
						
						if (state == STATE_WAS_AFTER_PARENT)
						{
							state = STATE_WAS_IN_VALUE;
						}
					}
				}
				
				if (state != STATE_READED)
				{
					String valueStr = buf.toString();
					
					if (StringUtils.isLong(valueStr))
					{
						long value = Long.valueOf(valueStr);
						
						state = STATE_READED;
						ret.add(value);
						
						if (isRecursive)
						{
							find(ret, value, isRecursive);
						}
					}
				}

				reader.close();
			}
			catch (Throwable e)
			{
				break;
			}
		}
		
		scan.close();
	}
	
	@Override
	public IExtendedList<Long> apply(Long pid, Boolean isRecursive)
	{
		return logger.safeReturn(() -> {
			IExtendedList<Long> ret = CollectionsSWL.createExtendedList();
				find(ret, pid, isRecursive);
			return ret;
		}, null, "");
	}
}
