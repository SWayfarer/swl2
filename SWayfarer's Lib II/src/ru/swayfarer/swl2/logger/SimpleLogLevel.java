package ru.swayfarer.swl2.logger;

import java.util.logging.Level;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor(staticName = "of")
public class SimpleLogLevel implements ILogLevel {

	public String prefix;
	public Level javaLevel;
	public int weight;

}
