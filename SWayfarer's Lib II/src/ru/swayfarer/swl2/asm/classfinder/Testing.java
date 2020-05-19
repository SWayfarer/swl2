package ru.swayfarer.swl2.asm.classfinder;

import ru.swayfarer.swl2.app.ApplicationSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;

public class Testing extends ApplicationSWL{
	
	@Override
	public boolean initDI(IExtendedList<String> args)
	{
		scanDIComponents();
		return true;
	}
	
	@Override
	public void start(IExtendedList<String> args)
	{
		ClassFinder finder = new ClassFinder();
		
		finder.classSourcesFun = finder.classSources.ofClasspath();
		
		finder.eventScan.subscribe((info) -> {
			logger.info(info.name);
		});
		
		finder.scan("ru.swayfarer");
	}
	
	public static void main(String[] args)
	{
		startApplication(args);
	}
}