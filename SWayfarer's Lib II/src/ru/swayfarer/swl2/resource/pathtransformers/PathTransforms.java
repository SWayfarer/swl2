package ru.swayfarer.swl2.resource.pathtransformers;

import java.util.Date;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction0;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.resource.pathtransformers.actor.IPathActor;
import ru.swayfarer.swl2.resource.pathtransformers.actor.PathActor;
import ru.swayfarer.swl2.resource.pathtransformers.actor.RandomActor;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.system.SystemUtils;

/**
 * Контроллер "актеров" пути, таких, как %appDir%, %user% и пр.
 * @author swayfarer
 */
public class PathTransforms {

	/** Зарегистрированные актеры */
	@InternalElement
	public static IExtendedList<IPathActor> registeredActors = CollectionsSWL.createExtendedList();
	
	/** Лист с трансформерами пути. В отличии от актеров, трансформеры не имеют обратного действия. */
	@InternalElement
	public static IExtendedList<IFunction1<String, String>> registeredTransformers = CollectionsSWL.createExtendedList();
	
	/** Запаковать путь через актеров (например, заменить путь до рабочей директории на %appDir%) */
	public static String pack(@ConcattedString Object... text)
	{
		String s = StringUtils.concat(text);
		
		if (StringUtils.isEmpty(s))
			return s;
		
		for (IPathActor actor : registeredActors)
			if (actor.isPackingSupported())
				s = s.replace(actor.getTransformedString(), actor.getUnransformedString());
		
		return s;
	}
	
	/** Пофиксить слэши */
	public static String fixSlashes(String path)
	{
		if (path == null)
			return null;
		
		while (path.contains("//"))
			path = path.replace("//", "/");
		
		while (path.contains("\\"))
			path = path.replace("\\", "/");
		
		return path;
	}
	
	/** Преобразовать путь через актеров (например, заменить %appDir% на путь до рабочей директории)*/
	public static String transform(@ConcattedString Object... text)
	{
		String s = StringUtils.concat(text);
		
		if (StringUtils.isEmpty(s))
			return s;
		
		for (IPathActor actor : registeredActors)
			s = s.replace(actor.getUnransformedString(), actor.getTransformedString());
		
		for (IFunction1<String, String> transformer : registeredTransformers)
			s = transformer.apply(s);
		
		return s;
	}
	
	/** Зарегистрировать "актера" пути*/
	public static void registerActor(String untransformed, IFunction0<String> transformedFun) 
	{
		registerActor(new IPathActor()
		{
			
			@Override
			public boolean isPackingSupported()
			{
				return false;
			}
			
			@Override
			public String getUnransformedString()
			{
				return untransformed;
			}
			
			@Override
			public String getTransformedString()
			{
				return transformedFun.apply();
			}
		});
	}
	
	/** Зарегистрировать "актера" пути*/
	public static void registerActor(String untransformed, String transformed) 
	{
		registerActor(new PathActor(transformed, untransformed));
	}
	
	/** Зарегистрировать "актера" пути*/
	public static void registerActor(IPathActor actor) 
	{
		if (actor == null)
			return;
		
		registeredActors.addExclusive(actor);
	}
	
	/** Зарегистрировать трансформер пути */
	public static void registrerTransformer(IFunction1<String, String> transformer)
	{
		registeredTransformers.addExclusive(transformer);
	}
	
	/** Регистрация стандартных актеров */
	@InternalElement
	public static void registerDefaultActors()
	{
		registerActor(new RandomActor());
		registerActor("%appDir%", System.getProperty("user.dir"));
		registerActor("%javaHome%", System.getProperty("java.home"));
		registerActor("%user%", System.getProperty("user.home"));
		registerActor("%java%", System.getProperty("java.home")+"/bin/java"+(SystemUtils.isWindows() ? ".exe" : ""));
		registerActor("%temp%", SystemUtils.isWindows() ? System.getProperty("user.home")+"/AppData/Temp/" : "/tmp/");
	
	}
	
	/** Регистрация старндартных трансформеров */
	@InternalElement
	public static void registerDefaultTransformers()
	{
		registrerTransformer(new DatePathTransformer("date"));
		registrerTransformer(new DatePathTransformer(new Date(), "startDate"));
	}
	
	/** Инициализация утилиты */
	@InternalElement
	public static void init()
	{
		registerDefaultTransformers();
		registerDefaultActors();
	}
	
	static {
		init();
	}
	
}
