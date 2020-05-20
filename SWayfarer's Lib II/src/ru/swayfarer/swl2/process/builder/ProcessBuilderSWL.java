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

	/** см {@link ProcessBuilder#command(List<String>)} */
	public ProcessBuilderSWL command(List<String> command)
	{
		wrappedBuilder.command(command);
		return this;
	}

	/** см {@link ProcessBuilder#command(String...)} */
	public ProcessBuilderSWL command(String... command)
	{
		wrappedBuilder.command(command);
		return this;
	}

	/** см {@link ProcessBuilder#command()} */
	public IExtendedList<String> command()
	{
		return CollectionsSWL.createExtendedList(wrappedBuilder.command());
	}

	/** см {@link ProcessBuilder#environment()} */
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

	/** см {@link ProcessBuilder#directory()} */
	public FileSWL directory()
	{
		return FileSWL.of(wrappedBuilder.directory());
	}

	/** см {@link ProcessBuilder#directory(directory)} */
	public ProcessBuilderSWL directory(File directory)
	{
		wrappedBuilder.directory(directory);
		return this;
	}

	/** см {@link ProcessBuilder#redirectInput(source)} */
	public ProcessBuilderSWL redirectInput(Redirect source)
	{
		wrappedBuilder.redirectInput(source);
		return this;
	}

	/** см {@link ProcessBuilder#redirectOutput(destination)} */
	public ProcessBuilderSWL redirectOutput(Redirect destination)
	{
		wrappedBuilder.redirectInput(destination);
		return this;
	}

	/** см {@link ProcessBuilder#redirectError(destination)} */
	public ProcessBuilderSWL redirectError(Redirect destination)
	{
		wrappedBuilder.redirectInput(destination);
		return this;
	}

	/** см {@link ProcessBuilder#redirectInput(file)} */
	public ProcessBuilderSWL redirectInput(File file)
	{
		return redirectInput(Redirect.from(file));
	}

	/** см {@link ProcessBuilder#redirectOutput(file)} */
	public ProcessBuilderSWL redirectOutput(File file)
	{
		return redirectOutput(Redirect.to(file));
	}

	/** см {@link ProcessBuilder#redirectError(file)} */
	public ProcessBuilderSWL redirectError(File file)
	{
		return redirectError(Redirect.to(file));
	}

	/** см {@link ProcessBuilder#redirectInput()} */
	public Redirect redirectInput()
	{
		return wrappedBuilder.redirectInput();
	}

	/** см {@link ProcessBuilder#redirectOutput()} */
	public Redirect redirectOutput()
	{
		return wrappedBuilder.redirectOutput();
	}

	/** см {@link ProcessBuilder#redirectError()} */
	public Redirect redirectError()
	{
		return wrappedBuilder.redirectError();
	}

	/** см {@link ProcessBuilder#inheritIO()} */
	public ProcessBuilderSWL inheritIO()
	{
		wrappedBuilder.inheritIO();
		return this;
	}

	/** см {@link ProcessBuilder#redirectErrorStream()} */
	public boolean redirectErrorStream()
	{
		return wrappedBuilder.redirectErrorStream();
	}

	/** см {@link ProcessBuilder#redirectErrorStream(redirectErrorStream)} */
	public ProcessBuilderSWL redirectErrorStream(boolean redirectErrorStream)
	{
		wrappedBuilder.redirectErrorStream(redirectErrorStream);
		return this;
	}
	
	/**
	 * Запустить процесс
	 * @return Запущенный процесс
	 * @throws IOException Если файл процесса не будет найден 
	 */
	public IProcess run() throws IOException 
	{
		return new SimpleProcess(start());
	}
	
	/** Выполнить {@link #run()} беопасно */
	public IProcess runSafe()
	{
		Process process = startSafe();
		
		if (process != null)
			return new SimpleProcess(process);
		
		return null;
	}
	
	/** Выполнить {@link #start()} безопасно */
	public Process startSafe()
	{
		return logger.safeReturn(this::start, null, "Error while starting process");
	}
	
	/** см {@link ProcessBuilder#start()} */
	public Process start() throws IOException
	{
		return wrappedBuilder.start();
	}
}
