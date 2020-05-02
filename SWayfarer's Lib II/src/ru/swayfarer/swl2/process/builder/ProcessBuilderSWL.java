package ru.swayfarer.swl2.process.builder;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.List;
import java.util.Map;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.collections.extended.IExtendedMap;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.process.IProcess;
import ru.swayfarer.swl2.process.SimpleProcess;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.string.property.StringProperty;

public class ProcessBuilderSWL {

	public static ILogger logger = LoggingManager.getLogger();
	
	public ProcessBuilder wrappedBuilder;
	
	public ProcessBuilderSWL(List<String> command)
	{
		wrappedBuilder = new ProcessBuilder(command);
	}

	public ProcessBuilderSWL(String... command)
	{
		wrappedBuilder = new ProcessBuilder(command);
	}

	public ProcessBuilderSWL command(List<String> command)
	{
		wrappedBuilder.command(command);
		return this;
	}

	public ProcessBuilderSWL command(String... command)
	{
		wrappedBuilder.command(command);
		return this;
	}

	public IExtendedList<String> command()
	{
		return CollectionsSWL.createExtendedList(wrappedBuilder.command());
	}

	public IExtendedMap<String, StringProperty> environment()
	{
		Map<String, String> env = wrappedBuilder.environment();
		IExtendedMap<String, StringProperty> environment = CollectionsSWL.createExtendedMap();
		
		for (Map.Entry<String, String> entry : env.entrySet())
		{
			environment.put(entry.getKey(), new StringProperty(entry.getValue()));
		}
		
		return environment;
	}

	public FileSWL directory()
	{
		return FileSWL.of(wrappedBuilder.directory());
	}

	public ProcessBuilderSWL directory(File directory)
	{
		wrappedBuilder.directory(directory);
		return this;
	}

	public ProcessBuilderSWL redirectInput(Redirect source)
	{
		wrappedBuilder.redirectInput(source);
		return this;
	}

	public ProcessBuilderSWL redirectOutput(Redirect destination)
	{
		wrappedBuilder.redirectInput(destination);
		return this;
	}

	public ProcessBuilderSWL redirectError(Redirect destination)
	{
		wrappedBuilder.redirectInput(destination);
		return this;
	}

	public ProcessBuilderSWL redirectInput(File file)
	{
		return redirectInput(Redirect.from(file));
	}

	public ProcessBuilderSWL redirectOutput(File file)
	{
		return redirectOutput(Redirect.to(file));
	}

	public ProcessBuilderSWL redirectError(File file)
	{
		return redirectError(Redirect.to(file));
	}

	public Redirect redirectInput()
	{
		return wrappedBuilder.redirectInput();
	}

	public Redirect redirectOutput()
	{
		return wrappedBuilder.redirectOutput();
	}

	public Redirect redirectError()
	{
		return wrappedBuilder.redirectError();
	}

	public ProcessBuilderSWL inheritIO()
	{
		wrappedBuilder.inheritIO();
		return this;
	}

	public boolean redirectErrorStream()
	{
		return wrappedBuilder.redirectErrorStream();
	}

	public ProcessBuilderSWL redirectErrorStream(boolean redirectErrorStream)
	{
		wrappedBuilder.redirectErrorStream(redirectErrorStream);
		return this;
	}
	
	public IProcess run() throws IOException 
	{
		return new SimpleProcess(start());
	}
	
	public IProcess runSafe()
	{
		Process process = startSafe();
		
		if (process != null)
			return new SimpleProcess(process);
		
		return null;
	}
	
	public Process startSafe()
	{
		return logger.safeReturn(this::start, null, "Error while starting process");
	}
	
	public Process start() throws IOException
	{
		return wrappedBuilder.start();
	}
}
