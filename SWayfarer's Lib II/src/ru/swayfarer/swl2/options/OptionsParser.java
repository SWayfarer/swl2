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

	public String optionPrefix = "-";

	/** Локализованное описание команды --help */
	public String HELP_COMMAND_DESCRIPTION = "Shows this help";
	
	/** Локализованный заголовок помощи, выводимой командой --help */
	public String HELP_HEADER_TEXT = "Help:";
	
	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Зарегистрированные опции */
	@InternalElement
	public IExtendedList<Option> registeredOptions = CollectionsSWL.createExtendedList();  
	
	/** Функция, которая будет вызывана, если попадется незарегистрированный агрумент */
	public IFunction1NoR<String> unregisteredOptionHandler;
	
	/** Функция, которая вызовется, если для опции не указан обязательный агрумент */
	public IFunction1NoR<String> missingValueHandler;
	
	/** Функция, которая вызовется, если для опции, не имеющей значения, оно будет указано */
	public IFunction1NoR<String> notPossibleValueHandler;
	
	/** Функция, которая вызовется, если отсутствует одна из обязательных опций */
	public IFunction1NoR<IExtendedList<Option>> missingOptionsHandler;
	
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
		IExtendedList<Option> missingOptions = getRequiredMissing(options);
		
		if (!CollectionsSWL.isNullOrEmpty(missingOptions))
			if (missingOptionsHandler != null)
				missingOptionsHandler.apply(missingOptions);
		
		logger.safe(() -> options.dataStream().each(Option::apply), "Error while applying options");
	}
	
	/** Показать помощь */
	public void showHelp(ILogger logger)
	{
		if (registeredOptions.isEmpty())
			return;
		
		if (logger == null)
			return;
		
		logger.info(this.HELP_HEADER_TEXT);
		
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
			if (arg.startsWith(optionPrefix))
			{
				String argName = arg.substring(optionPrefix.length());
				
				if (option != null && option.hasValue && option.value == null)
					missingValueHandler.apply(option.name);
				
				option = registeredOptions.dataStream()
						.find((o) -> o.name.equals(argName));
				
				if (option == null)
				{
					if (unregisteredOptionHandler != null)
						unregisteredOptionHandler.apply(argName);
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
	
	/** Добавить опцию --help */
	public <T extends OptionsParser> T addHelpOption(ILogger logger)
	{
		registerOption("help", HELP_COMMAND_DESCRIPTION, false, false, (o) -> showHelp(logger));
		return (T) this;
	}
	
	/** Зарегистрировать опцию */
	public <T extends OptionsParser> T registerOption(String name, String description, boolean hasValue, boolean isRequired, IFunction1NoR<Option> initFun)
	{
		return registerOption(new Option().setName(name).setDescription(description).setValueEnabled(hasValue).setInitFun(initFun));
	}
	
	/** Зарегистрировать опцию */
	public <T extends OptionsParser> T registerOption(String name, String description, boolean hasValue, boolean isRequired)
	{
		return registerOption(new Option().setName(name).setDescription(description).setValueEnabled(hasValue).setRequired(isRequired));
	}
	
	/** Зарегистрировать опцию */
	public <T extends OptionsParser> T registerOption(Option option)
	{
		registeredOptions.addExclusive(option);
		return (T) this;
	}
	
}
