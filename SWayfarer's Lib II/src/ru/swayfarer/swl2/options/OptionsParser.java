package ru.swayfarer.swl2.options;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1NoR;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Парсер опций командной строки
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class OptionsParser {

	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Локализованный текст помощи */
	public String helpText = "Help:";
	
	/** Зарегистрированные опции */
	@InternalElement
	public IExtendedList<Option> registeredOptions = CollectionsSWL.createExtendedList();  
	
	/** Функция, которая будет вызывана, если попадется незарегистрированный агрумент */
	public IFunction1NoR<String> missingOptionHandler;
	
	/** Функция, которая вызовется, если для опции не указан обязательный агрумент */
	public IFunction1NoR<String> missingValueHandler;
	
	/** Функция, которая вызовется, если для опции, не имеющей значения, оно будет указано */
	public IFunction1NoR<String> notPossibleValueHandler;
	
	/** Не добавлена обязательная опция */
	public IExtendedList<Option> getRequiredMissing(IExtendedList<Option> options)
	{
		return registeredOptions.dataStream()
			.removeAll(registeredOptions::contains)
			.filter(Option::isRequied)
			.toList();
	}
	
	public void onInit(ILogger logger, IExtendedList<Option> options)
	{
		init(logger, options);
	}
	
	/** Инициализация опций через {@link Option#apply()}*/
	public void init(ILogger logger, IExtendedList<Option> options)
	{
		logger.safe(() -> options.dataStream().each(Option::apply), "Error while applying options");
	}
	
	/** Показать помощь */
	public void showHelp(ILogger logger)
	{
		if (registeredOptions.isEmpty())
			return;
		
		if (logger == null)
			return;
		
		logger.info(this.helpText);
		
		int maxOptionLenght = registeredOptions.dataStream()
				.mapped((o) -> o.name.length())
				.sorted()
				.last();
		
		registeredOptions.dataStream().each((o) -> {
			
			logger.info(" -" + o.name, StringUtils.createSpacesSeq(maxOptionLenght - o.name.length() + 4), "|", o.description);
			
		});
	}
	
	/** Чтение опций */
	public IExtendedList<Option> parse(IExtendedList<String> argsList)
	{
		IExtendedList<Option> ret = CollectionsSWL.createExtendedList();
		Option option = null;
		
		for (String arg : argsList)
		{
			if (arg.startsWith("-"))
			{
				String argName = arg.substring(1);
				
				if (option != null && option.hasValue && option.value == null)
					missingValueHandler.apply(option.name);
				
				option = registeredOptions.dataStream()
						.find((o) -> o.name.equals(argName));
				
				if (option == null)
				{
					if (missingOptionHandler != null)
						missingOptionHandler.apply(argName);
				}
				else
				{
					ret.add(option);
				}
			}
			else if (option != null)
			{
				if (option.hasValue)
					option.loadValue(arg);
				else
					notPossibleValueHandler.apply(option.name);
			}
		}
		
		return ret;
	}
	
	/** Зарегистрировать опцию */
	public <T extends OptionsParser> T registerOption(String name, String description, boolean hasValue, IFunction1NoR<Option> initFun)
	{
		return registerOption(new Option().setName(name).setDescription(description).setValueEnabled(hasValue).setInitFun(initFun));
	}
	
	/** Зарегистрировать опцию */
	public <T extends OptionsParser> T registerOption(String name, String description, boolean hasValue)
	{
		return registerOption(new Option().setName(name).setDescription(description).setValueEnabled(hasValue));
	}
	
	/** Зарегистрировать опцию */
	public <T extends OptionsParser> T registerOption(Option option)
	{
		registeredOptions.addExclusive(option);
		return (T) this;
	}
	
}
