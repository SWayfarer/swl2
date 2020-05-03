package ru.swayfarer.swl2.swconf.serialization.providers;

import java.io.File;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2;
import ru.swayfarer.swl2.swconf.primitives.SwconfString;
import ru.swayfarer.swl2.swconf.serialization.ISwconfSerializationProvider;

public class FileSwconfSerializationProvider  implements ISwconfSerializationProvider<SwconfString, File>{

	public IExtendedList<IFunction2<Class<?>, String, File>> registeredGenerators = CollectionsSWL.createExtendedList();
	
	@Override
	public boolean isAccetps(Class<?> type)
	{
		return File.class.isAssignableFrom(type);
	}

	@Override
	public File deserialize(Class<?> cl, File obj, SwconfString swconfObject)
	{
		obj = createNewFile(cl, swconfObject.getValue());
		return obj;
	}

	@Override
	public SwconfString serialize(File obj)
	{
		return new SwconfString().setValue(obj.getAbsolutePath());
	}

	public File createNewFile(Class<?> classOfFile, String filepath)
	{
		for (IFunction2<Class<?>, String, File> generator : registeredGenerators)
		{
			File ret = generator.apply(classOfFile, filepath);
			
			if (ret != null)
				return ret;
		}
		
		return null;
	}
	
	@Override
	public File createNewInstance(Class<?> classOfObject, SwconfString swconfObject)
	{
		return null;
	}

}
