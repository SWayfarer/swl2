package ru.swayfarer.swl2.logger.config;

import java.util.ArrayList;
import java.util.List;

import ru.swayfarer.swl2.json.JsonUtils;
import ru.swayfarer.swl2.json.NonJson;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.logger.config.files.FileInfo;
import ru.swayfarer.swl2.logger.config.filtering.FilteringInfo;
import ru.swayfarer.swl2.logger.config.print.PrintingInfo;
import ru.swayfarer.swl2.resource.file.FileSWL;

public class SimpleLoggerConfigutaror implements ILoggerConfigurator {

	@NonJson
	public ILogger logger = LoggingManager.getLogger();
	
	public List<String> basePackages = new ArrayList<>();
	
	public PrintingInfo printing;
	public FilteringInfo filtering;
	public FileInfo file;
	
	@Override
	public void configure(ILogger logger)
	{
		this.logger.safe(() -> {
			
			if (printing != null)
			{
				printing.apply(logger);
			}
			
			if (filtering != null)
				filtering.apply(logger);
			
			if (file != null)
			{
				file.apply(logger);
			}
		}, "Error while configuring logger", logger);
	}
	
	@Override
	public boolean isLoggerAcceptable(ILogger logger)
	{
		if (logger == null)
			return false;
		
		String creatorClassName = logger.getFrom().firstElement().getClassName();
		
		for (String basePackage : basePackages)
		{
			if (creatorClassName.startsWith(basePackage))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public void toFile(String fileName)
	{
		FileSWL file = new FileSWL(fileName).createIfNotFoundSafe();
		JsonUtils.saveToFile(this, file);
	}
}
