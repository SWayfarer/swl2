package ru.swayfarer.swl2.ioc.context;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import lombok.Synchronized;
import lombok.var;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.ioc.DIAnnotation;
import ru.swayfarer.swl2.ioc.DIManager;
import ru.swayfarer.swl2.ioc.context.elements.DIContextElementSingleton;
import ru.swayfarer.swl2.ioc.context.elements.IDIContextElement;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.threads.ThreadsUtils;
import ru.swayfarer.swl2.threads.lock.SynchronizeLock;

/**
 * DI-контекст, хранящий элементы DI
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class DIContext {

	public static ILogger logger = LoggingManager.getLogger();
	
	/** Поток, который заморозил иньекции из этого контекста */
	@InternalElement
	public volatile Thread lockedThread;
	
	/** Лок, на котором заморожены иньекции из контекста */
	@InternalElement
	public volatile SynchronizeLock lock = new SynchronizeLock();

	/** Элементы контекста. Имя -> Класс -> Элемент */
	@InternalElement
	public Map<String, Map<Class<?>, IDIContextElement>> contextElements = new HashMap<>();
	
	/** Постпроцессоры для элементов контекста */
	@InternalElement
	public IExtendedList<IFunction1<Object, Object>> elementPostprocessors = CollectionsSWL.createExtendedList();
	
	public IExtendedList<DIContext> parents = CollectionsSWL.createExtendedList();

	/** 
	 * Пройти блокировку иньекций. <br>
	 * Если контекст заблокирован, то пройти ее сможет только поток, который поставил блокировку <br> 
	 * Если не заблокирован, то пройдет дальше
	 */
	public void passLock()
	{
		if (isInjectionsLocked())
		{
			if (!ThreadsUtils.isThis(lockedThread))
			{
				lock.waitFor();
			}
		}
	}
	
	/**
	 * Постобработка элемента контекста <br>
	 * На этапе постобработки объект может быть заменен, это стоит учитывать при использовании метода. 
	 * Новое значение возвращается методом.
	 * @param obj Обрабатываемый объект
	 * @return Обработанный элемент контекста
	 */
	public <T> T postProcessElement(Object obj)
	{
		for (var postProcessorFun : elementPostprocessors)
		{
			obj = postProcessorFun.apply(obj);
		}
		
		return (T) obj;
	}

	/**
	 * Получить элемент контекста для поля
	 * @param cl Класс, для поля которого ищется элемент
	 * @param field Поле, для которого ищется элемент
	 * @return Элемент контекста, соответствующий полю, или null, если не найдется
	 */
	public IDIContextElement getFieldElement(Class<?> cl, Field field)
	{
		Annotation annotation = DIAnnotation.findDIAnnotation(field); 
		
		if (annotation != null)
		{
			String name = DIAnnotation.getElementName(annotation);
			
			if (StringUtils.isBlank(name))
				name = field.getName();
				
			IDIContextElement element = this.getContextElement(name, !DIAnnotation.isElementUsingName(annotation), field.getType());
			
			return element;
		}
		
		return null;
	}
	
	/**
	 * Разблокировать контекст
	 * @return Этот объект (this)
	 */
	public <T extends DIContext> T unlock()
	{
		if (isInjectionsLocked())
		{
			lock.notifyLockAll();
			lockedThread = null;
		}

		return (T) this;
	}

	/**
	 * Заблокировать иньекции их контекста на потоке
	 * @param thread Поток, на котором происходит блокировка (только он сможет иметь доступ к контексту на это время)
	 * @return Этот объект (this)
	 */
	public <T extends DIContext> T lockOn(Thread thread)
	{
		if (thread == null)
			return (T) this;

		if (isInjectionsLocked())
		{
			DIManager.logger.warning("Trying to lock context", this, "on thread", thread.getName(), "while it's already locked on", lockedThread.getName());
		}

		lockedThread = thread;

		return (T) this;
	}

	/** Заблокированы ли иньекции из контекста? */
	public boolean isInjectionsLocked()
	{
		return lockedThread != null;
	}

	/**
	 * Задать элемент контекста
	 * 
	 * @param name
	 *            Имя элемента
	 * @param value
	 *            Значение элемента
	 * @return Оригинальный {@link DIContext} (this)
	 */
	@Synchronized("contextElements")
	public <T extends DIContext> T setContextElement(String name, Object value)
	{
		return setContextElement(name, value.getClass(), value);
	}

	/**
	 * Задать элемент контекста
	 * 
	 * @param name
	 *            Имя элемента
	 * @param associatedClass
	 *            Класс, с которым будет ассоциироваться элемент
	 * @param value
	 *            Значение элемента
	 * @return Оригинальный {@link DIContext} (this)
	 */
	@Synchronized("contextElements")
	public <T extends DIContext> T setContextElement(String name, Class<?> associatedClass, IDIContextElement value)
	{
		Map<Class<?>, IDIContextElement> contextElements = getElementsForName(name, true);
		contextElements.put(associatedClass, value);
		return (T) this;
	}

	/**
	 * Задать элемент контекста
	 * 
	 * @param name
	 *            Имя элемента
	 * @param associatedClass
	 *            Класс, с которым будет ассоциироваться элемент
	 * @param value
	 *            Значение элемента
	 * @return Оригинальный {@link DIContext} (this)
	 */
	@Synchronized("contextElements")
	public <T extends DIContext> T setContextElement(String name, Class<?> associatedClass, Object value)
	{
		return setContextElement(name, associatedClass, new DIContextElementSingleton()
			.setName(name)
			.setAssociatedClass(associatedClass)
			.setObjectCreationFun(() -> value)
		);
	}

	/**
	 * Есть ли элемент контекста
	 * 
	 * @param name
	 *            Имя элемента
	 * @param associatedClass
	 *            Класс, с которым ассоциируется элемент
	 * @return Есть ли элемент?
	 */
	@Synchronized("contextElements")
	public boolean hasContextElement(String name, boolean isSkipNames, Class<?> associatedClass)
	{
		passLock();
		return getContextElement(name, isSkipNames, associatedClass) != null;
	}

	/**
	 * Получить элемент контекста
	 * 
	 * @param name
	 *            Имя элемента
	 * @param className
	 *            Имя класа. Не каноничное, но и не Internal. То, что
	 *            {@link Class#getName()}
	 * @return Элемент. Nyll, если не найден.
	 */
	@Synchronized("contextElements")
	public IDIContextElement getContextElement(String name, boolean isSkipNames, String className)
	{
		passLock();

		try
		{
			return getContextElement(name, isSkipNames, Class.forName(className));
		}
		catch (ClassNotFoundException e)
		{
			DIManager.logger.error(e, "Error while getting context element", name, "of class", className, "from", this);
		}

		return null;
	}
	
	/**
	 * Получить элемент контекста
	 * 
	 * @param name
	 *            Имя элемента
	 * @param associatedClass
	 *            Класс, с которым ассоциируется элемент
	 * @return Элемент. Nyll, если не найден.
	 */
	@Synchronized("contextElements")
	@Deprecated
	public IDIContextElement getContextElement(String name, Class<?> associatedClass)
	{
		return getContextElement(name, false, associatedClass);
	}

	/**
	 * Получить элемент контекста
	 * 
	 * @param name
	 *            Имя элемента
	 * @param associatedClass
	 *            Класс, с которым ассоциируется элемент
	 * @return Элемент. Nyll, если не найден.
	 */
	@Synchronized("contextElements")
	public IDIContextElement getContextElement(String name, boolean skipName, Class<?> associatedClass)
	{
		passLock();

		Iterable<IDIContextElement> elements = null;
		
		
		Class<?> cl;
		IDIContextElement assignableElement = null;
		
		if (!skipName)
		{
			Map<Class<?>, IDIContextElement> elementsForName = getElementsForName(name, false);

			if (elementsForName == null)
				return null;

			if (elementsForName.containsKey(associatedClass))
				return elementsForName.get(associatedClass);
			
			elements = elementsForName.values();
		}
		else
		{
			IExtendedList<IDIContextElement> list = CollectionsSWL.createExtendedList();
			elements = list;
			
			for (var elem : contextElements.values())
			{
				list.addAll(elem.values());
			}
		}
		
		for (IDIContextElement contextElement : elements)
		{
			cl = contextElement.getAssociatedClass();

			if (cl == associatedClass)
				return contextElement;
			else if (associatedClass.isAssignableFrom(cl))
				assignableElement = contextElement;
		}

		if (assignableElement != null)
		{
			assignableElement = getParentElements(name, skipName, associatedClass);
		}
		
		return assignableElement;
	}

	public IDIContextElement getParentElements(String name, boolean skipName, Class<?> associatedClass)
	{
		IExtendedList<DIContext> visitedContexts = CollectionsSWL.createExtendedList();
		
		if (!CollectionsSWL.isNullOrEmpty(parents))
		{
			for (DIContext parentContext : parents)
			{
				if (!visitedContexts.contains(parentContext))
				{
					IDIContextElement element = parentContext.getContextElement(name, skipName, associatedClass);
					
					if (element != null)
						return element;
					
					visitedContexts.add(parentContext);
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Получить карту Class -> {@link IDIContextElement} элементов
	 * 
	 * @param name
	 *            Имя элементов
	 * @param forceCreate
	 *            Создать ли, если не найдено?
	 */
	@Synchronized("contextElements")
	public Map<Class<?>, IDIContextElement> getElementsForName(String name, boolean forceCreate)
	{
		passLock();

		Map<Class<?>, IDIContextElement> ret = contextElements.get(name);

		if (ret == null && forceCreate)
		{
			ret = CollectionsSWL.createHashMap();
			contextElements.put(name, ret);
		}

		return ret;
	}

}