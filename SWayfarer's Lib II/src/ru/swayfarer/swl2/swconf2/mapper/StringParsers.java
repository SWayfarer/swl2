package ru.swayfarer.swl2.swconf2.mapper;

import java.io.File;
import java.lang.reflect.Method;

import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.equals.EqualsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.resource.file.FileSWL;

@SuppressWarnings( {"rawtypes", "unchecked"} )
public class StringParsers {

	public static ILogger logger = LoggingManager.getLogger();
	
	public IExtendedList<IStringMappingProvider> registeredProviders = CollectionsSWL.createExtendedList();
	
	public boolean isSomeAccepting(String str, Class<?> type)
	{
		return registeredProviders.dataStream().contains((provider) -> provider.isAccepting(str, type));
	}
	
	public <T> T parse(String str, Class<?> type)
	{
		IStringMappingProvider provider = findProviderFor(str, type);
		
		if (provider == null)
			return null;
		
		return (T) provider.read(str, type);
	}
	
	public IStringMappingProvider findProviderFor(String str, Class<?> type)
	{
		return registeredProviders.dataStream().first((provider) -> provider.isAccepting(str, type));
	}
	
	public <T extends StringParsers> T registerProvider(IFunction2<String, Class<?>, Boolean> filterFun, IFunction2<String, Class<?>, ? extends Object> valueFun)
	{
		return registerProvider(new IStringMappingProvider()
		{
			
			@Override
			public Object read(String str, Class<?> type)
			{
				return valueFun.apply(str, type);
			}
			
			@Override
			public boolean isAccepting(String str, Class<?> type)
			{
				return filterFun == null || filterFun.apply(str, type);
			}
		});
	}
	
	public <T extends StringParsers> T registerProvider(IStringMappingProvider provider)
	{
		this.registeredProviders.addExclusive(provider);
		return (T) this;
	}
	
	public <T extends StringParsers> T registerDefaultProviders()
	{
		// Enum'ки 
		registerProvider(
			(str, cl) -> {
				return cl.isEnum() && ReflectionUtils.invokeMethod(cl, null, "valueOf", new Object[] {str}).getResult() != null;
			}, 
			(str, cl) -> {
				return ReflectionUtils.invokeMethod(cl, null, "valueOf", new Object[] {str}).getResult();
			}
		);
		
		// Boolean'ки 
		registerProvider(
			(str, cl) -> {
				return EqualsUtils.objectEqualsSome(cl, boolean.class, Boolean.class);
			}, 
			(str, cl) -> {
				return Boolean.valueOf(str);
			}
		);
		
		// Файлы 
		registerProvider(
			(str, cl) -> {
				return File.class.isAssignableFrom(cl);
			}, 
			(str, cl) -> {
				return new FileSWL(str);
			}
		);
		
		registerProvider(new NumberProvider(Byte.class, byte.class, Byte.class));
		registerProvider(new NumberProvider(Short.class, short.class, Short.class));
		registerProvider(new NumberProvider(Integer.class, int.class, Integer.class));
		registerProvider(new NumberProvider(Long.class, long.class, Long.class));
		registerProvider(new NumberProvider(Float.class, float.class, Float.class));
		registerProvider(new NumberProvider(Double.class, double.class, Double.class));
		
//		registerProvider(
//			(str, cl) -> {
//				return false;
//			}, 
//			(str, cl) -> {
//				return null;
//			}
//		);
		
		return (T) this;
	}
	
	public static class NumberProvider implements IStringMappingProvider {

		public Class<?> classOfNumber;
		public Method valueOfMethod;
		public IExtendedList<Class<?>> acceptingClasses = CollectionsSWL.createExtendedList();
		
		public NumberProvider(Class<?> classOfNumber, Class... acceptingClasses)
		{
			this.acceptingClasses.addAll(acceptingClasses);
			this.valueOfMethod = ReflectionUtils.findMethod(classOfNumber, new Class[] {String.class}, "valueOf");
		}
		
		@Override
		public boolean isAccepting(String str, Class<?> type)
		{
			return acceptingClasses.contains(type) && isPassedByValueOf(str);
		}
		
		public boolean isPassedByValueOf(String str)
		{
			try
			{
				valueOfMethod.invoke(null, str);
				return true;
			}
			catch (Throwable e)
			{
				return false;
			}
		}

		@Override
		public Object read(String str, Class<?> type)
		{
			return logger.safeReturn(() -> valueOfMethod.invoke(null, str), 0, "Error while converting string", str, "to num!");
		}
	}
	
	public static interface IStringMappingProvider {
		public boolean isAccepting(String str, Class<?> type);
		public Object read(String str, Class<?> type);
	}
	
}
