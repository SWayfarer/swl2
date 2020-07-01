package ru.swayfarer.swl2.ioc.context.elements;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import lombok.Setter;
import lombok.experimental.Accessors;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2;
import ru.swayfarer.swl2.ioc.DIManager;
import ru.swayfarer.swl2.ioc.context.DIContext;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;

/**
 * Элемент контекста, возвращающий значение из метода
 * 
 * @author swayfarer
 *
 */

@Setter @Accessors(chain = true)
public class DIContextElementFromMethod implements IDIContextElement {

	/** Класс, с которым ассоциируется элемент */
	@InternalElement
	public Class<?> associatedClass;
	
	@InternalElement
	public Class<?> methodClass;

	/** Класс, с которым ассоциируется имя */
	@InternalElement
	public String name;

	/** Источник, из которого берется элемент */
	@InternalElement
	public Object sourceInstance;

	/** Метод, из которого берертся элемент */
	@InternalElement
	public Method method;

	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Контекст элемента */
	@InternalElement
	public DIContext context;
	
	public DIContextElementFromMethod(DIContext context)
	{
		this.context = context;
	}

	/** Функция, которая вызывает метод */
	public IFunction2<Method, Object, Object> methodInvokationFun = (method, obj) -> {
		
		try
		{
			Parameter[] params = method.getParameters();

			Object[] args = new Object[0];
			
			if (!CollectionsSWL.isNullOrEmpty(params))
			{
				args = DIManager.getArgsFromContext(methodClass, params).toArray();
			}
			
			return method.invoke(obj, args);
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while invoking method", method, "of object", obj);
		}

		return null;
		
	};

	@Override
	public Object getElementValue()
	{
		return methodInvokationFun.apply(method, sourceInstance);
	}

	@Override
	public Class<?> getAssociatedClass()
	{
		return associatedClass;
	}

	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public String toString()
	{
		return "prototype from: " + method + " and instance " + sourceInstance;
	}

	@Override
	public DIContext getContext()
	{
		return context;
	}

	@Override
	public boolean mustPostProcess()
	{
		return true;
	}
}