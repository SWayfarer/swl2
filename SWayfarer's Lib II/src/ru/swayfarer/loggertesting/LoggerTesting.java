package ru.swayfarer.loggertesting;

import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.logger.SimpleLoggerSWL;
import ru.swayfarer.swl2.logger.configurator.LoggingConfiguration;
import ru.swayfarer.swl2.logger.configurator.SwconfLoggerConfiguration;
import ru.swayfarer.swl2.logger.configurator.entry.ConfiguartorEntry;
import ru.swayfarer.swl2.logger.configurator.entry.ConfiguratorArchivingEntry;
import ru.swayfarer.swl2.logger.configurator.entry.ConfiguratorFileEntry;
import ru.swayfarer.swl2.resource.rlink.RLUtils;
import ru.swayfarer.swl2.resource.rlink.ResourceLink;
import ru.swayfarer.swl2.swconf2.helper.SwconfIO;

public class LoggerTesting {

	public static void main(String[] args)
	{
		try
		{
			showExample();
		}
		catch (Throwable e)
		{
			
		}
	}
	
	public static void readDefaultConfig()
	{
		LoggingConfiguration.loadDefaultConfigToRoot();
		
		ILogger logger = LoggingManager.getLogger("Example");
		
		logger.info("Hello, World!");
	}
	
	public static void readConfig()
	{
		SwconfLoggerConfiguration configuration = new SwconfLoggerConfiguration().setResourceLink(RLUtils.pkg("logging.swconf"));
		configuration.init();
		
		ILogger logger = new SimpleLoggerSWL("Example").lateinit();
		configuration.applyToLogger(logger);
		
		logger.info("Hello, World!");
		logger.info(new Throwable(), "123");
	}
	
	public static void showExample()
	{
		SwconfLoggerConfiguration configuration = new SwconfLoggerConfiguration();
		
		ConfiguartorEntry entry = new ConfiguartorEntry();
		entry.applySources.add("mask:ru.swayfarer.*");
		
		ConfiguratorFileEntry fileEntry = new ConfiguratorFileEntry();
		entry.files.add(fileEntry);
		
		ConfiguratorArchivingEntry archivingEntry = new ConfiguratorArchivingEntry();
		fileEntry.archiving = archivingEntry;
		
		configuration.configuration.add(entry);
		
		ResourceLink rlink = RLUtils.createLink("f:output.yaml");
		
		new SwconfIO().serialize(configuration, rlink);
	}
	
}
