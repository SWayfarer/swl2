package ru.swayfarer.swl2.jfx.starter;

import java.util.List;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction0NoR;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1NoR;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.jvm.JavaUtils;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;

public class JfxStarter implements IFunction2NoR<List<String>, IFunction1NoR<List<String>>> {

	public static ILogger logger = LoggingManager.getLogger();
	
	public IFunction1NoR<String> noJfxFoundFun = logger::error;
	
	public void start(String className, String methodName, String... args)
	{
		if (!JavaUtils.isJfxPresent())
		{
			noJfxFoundFun.apply("No JavaFX found on your JVM! Please, install it and run again!");
		}
		else
		{
			DummyApplication.run = () -> ReflectionUtils.invokeMethod(ReflectionUtils.findClass(className), null, methodName, (Object[]) args);
			DummyApplication.launch(DummyApplication.class, args);
		}
	}
	
	public static class DummyApplication extends Application {

		public static IFunction0NoR run;
		
		@Override
		public void start(Stage primaryStage) throws Exception
		{
			run.apply();
		}
	}

	@Override
	public void applyNoR(List<String> args, IFunction1NoR<List<String>> fun)
	{
		if (!JavaUtils.isJfxPresent())
		{
			noJfxFoundFun.apply("No JavaFX found on your JVM! Please, install it and run again!");
		}
		else
		{
			DummyApplication.run = () -> fun.apply(args);
			DummyApplication.launch(DummyApplication.class, args.toArray(new String[args.size()]));
		}
	}
	
}
