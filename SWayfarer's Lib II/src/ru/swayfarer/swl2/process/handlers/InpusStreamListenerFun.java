package ru.swayfarer.swl2.process.handlers;

import java.io.InputStream;
import java.util.Scanner;

import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction0;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction0NoR;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.observable.IObservable;

public class InpusStreamListenerFun implements IFunction0NoR {

	public static ILogger logger = LoggingManager.getLogger();
	
	public IObservable<String> listener;
	public IFunction0<InputStream> streamFun;

	public InpusStreamListenerFun(IObservable<String> listener, IFunction0<InputStream> streamFun)
	{
		super();
		this.listener = listener;
		this.streamFun = streamFun;
	}

	@Override
	public void applyNoR()
	{
		Scanner scan = new Scanner(streamFun.apply());
		
		while (true)
		{
			try
			{
				listener.next(scan.nextLine());
			}
			catch (Throwable e)
			{
				break;
			}
		}
		
		logger.safe(scan::close);
	}

}
