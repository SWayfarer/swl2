package ru.swayfarer.swl2.logger.config;

import java.net.URL;

import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.json.JsonUtils;
import ru.swayfarer.swl2.json.NonJson;
import ru.swayfarer.swl2.json.config.AbstractJsonConfig;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.observable.subscription.ISubscription;
import ru.swayfarer.swl2.resource.rlink.RLUtils;
import ru.swayfarer.swl2.resource.rlink.ResourceLink;
import ru.swayfarer.swl2.string.StringUtils;

public class LoggerConfigHandler implements IFunction2NoR<ISubscription<ILogger>, ILogger>{

	/** Логгер */
	public static ILogger logger = LoggingManager.getLogger();
	
	public static String configsPathPropertyName = "swl2.logging.config";
	public static String defaultConfigsPath = "assets/config/swl2/logs/";
	
	@Override
	public void applyNoR(ISubscription<ILogger> sub, ILogger log)
	{
//		logger.safe(() -> {
//			
//			String configsPath = getConfigsPackage();
//			
//			StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
//			StackTraceElement last = stackTraceElements[stackTraceElements.length - 1];
//			
//			Class<?> firstClass = Class.forName(last.getClassName());
//			
//			URL url = firstClass.getClassLoader().getResource(configsPath + "/logging.json");
//			
//			ResourceLink configsRoot = RLUtils.url(url.toExternalForm());
//			
//			IExtendedList<ResourceLink> configs = configsRoot.getAdjacents(true, (rl) -> rl.content.endsWith(".json") && rl.content.contains(configsPath + "configs/" ));
//			
//			for (ResourceLink cfg : configs)
//			{
//				ConfigContainer configContainer = JsonUtils.loadFromJson(cfg.toStream().readAllAsUtf8(), ConfigContainer.class);
//			}
//			
//		}, "Error while loading logger configutarion!");
	}
	
	public String getConfigsPackage()
	{
		String config = System.getProperty(configsPathPropertyName);
		
		if (StringUtils.isEmpty(config))
		{
			config = defaultConfigsPath;
		}
		
		if (!config.endsWith("/"))
			config += "/";
		
		return config;
	}

}
