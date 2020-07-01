package ru.swayfarer.swl2.swconf2.yaml;

import java.io.EOFException;
import java.io.InputStream;

import lombok.var;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.swconf2.types.SwconfObject;
import ru.swayfarer.swl2.swconf2.types.SwconfTable;
import ru.swayfarer.swl2.z.dependencies.org.ho.yaml.YamlDecoder;

public class Yaml2SwconfReader {

public static ILogger logger = LoggingManager.getLogger();
	
	public SwconfTable readYaml(InputStream yamlStream)
	{
		try
		{
			var parser = new YamlDecoder(yamlStream);

			try
			{
				while (true)
				{
					Object object = parser.readObject();
					SwconfObject swconfObject = SwconfObject.of(object);
					
					SwconfTable table = (SwconfTable) swconfObject;
					
					return table;
				}
			}
			catch (EOFException e)
			{
				
			}
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while mapping swconf to yaml");
		}
		
		return null;
	}
	
}
