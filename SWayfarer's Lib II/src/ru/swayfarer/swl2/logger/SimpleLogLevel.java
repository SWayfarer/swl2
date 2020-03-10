package ru.swayfarer.swl2.logger;

import java.util.logging.Level;

import lombok.Data;
import ru.swayfarer.swl2.ansi.AnsiFormatter;

@Data
public class SimpleLogLevel implements ILogLevel {

	public String prefix;
	public Level javaLevel;
	public int weight;
	
	public static SimpleLogLevel of(String prefix, Level javaLevel, int weight)
	{
		SimpleLogLevel level = new SimpleLogLevel();
		level.prefix = AnsiFormatter.instance.format(prefix);
		level.javaLevel = javaLevel;
		level.weight = weight;
		
		return level;
	}

}
