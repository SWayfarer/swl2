package ru.swayfarer.swl2.lua;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;

import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.resource.rlink.RLUtils;
import ru.swayfarer.swl2.resource.rlink.ResourceLink;
import ru.swayfarer.swl2.resource.streams.BytesInputStreamSWL;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.tasks.ITask;
import ru.swayfarer.swl2.tasks.TaskManager;
import ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.LoadState;
import ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.LuaTable;
import ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.LuaValue;
import ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.Varargs;
import ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.lib.LibFunction;
import ru.swayfarer.swl2.z.dependencies.org.luaj.vm2.lib.jse.JsePlatform;
import ru.swayfarer.swl2.z.dependencies.org.squiddev.luaj.luajc.LuaJC;

/**
 * Интерпретатор Lua-скриптов
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class LuaInterpreter {

	/** Объекты, из которых берутся элементы, отмеченные @{@link LuaElement}*/
	@InternalElement
	public IExtendedList<Object> processors = CollectionsSWL.createExtendedList();
	
	/** Корневая таблица скрпта */
	@InternalElement
	public LuaTable globalsTable = JsePlatform.debugGlobals();
	
	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Функция первого запуска */
	@InternalElement
	public ITask init = TaskManager.once(this::firstLaunch);
	
	/** Конструктор */
	public LuaInterpreter() {}
	
	/** Обработать кастомный скрипт */
	public void processCustomScript(String script)
	{
		process(BytesInputStreamSWL.createStream(script.getBytes()));
	}
	
	/** Регистрация объектов, из которого возьмутся элементы, отмеченные @{@link LuaElement} */
	public <T extends LuaInterpreter> T registerContentSources(Object... obj) 
	{
		processors.addAll(obj);
		return (T) this;
	}
	
	/** Обработать скрипт по ссылке */
	public void process(@ConcattedString Object... rlink)
	{
		String rlinkText = StringUtils.concat(rlink);
		process(RLUtils.createLink(rlinkText), rlinkText);
	}
	
	/** Обработать скрипт по ссылке */
	public void process(ResourceLink rlink)
	{
		process(rlink.toStream(), rlink.toRlinkString());
	}
	
	/** Обработать скрипт из потока с указанием имени скрипта(показывается при ошибках) */
	public void process(InputStream is, String scriptDisplayName)
	{
		init.start();
		
		logger.safe(() -> {
			LoadState.load(is, StringUtils.isEmpty(scriptDisplayName) ? "<some>.lua" : scriptDisplayName, globalsTable).call();
		}, "Error while processing lua script");
	}
	
	/** Первый запуск*/
	@InternalElement
	public void firstLaunch()
	{
		LuaJC.install();
		
		for (Object processor : processors)
		{
			LuaTable table = globalsTable;
			
			LuaElement classElementAnnotation = processor.getClass().getAnnotation(LuaElement.class);
			
			IExtendedList<LuaValueContainer> newValues = CollectionsSWL.createExtendedList();
			
			ReflectionUtils.forEachField(processor, (field, instamce, value) -> {
				
				LuaElement elementAnnotation = field.getAnnotation(LuaElement.class);
				
				if (elementAnnotation != null)
				{
					String name = elementAnnotation.name();
					
					if (StringUtils.isEmpty(name))
						name = field.getName();
					
					IExtendedList<String> names = CollectionsSWL.createExtendedList(elementAnnotation.alternates());
					names.add(name);
					
					newValues.add(new LuaValueContainer(names, LuaUtils.toLuaValue(value)));
				}
				
			});
			
			// Регистрация методов 
			
			ReflectionUtils.forEachMethod(processor.getClass(), processor, (method, obj) -> {
				LuaElement elementAnnotation = method.getAnnotation(LuaElement.class);
				
				if (elementAnnotation != null)
				{
					String name = elementAnnotation.name();
					
					if (StringUtils.isEmpty(name))
						name = method.getName();
					
					LuaMethodFunWrapper methodFunWrapper = new LuaMethodFunWrapper(method, obj);
					IExtendedList<String> names = CollectionsSWL.createExtendedList(elementAnnotation.alternates());
					names.add(name);
					newValues.add(new LuaValueContainer(names, methodFunWrapper));
				}
			});
			
			IExtendedList<String> prefixes = CollectionsSWL.createExtendedList();
			
			// Регистрация полей
			
			if (classElementAnnotation != null)
			{
				IExtendedList<String> names = CollectionsSWL.createExtendedList(classElementAnnotation.alternates());
				names.add(classElementAnnotation.name());
				
				for (String name : names)
				{
					if (!StringUtils.isEmpty(name))
					{
						LuaValue luaValue = globalsTable.get(name);
						
						if (luaValue == null || luaValue.equals(LuaValue.NIL) || !(luaValue instanceof LuaTable) )
						{
							table.set(name, table = new LuaTable());
						}
					}
				}
			}
			else
			{
				prefixes.add("");
			}
			
			for (String prefix : prefixes)
			{
				LuaTable tbl = null;
				
				if (StringUtils.isEmpty(prefix))
				{
					tbl = globalsTable;
				}
				else
				{
					tbl = LuaUtils.getOrCreateTable(prefix, globalsTable);
				}
				
				for (LuaValueContainer container : newValues)
				{
					for (String elementName : container.names)
					{
						tbl.set(elementName, container.value);
					}
				}
			}
		}
	}
	
	/** Получить аргументы для вызова java-метода, зная пришедшие lua-значения */
	@InternalElement
	public static Object[] getArgs(Method method, LuaValue... luaValues)
	{
		List<Object> ret = CollectionsSWL.createExtendedList();
		
		Class<?>[] ptypes = method.getParameterTypes();
		Class<?> cl;
		LuaValue luaValue;
		
		for (int i1 = 0; i1 < method.getParameterCount(); i1 ++)
		{
			cl = ptypes[i1];
			luaValue = luaValues[i1];
			ret.add(LuaUtils.toJavaValue(luaValue, cl));
		}
		
		return ret.toArray();
	}
	
	/** Контейнер для lua-элемента, знающий его имя */
	@InternalElement
	public static class LuaValueContainer {
		
		/** Имена объекта */
		@InternalElement
		public IExtendedList<String> names;
		
		/** Значение объекта */
		@InternalElement
		public LuaValue value;
		
		/** Конструктор */
		public LuaValueContainer(IExtendedList<String> names, LuaValue value)
		{
			super();
			this.names = names;
			this.value = value;
		}
	}
	
	/**
	 * Обертка на java-метод, конвертирующая lua-вызов в вызов этого метода
	 * @author swayfarer
	 *
	 */
	@InternalElement
	public static class LuaMethodFunWrapper extends LibFunction {

		/** Кол-во аргументов метода */
		@InternalElement
		public int argsCount = 0;
		
		/** Вызываемый метод */
		@InternalElement
		public Method method;
		
		/** Объект, у которого будет вызыван метод */
		@InternalElement
		public Object instance;
		
		/** Конструктор */
		public LuaMethodFunWrapper(Method method, Object instance)
		{
			super();
			this.method = method;
			this.instance = instance;
			this.argsCount = method.getParameterCount();
		}

		@Override
		public LuaValue call()
		{
			return logger.safeReturn(() -> {
				Object methodResult = method.invoke(instance);
				return LuaUtils.toLuaValue(methodResult);
			}, NIL);
		}
		
		@Override
		public LuaValue call(LuaValue arg)
		{
			return apply(arg);
		}
		
		@Override
		public LuaValue call(LuaValue arg1, LuaValue arg2)
		{
			return apply(arg1, arg2);
		}
		
		@Override
		public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3)
		{
			return apply(arg1, arg2, arg3);
		}
		
		/** Вызвать обернутый метод с аргументамми, полученными через {@link LuaInterpreter#getArgs(Method, LuaValue...)} */
		@InternalElement
		public LuaValue apply(LuaValue... args)
		{
			ExceptionsUtils.IfNot(argsCount == args.length, IllegalStateException.class, "Invalid arguments count! Excepted", argsCount, "and was", args.length);
			
			return logger.safeReturn(() -> {
				Object methodResult = method.invoke(instance, getArgs(method, args));
				return LuaUtils.toLuaValue(methodResult);
			}, NIL);
		}
		
		@Override
		public Varargs invoke(Varargs args)
		{
			if (argsCount == 0)
				return call();
			else 
			{
				LuaValue[] values = new LuaValue[argsCount];
				
				for (int i1 = 0; i1 < argsCount; i1 ++)
				{
					values[i1] = args.arg(i1);
				}
				
				return apply(values);
			}
		}
	}
}
