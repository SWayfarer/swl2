package ru.swayfarer.swl2.swconf.serialization.providers;

import java.lang.reflect.Field;
import java.util.Map;

import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.swconf.primitives.SwconfObject;
import ru.swayfarer.swl2.swconf.primitives.SwconfPrimitive;
import ru.swayfarer.swl2.swconf.serialization.ISwconfSerializationProvider;
import ru.swayfarer.swl2.swconf.serialization.SwconfSerialization;
import ru.swayfarer.swl2.swconf.serialization.comments.CommentSwconf;

/**
 * Провайдер для объектов, у которых есть поля. Обычно используется, если не нашлось более подходящего, ибо универсален.
 * @author swayfarer
 *
 */
public class ReflectionSwconfSerializationProvider implements ISwconfSerializationProvider<SwconfObject, Object> {

	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Сериализация, которая будет использоваться для дочерних элементов */
	@InternalElement
	public SwconfSerialization serialization;
	
	/** Кэшированные доступные поля */
	@InternalElement
	public Map<Class<?>, Map<String, Field>> cachedFields = CollectionsSWL.createIdentityMap();
	
	/** Конструктор */
	public ReflectionSwconfSerializationProvider(SwconfSerialization serialization)
	{
		ExceptionsUtils.IfNullArg(serialization, "Object's elements serialization can't be null!");
		this.serialization = serialization;
	}
	
	/** Получить доступные поля */
	public Map<String, Field> getAccessibleFields(Class<?> cl)
	{
		 Map<String, Field> ret = cachedFields.get(cl);
		 
		 if (ret == null)
		 {
			 ret = ReflectionUtils.getAccessibleFields(cl)
					 .dataStream()
					 .toMap((e) -> e.getName(), (e) -> e);
			 
			 cachedFields.put(cl, ret);
		 }
		 
		 return ret;
	}
	
	@Override
	public boolean isAccetps(Class<?> type)
	{
		return type != Object.class;
	}

	@Override
	public Object deserialize(Class<?> cl, Object obj, SwconfObject swconfObject)
	{
		Map<String, Field> accessibleFields = getAccessibleFields(obj.getClass());
		
		IExtendedList<SwconfObject> roots = CollectionsSWL.createExtendedList();
		roots.add(swconfObject);
		
		for (Field field : accessibleFields.values())
		{
			SwconfPrimitive primitive = swconfObject.getChild(field.getName());
			
			if (primitive != null)
			{
				Object value = serialization.deserialize(field.getType(), null, primitive, field, null);
				
				logger.safe(() -> field.set(obj, value), "Error while setting field " + field);
			}
		}
		
		return obj;
	}

	@Override
	public Object createNewInstance(Class<?> classOfObject, SwconfObject swconfObject)
	{
		return ReflectionUtils.newInstanceOf(classOfObject);
	}

	@Override
	public SwconfObject serialize(Object obj)
	{
		SwconfObject object = new SwconfObject();
		
		Map<String, Field> fields = getAccessibleFields(obj.getClass());
		
		for (Field field : fields.values())
		{
			logger.safe(() -> {
				SwconfPrimitive primitive = serialization.serialize(field.getType(), field.get(obj), null);
				primitive.setName(field.getName());
				object.addChild(primitive);
				
				CommentSwconf commentAnnotation = field.getAnnotation(CommentSwconf.class);
				
				if (commentAnnotation != null)
				{
					primitive.setComment(commentAnnotation.value());
				}
				
			}, "Error while serializing field", field, "of object", obj);
		}
		
		return object;
	}

}
